package in.sanitization.sanitization.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 07,August,2020
 */
public class ResponseModel {
    @SerializedName("responce")
    @Expose
    boolean responce;
    @SerializedName("url")
    @Expose
    String url;
    @SerializedName("message")
    @Expose
    String message;

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
