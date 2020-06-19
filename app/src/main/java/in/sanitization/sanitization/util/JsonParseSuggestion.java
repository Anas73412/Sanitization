package in.sanitization.sanitization.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Model.Socity_model;

public class JsonParseSuggestion {
    double current_latitude,current_longitude;
    public JsonParseSuggestion(){}
    public JsonParseSuggestion(double current_latitude, double current_longitude){
        this.current_latitude=current_latitude;
        this.current_longitude=current_longitude;
    }

    public List<Socity_model> getParseJsonSociety(String city,String name)
    {
        List<Socity_model> listData = new ArrayList<>();
        try {
            String temp=name.replace(" ", "%20");
            String tempCity=city.replace(" ", "%20");
            URL js = new URL(BaseUrl.GET_SOCITY_URL+"?city="+tempCity+"&society="+"%"+temp+"%");
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            JSONArray jsonArray = jsonResponse.getJSONArray("");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject r = jsonArray.getJSONObject(i);
                /*String socity_id;
                String socity_name;
                String pincode;

                String delivery_charge;*/
                listData.add(new Socity_model(r.getString("society_id"),r.getString("society"),r.getString("pincode")));
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return listData;
    }

}