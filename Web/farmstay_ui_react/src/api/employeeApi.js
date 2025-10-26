import axiosClient from './axiosClient';

const employeeApi = {
    getConversations: ({ token }) => {
        const url = '/employee/conversations';
        return axiosClient.get(url, { headers: { 'authenticate-jwt': token } });
    },
    getMessagesOfConversation: ({ token, conversationId }) => {
        const url = `/employee/messages/${conversationId}`;
        return axiosClient.get(url, { headers: { 'authenticate-jwt': token } });
    },
    sendMessage: ({ token, conversationId, text }) => {
        const url = `/employee/messages`;
        return axiosClient.post(
            url,
            { conversation_id: conversationId, text },
            { headers: { 'authenticate-jwt': token } },
        );
    },
    getAllEmployeesNoConversation: ({ token }) => {
        const url = '/employee/conversations/employees';
        return axiosClient.get(url, { headers: { 'authenticate-jwt': token } });
    },
    createConversation: ({ token, receiverId }) => {
        const url = '/employee/conversations';
        return axiosClient.post(url, { receiver_id: receiverId }, { headers: { 'authenticate-jwt': token } });
    },
};

export default employeeApi;
