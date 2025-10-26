import * as httpRequest from '~/utils/httpRequest';

export const search = async (query, type = 'less') => {
    try {
        const res = await httpRequest.get(`/users/search`, {
            params: {
                q: query,
                type,
            },
        });
        const { data } = res;
        return data;
    } catch (error) {
        throw error;
    }
};
