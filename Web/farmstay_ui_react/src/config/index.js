import routes from './routes';

const config = {
    routes,
    baseURL: process.env.REACT_APP_BASE_URL,
    socketURL: process.env.REACT_APP_SOCKET_URL,
    apiURL: process.env.REACT_APP_API_URL,
    localStorageKey: {
        auth: 'auth',
        user: 'user',
    },
};

export default config;
