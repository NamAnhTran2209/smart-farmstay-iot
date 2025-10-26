// const routes = {
//     home: '/',
//     farmstay: '/farmstays',
//     login: '/login',
// };

const routes = {
    auth: {
        path: '/auth',
        child: {
            login: 'login',
            register: 'register',
        },
    },
    home: {
        path: '/',
    },
    farmstays: {
        path: '/farmstays',
        child: {
            detail: ':farmstay_uuid',
        },
    },
    user: {
        path: '/user',
        child: {
            active: 'active',
            dashboard: 'farmstay/dashboard',
            forgotPassword: 'forgot-password',
        },
    },
    employee: {
        path: '/employee',
        child: {
            chat: 'chat',
        },
    },
};
export default routes;
