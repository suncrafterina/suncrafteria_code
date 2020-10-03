package suncrafterina.service.dto;

import org.springframework.web.multipart.MultipartFile;
import suncrafterina.constraint.ValidImage;

import javax.validation.constraints.NotNull;

public class BannerUpdateDTO {

    @NotNull
    private Long id;

    @NotNull
    private Long type_id;

    @ValidImage
    private MultipartFile image_file;

    public BannerUpdateDTO() {
    }

    public BannerUpdateDTO(@NotNull Long id, @NotNull Long type_id, MultipartFile image_file) {
        this.id = id;
        this.type_id = type_id;
        this.image_file = image_file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getType_id() {
        return type_id;
    }

    public void setType_id(Long type_id) {
        this.type_id = type_id;
    }

    public MultipartFile getImage_file() {
        return image_file;
    }

    public void setImage_file(MultipartFile image_file) {
        this.image_file = image_file;
    }
}
