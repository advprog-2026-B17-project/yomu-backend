package id.ac.ui.cs.advprog.yomubackend.auth.dto;

public class UpdateProfileRequest {
    private String displayName;
    private String phoneNumber;
    private String password;

    public UpdateProfileRequest() {}

    public UpdateProfileRequest(String displayName, String phoneNumber, String password) {
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
