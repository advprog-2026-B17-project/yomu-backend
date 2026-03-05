package id.ac.ui.cs.advprog.yomubackend.auth.dto;

public class AuthResponse {
    private String token;
    private String message;

    // Constructor Kosong
    public AuthResponse() {}

    // Constructor Isi
    public AuthResponse(String token) {
        this.token = token;
        this.message = "Authentication Successful";
    }

    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // Getter & Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
