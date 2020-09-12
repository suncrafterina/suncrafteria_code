package suncrafterina.service.dto;

import javax.validation.constraints.NotBlank;

public class VerificationCode {

    @NotBlank
    private String verification_code;

    public VerificationCode() {
    }

    public VerificationCode(@NotBlank String verification_code) {
        this.verification_code = verification_code;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    @Override
    public String toString() {
        return "VerificationCode{" +
            "verification_code='" + verification_code + '\'' +
            '}';
    }
}
