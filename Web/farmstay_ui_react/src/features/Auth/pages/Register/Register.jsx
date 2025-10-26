import classNames from 'classnames/bind';

import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import styles from './Register.module.scss';
import { Link } from 'react-router-dom';

import config from '~/config';
import FormRegister from '../../components/FormRegister';

const cx = classNames.bind(styles);

function Register() {
    return (
        <div className={cx('wrapper')}>
            <div className={cx('background')}></div>
            <div className={cx('container')}>
                <Row className={cx('h-100', 'g-0')}>
                    <Col md={6} className={cx('h-100')}>
                        <div className={cx('logo-background')}>
                            <div className={cx('logo')}></div>
                        </div>
                    </Col>
                    <Col md={6}>
                        <div className={cx('wrapper-form')}>
                            <h2 className={cx('form-title')}>Đăng ký tài khoản</h2>
                            <FormRegister />
                            <p className={cx('text-center', 'mt-3')}>
                                Bạn đã có tài khoản?{' '}
                                <Link
                                    to={[config.routes.auth.path, config.routes.auth.child.login].join('/')}
                                    className={cx('text-primary')}
                                >
                                    Đăng nhập
                                </Link>
                            </p>
                            <p className={cx('text-center')}>
                                <Link to={config.routes.home.path} className={cx('text-primary')}>
                                    Về trang chủ
                                </Link>
                            </p>
                        </div>
                    </Col>
                </Row>
            </div>
        </div>
    );
}

export default Register;
