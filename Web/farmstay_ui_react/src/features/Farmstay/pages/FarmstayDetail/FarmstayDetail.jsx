import classNames from 'classnames/bind';
import { useEffect, useState } from 'react';

import { useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { farmstayApi } from '~/api';
import styles from './FarmstayDetail.module.scss';
import { Card, Carousel, Tab, Tabs, ListGroup, Image, Button } from 'react-bootstrap';
import config from '~/config';

const cx = classNames.bind(styles);

function FarmstayDetail() {
    const { farmstay_uuid } = useParams();
    const [key, setKey] = useState('infomation');

    const auth = useSelector((state) => state.auth);
    const { token } = auth;
    const [farmstay, setFarmstay] = useState(null);
    useEffect(() => {
        if (token) {
            farmstayApi
                .getDetailFarmstay(token, farmstay_uuid)
                .then((res) => {
                    setFarmstay(res.data);
                })
                .catch((err) => {
                    console.log(err);
                });
        }
    }, [token, farmstay_uuid]);
    console.log(farmstay);
    const handleClickRentFarm = (e) => {
        const returnURL = `${config.baseURL}${config.routes.farmstays.path}/${farmstay_uuid}/status_payment_deposit`;
        farmstayApi
            .createPaymentURL({ token, farmstayUUID: farmstay_uuid, returnURL })
            .then((res) => {
                window.open(res.data, '_blank');
            })
            .catch((err) => {
                console.log(err);
            });
    };
    console.log(farmstay);
    return (
        <div className={cx('wrapper')}>
            {farmstay ? (
                <>
                    <div className={cx('carousel-wrapper')}>
                        <Carousel className={cx('carousel')} interval={3000}>
                            {farmstay.images.map((image) => {
                                return (
                                    <Carousel.Item key={image}>
                                        <img
                                            draggable={false}
                                            style={{
                                                display: 'block',
                                                width: '100%',
                                                height: '100%',
                                                objectFit: 'cover',
                                            }}
                                            src={image}
                                            alt=""
                                        />
                                    </Carousel.Item>
                                );
                            })}
                        </Carousel>
                    </div>
                    <div className={cx('wrapper-content', 'my-4')}>
                        <Tabs id="controlled-tab-example" activeKey={key} onSelect={(k) => setKey(k)} className="mb-3">
                            <Tab eventKey="infomation" title="Tổng quan">
                                <Card style={{ width: '100%' }}>
                                    <Card.Body>
                                        <Card.Title className="fw-bold fs-2">{farmstay.name}</Card.Title>
                                        <Card.Subtitle className="mb-2 text-danger fs-5">
                                            Giá thuê:{' '}
                                            {farmstay.rent_cost_per_day.toLocaleString('it-IT', {
                                                style: 'currency',
                                                currency: 'VND',
                                            })}
                                            /ngày
                                        </Card.Subtitle>
                                        <Card.Text className="fs-5">{'Mô tả: ' + farmstay.description}</Card.Text>
                                        <Card.Text>
                                            Địa chỉ:{' '}
                                            {farmstay.address.ward.full_name +
                                                ', ' +
                                                farmstay.address.district.full_name +
                                                ', ' +
                                                farmstay.address.province.full_name +
                                                ' - '}
                                            <Card.Link
                                                className="text-primary"
                                                href={farmstay.address.link}
                                                target="_blank"
                                            >
                                                Mở bản đồ
                                            </Card.Link>
                                        </Card.Text>
                                        <div
                                            className="d-flex justify-content-center"
                                            dangerouslySetInnerHTML={{
                                                __html: farmstay.address?.embedded_link,
                                            }}
                                        ></div>

                                        <div className="d-flex flex-row-reverse">
                                            <Button variant="success" className="px-4" onClick={handleClickRentFarm}>
                                                Thuê ngay
                                            </Button>
                                        </div>
                                    </Card.Body>
                                </Card>
                            </Tab>
                            <Tab eventKey="address" title="Trang thiết bị">
                                <ListGroup>
                                    {farmstay.equipments.map((equipment) => {
                                        return (
                                            <ListGroup.Item className={cx('list-item')} key={equipment.name}>
                                                <Image
                                                    draggable={false}
                                                    className={cx('list-item-image')}
                                                    src={equipment.image}
                                                />
                                                <span className={cx('list-item-title', 'mx-4')}>{equipment.name}</span>
                                                <span className="text-danger">
                                                    Giá thuê:{' '}
                                                    {equipment.rent_cost.toLocaleString('it-IT', {
                                                        style: 'currency',
                                                        currency: 'VND',
                                                    })}
                                                </span>
                                            </ListGroup.Item>
                                        );
                                    })}
                                </ListGroup>
                            </Tab>
                        </Tabs>
                    </div>
                </>
            ) : (
                <div className="text-center">
                    <h5 className="fw-bold">Đăng nhập để xem</h5>
                </div>
            )}
        </div>
    );
}

export default FarmstayDetail;
