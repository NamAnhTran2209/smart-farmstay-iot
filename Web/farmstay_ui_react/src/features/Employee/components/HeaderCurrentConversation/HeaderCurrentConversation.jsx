import classNames from 'classnames/bind';
import styles from './HeaderCurrentConversation.module.scss';
import images from '~/assets/images';
import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';
const cx = classNames.bind(styles);

function HeaderCurrentConversation({ conversation }) {
    // console.log(conversation);
    const user = useSelector((state) => state.user);
    const [conversationName, setConversationName] = useState('');
    useEffect(() => {
        if (user && conversation) {
            const { members } = conversation;
            const {
                employee: { id },
            } = user;
            const memberChat = Array.from(members).find((member) => {
                return member.id !== id;
            });
            setConversationName(memberChat.fullname);
        }
    }, [user, conversation]);
    return (
        <>
            <img src={images.noUser} alt="avatar" className={cx('image')} />
            <div className={cx('title')}>
                <h5>{conversationName}</h5>
            </div>
        </>
    );
}

export default HeaderCurrentConversation;
