package suncrafterina.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EmailDto {

    @NotBlank
    @Email
    private String email;

    public EmailDto() {
    }

    public EmailDto(@NotBlank String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EmailDto{" +
            "email='" + email + '\'' +
            '}';
    }
}
