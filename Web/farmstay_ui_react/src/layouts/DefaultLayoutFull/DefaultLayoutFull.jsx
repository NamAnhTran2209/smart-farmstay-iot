import styles from './DefaultLayoutFull.module.scss';
import classNames from 'classnames/bind';
import Header from '~/layouts/components/Header';
import PropTypes from 'prop-types';
const cx = classNames.bind(styles);

function DefaultLayout({ children }) {
    return (
        <>
            <Header />
            <div className={cx('wrapper')}>
                <div className={cx('container')}>
                    <div className={cx('content')}>{children}</div>
                </div>
            </div>
        </>
    );
}
DefaultLayout.propTypes = {
    children: PropTypes.node.isRequired,
};
export default DefaultLayout;
