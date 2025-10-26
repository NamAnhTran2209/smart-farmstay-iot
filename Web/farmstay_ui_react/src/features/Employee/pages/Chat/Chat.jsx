import classNames from 'classnames/bind';
import styles from './Chat.module.scss';
import { Button, Col, Container, Row, Stack } from 'react-bootstrap';
import Conversation from '../../components/Conversation';
import Message from '../../components/Message';
import { useSelector } from 'react-redux';
import { useEffect, useRef, useState } from 'react';
import { employeeApi } from '~/api';
import FormSendMessage from '../../components/FormSendMessage';

import HeaderCurrentConversation from '../../components/HeaderCurrentConversation/HeaderCurrentConversation';
import { useNavigate } from 'react-router-dom';
import EmployeeItem from '../../components/EmployeeItem/EmployeeItem';
// import component 👇
import Drawer from 'react-modern-drawer';
//import styles 👇
import 'react-modern-drawer/dist/index.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserGroup } from '@fortawesome/free-solid-svg-icons';

const cx = classNames.bind(styles);

function Chat({ chatSocket }) {
    const auth = useSelector((state) => state.auth);
    const user = useSelector((state) => state.user);

    const [conversations, setConversations] = useState([]);
    const [currentConversation, setCurrentConversation] = useState(null);
    const [messages, setMessages] = useState([]);
    const [employees, setEmployees] = useState([]);

    const [isOpen, setIsOpen] = useState(false);
    const toggleDrawer = () => {
        setIsOpen((prevState) => !prevState);
    };

    const navigate = useNavigate();

    const scrollRef = useRef();
    const { token } = auth;

    useEffect(() => {
        const { user_type } = user;
        if (user_type !== 'employee' && user_type !== null) {
            navigate('/');
        }
    }, [user, navigate]);

    useEffect(() => {
        if (token && user.user_type === 'employee') {
            employeeApi
                .getConversations({ token })
                .then((res) => {
                    setConversations(() => {
                        if (Array.isArray(res.data) && res.data.length > 0) {
                            setCurrentConversation(res.data[0]);
                            return res.data;
                        }
                        return [];
                    });
                })
                .catch((err) => {
                    const {
                        response: {
                            data: { code },
                        },
                    } = err;
                    if (code === 403) {
                        navigate('/');
                    } else if (code === 401) {
                        navigate('/auth/login');
                    }
                });
            chatSocket.open();
            employeeApi
                .getAllEmployeesNoConversation({ token })
                .then((res) => {
                    setEmployees(() => {
                        if (Array.isArray(res.data)) {
                            return res.data;
                        }
                        return [];
                    });
                })
                .catch((err) => {
                    const {
                        response: {
                            data: { code },
                        },
                    } = err;
                    if (code === 403) {
                        navigate('/');
                    } else if (code === 401) {
                        navigate('/auth/login');
                    }
                });
        }

        return () => {
            chatSocket.close();
        };
    }, [token, navigate, user, chatSocket]);

    useEffect(() => {
        if (token && user && currentConversation) {
            const func = (res) => {
                const {
                    employee: { id },
                } = user;
                if (res.conversation_id === currentConversation._id && id !== res.sender_id) {
                    setMessages((prev) => {
                        return [...prev, res];
                    });
                }
                setConversations((prev) => {
                    const data = res;
                    const index = prev.findIndex((conversation) => {
                        const { _id } = conversation;
                        const { conversation_id } = data;
                        return _id === conversation_id;
                    });

                    if (index > -1) {
                        prev[index]['latest_message'] = data;
                    }
                    return [...prev];
                });
            };
            chatSocket.on('new_message', func);

            return () => {
                chatSocket.removeListener('new_message', func);
            };
        }
    }, [token, user, currentConversation, chatSocket]);

    useEffect(() => {
        const funcNewConversation = (res) => {
            chatSocket.emit('join_conversation', { conversation_id: res._id });
            setConversations((prev) => {
                return [res, ...prev];
            });
            if (currentConversation === null) {
                setCurrentConversation(res);
            }
            console.log(res);
        };
        chatSocket.on('new_conversation', funcNewConversation);

        return () => {
            chatSocket.removeListener('new_conversation', funcNewConversation);
        };
    }, [currentConversation, chatSocket]);

    useEffect(() => {
        if (token && currentConversation) {
            if (currentConversation.is_new_conversation === true) {
                setMessages([]);
            } else {
                employeeApi
                    .getMessagesOfConversation({ token, conversationId: currentConversation._id })
                    .then((res) => {
                        setMessages(res.data);
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            }
        }
    }, [currentConversation, token]);

    useEffect(() => {
        scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }, [messages]);

    const handleSubmitSendMessage = (values, { resetForm }) => {
        const { message } = values;
        if (token && currentConversation && message !== '') {
            if (currentConversation.is_new_conversation === true) {
                const { members } = currentConversation;
                const { employee } = user;
                const index = members.findIndex((member) => {
                    return member.id !== employee.id;
                });
                if (index > -1) {
                    const { id: receiverId } = members[index];
                    employeeApi
                        .createConversation({ token, receiverId })
                        .then((res) => {
                            const { data } = res;
                            chatSocket.emit('join_conversation', { conversation_id: data._id });
                            setCurrentConversation(data);
                            setEmployees((prev) => {
                                const employeeRemove = prev.findIndex((item) => {
                                    return item.id === receiverId;
                                });
                                if (employeeRemove > -1) {
                                    prev.splice(employeeRemove, 1);
                                }
                                return prev;
                            });
                            const { _id } = data;
                            // send message
                            employeeApi
                                .sendMessage({ token, conversationId: _id, text: message })
                                .then((res) => {
                                    setMessages((prev) => {
                                        return [...prev, res.data];
                                    });
                                    setConversations((prev) => {
                                        data['latest_message'] = res.data;
                                        return [data, ...prev];
                                    });
                                    resetForm();
                                })
                                .catch((err) => {
                                    console.log(err);
                                });
                        })
                        .catch((err) => {
                            console.log(err);
                        });
                }
            } else {
                const { _id } = currentConversation;
                employeeApi
                    .sendMessage({ token, conversationId: _id, text: message })
                    .then((res) => {
                        setMessages((prev) => {
                            return [...prev, res.data];
                        });
                        setConversations((prev) => {
                            const { data } = res;
                            const index = prev.findIndex((conversation) => {
                                const { _id } = conversation;
                                const { conversation_id } = data;
                                return _id === conversation_id;
                            });
                            if (index > -1) {
                                prev[index]['latest_message'] = data;
                            }
                            return [...prev];
                        });
                        resetForm();
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            }
        }
    };
    return (
        <Container fluid className={cx('wrapper')}>
            <Row>
                <Col md={5} lg={4} xl={3} className={cx('member')}>
                    <div className={cx('member-header')}>
                        <h3 className="fw-bold">Chat Nội Bộ</h3>
                    </div>
                    <div className={cx('member-body')}>
                        <Stack>
                            {conversations.map((conversation) => {
                                return (
                                    <Conversation
                                        key={conversation._id}
                                        onClick={() => {
                                            if (currentConversation._id !== conversation._id) {
                                                setCurrentConversation(conversation);
                                            }
                                        }}
                                        active={currentConversation && currentConversation._id === conversation._id}
                                        conversation={conversation}
                                    />
                                );
                            })}
                        </Stack>
                    </div>
                </Col>
                <Col md={6} lg={7} xl={8} className="g-0">
                    <div className={cx('messages-wrapper')}>
                        <div className={cx('messages-header')}>
                            <HeaderCurrentConversation conversation={currentConversation} />
                        </div>
                        <div ref={scrollRef} className={cx('messages-body')}>
                            {messages.length > 0 && user && user.employee ? (
                                messages.map((message) => {
                                    const { employee } = user;
                                    // console.log('message:', message);
                                    // console.log('employee:', employee);
                                    return (
                                        <Message
                                            message={message}
                                            key={message._id}
                                            own={employee.id === message.sender_id}
                                        >
                                            {message.content}
                                        </Message>
                                    );
                                })
                            ) : (
                                <div className="mt-4">
                                    <p className="text-center fs-5">Gửi tin nhắn để bắt đầu cuộc trò chuyện</p>
                                </div>
                            )}
                        </div>
                        <div className={cx('messages-footer')}>
                            <FormSendMessage
                                token={token}
                                currentConversation={currentConversation}
                                onSubmit={handleSubmitSendMessage}
                            />
                        </div>
                    </div>
                </Col>
                <Col md={1} lg={1} xl={1} className={cx('member')}>
                    <div className={cx('member-body')}>
                        <Button variant="success" className="w-100 mt-4" onClick={toggleDrawer}>
                            <FontAwesomeIcon icon={faUserGroup} />
                        </Button>
                        <Drawer open={isOpen} onClose={toggleDrawer} direction="right" className={cx('drawer')}>
                            <div className={cx('member-header')}>
                                <h3 className="fw-bold">Danh sách nhân viên</h3>
                            </div>
                            <Stack>
                                {employees.map((employee) => {
                                    return (
                                        <div
                                            key={employee.id}
                                            onClick={() => {
                                                const memberSend = {
                                                    id: user.employee.id,
                                                    fullname: user.employee.fullname,
                                                };
                                                const memberReceive = { id: employee.id, fullname: employee.fullname };

                                                const newConversation = { is_new_conversation: true };
                                                newConversation['name'] = employee.fullname;
                                                newConversation['members'] = [memberSend, memberReceive];
                                                // console.log(newConversation);
                                                // console.log(currentConversation);
                                                setIsOpen(false);
                                                setCurrentConversation(newConversation);
                                            }}
                                        >
                                            <EmployeeItem employee={employee} />
                                        </div>
                                    );
                                })}
                            </Stack>
                        </Drawer>
                    </div>
                </Col>
            </Row>
        </Container>
    );
}

export default Chat;
