package suncrafterina.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="jhi_client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String image_file;

    @Column
    private String image_file_thumb;

    @Column
    private Boolean is_active;

    @Column
    private LocalDateTime created_at;

    @Column
    private LocalDateTime updated_at;

    public Client() {
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

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
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

    @Override
    public String toString() {
        return "Client{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", image_file='" + image_file + '\'' +
            ", image_file_thumb='" + image_file_thumb + '\'' +
            ", is_active=" + is_active +
            ", created_at=" + created_at +
            ", updated_at=" + updated_at +
            '}';
    }
}
