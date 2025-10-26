import socketio from 'socket.io-client';

import React from 'react';
import config from '~/config';

export const socket = socketio.connect(config.socketURL);
export const SocketContext = React.createContext();
