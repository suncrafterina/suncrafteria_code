package suncrafterina.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class ProductListDTO {

    private Long id;

    private String image_file;

    private String image_file_thumb;

    private String name;

    private String sku=null;

    private String vendor;

    private String manufacturer;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate added_on;

    private Double price=null;

    private Boolean vendor_show_status = true;

    private Double rating;

    private Boolean admin_show_status = true;

    private Boolean is_sponsored;

    private Boolean is_new_arrival;

    public ProductListDTO() {

    }

    public ProductListDTO(Long id, String image_file, String image_file_thumb, String name, String sku, String vendor, String manufacturer, LocalDate added_on, Double price, Boolean vendor_show_status, Double rating, Boolean admin_show_status, Boolean is_sponsored, Boolean is_new_arrival) {
        this.id = id;
        this.image_file = image_file;
        this.image_file_thumb = image_file_thumb;
        this.name = name;
        this.sku = sku;
        this.vendor = vendor;
        this.manufacturer = manufacturer;
        this.added_on = added_on;
        this.price = price;
        this.vendor_show_status = vendor_show_status;
        this.rating = rating;
        this.admin_show_status = admin_show_status;
        this.is_sponsored = is_sponsored;
        this.is_new_arrival = is_new_arrival;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public LocalDate getAdded_on() {
        return added_on;
    }

    public void setAdded_on(LocalDate added_on) {
        this.added_on = added_on;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getVendor_show_status() {
        return vendor_show_status;
    }

    public void setVendor_show_status(Boolean vendor_show_status) {
        this.vendor_show_status = vendor_show_status;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }


    public Boolean getAdmin_show_status() {
        return admin_show_status;
    }

    public void setAdmin_show_status(Boolean admin_show_status) {
        this.admin_show_status = admin_show_status;
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

    @Override
    public String toString() {
        return "ProductDto{" +
            "id=" + id +
            ", image_file='" + image_file + '\'' +
            ", name='" + name + '\'' +
            ", sku='" + sku + '\'' +
            ", vendor='" + vendor + '\'' +
            ", manufacturer='" + manufacturer + '\'' +
            ", added_on=" + added_on +
            ", price=" + price +
            ", vendor_show_status=" + vendor_show_status +
            ", rating=" + rating +
            '}';
    }
}
