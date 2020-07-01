package in.sanitization.sanitization.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 30,June,2020
 */
public class DistrictModel {
    String d_id,state_id,district_name;

    public DistrictModel() {
    }

    public DistrictModel(String d_id, String state_id, String district_name) {
        this.d_id = d_id;
        this.state_id = state_id;
        this.district_name = district_name;
    }

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }
}
