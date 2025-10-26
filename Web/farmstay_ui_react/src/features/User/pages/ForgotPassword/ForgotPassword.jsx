import classNames from 'classnames/bind';
import styles from './ForgotPassword.module.scss';
import { Formik, Form as FormFormik, FastField } from 'formik';
import InputField from '~/customs/fields/InputField';
import * as Yup from 'yup';
import { Button, Alert, Spinner } from 'react-bootstrap';
import { useState } from 'react';
import { userApi } from '~/api';
import { Link } from 'react-router-dom';
const cx = classNames.bind(styles);

function ForgotPassword() {
    const [step, setStep] = useState(1);
    const [email, setEmail] = useState(null);
    const [otp, setOtp] = useState(null);

    const SendOtp = () => {
        const [showFailed, setShowFailed] = useState('');
        const [loading, setLoading] = useState(false);
        const initialValues = {
            email: '',
        };
        const validationSchema = Yup.object().shape({
            email: Yup.string().email('Email không hợp lệ').required('Trường này bắt buộc'),
        });
        const handleSubmit = (values) => {
            const { email } = values;
            setLoading(true);
            userApi
                .sendEmailForgotPassword({ email })
                .then((res) => {
                    setEmail(email);
                    setStep(2);
                })
                .catch((err) => {
                    const { response } = err;
                    setShowFailed(response.data.message);
                });
            // setStep(2);
        };
        return (
            <div className={cx('form-wrapper')}>
                <h4 className="fw-bold text-center">Quên mật khẩu</h4>
                <div>
                    <Formik initialValues={initialValues} validationSchema={validationSchema} onSubmit={handleSubmit}>
                        {(formikProps) => {
                            // Do somthing
                            // const { values, errors, touched } = formikProps;
                            return (
                                <FormFormik id="formSendEmail">
                                    <FastField
                                        name="email"
                                        component={InputField}
                                        //
                                        type="text"
                                        label="Email đã đăng ký tài khoản"
                                        placeholder="name@example.com"
                                    ></FastField>
                                    {showFailed !== '' && (
                                        <Alert variant="danger" className="text-center p-1 fw-bold mb-0">
                                            {showFailed}
                                        </Alert>
                                    )}
                                    <Button
                                        type="submit"
                                        className={cx('btn-form')}
                                        variant="success"
                                        disabled={loading}
                                    >
                                        Gửi mã otp
                                        {loading && (
                                            <Spinner
                                                animation="border"
                                                style={{
                                                    width: '20px',
                                                    height: '20px',
                                                    marginLeft: '4px',
                                                }}
                                            />
                                        )}
                                    </Button>
                                </FormFormik>
                            );
                        }}
                    </Formik>
                </div>
            </div>
        );
    };
    const VerifyOtp = () => {
        const [showFailed, setShowFailed] = useState('');
        const initialValues = {
            otp: '',
        };
        const validationSchema = Yup.object().shape({
            otp: Yup.number()
                .typeError('Mã OTP phải là số')
                .min(100000, 'Mã OTP không hợp lệ')
                .max(999999, 'Mã OTP không hợp lệ')
                .required('Trường này bắt buộc'),
        });

        const handleSubmit = (values) => {
            const { otp } = values;
            userApi
                .verifyOtp({ email, otp })
                .then((res) => {
                    setOtp(otp);
                    setStep(3);
                })
                .catch((err) => {
                    const { response } = err;
                    setShowFailed(response.data.message);
                });
        };
        return (
            <div className={cx('form-wrapper')}>
                <h4 className="fw-bold text-center">Nhập mã otp</h4>
                <div>
                    <Formik initialValues={initialValues} validationSchema={validationSchema} onSubmit={handleSubmit}>
                        {(formikProps) => {
                            // Do somthing
                            // const { values, errors, touched } = formikProps;
                            return (
                                <FormFormik id="formVerifyOtp">
                                    <FastField
                                        name="otp"
                                        component={InputField}
                                        //
                                        type="text"
                                        label="Nhập mã otp"
                                        placeholder=""
                                    ></FastField>
                                    {showFailed !== '' && (
                                        <Alert variant="danger" className="text-center p-1 fw-bold mb-0">
                                            {showFailed}
                                        </Alert>
                                    )}
                                    <Button type="submit" className={cx('btn-form')} variant="success">
                                        Xác nhận
                                    </Button>
                                </FormFormik>
                            );
                        }}
                    </Formik>
                </div>
            </div>
        );
    };
    const SetNewPassword = () => {
        const [showFailed, setShowFailed] = useState('');
        const [showSuccess, setShowSuccess] = useState(false);

        const initialValues = {
            password: '',
            confirm_password: '',
        };
        const validationSchema = Yup.object().shape({
            password: Yup.string()
                .min('3', 'Mật khẩu tối thiểu 3 ký tự')
                .max('256', 'Mật khẩu tối đa 256 ký tự')
                .required('Trường này bắt buộc'),
            confirm_password: Yup.string()
                .min('3', 'Mật khẩu tối thiểu 3 ký tự')
                .max('256', 'Mật khẩu tối đa 256 ký tự')
                .oneOf([Yup.ref('password'), null], 'Nhập mật khẩu trùng với mật khẩu nhập ở trên')
                .required('Trường này bắt buộc'),
        });

        const handleSubmit = (values) => {
            const { password } = values;
            userApi
                .changeForgotPassword({ email, otp, new_password: password })
                .then((res) => {
                    setShowSuccess(true);
                })
                .catch((err) => {
                    const { response } = err;
                    setShowFailed(response.data.message);
                });
        };
        return (
            <div className={cx('form-wrapper')}>
                <h4 className="fw-bold text-center">Đặt lại mật khẩu</h4>
                <div>
                    <Formik initialValues={initialValues} validationSchema={validationSchema} onSubmit={handleSubmit}>
                        {(formikProps) => {
                            // Do somthing
                            // const { values, errors, touched } = formikProps;
                            return (
                                <FormFormik id="formVerifyOtp">
                                    <FastField
                                        name="password"
                                        component={InputField}
                                        //
                                        type="password"
                                        label="Mật khẩu"
                                        placeholder="Nhập mật khẩu của bạn"
                                    ></FastField>
                                    <FastField
                                        name="confirm_password"
                                        component={InputField}
                                        //
                                        type="password"
                                        label="Nhập lại mật khẩu"
                                        placeholder="Nhập lại mật khẩu của bạn"
                                    ></FastField>
                                    {showFailed !== '' && (
                                        <Alert variant="danger" className="text-center p-1 fw-bold mb-0">
                                            {showFailed}
                                        </Alert>
                                    )}
                                    {showSuccess && (
                                        <Alert variant="success" className="text-center p-1 fw-bold mb-0">
                                            Đặt lại mật khẩu thành công.{' '}
                                            <Link to={'/'} className="text-primary fw-light">
                                                Về trang chủ
                                            </Link>
                                        </Alert>
                                    )}
                                    <Button type="submit" className={cx('btn-form')} variant="success">
                                        Xác nhận
                                    </Button>
                                </FormFormik>
                            );
                        }}
                    </Formik>
                </div>
            </div>
        );
    };
    const renderStep = () => {
        switch (step) {
            case 1: {
                return <SendOtp></SendOtp>;
            }
            case 2: {
                return <VerifyOtp></VerifyOtp>;
            }
            case 3: {
                return <SetNewPassword></SetNewPassword>;
            }
            default:
                break;
        }
    };

    return (
        <div className={cx('wrapper')}>
            <div>{renderStep()}</div>
        </div>
    );
}

export default ForgotPassword;
