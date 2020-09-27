package suncrafterina.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import suncrafterina.enums.Currency;

public class LoginProfileDTO {

    private String token;

    private Long user_id;

    private String email;

    private String first_name;

    private String last_name;

    private String image_file;

    private Boolean profile_status;

    private String lang;

    private String role;

    private Currency currency;

    public LoginProfileDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }

    public Boolean getProfile_status() {
        return profile_status;
    }

    public void setProfile_status(Boolean profile_status) {
        this.profile_status = profile_status;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "LoginProfileDTO{" +
            "user_id=" + user_id +
            ", email='" + email + '\'' +
            ", first_name='" + first_name + '\'' +
            ", last_name='" + last_name + '\'' +
            ", image_file='" + image_file + '\'' +
            ", profile_status=" + profile_status +
            ", lang='" + lang + '\'' +
            ", role='" + role + '\'' +
            ", currency='" + currency + '\'' +
            '}';
    }
}
