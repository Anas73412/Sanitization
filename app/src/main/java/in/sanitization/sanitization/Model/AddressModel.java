package in.sanitization.sanitization.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 19,June,2020
 */
public class AddressModel {
    String location_id,receiver_name,receiver_mobile,socity_id,pincode,address,state,city,description,address_type;
    boolean ischeckd;
    public AddressModel() {
    }

    public String getLocation_id() {
        return location_id;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public String getReceiver_mobile() {
        return receiver_mobile;
    }

    public String getSocity_id() {
        return socity_id;
    }

    public String getPincode() {
        return pincode;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress_type() {
        return address_type;
    }

    public boolean isIscheckd() {
        return ischeckd;
    }

    public void setIscheckd(boolean ischeckd) {
        this.ischeckd = ischeckd;
    }
}
