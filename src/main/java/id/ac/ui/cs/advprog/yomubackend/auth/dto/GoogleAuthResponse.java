package id.ac.ui.cs.advprog.yomubackend.auth.dto;

public class GoogleAuthResponse {
    private String token;
    private boolean isNewUser;

    public GoogleAuthResponse() {}

    public GoogleAuthResponse(String token, boolean isNewUser) {
        this.token = token;
        this.isNewUser = isNewUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean isNewUser) {
        this.isNewUser = isNewUser;
    }
}
