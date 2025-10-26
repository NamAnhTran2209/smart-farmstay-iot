import axios from 'axios';
import queryString from 'query-string';
import config from '~/config';

const axiosClient = axios.create({
    baseURL: config.apiURL,
    headers: {
        'Content-Type': 'application/json',
    },
    paramsSerializer: {
        encode: queryString.parse,
        serialize: queryString.stringify,
    },
});

axiosClient.interceptors.request.use(async (config) => {
    return config;
});

axiosClient.interceptors.response.use(
    (response) => {
        if (response && response.data) {
            return response.data;
        }
        return response;
    },
    (error) => {
        throw error;
    },
);

export default axiosClient;
