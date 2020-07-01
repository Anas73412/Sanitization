package in.sanitization.sanitization.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 30,June,2020
 */
public class BlockModel {
    String b_id,state_id,district_id,block_name;

    public BlockModel() {
    }

    public BlockModel(String b_id, String state_id, String district_id, String block_name) {
        this.b_id = b_id;
        this.state_id = state_id;
        this.district_id = district_id;
        this.block_name = block_name;
    }

    public String getB_id() {
        return b_id;
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }
}
