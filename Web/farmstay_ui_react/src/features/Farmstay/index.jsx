import React from 'react';
import { Outlet, Route, Routes } from 'react-router-dom';
import config from '~/config';
import { DefaultLayout } from '~/layouts';
import FarmstayDetail from './pages/FarmstayDetail/FarmstayDetail';
import StatusPayment from './pages/StatusPaymentDeposit/StatusPaymentDeposit';

function Farmstay() {
    return (
        <>
            <Routes>
                <Route
                    path={config.routes.farmstays.child.detail}
                    element={
                        <DefaultLayout>
                            <FarmstayDetail />
                        </DefaultLayout>
                    }
                />
                <Route
                    path={config.routes.farmstays.child.detail + '/status_payment_deposit'}
                    element={
                        <DefaultLayout>
                            <StatusPayment />
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

export default Farmstay;
