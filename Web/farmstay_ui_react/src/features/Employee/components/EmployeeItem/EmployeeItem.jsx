import classNames from 'classnames/bind';
import styles from './EmployeeItem.module.scss';
import images from '~/assets/images';
const cx = classNames.bind(styles);

function EmployeeItem({ employee }) {
    return (
        <div className={cx('wrapper', {})}>
            <img src={images.noUser} alt="avatar" className={cx('image')} />
            <div className={cx('title-wrapper')}>
                <h5 style={{ fontSize: '1rem' }}>{employee.fullname}</h5>
            </div>
        </div>
    );
}

export default EmployeeItem;
