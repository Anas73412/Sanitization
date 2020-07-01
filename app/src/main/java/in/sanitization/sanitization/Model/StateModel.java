package in.sanitization.sanitization.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 30,June,2020
 */
public class StateModel {
    String s_id,state_name;

    public StateModel() {
    }

    public StateModel(String s_id, String state_name) {
        this.s_id = s_id;
        this.state_name = state_name;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
}
