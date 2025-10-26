import axiosClient from './axiosClient';

const farmstayApi = {
    getAllFarmstay: () => {
        const url = '/farmstays';
        return axiosClient.get(url);
    },
    getDetailFarmstay: (token, farmstayUUID) => {
        const url = `/farmstays/${farmstayUUID}`;
        return axiosClient.get(url, { headers: { 'authenticate-jwt': token } });
    },
    createPaymentURL: ({ token, farmstayUUID, bankCode = '', language = 'vi', returnURL }) => {
        const url = `/farmstays/${farmstayUUID}/create_deposit_payment_url`;
        return axiosClient.post(
            url,
            { bank_code: bankCode, language, return_url: returnURL },
            { headers: { 'authenticate-jwt': token } },
        );
    },
    checkDepositPayment: ({ token, farmstayUUID, body }) => {
        const url = `/farmstays/${farmstayUUID}/check_deposit_payment`;
        return axiosClient.post(url, body, { headers: { 'authenticate-jwt': token } });
    },
};

export default farmstayApi;
