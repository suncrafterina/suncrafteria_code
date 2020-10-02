package suncrafterina.service.dto;

import suncrafterina.enums.LengthUnit;
import suncrafterina.enums.WeightUnit;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProductDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private String sku;

    @NotBlank
    private String manufacturer;

    @NotNull
    private Double price;

    @Size(max = 255)
    private String dimension;

    private LengthUnit length_unit;

    @Size(max = 255)
    private String weight;

    private WeightUnit weight_unit;

    @NotNull
    private Long category_id;

    public ProductDTO() {
    }

    public ProductDTO(Long id, @NotBlank String name, @NotBlank String description, String sku, @NotBlank String manufacturer, @NotNull Double price, @Size(max = 255) String dimension, LengthUnit length_unit, @Size(max = 255) String weight, WeightUnit weight_unit, @NotNull Long category_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.manufacturer = manufacturer;
        this.price = price;
        this.dimension = dimension;
        this.length_unit = length_unit;
        this.weight = weight;
        this.weight_unit = weight_unit;
        this.category_id = category_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", sku='" + sku + '\'' +
            ", manufacturer='" + manufacturer + '\'' +
            ", price=" + price +
            ", dimension='" + dimension + '\'' +
            ", length_unit=" + length_unit +
            ", weight='" + weight + '\'' +
            ", weight_unit=" + weight_unit +
            ", category_id=" + category_id +
            '}';
    }
}
