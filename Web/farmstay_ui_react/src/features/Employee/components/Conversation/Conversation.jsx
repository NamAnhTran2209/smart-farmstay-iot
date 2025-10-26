import classNames from 'classnames/bind';
import styles from './Conversation.module.scss';
import images from '~/assets/images';
import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';

import 'moment/locale/vi';

const cx = classNames.bind(styles);

function Conversation({ conversation, active, onClick }) {
    const user = useSelector((state) => state.user);
    const [conversationName, setConversationName] = useState('');
    const [latestMessage, setLatestMessage] = useState('');
    // const [newMessage, setNewMessage] = useState(false);

    useEffect(() => {
        if (user) {
            const { members } = conversation;
            const {
                employee: { id },
            } = user;
            const memberChat = Array.from(members).find((member) => {
                return member.id !== id;
            });
            setConversationName(memberChat.fullname);
            if (conversation.latest_message) {
                const { latest_message } = conversation;
                const { sender_id } = latest_message;
                let memberSend = 'Bạn';
                if (sender_id === memberChat.id) {
                    memberSend = memberChat.fullname;
                }
                setLatestMessage(`${memberSend}: ${latest_message.content}`);
            }
        }
    }, [user, conversation, conversation.latest_message]);

    return (
        <div
            className={cx('wrapper', {
                active: active,
            })}
            onClick={onClick}
        >
            <img src={images.noUser} alt="avatar" className={cx('image')} />
            <div className={cx('content')}>
                <h5 className={cx('content-title')}>{conversationName}</h5>
                <p className={cx('content-message')}>{latestMessage}</p>
            </div>
            {/* {!!newMessage && (
                <div className={cx('noti-wrapper')}>
                    <span className={cx('noti')}></span>
                </div>
            )} */}
        </div>
    );
}

export default Conversation;
