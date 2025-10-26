import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { publicRoutes } from '~/routes';

import { Suspense } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

const loading = <div>...loading</div>;
function App() {
    return (
        <Suspense fallback={loading}>
            <Router>
                <div className="App">
                    <Routes>
                        {publicRoutes.map((route) => {
                            const PageComponent = route.component;
                            return <Route key={route.path} path={`${route.path}/*`} element={<PageComponent />} />;
                        })}

                        <Route
                            path="*"
                            element={
                                <>
                                    <div>Page Not Found</div>
                                </>
                            }
                        />
                    </Routes>
                </div>
            </Router>
        </Suspense>
    );
}

export default App;
