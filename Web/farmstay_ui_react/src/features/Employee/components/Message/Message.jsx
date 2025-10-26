import classNames from 'classnames/bind';
import styles from './Message.module.scss';
import Tippy from '@tippyjs/react';
import 'tippy.js/dist/tippy.css';
import 'tippy.js/animations/shift-away.css';
import moment from 'moment';
import 'moment/locale/vi';
import { useState } from 'react';
// import images from '~/assets/images';
const cx = classNames.bind(styles);

function Message({ own, children, message }) {
    const [time, setTime] = useState('');
    const formatDate = (date) => {
        const dateMessage = Date.parse(date);
        const dateNow = Date.now();
        const momentVi = moment(date);
        const checkThan1Day = dateNow - dateMessage > 86_400_000;
        const checkThan1Hour = dateNow - dateMessage > 3_600_000;
        if (checkThan1Hour && !checkThan1Day) {
            return momentVi.format('LT');
        } else if (checkThan1Day) {
            return momentVi.format('lll');
        } else {
            return momentVi.fromNow();
        }
    };
    return (
        <div className={cx('wrapper', { 'wrapper-own': own })}>
            <Tippy
                content={time}
                placement={own ? 'left' : 'right'}
                animation="shift-away"
                duration={[150, 0]}
                onShow={() => {
                    setTime(formatDate(message.createdAt));
                }}
            >
                <div
                    className={cx('wrapper-content', {
                        'wrapper-content-own': own,
                    })}
                >
                    <p className="mb-0">{children}</p>
                </div>
            </Tippy>
        </div>
    );
}

export default Message;
