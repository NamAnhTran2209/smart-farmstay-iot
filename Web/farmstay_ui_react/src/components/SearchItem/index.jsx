import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import classNames from 'classnames/bind';
import { Link } from 'react-router-dom';
import styles from './SearchItem.module.scss';
import Image from '~/components/Image';
import PropTypes from 'prop-types';

const cx = classNames.bind(styles);

function SearchItem({ data }) {
    return (
        <Link to={`/@${data.nickname}`} className={cx('wrapper')}>
            <Image className={cx('avatar')} src={data.avatar} alt={data.full_name} />
            <div className={cx('info')}>
                <h4 className={cx('title')}>
                    <span>{data.full_name}</span>
                    {data.tick && <FontAwesomeIcon className={cx('check')} icon={faCheckCircle} />}
                </h4>
                <span className={cx('sub-title')}>{data.nickname}</span>
            </div>
        </Link>
    );
}
SearchItem.propTypes = {
    data: PropTypes.object.isRequired,
};
export default SearchItem;
