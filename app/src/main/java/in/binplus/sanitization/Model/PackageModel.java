package in.binplus.sanitization.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 18,May,2020
 */
public class PackageModel {
    //    String id,title,amount,desc;
    String plan_id;
    String plan_name;
    String plan_price;
    String plan_image;
    String plan_status;
    String product_name;
    String plan_date;
    String created_at;


    public PackageModel() {
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getPlan_price() {
        return plan_price;
    }

    public void setPlan_price(String plan_price) {
        this.plan_price = plan_price;
    }

    public String getPlan_image() {
        return plan_image;
    }

    public void setPlan_image(String plan_image) {
        this.plan_image = plan_image;
    }

    public String getPlan_status() {
        return plan_status;
    }

    public void setPlan_status(String plan_status) {
        this.plan_status = plan_status;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPlan_date() {
        return plan_date;
    }

    public void setPlan_date(String plan_date) {
        this.plan_date = plan_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
