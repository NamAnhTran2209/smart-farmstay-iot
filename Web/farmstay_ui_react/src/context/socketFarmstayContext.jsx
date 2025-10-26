import socketio from 'socket.io-client';

import React from 'react';
import config from '~/config';

export const socket = () => {
    const auth = JSON.parse(localStorage.getItem(config.localStorageKey.auth));
    return socketio.connect(config.socketURL + '/farmstay', {
        extraHeaders: {
            'authenticate-jwt': auth ? auth.token : '',
        },
        autoConnect: false,
        reconnection: false,
    });
};
export const SocketFarmstayContext = React.createContext();
