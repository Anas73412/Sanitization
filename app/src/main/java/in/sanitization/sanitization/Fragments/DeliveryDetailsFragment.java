package in.sanitization.sanitization.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.SubscriptionActivity;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.Config.Constants.KEY_ID;
import static in.sanitization.sanitization.Fragments.HomeFragment.gst;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryDetailsFragment extends Fragment implements View.OnClickListener{

    TextView tv_rev_name,tv_rev_mobile,tv_rev_pincode,tv_rev_address,tvItems,tvprice,tvMrp,tvDiscount,tvSubTotal ,tv_gst;
    RelativeLayout rel_order;
    float tot;

    String loc_id="",gst_price,rev_name,rev_mobile,rev_address,rev_state,rev_city,rev_pincode,rev_soc_id,plan_id,plan_name,mrp,price,working_days,plan_expiry;
    Session_management session_management;
    Module module;
    LoadingBar loadingBar;
    ToastMsg toastMsg;
    public DeliveryDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_delivery_details_fragment, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        tv_rev_name=v.findViewById(R.id.tv_rev_name);
        tv_rev_mobile=v.findViewById(R.id.tv_rev_mobile);
        tv_rev_pincode=v.findViewById(R.id.tv_rev_pincode);
        tv_rev_address=v.findViewById(R.id.tv_rev_address);
        tvItems=v.findViewById(R.id.tvItems);
        tvprice=v.findViewById(R.id.tvprice);
        tvMrp=v.findViewById(R.id.tvMrp);
        tv_gst=v.findViewById(R.id.tvGst);
        session_management=new Session_management(getActivity());
        tvDiscount=v.findViewById(R.id.tvDiscount);
        tvSubTotal=v.findViewById(R.id.tvSubTotal);
        rel_order=v.findViewById(R.id.rel_order);
        loc_id=getArguments().getString("loc_id");
        rev_name=getArguments().getString("name");
        rev_mobile=getArguments().getString("mobile");
        rev_soc_id=getArguments().getString("socity_id");
        rev_pincode=getArguments().getString("pincode");
        rev_address=getArguments().getString("address");
        rev_state=getArguments().getString("state");
        rev_city=getArguments().getString("city");
        plan_id=getArguments().getString("plan_id");
        plan_name=getArguments().getString("plan_name");
        working_days=getArguments().getString("working_days");
        plan_expiry=getArguments().getString("plan_expiry");
        mrp=getArguments().getString("mrp");
        price=getArguments().getString("price");
        ((SubscriptionActivity)getActivity()).setTitle("Order Details");
        rel_order.setOnClickListener(this);
        module=new Module(getActivity());
        tv_rev_name.setText(rev_name);
        tv_rev_mobile.setText(rev_mobile);
        tv_rev_pincode.setText(rev_pincode);
        String address=rev_address+"\n "+rev_state+" ("+rev_pincode+")";
        tv_rev_address.setText(address);

        tvItems.setText("1");
        tvprice.setText(getResources().getString(R.string.currency)+" "+price);
        tvMrp.setText(getResources().getString(R.string.currency)+" "+mrp);
        float dmrp=Float.parseFloat(mrp);
    float dprice=Float.parseFloat(price);
        int dis=(int)(dmrp-dprice);
        if(dis<=0)
        {
            tvDiscount.setText("No Discount");
        }
        else
        {
            tvDiscount.setText("-"+getResources().getString(R.string.currency)+" "+dis);
        }
//        gst =new Module(getActivity()).getGSt(gst,price)
//         tot =  dprice+;
//        tvSubTotal.setText(getResources().getString(R.string.currency)+" "+tot);
//        tv_gst.setText(getResources().getString(R.string.currency)+" "+module.getGSt(price));

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rel_order)
        {

            String user_id=session_management.getUserDetails().get(KEY_ID);
            String payment_status="paid";
            String trans_id="transae123";

            attemptOrder(user_id,payment_status,trans_id,plan_id,plan_name,mrp,price,plan_expiry,module.getCurrentDate());

        }
    }

    private void attemptOrder(String user_id, String payment_status, String trans_id, String plan_id, String plan_name, String mrp, String price, String plan_expiry, String currentDate) {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("payment",payment_status);
        params.put("trans_id",trans_id);
        params.put("package_id",plan_id);
        params.put("package_name",plan_name);
        params.put("package_mrp",mrp);
        params.put("package_price",price);
        params.put("package_duration",plan_expiry);
        params.put("order_date",currentDate);
        Log.e("paramssss",""+params.toString());
        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.ATTEMPT_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                   boolean resp=response.getBoolean("responce");
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    toastMsg.toastIconError("Something Went Wrong");
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

    public void loadFragment(Fragment fm,Bundle args)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fm.setArguments(args);
        fragmentManager.beginTransaction()
                .replace( R.id.content_frame,fm)
                .addToBackStack(null)
                .commit();
    }



}
