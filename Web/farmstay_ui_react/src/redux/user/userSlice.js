import { createSlice } from '@reduxjs/toolkit';

const userInitialState = {
    username: null,
    user_type: null,
    email: null,
    user_id: null,
    is_active: null,
    customer: null,
    employee: null,
};
const user = createSlice({
    name: 'user',
    initialState: userInitialState,
    reducers: {
        setUser: (state, action) => {
            const { username, user_type, email, user_id, is_active } = action.payload;
            Object.assign(state, { username, user_type, email, user_id, is_active });
            if (user_type === 'customer') {
                const { customer } = action.payload;
                Object.assign(state, { customer });
            } else if (user_type === 'employee') {
                const { employee } = action.payload;
                Object.assign(state, { employee });
            }
        },
        resetUser: (state, action) => {
            Object.assign(state, null);
        },
    },
});

const { reducer, actions } = user;
export const { resetUser, setUser } = actions;
export default reducer;
