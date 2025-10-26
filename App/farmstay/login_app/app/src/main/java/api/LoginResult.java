package api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class LoginResult {

    private String email;
    private String username;
    private JsonObject data;
    private String msg;
    private String message;
    private String status;
    private String msg_vi;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsg_vi() {
        return msg_vi;
    }

    public void setMsg_vi(String msg_vi) {
        this.msg_vi = msg_vi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                ", msg='" + msg + '\'' +
                ", msg_vi='" + msg_vi + '\'' +
                '}';
    }
}
