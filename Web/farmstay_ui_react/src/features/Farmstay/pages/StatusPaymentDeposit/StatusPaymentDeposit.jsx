import classNames from 'classnames/bind';
import styles from './StatusPaymentDeposit.module.scss';
import { Link, useParams, useSearchParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle, faTimesCircle } from '@fortawesome/free-solid-svg-icons';
import { Button, Spinner } from 'react-bootstrap';
import { useSelector } from 'react-redux';
import { farmstayApi } from '~/api';
const cx = classNames.bind(styles);
function StatusPaymentDeposit() {
    const [searchParams] = useSearchParams();
    const { farmstay_uuid } = useParams();
    const [message, setMessage] = useState(null);
    const [loading, setLoading] = useState(true);
    const [paymentStatus, setPaymentStatus] = useState(false);

    const [orderDeposit, setOrderDeposit] = useState(null);
    const auth = useSelector((state) => state.auth);
    const { token } = auth;
    useEffect(() => {
        if (token) {
            const vnpParams = {};
            searchParams.forEach((value, key) => {
                Object.assign(vnpParams, { [key]: value });
            });
            setOrderDeposit(vnpParams);
            farmstayApi
                .checkDepositPayment({ token, farmstayUUID: farmstay_uuid, body: vnpParams })
                .then((res) => {
                    console.log(res);
                    setMessage(res.message);
                    setLoading(false);
                    setPaymentStatus(true);
                })
                .catch((err) => {
                    const { response } = err;
                    if (response && response.data) {
                        setMessage(response.data.message);
                        setPaymentStatus(false);
                        setLoading(false);
                    }
                });
        }
    }, [searchParams, token, farmstay_uuid]);
    console.log(paymentStatus);
    const renderIconStatus = () => {
        return paymentStatus ? (
            <FontAwesomeIcon icon={faCheckCircle} className={cx('icon-status')} />
        ) : (
            <FontAwesomeIcon icon={faTimesCircle} className={cx('icon-status')} />
        );
    };
    return (
        <div className={cx('wrapper')}>
            {orderDeposit && (
                <div className={cx('card')}>
                    <div
                        className={cx('card-status', {
                            'bg-success': !!paymentStatus,
                            'bg-danger': !paymentStatus,
                        })}
                    >
                        <div>
                            {loading ? (
                                <Spinner animation="border" variant="light" className={cx('icon-status')} />
                            ) : (
                                renderIconStatus()
                            )}
                        </div>
                    </div>
                    <div className={cx('card-body')}>
                        <p className="fw-bold fs-2 text-center">
                            {(parseInt(orderDeposit.vnp_Amount) / 100).toLocaleString('it-IT', {
                                style: 'currency',
                                currency: 'VND',
                            })}
                        </p>
                        <p className="fw-bold">
                            Thông tin: <span className="fw-normal">{orderDeposit.vnp_OrderInfo}</span>
                        </p>
                        <p className="fw-bold">
                            Mã hóa đơn: <span className="fw-normal">{orderDeposit.vnp_TxnRef}</span>
                        </p>
                        {message && (
                            <p
                                className={cx('fw-bold', 'fs-5', 'text-center', {
                                    'text-success': !!paymentStatus,
                                    'text-danger': !paymentStatus,
                                })}
                            >
                                {message}
                            </p>
                        )}
                    </div>
                    <div className={cx('card-footer')}>
                        {paymentStatus && (
                            <Link to="/user/farmstay/dashboard">
                                <Button variant="success">Đi đến farmstay của bạn</Button>
                            </Link>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}

export default StatusPaymentDeposit;
