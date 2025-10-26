// import { Col, Row } from 'react-bootstrap';
// import LineChart from '../LineChart';
import classNames from 'classnames/bind';
import styles from './VisualizationGroup.module.scss';
import Switch from 'react-switch';
import ProgressBar from '../ProgressBar';
import { useState, useContext, useEffect } from 'react';
import { SocketFarmstayContext } from '~/context/socketFarmstayContext';
import { userApi } from '~/api';
import { useSelector } from 'react-redux';
import LineChart from '../LineChart/LineChart';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faRepeat } from '@fortawesome/free-solid-svg-icons';
import { Button } from 'react-bootstrap';
import { toast } from 'react-toastify';

const cx = classNames.bind(styles);
const BUTTON = 'button';
const CHART = 'chart';

function VisualizationGroup({ field, className, ...props }) {
    const [checked, setChecked] = useState(false);
    const [data, setData] = useState([]);
    const auth = useSelector((state) => state.auth);
    const [showChart, setShowChart] = useState(false);
    const socket = useContext(SocketFarmstayContext);
    const [danger, setDanger] = useState(false);
    useEffect(() => {
        const handleSocketData = (res) => {
            const { value, danger: isDanger } = res;
            Object.assign(res, { value: value });
            if (isDanger) {
                toast.error(`Cảnh báo: ${field.alias_field_name} ${value}${field.unit_symbol}`, {
                    position: 'top-right',
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: 'light',
                });
                setDanger(true);
            } else {
                setDanger(false);
            }

            setData((prev) => {
                if (prev.length >= 30) {
                    return [...prev.slice(-29), res];
                } else {
                    return [...prev, res];
                }
            });
            setChecked(!!value);
        };
        if (auth.token) {
            userApi
                .getLatestDataEquipments({ token: auth.token, field: field.field_name })
                .then((res) => {
                    socket.on(field.field_name, handleSocketData);
                    setData((prev) => {
                        return [...res.data];
                    });

                    if (Array.isArray(res.data) && res.data.length > 0) {
                        setChecked(!!res.data[res.data.length - 1].value);
                        const data = res.data[res.data.length - 1].value;

                        if (data <= field.danger_min || data >= field.danger_max) {
                            setDanger(true);
                            toast.error(`Cảnh báo: ${field.alias_field_name} ${data}${field.unit_symbol}`, {
                                position: 'top-right',
                                autoClose: 5000,
                                hideProgressBar: false,
                                closeOnClick: true,
                                pauseOnHover: true,
                                draggable: true,
                                progress: undefined,
                                theme: 'light',
                            });
                        }
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
        }

        return () => {
            socket.off(field.field_name, handleSocketData);
        };
    }, [
        field.field_name,
        socket,
        auth.token,
        field.alias_field_name,
        field.unit_symbol,
        field.danger_min,
        field.danger_max,
    ]);

    const handleChange = (nextChecked) => {
        setChecked(nextChecked);
        if (field.visualization === BUTTON) {
            if (socket.connected) {
                socket.emit('client_control', {
                    farmstay_id: field.farmstay_id,
                    hardware_id: field.hardware_id,
                    value: +nextChecked,
                });
            }
        }
    };

    const getLatestData = () => {
        if (data.length > 0) {
            return data[data.length - 1].value;
        } else {
            return 0;
        }
    };
    const renderVisualization = (type) => {
        if (type === CHART) {
            return (
                <div className={cx('wrap-switch')}>
                    <ProgressBar
                        value={getLatestData()}
                        minValue={field.min}
                        maxValue={field.max}
                        text={`${getLatestData()}${field.unit_symbol}`}
                        danger={danger}
                    />
                </div>
            );
        } else if (type === BUTTON) {
            return (
                <div className={cx('wrap-switch')}>
                    <Switch
                        width={120}
                        height={60}
                        onChange={handleChange}
                        checked={checked}
                        className="react-switch"
                        uncheckedIcon={
                            <div
                                style={{
                                    display: 'flex',
                                    justifyContent: 'center',
                                    alignItems: 'center',
                                    height: '100%',
                                    fontSize: 15,

                                    fontWeight: 700,
                                    color: '#FFF',
                                }}
                            >
                                OFF
                            </div>
                        }
                        checkedIcon={
                            <div
                                style={{
                                    display: 'flex',
                                    justifyContent: 'center',
                                    alignItems: 'center',
                                    height: '100%',
                                    fontSize: 15,

                                    fontWeight: 700,
                                    color: '#FFF',
                                }}
                            >
                                ON
                            </div>
                        }
                    />
                </div>
            );
        }
    };
    return (
        <div className={cx('wrapper')}>
            <div className={cx('header')}>
                <h5 className={cx('header-title', 'fw-semibold')}>{field.alias_field_name}</h5>
                <Button
                    size="sm"
                    className={cx('header-btn')}
                    onClick={() => {
                        setShowChart((prev) => {
                            return !prev;
                        });
                    }}
                >
                    <FontAwesomeIcon icon={faRepeat} />
                </Button>
            </div>
            <div className={cx('body')}>
                {showChart ? (
                    <LineChart
                        data={data}
                        XAxisKey={'timestamp'}
                        lineKey={'value'}
                        danger={danger}
                        dangerMax={field.danger_max}
                        dangerMaxLabel={`${field.danger_max}` + (field.unit_symbol ? `(${field.unit_symbol})` : '')}
                        dangerMin={field.danger_min}
                        dangerMinLabel={`${field.danger_min}` + (field.unit_symbol ? `(${field.unit_symbol})` : '')}
                        showDangerLine={field.visualization === 'chart'}
                        name={`${field.alias_field_name}` + (field.unit_symbol ? `(${field.unit_symbol})` : '')}
                    />
                ) : (
                    renderVisualization(field.visualization)
                )}
            </div>
        </div>
    );
}

export default VisualizationGroup;
