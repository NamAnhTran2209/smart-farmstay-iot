import styles from './EmptyLayout.module.scss';
import classNames from 'classnames/bind';

import PropTypes from 'prop-types';
const cx = classNames.bind(styles);

function NoHeaderLayout({ children }) {
    return (
        <>
            <div className={cx('wrapper')}>{children}</div>
        </>
    );
}
NoHeaderLayout.propTypes = {
    children: PropTypes.node.isRequired,
};
export default NoHeaderLayout;
