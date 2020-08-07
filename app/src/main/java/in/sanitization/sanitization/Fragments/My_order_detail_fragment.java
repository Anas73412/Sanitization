package in.sanitization.sanitization.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.KeyPair;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import in.sanitization.sanitization.Adapter.ComplainAdapter;
import in.sanitization.sanitization.Adapter.LogsAdapter;
import in.sanitization.sanitization.Adapter.Order_detail_adapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.CustomSlider;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.MainActivity;
import in.sanitization.sanitization.Model.ComplainModel;
import in.sanitization.sanitization.Model.KeyValuePairModel;
import in.sanitization.sanitization.Model.My_order_detail_model;
import in.sanitization.sanitization.PackageDetails;
import in.sanitization.sanitization.PaymentActivity;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.Config.BaseUrl.ADD_COMPLAIN_URL;
import static in.sanitization.sanitization.Config.BaseUrl.DELETE_ORDER_URL;
import static in.sanitization.sanitization.Config.BaseUrl.GET_COMPLAIN_URL;
import static in.sanitization.sanitization.Config.BaseUrl.IMG_PLAN_URL;
import static in.sanitization.sanitization.Config.BaseUrl.ORDER_DETAIL_URL;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;
import static in.sanitization.sanitization.Model.KeyValuePairModel.camp_date;


public class My_order_detail_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = My_order_detail_fragment.class.getSimpleName();
    Module module;
    private TextView tv_p_name ,tv_expire,tv_duration ,tv_price,tv_status,tv_desc,tv_r_name,tv_r_mobile,tv_address,
            w_mobile,w_name,w_email ,tv_date,tv_id,tv_tot,tv_gst,tv_chkexpire;
    private RelativeLayout btn_cancle,rel_reorder;
    private RecyclerView rv_logs,rv_detail_order,rv_complain;
    ImageView plan_img , w_img ,iv_logs ,log_icon;
    List<String> image_list;
    private String package_id ,worker_id,location_id,user_id,date,time,name,mobile,status,order_id;
   LoadingBar loadingBar;
    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();
    CardView card_worker;
    JSONArray worker_arr;
    float gst_per ;
    RelativeLayout rel_log,rel_rvlogs,rel_rvcpl;
    LogsAdapter logsAdapter;
    boolean logs_flag=false;
    Button btn_complaints,btn_view,btn_yes,btn_no;
    Dialog dialog;
    EditText et_remark;
    ArrayList<ComplainModel> complainList;
    ComplainAdapter complainAdapter;
    //Plan Expire
    CardView card_expire;
    String reLocId="",reName="",reMobile="",reAddress="",reState="",rePlan_id="",rePlaeNm="",reWorlingDays="",rePlanEx="",reMrp="",rePrice="",rePincode="";

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
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.complaints_layout);
        dialog.setCanceledOnTouchOutside(false);

        rv_logs= v.findViewById(R.id.rv_logs);
        tv_chkexpire= v.findViewById(R.id.tv_chkexpire);
        card_expire= v.findViewById(R.id.card_expire);
        rv_complain= v.findViewById(R.id.rv_complain);
        rel_rvlogs= v.findViewById(R.id.rel_rvlogs);
        rel_rvcpl= v.findViewById(R.id.rel_rvcpl);
        rel_log= v.findViewById(R.id.rel_log);
        iv_logs= v.findViewById(R.id.iv_logs);
        tv_address= v.findViewById(R.id.address);
        tv_id= v.findViewById(R.id.order_id);
        tv_desc= v.findViewById(R.id.description);
        tv_duration= v.findViewById(R.id.duration);
        tv_expire= v.findViewById(R.id.expires);
        tv_status= v.findViewById(R.id.status);
        tv_date= v.findViewById(R.id.date);
        tv_price= v.findViewById(R.id.price);
        tv_tot= v.findViewById(R.id.total);
        tv_gst= v.findViewById(R.id.tvGst);
        iv_logs= v.findViewById(R.id.iv_logs);
        complainList=new ArrayList<>();
        plan_img= v.findViewById(R.id.plan_img);
        w_img= v.findViewById(R.id.w_img);
        tv_r_mobile= v.findViewById(R.id.mobile);
        tv_r_name= v.findViewById(R.id.r_name);
        tv_p_name= v.findViewById(R.id.name);
        w_email= v.findViewById(R.id.w_email);
        rel_reorder= v.findViewById(R.id.rel_reorder);
        w_mobile= v.findViewById(R.id.w_mobile);
        w_name= v.findViewById(R.id.w_name);
        btn_complaints= v.findViewById(R.id.btn_complaints);
        btn_view= v.findViewById(R.id.btn_view);
      card_worker= v.findViewById(R.id.card_worker);
       btn_cancle= v.findViewById(R.id.btn_order_detail_cancle);
        worker_id =getArguments().getString("worker_id");
        package_id =getArguments().getString("package_id");
        rePlan_id=package_id;
        location_id =getArguments().getString("location_id");
        reLocId=location_id;
        user_id =getArguments().getString("user_id");
        order_id =getArguments().getString("order_id");
       date =getArguments().getString("date");
        time =getArguments().getString("time");
        status =getArguments().getString("status");
        name =getArguments().getString("r_name");
        reName=name;
       mobile =getArguments().getString("r_mobile");
       reMobile=mobile;
       tv_id.setText("A2Z_ID"+order_id);
       tv_date.setText(date);
       tv_r_name.setText(name);
       tv_r_mobile.setText(mobile);
       rel_log.setOnClickListener(this);
       btn_complaints.setOnClickListener(this);
       btn_view.setOnClickListener(this);
        rel_reorder.setOnClickListener(this);
        DecimalFormat precision = new DecimalFormat("0.0");
       gst_per=  new Module(getActivity()).getGSt(getArguments().getString("gst"),getArguments().getString("package_price"));
      tv_tot.setText(getActivity().getResources().getString(R.string.currency)+getArguments().getString("total"));
      tv_gst.setText(getActivity().getResources().getString(R.string.currency)+precision.format(gst_per)+"0");
//       tv_p_name.setText(getArguments().getString("package_name"));
//       tv_duration.setText(getArguments().getString("package_duration"));
       if (worker_id.equals("0") || worker_id.equals("null"))
       {
           card_worker.setVisibility(View.GONE);
       }
       if (ConnectivityReceiver.isConnected()) {
           getDetails(location_id, package_id, worker_id,order_id);
       } else
       {
           Intent intent = new Intent(getActivity(), NoInternetConnection.class);
           startActivity(intent);
       }
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

        card_worker.setOnClickListener(this);

    }

    private void setExpiryBlock(String date,String d) {
        String[] dt=date.split(" ");
        int days=module.getDateDiff(dt[0].toString())-Integer.parseInt(d);
        Log.e("asdasd",""+days+"\n"+dt[0].toString());

        if(days>-4)
        {
            if(card_expire.getVisibility()==View.GONE)
                card_expire.setVisibility(View.VISIBLE);

            if(days>=0)
            {
                tv_chkexpire.setText("Your plan has expired");
            }
            else
            {

                if(days==-1){
                    tv_chkexpire.setText("Your plan will expire in "+1+" day");
                }
                else if(days == -2){
                    tv_chkexpire.setText("Your plan will expire in "+2+" days");
                }else if(days == -3){
                    tv_chkexpire.setText("Your plan will expire in "+3+" days");
                }

            }
        }
        else
        {
            if(card_expire.getVisibility()==View.VISIBLE)
                card_expire.setVisibility(View.GONE);
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



    private void getDetails(final String location_id, String plan_id, final String worker_id, String order_id)
    {
        loadingBar.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("worker_id",worker_id);
        params.put("plan_id",plan_id);
        params.put("location_id",location_id);
        params.put("order_id",order_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ORDER_DETAIL_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("order_details", response.toString());
                loadingBar.dismiss();
                try {
                    boolean status = response.getBoolean("responce");
                    if (status) {
                        JSONArray loc_arr = response.getJSONArray("location");
                        JSONArray plan_arr = response.getJSONArray("plan");
                        JSONArray logs_arr = response.getJSONArray("order_details");
                        worker_arr = response.getJSONArray("worker");
                        ArrayList<String> imgList = new ArrayList<>();
                        HashMap<String, String> url_maps = new HashMap<String, String>();
                        if (plan_arr.length() > 0) {
                            JSONObject data = plan_arr.getJSONObject(0);
                            String img_obj = data.getString("plan_image");
                            JSONArray img_arr = new JSONArray(img_obj);
                            if (img_obj.isEmpty()) {
                                module.showToast("No Images Available");
                                plan_img.setVisibility(View.GONE);
                            }


                            Glide.with(getActivity())
                                    .load(IMG_PLAN_URL + img_arr.get(0))
                                    .placeholder(R.drawable.logo)
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .dontAnimate()
                                    .into(plan_img);


                            tv_desc.setText(data.getString("plan_description"));
                            tv_p_name.setText(data.getString("plan_name"));
                            rePlaeNm=data.getString("plan_name");
                            tv_price.setText(getResources().getString(R.string.currency) + "" + (data.getString("plan_price")));
                            rePrice=data.getString("plan_price");
                            reMrp=data.getString("plan_mrp");
                            tv_duration.setText(data.getString("plan_no_of_working_days"));
                            reWorlingDays=data.getString("plan_no_of_working_days");
                            tv_expire.setText(data.getString("plan_expiry") + " from Subscription");
                            rePlanEx=data.getString("plan_expiry");
                            String[] exp=data.getString("plan_expiry").toString().split(" ");
                            setExpiryBlock(date,exp[0].toString());

                        }
                        if (loc_arr.length() > 0) {
                            JSONObject loc = loc_arr.getJSONObject(0);
                            tv_address.setText(loc.getString("address")
                                    + "\nBlock : " + loc.getString("block_name")
                                    + "\n District : " + loc.getString("district_name")
                                    + "\n State : " + loc.getString("state") + " ( " + loc.getString("pincode") + ")");
                           reAddress=loc.getString("address");
                           reState=loc.getString("state");
                           rePincode=loc.getString("pincode");

                        }

                        if (worker_arr.length() > 0) {
                            card_worker.setVisibility(View.VISIBLE);
                            JSONObject worker = worker_arr.getJSONObject(0);
                            w_email.setText("Email : " + worker.getString("email"));
                            w_name.setText("Name : " + worker.getString("name"));
                            w_mobile.setText("Mobile : " + worker.getString("mobile"));
                            JSONArray w_ar = new JSONArray(worker.getString("photo"));

                            Glide.with(getActivity())
                                    .load(BaseUrl.IMG_WORKER_URL + w_ar.get(0))
                                    .placeholder(R.drawable.logo)
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .dontAnimate()
                                    .into(w_img);
                        } else {
                            card_worker.setVisibility(View.GONE);
                        }
                        String logStr = logs_arr.getJSONObject(0).getString("activity_log");
                     Log.e("logstr",logStr.toString());
                        if (logStr.equals("null")||logStr==null || logStr.isEmpty()) {
                            if (rel_log.getVisibility() == View.VISIBLE) {
                                rel_log.setVisibility(View.GONE);
                            }
                        } else {
                            JSONObject logObj = new JSONObject(logStr);
                            Log.e("activity_logs", "" + logObj.toString());
                            ArrayList<KeyValuePairModel> logsList = module.getValuesFromJSON(logObj);
                            for (int l = 0; l < logsList.size(); l++) {
                                logsList.get(l).setDays(String.valueOf(module.getDateDiff(logsList.get(l).getKey().toString())));
                            }
                            Collections.sort(logsList, camp_date);
                            if (logsList.size() <= 0) {
                                if (rel_log.getVisibility() == View.VISIBLE) {
                                    rel_log.setVisibility(View.GONE);
                                }

                            } else {
                                if (rel_log.getVisibility() == View.GONE) {
                                    rel_log.setVisibility(View.VISIBLE);

                                }
                                rv_logs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                logsAdapter = new LogsAdapter(logsList, getActivity());
                                rv_logs.setAdapter(logsAdapter);
                                logsAdapter.notifyDataSetChanged();
                            }
                        }
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
        AppController.getInstance().addToRequestQueue(jsonObjReq,"order_details");

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.card_worker)
        {
            Fragment fm=new ManagerDetailsFragment();
            Bundle bundle=new Bundle();
            bundle.putString("arr",worker_arr.toString());
            bundle.putString("type","worker");
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fm.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace( R.id.frame,fm)
                    .addToBackStack(null)
                    .commit();
        }
        else if(v.getId()==R.id.rel_log)
        {
            if(rel_rvlogs.getVisibility()==View.GONE)
            {
                rel_rvlogs.setVisibility(View.VISIBLE);
                iv_logs.setBackground(getActivity().getResources().getDrawable(R.drawable.icons8_minus_30px));
            }
            else if(rel_rvlogs.getVisibility()==View.VISIBLE)
            {
                rel_rvlogs.setVisibility(View.GONE);
                iv_logs.setBackground(getActivity().getResources().getDrawable(R.drawable.icons8_plus_30px));
            }
        }
        else if(v.getId() == R.id.btn_complaints)
        {
            btn_no=(Button)dialog.findViewById(R.id.btn_no);
            btn_yes=(Button)dialog.findViewById(R.id.btn_yes);
            et_remark=(EditText) dialog.findViewById(R.id.et_remark);
            dialog.show();
            et_remark.setText("");

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    String remark=et_remark.getText().toString();
                    if(remark.isEmpty())
                    {
                        et_remark.setError("Please provide a reason");
                        et_remark.requestFocus();
                    }
                    else if(remark.length()<20)
                    {
                        et_remark.setError("Too short");
                        et_remark.requestFocus();

                    }
                    else
                    {
                        if (ConnectivityReceiver.isConnected()) {
                          addComplaints(user_id,order_id,et_remark.getText().toString());

                        }

                    }
                    // check internet connection
                }
            });

            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    dialog.dismiss();
                }
            });

        }
        else if(v.getId() == R.id.btn_view)
        {
            if(rel_rvcpl.getVisibility()==View.GONE)
            {
                btn_view.setText("Hide Previous Complaints");
                getAllComplaints(order_id);
                rel_rvcpl.setVisibility(View.VISIBLE);

            }
            else if(rel_rvcpl.getVisibility()==View.VISIBLE)
            {
                btn_view.setText("View Previous Complaints");

                rel_rvcpl.setVisibility(View.GONE);
            }


        }
        else if(v.getId() == R.id.rel_reorder)
        {
            reorder();   
        }
    }

    private void reorder() {
        Intent bundle=new Intent(getActivity(), PaymentActivity.class);

        bundle.putExtra("plan_id",rePlan_id);
        bundle.putExtra("plan_name",rePlaeNm);
        bundle.putExtra("mrp",reMrp);
        bundle.putExtra("price",rePrice);
        bundle.putExtra("loc_id",reLocId);
        bundle.putExtra("address",reAddress);
        bundle.putExtra("name",reName);
        bundle.putExtra("pincode",rePincode);
        bundle.putExtra("mobile",reMobile);
        bundle.putExtra("state",reState);
        bundle.putExtra("city","");
        bundle.putExtra("socity_id","");
        bundle.putExtra("plan_expiry",rePlanEx);
        bundle.putExtra("working_days",reWorlingDays);
//        Log.e("addreess_fragment",""+addressMap.toString());
        startActivity(bundle);

    }

    private void addComplaints(String user_id, String order_id, String complain) {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("order_id",order_id);
        params.put("complain",complain);
        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, ADD_COMPLAIN_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
             try {
                 boolean status = response.getBoolean("responce");
                 if (status) {

                     String msg = response.getString("message");
                     dialog.dismiss();
                     module.showToast(""+msg);

                 } else {
                     String error = response.getString("error");

                     module.showToast(""+error);
                 }
             }
             catch (Exception ex)
             {
                 ex.printStackTrace();
             }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.errMessage(error);
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public void getAllComplaints(String order_id)
    {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("order_id",order_id);
        complainList.clear();
        CustomVolleyJsonArrayRequest arrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.POST, GET_COMPLAIN_URL, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadingBar.dismiss();
              try {
                  Gson gson=new Gson();
                  Type listType=new TypeToken<List<ComplainModel>>(){}.getType();
                  complainList=gson.fromJson(response.toString(),listType);
                  complainAdapter=new ComplainAdapter(complainList,getActivity());
                  if (complainList.size()>0) {
                      rv_complain.setLayoutManager(new LinearLayoutManager(getActivity()));
                      rv_complain.setAdapter(complainAdapter);
                      complainAdapter.notifyDataSetChanged();
                  }
                  else
                  {
                      new ToastMsg(getActivity()).toastIconError("No Previous Complaints");
                  }

              }
              catch (Exception ex)
              {
                  ex.printStackTrace();
              }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.errMessage(error);
            }
        });
        AppController.getInstance().addToRequestQueue(arrayRequest);
    }
}
