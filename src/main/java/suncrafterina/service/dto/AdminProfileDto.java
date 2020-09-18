package suncrafterina.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AdminProfileDto {

    private Long user_id;

    @NotBlank
    @Size(max = 25)
    private String first_name;

    @NotBlank
    @Size(max = 25)
    private String last_name;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private String image_url;

    public AdminProfileDto() {
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        return "AdminProfileDto{" +
            "user_id=" + user_id +
            ", first_name='" + first_name + '\'' +
            ", last_name='" + last_name + '\'' +
            ", email='" + email + '\'' +
            ", image_url='" + image_url + '\'' +
            '}';
    }
}
