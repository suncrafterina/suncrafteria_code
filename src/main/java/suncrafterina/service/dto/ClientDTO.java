package suncrafterina.service.dto;

import org.springframework.web.multipart.MultipartFile;
import suncrafterina.constraint.ValidImage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ClientDTO {

    @NotBlank
    private String title;

  //  @NotNull
    @ValidImage
    private MultipartFile image_file;

    public ClientDTO() {
    }

    public ClientDTO(@NotBlank String title, @NotNull MultipartFile image_file) {
        this.title = title;
        this.image_file = image_file;
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
        return "ClientDTO{" +
            "title='" + title + '\'' +
            ", image_file=" + image_file +
            '}';
    }
}
