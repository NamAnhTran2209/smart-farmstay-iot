import React from 'react';
import { Outlet, Route, Routes } from 'react-router-dom';
import config from '~/config';
import { EmptyLayout, DefaultLayout } from '~/layouts';
import ActiveUser from './pages/ActiveUser';
import FarmstayDashboard from './pages/FarmstayDashboard';
import UserInfo from './pages/UserInfo';
import ForgotPassword from './pages/ForgotPassword/ForgotPassword';
import { socket } from '~/context/socketFarmstayContext';

function User() {
    return (
        <>
            <Routes>
                <Route
                    path={''}
                    element={
                        <DefaultLayout>
                            <UserInfo />
                        </DefaultLayout>
                    }
                />
                <Route
                    path={config.routes.user.child.active}
                    element={
                        <EmptyLayout>
                            <ActiveUser />
                        </EmptyLayout>
                    }
                />
                <Route
                    path={config.routes.user.child.dashboard}
                    element={
                        <DefaultLayout>
                            <FarmstayDashboard socket={socket()} />
                        </DefaultLayout>
                    }
                />
                <Route
                    path={config.routes.user.child.forgotPassword}
                    element={
                        <DefaultLayout>
                            <ForgotPassword />
                        </DefaultLayout>
                    }
                />
                <Route
                    path="*"
                    element={
                        <>
                            <div>Page Not Found</div>
                        </>
                    }
                />
            </Routes>
            <Outlet />
        </>
    );
}

export default User;
