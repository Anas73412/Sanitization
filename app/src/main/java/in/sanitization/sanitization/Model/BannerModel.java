package in.sanitization.sanitization.Model;

public class BannerModel {
    String banner_id;
    String banner_title;
    String banner_image;
    String banner_plan;
    String status;
    String is_delete;

    public BannerModel() {
    }

    public BannerModel(String banner_id, String banner_title, String banner_image, String banner_plan, String status, String is_delete) {
        this.banner_id = banner_id;
        this.banner_title = banner_title;
        this.banner_image = banner_image;
        this.banner_plan = banner_plan;
        this.status = status;
        this.is_delete = is_delete;
    }

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getBanner_title() {
        return banner_title;
    }

    public void setBanner_title(String banner_title) {
        this.banner_title = banner_title;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getBanner_plan() {
        return banner_plan;
    }

    public void setBanner_plan(String banner_plan) {
        this.banner_plan = banner_plan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }
}
