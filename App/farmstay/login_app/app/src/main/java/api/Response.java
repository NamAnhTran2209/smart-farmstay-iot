package api;

import com.google.gson.JsonObject;

import org.json.JSONObject;

public class Response {

    private String msg;
    private String msg_vi;
    private String socket_url;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg_vi() {
        return msg_vi;
    }

    public void setMsg_vi(String msg_vi) {
        this.msg_vi = msg_vi;
    }

    public String getSocket_url() {
        return socket_url;
    }

    public void setSocket_url(String socket_url) {
        this.socket_url = socket_url;
    }


    @Override
    public String toString() {
        return "Response{" +
                ", msg='" + msg + '\'' +
                ", msg_vi='" + msg_vi + '\'' +
                '}';
    }
}
