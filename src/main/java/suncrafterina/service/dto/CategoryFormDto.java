package suncrafterina.service.dto;

import org.springframework.web.multipart.MultipartFile;
import suncrafterina.constraint.ValidCategory;
import suncrafterina.constraint.ValidImage;
import suncrafterina.enums.CategoryLevelEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ValidCategory
public class CategoryFormDto {


    private Long id;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String title;

    @ValidImage
    private MultipartFile image_file;

    @NotNull
    private Long parent_id;

    @NotNull
    private CategoryLevelEnum category_level;

    @ValidImage
    private MultipartFile icon_file;

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

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public CategoryLevelEnum getCategory_level() {
        return category_level;
    }

    public void setCategory_level(CategoryLevelEnum category_level) {
        this.category_level = category_level;
    }

    public MultipartFile getIcon_file() {
        return icon_file;
    }

    public void setIcon_file(MultipartFile icon_file) {
        this.icon_file = icon_file;
    }

    public CategoryFormDto() {

    }

    @Override
    public String toString() {
        return "CategoryFormDto{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", image_file=" + image_file +
            ", parent_id=" + parent_id +
            ", category_level=" + category_level +
            ", icon_file=" + icon_file +
            '}';
    }
}
