package dto;
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//
public class CustomerDTO {
////    @JsonProperty("id")
////    private String customerId;
////
////    @JsonProperty("name")
////    private String customerName;
////
////    @JsonProperty("address")
////    private String customerAddress;
////
////    @JsonProperty("email")
////    private String customerEmail;
////
////    @JsonProperty("phone")
////    private String customerPhone;
private String customerId;
private String customerName;
private String customerAddress;
private String customerEmail;
private String customerPhone;

    public CustomerDTO() {
    }


    public CustomerDTO(String customerId, String customerName, String customerAddress, String customerEmail, String customerPhone) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}

//
////import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
////
////@JsonIgnoreProperties(ignoreUnknown = true)
////public class CustomerDTO {
////    private String customerId;
////    private String customerName;
////    private String customerAddress;
////    private String customerPhone;
////    private String customerEmail;
////
////    public CustomerDTO() {
////    }
////
////    public CustomerDTO(String customerId, String customerName, String customerAddress, String customerPhone, String customerEmail) {
////        this.customerId = customerId;
////        this.customerName = customerName;
////        this.customerAddress = customerAddress;
////        this.customerPhone = customerPhone;
////        this.customerEmail = customerEmail;
////    }
////
////    // Getters and setters...
////}


