package in.sanitization.sanitization.Model;


public class Slider_model {

    String slider_id,slider_title,slider_image,slider_plan,status,is_delete;

    public Slider_model() {
    }

    public Slider_model(String slider_id, String slider_title, String slider_image, String slider_plan, String status, String is_delete) {
        this.slider_id = slider_id;
        this.slider_title = slider_title;
        this.slider_image = slider_image;
        this.slider_plan = slider_plan;
        this.status = status;
        this.is_delete = is_delete;
    }

    public String getSlider_id() {
        return slider_id;
    }

    public void setSlider_id(String slider_id) {
        this.slider_id = slider_id;
    }

    public String getSlider_title() {
        return slider_title;
    }

    public void setSlider_title(String slider_title) {
        this.slider_title = slider_title;
    }

    public String getSlider_image() {
        return slider_image;
    }

    public void setSlider_image(String slider_image) {
        this.slider_image = slider_image;
    }

    public String getSlider_plan() {
        return slider_plan;
    }

    public void setSlider_plan(String slider_plan) {
        this.slider_plan = slider_plan;
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
