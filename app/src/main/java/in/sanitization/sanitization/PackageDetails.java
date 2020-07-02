package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;

import static in.sanitization.sanitization.Config.BaseUrl.BASE_URL;
import static in.sanitization.sanitization.Config.BaseUrl.IMG_PLAN_URL;

public class PackageDetails extends AppCompatActivity implements View.OnClickListener {
    TextView pckg_name ,pckg_price,pkg_product ,txt_title ,pkg_mrp ,pkg_discount,tv_expiry,tv_work_days;
    ImageView img_back;
    SliderLayout pkg_img ;
   HashMap<String ,Object> img_map;
    int sp , mp ,diff ;
    Button btn_buy ;
    Module module ;
    String sPrice="",sMrp="",sTitle="";
    LoadingBar loadingBar ;
    Activity activity= PackageDetails.this;
    String title= "",price="",product="",status="",plan_id="",plan_expiry="",working_days="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);
        initViews();
    }

    private void initViews() {
        pkg_img = findViewById(R.id.pkg_img);
        pckg_name=findViewById(R.id.package_name);
       pckg_price = findViewById(R.id.package_price);
       pkg_product = findViewById(R.id.description);
       pkg_mrp = findViewById(R.id.package_mrp);
       pkg_discount = findViewById(R.id.package_dis);
        tv_work_days = findViewById(R.id.tv_work_days);
        tv_expiry=findViewById(R.id.tv_expiry);
       btn_buy = findViewById(R.id.buy_now);
       txt_title= findViewById(R.id.tv_title);
       img_back = findViewById(R.id.iv_back);
        loadingBar = new LoadingBar(activity);
        module = new Module(activity);
        btn_buy.setOnClickListener(this);
        img_back.setOnClickListener(this);


        price = getIntent().getStringExtra("plan_price");
        title = getIntent().getStringExtra("plan_name");
        status = getIntent().getStringExtra("plan_status");
        plan_id = getIntent().getStringExtra("plan_id");


      pckg_price.setText(getResources().getString(R.string.currency)+""+price);
      pckg_name.setText(title);
      txt_title.setText(title);
      getDetails(plan_id);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back)
        {
            finish();
        }
        else if (id == R.id.buy_now)
        {
            Intent intent=new Intent(PackageDetails.this,SubscriptionActivity.class);
            intent.putExtra("id",String.valueOf(plan_id));
            intent.putExtra("name",sTitle);
            intent.putExtra("price",sPrice);
            intent.putExtra("mrp",sMrp);
            intent.putExtra("working_days",working_days);
            intent.putExtra("plan_expiry",plan_expiry);
            startActivity(intent);
        }
    }

    private void getDetails( String id){
        loadingBar.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("plan_id",id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.GET_DETAILS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("plan_details", response.toString());
                loadingBar.dismiss();
                try {
                    boolean status = response.getBoolean("responce");
                    if (status)
                    {
                        ArrayList<String> imgList=new ArrayList<>();
                        HashMap<String, String> url_maps = new HashMap<String, String>();
                        JSONObject data = response.getJSONObject("data");
                        String img_obj = data.getString("plan_image");
                        plan_expiry=data.getString("plan_expiry");
                        working_days=data.getString("plan_no_of_working_days");
                        tv_expiry.setText("Package will expire after "+plan_expiry+" of subscription date");
                        tv_work_days.setText(working_days);
                        JSONArray img_arr = new JSONArray(img_obj);
                        if(img_obj.isEmpty())
                        {
                            module.showToast("No Images Available");
                        }
                        else
                        {

                        }
                       for (int i = 0 ;i<img_arr.length();i++)
                       {


                           CustomSlider textSliderView = new CustomSlider(PackageDetails.this);
                           // initialize a SliderLayout
                           Log.e("ddddd",""+IMG_PLAN_URL +img_arr.get(i).toString());
                           textSliderView
                                   .image(IMG_PLAN_URL +img_arr.get(i).toString())
                                   .setScaleType(CustomSlider.ScaleType.CenterInside);
                           pkg_img.addSlider(textSliderView);


                       }
                        pkg_img.setDuration(10000);
                        pkg_product.setText(data.getString("plan_description"));
                        pckg_name.setText(data.getString("plan_name"));
                        sTitle=data.getString("plan_name").toString();
                        sPrice=data.getString("plan_price").toString();
                        sMrp=data.getString("plan_mrp").toString();
                        sp = Integer.parseInt(data.getString("plan_price"));
                        mp = Integer.parseInt(data.getString("plan_mrp"));
                      diff = module.getDiscount(data.getString("plan_price"),data.getString("plan_mrp"));

                        if (diff<= 0)
                        {
                            pkg_discount.setVisibility(View.GONE);
                        }
                        else
                        {
                            pkg_discount.setVisibility(View.VISIBLE);
                        pkg_discount.setText(""+diff + "% OFF");}

                        pckg_price.setText(getResources().getString(R.string.currency)+""+sp);
                        pkg_mrp.setText(getResources().getString(R.string.currency)+""+mp);
                        pkg_mrp.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(activity,""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");

    }


}
