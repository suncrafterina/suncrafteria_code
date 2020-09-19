package suncrafterina.service.dto;


import suncrafterina.enums.CategoryLevelEnum;

public class CategoryLevelTwoDto {

    public Long id;

    public String title;

    public Long parent_id;

    public CategoryLevelEnum category_level;

    public Long product_count=null;

    public String image_file;

    public String image_file_thumb;

    public String icon_file;

    public String slug;

    public CategoryLevelTwoDto() {
    }

    public CategoryLevelTwoDto(Long id, String title, Long parent_id, CategoryLevelEnum category_level, Long product_count, String image_file, String image_file_thumb, String icon_file, String slug) {
        this.id = id;
        this.title = title;
        this.parent_id = parent_id;
        this.category_level = category_level;
        this.product_count = product_count;
        this.image_file = image_file;
        this.image_file_thumb = image_file_thumb;
        this.icon_file = icon_file;
        this.slug = slug;
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

    public Long getProduct_count() {
        return product_count;
    }

    public void setProduct_count(Long product_count) {
        this.product_count = product_count;
    }

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }

    public String getImage_file_thumb() {
        return image_file_thumb;
    }

    public void setImage_file_thumb(String image_file_thumb) {
        this.image_file_thumb = image_file_thumb;
    }

    public String getIcon_file() {
        return icon_file;
    }

    public void setIcon_file(String icon_file) {
        this.icon_file = icon_file;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return "CategoryLevelThreeDto{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", parent_id=" + parent_id +
            ", category_level=" + category_level +
            ", product_count=" + product_count +
            ", image_file='" + image_file + '\'' +
            ", image_file_thumb='" + image_file_thumb + '\'' +
            ", icon_file='" + icon_file + '\'' +
            ", slug='" + slug + '\'' +
            '}';
    }
}
