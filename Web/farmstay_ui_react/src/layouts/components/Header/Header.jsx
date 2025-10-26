import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faBell,
    faCreditCard,
    faEarthAsia,
    faHouseSignal,
    faSignOut,
    // faMessage,
    faUser,
    faUsers,
} from '@fortawesome/free-solid-svg-icons';
import classNames from 'classnames/bind';
import { Link, useNavigate } from 'react-router-dom';
import Tippy from '@tippyjs/react';
import 'tippy.js/dist/tippy.css';
import 'tippy.js/animations/shift-away.css';

import styles from './Header.module.scss';
import images from '~/assets/images';
import Button from '~/components/Button';
import Menu from '~/components/Popper/Menu';
import Image from '~/components/Image';
import Search from '~/layouts/components/Search';
import config from '~/config';
import { useDispatch, useSelector } from 'react-redux';
import { login as loginAction, logout as logoutAction } from '~/redux/auth/authSlice';
import { authApi } from '~/api';
import { useEffect } from 'react';
import { resetUser, setUser } from '~/redux/user/userSlice';

const cx = classNames.bind(styles);

const MENU_ITEMS = [
    {
        icon: <FontAwesomeIcon icon={faEarthAsia} />,
        title: 'Ngôn ngữ',
        children: {
            title: 'Chọn ngôn ngữ',
            data: [
                { code: 'en', title: 'English' },
                { code: 'vi', title: 'Tiếng Việt' },
            ],
        },
    },
];

function Header() {
    const auth = useSelector((state) => state.auth);
    const user = useSelector((state) => state.user);

    const navigate = useNavigate();
    const dispatch = useDispatch();
    useEffect(() => {
        if (!auth.token) {
            const jwt = JSON.parse(localStorage.getItem(config.localStorageKey.auth));
            if (jwt && jwt.token) {
                const { token, exp } = jwt;
                const action = loginAction({ token, exp });
                dispatch(action);
            }
        }
        if (!user.username) {
            const userStore = JSON.parse(localStorage.getItem(config.localStorageKey.user));
            if (userStore && userStore.username) {
                const actionSetUser = setUser(userStore);
                dispatch(actionSetUser);
            }
        }
    });

    const currentUser = auth.token ? true : false;

    // const hanleChangeMenu = (menuItem) => {
    //     console.log(menuItem);
    // };
    const userMenu = [
        {
            icon: <FontAwesomeIcon icon={faUser} />,
            title: 'Thông tin người dùng',
            to: config.routes.user.path,
        },
        {
            icon: <FontAwesomeIcon icon={faCreditCard} />,
            title: 'Thanh toán',
            to: '/feedback',
        },
        ...MENU_ITEMS,
        {
            icon: <FontAwesomeIcon icon={faSignOut} />,
            title: 'Đăng xuất',
            onClick: () => {
                authApi
                    .logout()
                    .then(() => {
                        localStorage.removeItem(config.localStorageKey.auth);
                        localStorage.removeItem(config.localStorageKey.user);

                        const action = logoutAction();
                        const actionResetUser = resetUser();
                        dispatch(action);
                        dispatch(actionResetUser);
                        navigate('/');
                    })
                    .catch((err) => {
                        localStorage.removeItem(config.localStorageKey.auth);
                        localStorage.removeItem(config.localStorageKey.user);
                        const action = logoutAction();
                        dispatch(action);
                        navigate('/');
                    });
            },
            separate: true,
        },
    ];

    const renderNavIcon = () => {
        if (user) {
            if (user.user_type === 'customer') {
                return (
                    <Tippy content="Farmstay của tôi" placement="bottom" animation="shift-away" duration={[150, 0]}>
                        <Link to="/user/farmstay/dashboard">
                            <button className={cx('action-btn')}>
                                <FontAwesomeIcon icon={faHouseSignal} />
                            </button>
                        </Link>
                    </Tippy>
                );
            } else if (user.user_type === 'employee') {
                return (
                    <Tippy content="Nhân viên" placement="bottom" animation="shift-away" duration={[150, 0]}>
                        <Link to="/employee">
                            <button className={cx('action-btn')}>
                                <FontAwesomeIcon icon={faUsers} />
                            </button>
                        </Link>
                    </Tippy>
                );
            }
        }
    };
    return (
        <header className={cx('wrapper')}>
            <div className={cx('inner')}>
                <Link to={config.routes.home.path}>
                    <div className={cx('logo')}>
                        <svg width="54" height="54">
                            <image href={images.logo} width="54" height="54" />
                        </svg>
                    </div>
                </Link>
                {/* Search */}
                <Search></Search>
                <div className={cx('action')}>
                    {currentUser ? (
                        <>
                            {renderNavIcon()}
                            <Tippy content="Thông báo" placement="bottom" animation="shift-away" duration={[150, 0]}>
                                <button className={cx('action-btn')}>
                                    <FontAwesomeIcon icon={faBell} />
                                    {/* <NotifiIcon></NotifiIcon> */}
                                </button>
                            </Tippy>
                        </>
                    ) : (
                        <>
                            <Button outline to={[config.routes.auth.path, config.routes.auth.child.login].join('/')}>
                                Đăng Nhập
                            </Button>
                            <Button primary to={[config.routes.auth.path, config.routes.auth.child.register].join('/')}>
                                Đăng Ký
                            </Button>
                        </>
                    )}
                    {currentUser && (
                        <Menu items={userMenu}>
                            <Image
                                className={cx('user-avatar')}
                                src={images.noUser}
                                alt="Tang Ho Trung Nam"
                                fallBack={images.noUser}
                            ></Image>
                        </Menu>
                    )}
                </div>
            </div>
        </header>
    );
}

export default Header;
