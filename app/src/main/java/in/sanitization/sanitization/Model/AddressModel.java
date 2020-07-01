package in.sanitization.sanitization.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 19,June,2020
 */
public class AddressModel {
    String location_id,receiver_name,receiver_mobile,socity_id,district_id,block_id,pincode,address,state,description,address_type
            ,district_name,block_name;
    boolean ischeckd;
    public AddressModel() {
    }

    public String getDistrict_id() {
        return district_id;
    }

    public String getBlock_id() {
        return block_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public String getBlock_name() {
        return block_name;
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
