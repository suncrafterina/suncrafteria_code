package suncrafterina.service.dto;

import java.math.BigInteger;

public class DashboardTopCustomersDto {

    private BigInteger user_id;

    private String customer_name;

    private String email;

    private String image_url;

    private BigInteger orderd_qty;


    public DashboardTopCustomersDto() {

    }

    public DashboardTopCustomersDto(BigInteger user_id, String customer_name, String email, String image_url, BigInteger orderd_qty) {
        this.user_id = user_id;
        this.customer_name = customer_name;
        this.email = email;
        this.image_url = image_url;
        this.orderd_qty = orderd_qty;
    }

    public BigInteger getUser_id() {
        return user_id;
    }

    public void setUser_id(BigInteger user_id) {
        this.user_id = user_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public BigInteger getOrderd_qty() {
        return orderd_qty;
    }

    public void setOrderd_qty(BigInteger orderd_qty) {
        this.orderd_qty = orderd_qty;
    }

    @Override
    public String toString() {
        return "DashboardTopCustomersDto{" +
            "user_id=" + user_id +
            ", customer_name='" + customer_name + '\'' +
            ", email='" + email + '\'' +
            ", image_url='" + image_url + '\'' +
            ", orderd_qty=" + orderd_qty +
            '}';
    }
}
