package in.sanitization.sanitization.Model;

public class FAQModel {
    String faq_id;
    String faq_ans ;
    String faq_que;
    String status;

    public FAQModel(String faq_id, String faq_ans, String faq_que, String status) {
        this.faq_id = faq_id;
        this.faq_ans = faq_ans;
        this.faq_que = faq_que;
        this.status = status;
    }

    public String getFaq_id() {
        return faq_id;
    }

    public void setFaq_id(String faq_id) {
        this.faq_id = faq_id;
    }

    public String getFaq_ans() {
        return faq_ans;
    }

    public void setFaq_ans(String faq_ans) {
        this.faq_ans = faq_ans;
    }

    public String getFaq_que() {
        return faq_que;
    }

    public void setFaq_que(String faq_que) {
        this.faq_que = faq_que;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
