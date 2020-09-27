package suncrafterina.service.dto;

import org.springframework.web.multipart.MultipartFile;
import suncrafterina.constraint.ValidImage;

import javax.validation.constraints.NotBlank;

public class ClientUpdateDTO {

    private Long id;

    @NotBlank
    private String title;

    @ValidImage
    private MultipartFile image_file;

    public ClientUpdateDTO() {
    }

    public ClientUpdateDTO(Long id, @NotBlank String title, MultipartFile image_file) {
        this.id = id;
        this.title = title;
        this.image_file = image_file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MultipartFile getImage_file() {
        return image_file;
    }

    public void setImage_file(MultipartFile image_file) {
        this.image_file = image_file;
    }

    @Override
    public String toString() {
        return "ClientUpdateDTO{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", image_file=" + image_file +
            '}';
    }
}
