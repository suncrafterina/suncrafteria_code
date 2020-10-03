package suncrafterina.service.dto;


public class BannerDetailDTO {

    private Long id;

    private Long type_id;

    private String name;

    private String image_url;

    private String image_ur_thumb;

    private Boolean is_active;

    public BannerDetailDTO() {
    }

    public BannerDetailDTO(Long id, Long type_id, String name, String image_url, String image_ur_thumb, Boolean is_active) {
        this.id = id;
        this.type_id = type_id;
        this.name = name;
        this.image_url = image_url;
        this.image_ur_thumb = image_ur_thumb;
        this.is_active = is_active;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


}
