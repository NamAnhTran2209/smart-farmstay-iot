import { createSlice } from '@reduxjs/toolkit';

const authInitialState = {
    token: null,
    exp: null,
};
const auth = createSlice({
    name: 'auth',
    initialState: authInitialState,
    reducers: {
        login: (state, action) => {
            const { token, exp } = action.payload;
            Object.assign(state, { token, exp });
        },
        logout: (state, action) => {
            Object.assign(state, authInitialState);
        },
    },
});

const { reducer, actions } = auth;
export const { login, logout } = actions;
export default reducer;
