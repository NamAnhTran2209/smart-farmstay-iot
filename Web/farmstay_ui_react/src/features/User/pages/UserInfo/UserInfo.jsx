import classNames from 'classnames/bind';
import styles from './UserInfo.module.scss';
import Image from 'react-bootstrap/Image';
import { Col, ListGroup, Row } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import images from '~/assets/images';
import { useSelector } from 'react-redux';
const cx = classNames.bind(styles);

function UserInfo() {
    const user = useSelector((state) => state.user);
    console.log(user);

    const renderActiveUser = () => {
        if (user) {
            if (user.is_active) {
                return <span>đã kích hoạt</span>;
            } else {
                return (
                    <>
                        <span>chưa kích hoạt</span>{' '}
                        <Link to="/user/active" className={cx('text-primary', 'fw-bold')}>
                            <span>Kích hoạt ngay</span>
                        </Link>
                    </>
                );
            }
        }
    };
    const renderTypeUser = () => {
        if (user) {
            if (user.user_type === 'employee') {
                return <span>Nhân viên</span>;
            } else if (user.user_type === 'customer') {
                return <span>Khách hàng</span>;
            }
        }
    };
    return (
        <div className={cx('wrapper')}>
            <div className={cx('header')}>
                <div className={cx('header-bg')}></div>
                <div className={cx('header-footer')}></div>
                <Image className={cx('user-avatar')} roundedCircle src={images.noUser}></Image>
                <div className={cx('username')}>
                    <p>{user.username}</p>
                </div>
            </div>
            <div className={cx('body', 'mt-4')}>
                <Row>
                    <Col xl="3">
                        <ListGroup variant="flush" className="fs-5">
                            <ListGroup.Item className={cx('menu-list', 'w-100')}>
                                <span>Thông tin chung</span>
                            </ListGroup.Item>
                            <ListGroup.Item className={cx('menu-list', 'w-100')}>
                                <span>Farmstay đang thuê</span>
                            </ListGroup.Item>
                        </ListGroup>
                    </Col>
                    <Col xl="9">
                        <ListGroup variant="flush" className="fs-5">
                            <ListGroup.Item>
                                <span>Trạng thái tài khoản: </span>
                                {renderActiveUser()}
                            </ListGroup.Item>
                            <ListGroup.Item>
                                <span>Email: </span>
                                <span className="fw-semibold">{user.email}</span>
                            </ListGroup.Item>
                            <ListGroup.Item>
                                <span>Loại tài khoản: </span>
                                {renderTypeUser()}
                            </ListGroup.Item>
                        </ListGroup>
                    </Col>
                </Row>
            </div>
        </div>
    );
}

export default UserInfo;
