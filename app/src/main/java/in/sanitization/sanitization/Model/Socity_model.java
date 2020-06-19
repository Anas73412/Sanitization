package in.sanitization.sanitization.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 14,June,2020
 */
public class Socity_model {
    String society_id;
    String society;
    String pincode;

    public Socity_model(String society_id, String society, String pincode) {
        this.society_id = society_id;
        this.society = society;
        this.pincode = pincode;
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
    }

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
