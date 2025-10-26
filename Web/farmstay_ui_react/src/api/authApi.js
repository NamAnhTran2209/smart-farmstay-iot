import config from '~/config';
import axiosClient from './axiosClient';

const authApi = {
    login: ({ login, password }) => {
        const url = '/auth/login';
        return axiosClient.post(url, { login, password });
    },
    register: ({ email, username, password }) => {
        const url = '/auth/register';
        return axiosClient.post(url, { email, username, password });
    },
    logout: () => {
        const url = '/auth/logout';
        const auth = JSON.parse(localStorage.getItem(config.localStorageKey.auth));
        const { token } = auth;
        return axiosClient.get(url, { headers: { 'authenticate-jwt': token } });
    },
};

export default authApi;
