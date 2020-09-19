package suncrafterina.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import suncrafterina.enums.CategoryLevelEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name="jhi_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String title;

    @Column
    private String image_file;

    @Column
    private Long parent_id;

    @Enumerated
    @Column(columnDefinition = "smallint")
    @NotNull
    private CategoryLevelEnum category_level;

    @Column
    @NotNull
    private LocalDateTime created_at;

    @Column
    private LocalDateTime updated_at;

    @Column
    private String image_file_thumb;

    @Column
    private String icon_file;

    @Column(columnDefinition = "text",nullable = false,unique = true)
    private String slug;


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

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
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

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
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

    public Category() {

    }

}
