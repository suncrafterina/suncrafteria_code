package suncrafterina.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import suncrafterina.enums.LengthUnit;
import suncrafterina.enums.WeightUnit;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="jhi_product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 255,nullable = false)
    private String name;

    @Column(columnDefinition = "text",nullable = false,unique = true)
    private String slug;

    @Column(columnDefinition = "text",nullable = false)
    private String description;

    @Column(length = 255)
    private String image_file;

    @Column(length = 255)
    private String video_file;

    @Column(length = 255,nullable = false)
    private String sku;

    @Column(length = 255)
    private String manufacturer;

    @Column(length = 255)
    private String model;

    @Column(nullable = false,precision =10,scale = 2)
    private Double price;

    @Column
    private String dimension;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private LengthUnit length_unit;

    @Column
    private String weight;

    @Enumerated
    @Column(columnDefinition = "smallint")
    private WeightUnit weight_unit;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private Boolean vendor_show_status = true;

    @Column(nullable = false)
    private Boolean admin_show_status = true;

    @Column
    private LocalDateTime created_at;

    @Column
    private LocalDateTime updated_at;

    @Column
    private String image_file_thumb;

    @Column(nullable = false)
    private Boolean is_sponsored=false;

    @Column(nullable = false)
    private Boolean is_new_arrival=false;

    @Column(nullable = false)
    private Boolean is_top_selling=false;


    public Product() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }

    public String getVideo_file() {
        return video_file;
    }

    public void setVideo_file(String video_file) {
        this.video_file = video_file;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public LengthUnit getLength_unit() {
        return length_unit;
    }

    public void setLength_unit(LengthUnit length_unit) {
        this.length_unit = length_unit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public WeightUnit getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(WeightUnit weight_unit) {
        this.weight_unit = weight_unit;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getVendor_show_status() {
        return vendor_show_status;
    }

    public void setVendor_show_status(Boolean vendor_show_status) {
        this.vendor_show_status = vendor_show_status;
    }

    public Boolean getAdmin_show_status() {
        return admin_show_status;
    }

    public void setAdmin_show_status(Boolean admin_show_status) {
        this.admin_show_status = admin_show_status;
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

    public Boolean getIs_sponsored() {
        return is_sponsored;
    }

    public void setIs_sponsored(Boolean is_sponsored) {
        this.is_sponsored = is_sponsored;
    }

    public Boolean getIs_new_arrival() {
        return is_new_arrival;
    }

    public void setIs_new_arrival(Boolean is_new_arrival) {
        this.is_new_arrival = is_new_arrival;
    }

    public Boolean getIs_top_selling() {
        return is_top_selling;
    }

    public void setIs_top_selling(Boolean is_top_selling) {
        this.is_top_selling = is_top_selling;
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + id +
            ", user=" + user +
            ", name='" + name + '\'' +
            ", slug='" + slug + '\'' +
            ", description='" + description + '\'' +
            ", image_file='" + image_file + '\'' +
            ", video_file='" + video_file + '\'' +
            ", sku='" + sku + '\'' +
            ", manufacturer='" + manufacturer + '\'' +
            ", model='" + model + '\'' +
            ", price=" + price +
            ", dimension='" + dimension + '\'' +
            ", length_unit=" + length_unit +
            ", weight='" + weight + '\'' +
            ", weight_unit=" + weight_unit +
            ", category=" + category +
            ", vendor_show_status=" + vendor_show_status +
            ", admin_show_status=" + admin_show_status +
            ", created_at=" + created_at +
            ", updated_at=" + updated_at +
            ", image_file_thumb='" + image_file_thumb + '\'' +
            '}';
    }
}
