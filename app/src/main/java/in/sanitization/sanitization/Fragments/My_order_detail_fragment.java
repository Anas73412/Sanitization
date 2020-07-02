package in.sanitization.sanitization.Fragments;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sanitization.sanitization.Adapter.Order_detail_adapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.CustomSlider;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.MainActivity;
import in.sanitization.sanitization.Model.My_order_detail_model;
import in.sanitization.sanitization.PackageDetails;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.Config.BaseUrl.DELETE_ORDER_URL;
import static in.sanitization.sanitization.Config.BaseUrl.IMG_PLAN_URL;
import static in.sanitization.sanitization.Config.BaseUrl.ORDER_DETAIL_URL;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;


public class My_order_detail_fragment extends Fragment {

    private static String TAG = My_order_detail_fragment.class.getSimpleName();
    Module module;
    private TextView tv_p_name ,tv_duration ,tv_price,tv_status,tv_desc,tv_r_name,tv_r_mobile,tv_address,w_mobile,w_name,w_email ,tv_date,tv_id;
    private RelativeLayout btn_cancle;
    private RecyclerView rv_detail_order;
    List<String> image_list;
    private String package_id ,worker_id,location_id,user_id,date,time ,name,mobile,status,order_id;
   LoadingBar loadingBar;
    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();

    public My_order_detail_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingBar = new LoadingBar(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order_detail, container, false);
        loadingBar = new LoadingBar(getActivity());
        module=new Module(getActivity());

        ((HomeActivity) getActivity()).setTitle("Order Details");
      initViews(view);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog();
            }
        });

        return view;
    }

    void initViews(View v)
    {
        tv_address= v.findViewById(R.id.address);
        tv_id= v.findViewById(R.id.order_id);
        tv_desc= v.findViewById(R.id.description);
        tv_duration= v.findViewById(R.id.duration);
        tv_status= v.findViewById(R.id.status);
        tv_date= v.findViewById(R.id.date);
        tv_price= v.findViewById(R.id.price);
        tv_r_mobile= v.findViewById(R.id.mobile);
        tv_r_name= v.findViewById(R.id.r_name);
        tv_p_name= v.findViewById(R.id.name);
        w_email= v.findViewById(R.id.w_email);
        w_mobile= v.findViewById(R.id.w_mobile);
        w_name= v.findViewById(R.id.w_name);
       btn_cancle= v.findViewById(R.id.btn_order_detail_cancle);
        worker_id =getArguments().getString("worker_id");
        package_id =getArguments().getString("package_id");
        location_id =getArguments().getString("location_id");
        user_id =getArguments().getString("user_id");
        order_id =getArguments().getString("order_id");
       date =getArguments().getString("date");
        time =getArguments().getString("time");
        status =getArguments().getString("status");
        name =getArguments().getString("r_name");
       mobile =getArguments().getString("r_mobile");
       tv_id.setText(order_id);
       tv_date.setText(date);
       tv_r_name.setText(name);
       tv_r_mobile.setText(mobile);
       tv_price.setText(getActivity().getResources().getString(R.string.currency)+getArguments().getString("package_price"));
       tv_p_name.setText(getArguments().getString("package_name"));
       tv_duration.setText(getArguments().getString("package_duration"));
        getDetails(package_id);
        if (status.equals("0"))
        {
            tv_status.setText("Pending");
        }
        else if (status.equals("1"))
        {
            tv_status.setText("Confirmed");
        }
        else if (status.equals("3"))
        {
            tv_status.setText("Cancelled");
        }


    }

    // alertdialog for cancle order
    private void showDeleteDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Reason for cancelling your purchase");
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Session_management sessionManagement = new Session_management(getActivity());
                String user_id = sessionManagement.getUserDetails().get(KEY_ID);

                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
//                    makeDeleteOrderRequest(sale_id, user_id);
                }

                dialogInterface.dismiss();

            }
        });

        alertDialog.show();

    }

    private void makeGetOrderDetailRequest(String sale_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
               ORDER_DETAIL_URL, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("saleeee", response.toString());

                Gson gson = new Gson();
                Type listType = new TypeToken<List<My_order_detail_model>>() {
                }.getType();

                my_order_detail_modelList = gson.fromJson(response.toString(), listType);

               Order_detail_adapter adapter = new Order_detail_adapter(my_order_detail_modelList);
                rv_detail_order.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (my_order_detail_modelList.isEmpty()) {
                   new ToastMsg(getActivity()).toastIconError("No Record Found");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeDeleteOrderRequest(String sale_id, String user_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_delete_order_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);
        params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                DELETE_ORDER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();

                        ((MainActivity) getActivity()).onBackPressed();

                    } else {
                        String error = response.getString("error");
                        Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
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
                        JSONArray img_arr = new JSONArray(img_obj);
                        if(img_obj.isEmpty())
                        {
                            module.showToast("No Images Avalaible");
                        }
                        else
                        {

                        }

//                        for(String name : url_maps.keySet()){
//
//                           CustomSlider textSliderView = new CustomSlider(PackageDetails.this);
//                            textSliderView
//                                    .description(name)
//                                    .image(url_maps.get(name))
//                                    .setScaleType(BaseSliderView.ScaleType.Fit);
//
//                            textSliderView.bundle(new Bundle());
//                            textSliderView.getBundle()
//                                    .putString("extra",name);
//                           pkg_img.addSlider(textSliderView);
//                        }
//                        pkg_img.setPresetTransformer(SliderLayout.Transformer.Accordion);
//                        pkg_img.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

                        tv_desc.setText(data.getString("plan_description"));
                        tv_p_name.setText(data.getString("plan_name"));
//                        sTitle=data.getString("plan_name").toString();
                       String sPrice=data.getString("plan_price").toString();
                       String sMrp=data.getString("plan_mrp").toString();
                       int sp = Integer.parseInt(data.getString("plan_price"));
                       int mp = Integer.parseInt(data.getString("plan_mrp"));
                       int diff = module.getDiscount(data.getString("plan_price"),data.getString("plan_mrp"));

//
//                       tv_price.setText(getResources().getString(R.string.currency)+""+sp);
//                        pkg_mrp.setText(getResources().getString(R.string.currency)+""+mp);
//                        pkg_mrp.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
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
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");

    }

}
