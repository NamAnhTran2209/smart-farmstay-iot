import React from 'react';
import { Outlet, Route, Routes } from 'react-router-dom';
import config from '~/config';
import { EmptyLayout } from '~/layouts';
import Login from './pages/Login';
import Register from './pages/Register';

function Auth() {
    return (
        <>
            <Routes>
                <Route
                    path={config.routes.auth.child.login}
                    element={
                        <EmptyLayout>
                            <Login />
                        </EmptyLayout>
                    }
                />
                <Route
                    path={config.routes.auth.child.register}
                    element={
                        <EmptyLayout>
                            <Register />
                        </EmptyLayout>
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

export default Auth;
