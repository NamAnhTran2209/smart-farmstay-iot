import socketio from 'socket.io-client';

import React from 'react';
import config from '~/config';

export const socket = () => {
    const { token } = JSON.parse(localStorage.getItem(config.localStorageKey.auth));
    return socketio.connect(config.socketURL + '/employee_chat', {
        extraHeaders: {
            'authenticate-jwt': token,
        },
        autoConnect: false,
        reconnection: false,
    });
};

export const SocketChatContext = React.createContext();
