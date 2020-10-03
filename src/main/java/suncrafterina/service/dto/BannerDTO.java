package suncrafterina.service.dto;

import org.springframework.web.multipart.MultipartFile;
import suncrafterina.constraint.ValidImage;

import javax.validation.constraints.NotNull;

public class BannerDTO {

    private Long id;

    @NotNull
    private Long type_id;

   // @NotNull
    @ValidImage
    private MultipartFile image_file;

    private Boolean is_active;

    public BannerDTO() {
    }

    public BannerDTO(Long id, @NotNull Long type_id, MultipartFile image_file, Boolean is_active) {
        this.id = id;
        this.type_id = type_id;
        this.image_file = image_file;
        this.is_active = is_active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MultipartFile getImage_file() {
        return image_file;
    }

    public void setImage_file(MultipartFile image_file) {
        this.image_file = image_file;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Long getType_id() {
        return type_id;
    }

    public void setType_id(Long type_id) {
        this.type_id = type_id;
    }
}
