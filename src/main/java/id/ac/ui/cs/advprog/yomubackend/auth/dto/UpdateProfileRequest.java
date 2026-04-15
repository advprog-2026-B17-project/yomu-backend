package id.ac.ui.cs.advprog.yomubackend.auth.dto;

public class UpdateProfileRequest {
    private String displayName;
    private String email;
    private String phoneNumber;
    private String password;

    public UpdateProfileRequest() {}

    public UpdateProfileRequest(String displayName, String email, String phoneNumber, String password) {
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
