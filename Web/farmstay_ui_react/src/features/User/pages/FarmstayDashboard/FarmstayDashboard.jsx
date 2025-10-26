import classNames from 'classnames/bind';

import { Col, ListGroup, Row, Spinner } from 'react-bootstrap';

import styles from './FarmstayDashboard.module.scss';
import { useEffect, useState } from 'react';
import AreaVisualization from '../../components/AreaVisualization/AreaVisualization';
import { SocketFarmstayContext } from '~/context/socketFarmstayContext';
import { useSelector } from 'react-redux';
import { userApi } from '~/api';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Link, useNavigate } from 'react-router-dom';
// import config from '~/config';

const cx = classNames.bind(styles);

// function convertTimestampToDate(timeStamp) {
//     const dateFormat = new Date(timeStamp);
//     return (
//         dateFormat.getDate() +
//         '/' +
//         (dateFormat.getMonth() + 1) +
//         '/' +
//         dateFormat.getFullYear() +
//         ' ' +
//         dateFormat.getHours() +
//         ':' +
//         dateFormat.getMinutes() +
//         ':' +
//         dateFormat.getSeconds()
//     );
// }

function FarmstayDashboard({ socket }) {
    const [areas, setAreas] = useState({});
    const auth = useSelector((state) => state.auth);
    const [loading, setLoading] = useState(true);
    const [isRented, setIsRented] = useState(false);
    const { token } = auth;
    const [farmstay, setFarmstay] = useState(null);
    const navigate = useNavigate();
    // useEffect(() => {
    //     socket.on('connect_error', (err) => {
    //         console.log(`connect_error due to ${err.message}`);
    //     });
    // }, []);

    useEffect(() => {
        if (token) {
            userApi
                .getFarmstayOwn({ token })
                .then((res) => {
                    console.log(res);
                    setFarmstay(res.data);
                })
                .catch((err) => {
                    if (err?.response?.data) {
                        const {
                            response: { data },
                        } = err;
                        if (data?.code === 403) {
                            setLoading(false);
                            setIsRented(false);
                        } else if (data?.code === 401) {
                            navigate('/auth/login');
                        }
                    }
                });

            userApi
                .getFieldEquipments({ token })
                .then((res) => {
                    setLoading(false);
                    const {
                        data: { equipments, farmstay_id },
                    } = res;
                    const areas = {};
                    equipments.forEach((equipment) => {
                        const { area, equipment_fields } = equipment;
                        const fields = Array.from(equipment_fields).map((field) => {
                            return Object.assign(field, { farmstay_id });
                        });
                        if (areas.hasOwnProperty(area)) {
                            areas[area].push(...fields);
                        } else {
                            areas[area] = [...fields];
                        }
                    });
                    socket.open();

                    setAreas(areas);
                    setIsRented(true);
                })
                .catch((err) => {
                    if (err?.response?.data) {
                        const {
                            response: { data },
                        } = err;
                        if (data?.code === 403) {
                            setLoading(false);
                            setIsRented(false);
                        } else if (data?.code === 401) {
                            navigate('/auth/login');
                        }
                    }
                });
        }
        return () => {
            socket.close();
        };
    }, [token, navigate, socket]);

    const render = () => {
        return isRented ? (
            <SocketFarmstayContext.Provider value={socket}>
                <ToastContainer />
                <div className={cx('wrapper')}>
                    <h2 className={cx('dashboard-title')}>Dashboard Farmstay</h2>
                    <div className={cx('container')}>
                        <Row>
                            <Col xl={12}>
                                <div className={cx('wrapper-farmstay')}>
                                    {farmstay && (
                                        <ListGroup>
                                            <ListGroup.Item>
                                                <h5>
                                                    Tên farmstay: <span className="fw-bold">{farmstay.name}</span>
                                                </h5>
                                            </ListGroup.Item>
                                            <ListGroup.Item>
                                                <h5>
                                                    Địa chỉ:{' '}
                                                    <span className="fw-bold">
                                                        {farmstay.address.ward.full_name +
                                                            ', ' +
                                                            farmstay.address.district.full_name +
                                                            ', ' +
                                                            farmstay.address.province.full_name}
                                                    </span>
                                                </h5>
                                            </ListGroup.Item>
                                            <ListGroup.Item>
                                                <h5>
                                                    Ngày thuê:{' '}
                                                    <span className="fw-bold">{farmstay.rental_info.rented_at}</span>
                                                </h5>
                                            </ListGroup.Item>
                                        </ListGroup>
                                    )}
                                </div>
                            </Col>
                            <Col xl={12}>
                                {Object.keys(areas).map((area) => {
                                    return <AreaVisualization key={area} name={area} fields={areas[area]} />;
                                })}
                            </Col>
                        </Row>
                    </div>
                </div>
            </SocketFarmstayContext.Provider>
        ) : (
            <div className={cx('wrapper')}>
                <h2 className="text-center">Bạn chưa thuê farmstay nào</h2>
                <h5 className="text-center fs-4 ">
                    <Link className="text-primary" to="/">
                        Về trang chủ
                    </Link>
                </h5>
            </div>
        );
    };

    return (
        <>
            {loading ? (
                <div className={cx('wrapper')}>
                    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                        <Spinner animation="border" style={{ width: '100px', height: '100px' }} />
                    </div>
                </div>
            ) : (
                render()
            )}
        </>
    );
}

export default FarmstayDashboard;
