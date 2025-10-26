import classNames from 'classnames/bind';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import * as Yup from 'yup';

import styles from './FormRegister.module.scss';
import { Formik, Form as FormFormik, FastField } from 'formik';
import InputField from '~/customs/fields/InputField';
import { authApi } from '~/api';
import config from '~/config';

const cx = classNames.bind(styles);

function FormRegister() {
    const navigate = useNavigate();
    const [showRegisterFailed, setShowRegisterFailed] = useState('');
    const initialValues = {
        email: '',
        username: '',
        password: '',
        confirm_password: '',
    };

    const validationSchema = Yup.object().shape({
        email: Yup.string().email('Trường này là email').required('Trường này bắt buộc'),
        username: Yup.string()
            .matches(
                /^[A-Za-z][A-Za-z0-9]*(?=[a-zA-Z0-9._]{3,120}$)(?!.*[_.]{2})[^_.].*[^_.]$/i,
                'Username chỉ chứa chữ cái, số, _ và tối thiểu 3 ký tự',
            )
            .required('Trường này bắt buộc'),
        password: Yup.string()
            .min('3', 'Mật khẩu tối thiểu 3 ký tự')
            .max('256', 'Mật khẩu tối đa 256 ký tự')
            .required('Trường này bắt buộc'),
        confirm_password: Yup.string()
            .min('3', 'Mật khẩu tối thiểu 3 ký tự')
            .max('256', 'Mật khẩu tối đa 256 ký tự')
            .oneOf([Yup.ref('password'), null], 'Nhập lại mật khẩu không trùng')
            .required('Trường này bắt buộc'),
    });

    const handleRegisterUser = (values) => {
        const { email, username, password } = values;
        authApi
            .register({ email, username, password })
            .then((res) => {
                navigate([config.routes.auth.path, config.routes.auth.child.login].join('/'));
            })
            .catch((error) => {
                const { response } = error;
                setShowRegisterFailed(response.data.message);
            });
    };
    return (
        <Formik initialValues={initialValues} validationSchema={validationSchema} onSubmit={handleRegisterUser}>
            {(formikProps) => {
                // Do somthing
                // const { values, errors, touched } = formikProps;
                return (
                    <FormFormik>
                        <FastField
                            name="email"
                            component={InputField}
                            //
                            type="email"
                            label="Email"
                            placeholder="name@example.com"
                        ></FastField>
                        <FastField
                            name="username"
                            component={InputField}
                            //
                            type="text"
                            label="Username"
                            placeholder="Nhập username của bạn"
                        ></FastField>
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
                        {showRegisterFailed !== '' && (
                            <Alert variant="danger" className="text-center p-1 fw-bold m-0">
                                {showRegisterFailed}
                            </Alert>
                        )}
                        <Button type="submit" className={cx('btn-form', 'mt-2')} variant="success">
                            Đăng ký tài khoản
                        </Button>
                    </FormFormik>
                );
            }}
        </Formik>
    );
}

export default FormRegister;
