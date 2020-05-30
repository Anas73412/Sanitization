package in.binplus.sanitization.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 18,May,2020
 */
public class PackageModel {
    String id,title,amount,desc;

    public PackageModel() {
    }

    public PackageModel(String id, String title, String amount, String desc) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
