import { configureStore } from '@reduxjs/toolkit';
import authReducer from '~/redux/auth/authSlice';
import userReducer from '~/redux/user/userSlice';

const rootReducer = {
    auth: authReducer,
    user: userReducer,
};

const store = configureStore({
    reducer: rootReducer,
});

export default store;
