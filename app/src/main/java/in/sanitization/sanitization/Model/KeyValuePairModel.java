package in.sanitization.sanitization.Model;

import java.util.Comparator;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 16,July,2020
 */
public class KeyValuePairModel {
    String key,value,days;

    public KeyValuePairModel() {
    }

    public KeyValuePairModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

   public static Comparator<KeyValuePairModel> camp_date=new Comparator<KeyValuePairModel>() {
       @Override
       public int compare(KeyValuePairModel o1, KeyValuePairModel o2) {
           int days1=Integer.parseInt(o1.getDays());
           int days2=Integer.parseInt(o2.getDays());
           return days1-days2;
       }
   };
}
