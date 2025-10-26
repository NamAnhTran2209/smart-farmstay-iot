import classNames from 'classnames/bind';
import styles from './Menu.module.scss';
import { Row, Col } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import images from '~/assets/images';

const cx = classNames.bind(styles);

function Menu() {
    return (
        <div className={cx('wrapper')}>
            <div className={cx('header')}>
                <h3 className="text-center fw-bold">Trang Nhân Viên</h3>
            </div>
            <div className={cx('body')}>
                <Row>
                    <Col xs={3}>
                        <div className={cx('card')}>
                            <h5 className="fw-bold">Điểm danh</h5>
                            <Link to="/employee/attendance">
                                <img className={cx('card-icon')} src={images.attendance} alt="attendance" />
                            </Link>
                        </div>
                    </Col>
                    <Col xs={3}>
                        <div className={cx('card')}>
                            <h5 className="fw-bold">Chat nội bộ</h5>
                            <Link to="/employee/chat">
                                <img className={cx('card-icon')} src={images.chat} alt="chat" />
                            </Link>
                        </div>
                    </Col>
                    <Col xs={3}>
                        <div className={cx('card')}>
                            <h5 className="fw-bold">Test</h5>
                            <Link to="/employee">
                                <img className={cx('card-icon')} src={images.noUser} alt="chat" />
                            </Link>
                        </div>
                    </Col>
                    <Col xs={3}>
                        <div className={cx('card')}>
                            <h5 className="fw-bold">Test</h5>
                            <Link to="/employee">
                                <img className={cx('card-icon')} src={images.noUser} alt="chat" />
                            </Link>
                        </div>
                    </Col>
                </Row>
            </div>
        </div>
    );
}

export default Menu;
