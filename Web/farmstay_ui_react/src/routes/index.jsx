import config from '~/config';
import React from 'react';

const Home = React.lazy(() => import('~/features/Home'));
const Auth = React.lazy(() => import('~/features/Auth'));
const User = React.lazy(() => import('~/features/User'));
const Farmstay = React.lazy(() => import('~/features/Farmstay'));
const Employee = React.lazy(() => import('~/features/Employee'));

// import Home from '~/features/Home';
// import Auth from '~/features/Auth';

//Layout

// Route không cần đăng nhập
const publicRoutes = [
    // { path: config.routes.home, component: Home, layout: DefaultLayout },
    { path: config.routes.home.path, component: Home },
    { path: config.routes.auth.path, component: Auth },
    { path: config.routes.user.path, component: User },
    { path: config.routes.farmstays.path, component: Farmstay },
    { path: config.routes.employee.path, component: Employee },

    // {
    //     path: config.routes.login,
    //     component: Login,
    //     layout: EmptyLayout,
    // },
    // { path: '/following', component: Following, layout: DefaultLayout },
    // { path: '/:nickname', component: Following, layout: HeaderOnlyLayout },
    // { path: '/upload', component: Upload, layout: HeaderOnlyLayout },
];

// Route cần đăng nhập
const privateRoutes = [];

export { publicRoutes, privateRoutes };
