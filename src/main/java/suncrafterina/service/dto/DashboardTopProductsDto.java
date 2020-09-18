package suncrafterina.service.dto;

import java.math.BigInteger;

public class DashboardTopProductsDto {

    private BigInteger product_id;

    private String product_name;

    private String sku;

    private String image_file;

    private String image_file_thumb;

    private BigInteger quantity_sales;

    public DashboardTopProductsDto(BigInteger product_id, String product_name, String sku, String image_file, String image_file_thumb, BigInteger quantity_sales) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.sku = sku;
        this.image_file = image_file;
        this.image_file_thumb = image_file_thumb;
        this.quantity_sales = quantity_sales;
    }

    public DashboardTopProductsDto() {
    }


    public BigInteger getProduct_id() {
        return product_id;
    }

    public void setProduct_id(BigInteger product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public BigInteger getQuantity_sales() {
        return quantity_sales;
    }

    @Override
    public String toString() {
        return "DashboardTopProductsDto{" +
            "product_id=" + product_id +
            ", product_name='" + product_name + '\'' +
            ", sku='" + sku + '\'' +
            ", image_file='" + image_file + '\'' +
            ", image_file_thumb='" + image_file_thumb + '\'' +
            ", quantity_sales=" + quantity_sales +
            '}';
    }

    public void setQuantity_sales(BigInteger quantity_sales) {
        this.quantity_sales = quantity_sales;
    }


}
