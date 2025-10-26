import classNames from 'classnames/bind';
import { Formik, Form as FormFormik, FastField } from 'formik';
import { Button, Alert } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import * as Yup from 'yup';

import styles from './ActiveUser.module.scss';
import config from '~/config';
import InputField from '~/customs/fields/InputField';
import { useEffect, useState } from 'react';
import { userApi } from '~/api';

const cx = classNames.bind(styles);

function ActiveUser() {
    const navigate = useNavigate();
    const [showFailed, setShowFailed] = useState('');
    const initialValues = {
        otp: '',
    };

    useEffect(() => {
        const sendOtp = async () => {
            try {
                const auth = JSON.parse(localStorage.getItem(config.localStorageKey.auth));
                const { token } = auth;
                await userApi.sendOtpActiveUser({ token });
            } catch (error) {
                const { response } = error;
                if (response.data.code === 410) {
                    setShowFailed(response.data.message);
                }
            }
        };
        sendOtp();
    }, [navigate]);

    const validationSchema = Yup.object().shape({
        otp: Yup.number()
            .typeError('Mã OTP chỉ là số')
            .min(100000, 'Mã OTP không hợp lệ')
            .max(999999, 'Mã OTP không hợp lệ')
            .required('Trường này bắt buộc'),
    });

    const handleSubmitOtp = (values) => {
        const { otp } = values;
        const auth = JSON.parse(localStorage.getItem(config.localStorageKey.auth));
        const { token } = auth;
        userApi
            .activeUser({ otp, token })
            .then((res) => {
                try {
                    const user = JSON.parse(localStorage.getItem(config.localStorageKey.user));
                    user.is_active = true;
                    localStorage.setItem(config.localStorageKey.user, JSON.stringify(user));
                    navigate(config.routes.home.path);
                } catch (error) {
                    console.log(error);
                }
            })
            .catch((error) => {
                const { response } = error;
                // console.log(response.data);
                setShowFailed(response.data.message);
            });
    };
    return (
        <div className={cx('wrapper')}>
            <div className={cx('background')}></div>
            <div className={cx('container')}>
                <div className={cx('wrapper-form')}>
                    <h2 className={cx('form-title')}>Kích hoạt tài khoản</h2>
                    <p>Chúng tôi đã gửi mã OTP 6 số đến email của bạn</p>
                    <Formik
                        initialValues={initialValues}
                        validationSchema={validationSchema}
                        onSubmit={handleSubmitOtp}
                    >
                        {(formikProps) => {
                            return (
                                <FormFormik className="w-100">
                                    <FastField
                                        name="otp"
                                        component={InputField}
                                        //
                                        type="text"
                                        placeholder="Nhập mã OTP của bạn"
                                    ></FastField>
                                    {showFailed !== '' && (
                                        <Alert variant="danger" className="text-center p-1 fw-bold w-100 mb-0">
                                            {showFailed}
                                        </Alert>
                                    )}
                                    <Button
                                        type="submit"
                                        variant="success"
                                        className="w-100 mt-4"
                                        style={{ borderRadius: '16px' }}
                                    >
                                        Tiếp tục
                                    </Button>
                                </FormFormik>
                            );
                        }}
                    </Formik>

                    <Button size="sm" variant="outline-success" className="my-2 px-4" style={{ borderRadius: '16px' }}>
                        Gửi lại mã
                    </Button>
                    <p className={cx('text-center')}>
                        <Link to={config.routes.home.path} className={cx('text-primary')}>
                            Về trang chủ
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
}

export default ActiveUser;
