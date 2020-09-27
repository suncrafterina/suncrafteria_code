package suncrafterina.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigInteger;
import java.time.LocalDate;

public class ClientListDTO {

    private BigInteger id;

    private String title;

    private String image_url;

    private String image_ur_thumb;

    private Boolean is_active;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate created_at;

    public ClientListDTO() {
    }

    public ClientListDTO(BigInteger id, String title, String image_url, String image_ur_thumb, Boolean is_active, LocalDate created_at) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
        this.image_ur_thumb = image_ur_thumb;
        this.is_active = is_active;
        this.created_at = created_at;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_ur_thumb() {
        return image_ur_thumb;
    }

    public void setImage_ur_thumb(String image_ur_thumb) {
        this.image_ur_thumb = image_ur_thumb;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "ClientListDTO{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", image_url='" + image_url + '\'' +
            ", image_ur_thumb='" + image_ur_thumb + '\'' +
            ", is_active=" + is_active +
            ", created_at=" + created_at +
            '}';
    }
}
