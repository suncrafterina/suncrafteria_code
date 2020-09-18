package suncrafterina.service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ResetPassword {

    @NotBlank
    private String otp;

    @NotBlank
    @Size(min = 8,max = 32)
    private String new_password;

    public ResetPassword() {
    }

    public ResetPassword(String otp, String new_password) {
        this.otp = otp;
        this.new_password = new_password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    @Override
    public String toString() {
        return "ResetPassword{" +
            "otp='" + otp + '\'' +
            ", new_password='" + new_password + '\'' +
            '}';
    }
}
