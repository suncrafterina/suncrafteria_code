package suncrafterina.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RegisterUserDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String role;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public RegisterUserDto() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "RegisterUserDto{" +
            "email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", role='" + role + '\'' +
            '}';
    }
}
