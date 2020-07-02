package in.sanitization.sanitization.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sanitization.sanitization.Adapter.AddressAdapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.Model.AddressModel;
import in.sanitization.sanitization.PaymentActivity;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.SubscriptionActivity;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.Config.Constants.KEY_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment implements View.OnClickListener{

    String user_id="",plan_name="",plan_id="",plan_price="0",plan_mrp="0",plan_amt="0",plan_expiry="",working_days="";
    Button btn_add_address;
    Session_management session_management;
    Module module;
    LoadingBar loadingBar;
    ToastMsg toastMsg;
    RelativeLayout btn_continue;
    AddressAdapter addressAdapter;
    TextView tv_name,tv_price,tv_mrp,tv_amount;
    ArrayList<AddressModel> address_list;
    RecyclerView rv_address;

    public AddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_address, container, false);
        initViews(view);
        if (ConnectivityReceiver.isConnected()) {
            getAllAddress(user_id);
        }  else
        {
            Intent intent = new Intent(getActivity(), NoInternetConnection.class);
            startActivity(intent);
        }
        return view;
    }

    private void initViews(View v) {
        module=new Module(getActivity());
        toastMsg=new ToastMsg(getActivity());
        loadingBar=new LoadingBar(getActivity());
        session_management=new Session_management(getActivity());
        btn_add_address=v.findViewById(R.id.btn_add_address);
        tv_name=v.findViewById(R.id.tv_name);
        tv_price=v.findViewById(R.id.tv_price);
        tv_mrp=v.findViewById(R.id.tv_mrp);
        tv_amount=v.findViewById(R.id.tv_amount);
        rv_address=v.findViewById(R.id.rv_address);
        btn_continue=v.findViewById(R.id.btn_continue);
        btn_add_address.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        user_id=session_management.getUserDetails().get(KEY_ID).toString();
        plan_id=getActivity().getIntent().getStringExtra("id");
        plan_name=getActivity().getIntent().getStringExtra("name");
        plan_price=getActivity().getIntent().getStringExtra("price");
        plan_mrp=getActivity().getIntent().getStringExtra("mrp");
        plan_expiry=getActivity().getIntent().getStringExtra("plan_expiry");
        working_days=getActivity().getIntent().getStringExtra("working_days");
        ((SubscriptionActivity)getActivity()).setTitle("Select Address");
        plan_amt=plan_price;
        address_list=new ArrayList<>();
        tv_name.setText(plan_name);
        tv_price.setText(getActivity().getResources().getString(R.string.currency)+" "+plan_price);
        tv_mrp.setText(getActivity().getResources().getString(R.string.currency)+" "+plan_mrp);
        tv_amount.setText(getActivity().getResources().getString(R.string.currency)+" "+plan_amt);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_add_address){

            session_management.clearSocities();
            AddAddressFragment fm=new AddAddressFragment();
            Bundle bundle=new Bundle();
            bundle.putString("is_edit","false");
            loadFragment(fm,bundle);
        }
        else if(v.getId()==R.id.btn_continue)
        {
          HashMap<String,String> addressMap=addressAdapter.getAlladdress();
            Intent bundle=new Intent(getActivity(), PaymentActivity.class);

          bundle.putExtra("plan_id",plan_id);
          bundle.putExtra("plan_name",plan_name);
          bundle.putExtra("mrp",plan_mrp);
          bundle.putExtra("price",plan_price);
          bundle.putExtra("loc_id",addressMap.get("location_id"));
          bundle.putExtra("address",addressMap.get("address"));
          bundle.putExtra("name",addressMap.get("name"));
          bundle.putExtra("pincode",addressMap.get("pincode"));
          bundle.putExtra("mobile",addressMap.get("phone"));
          bundle.putExtra("state",addressMap.get("state"));
          bundle.putExtra("city",addressMap.get("city"));
          bundle.putExtra("socity_id",addressMap.get("socity_id"));
          bundle.putExtra("plan_expiry",plan_expiry);
          bundle.putExtra("working_days",working_days);
          Log.e("addreess_fragment",""+plan_id.toString());
          startActivity(bundle);



        }
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

    public void getAllAddress(String user_id)
    {
     loadingBar.show();
     address_list.clear();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.GET_ADDRESS_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                Log.e("all_address",""+response.toString());
              try {
                  boolean resp=response.getBoolean("responce");
                  if(resp)
                  {
                      Gson gson=new Gson();
                      Type listType = new TypeToken<List<AddressModel>>() {
                      }.getType();

                      address_list = gson.fromJson(response.getString("data"), listType);
                      rv_address.setLayoutManager(new LinearLayoutManager(getActivity()));
                      addressAdapter=new AddressAdapter(getActivity(),address_list);
                      rv_address.setAdapter(addressAdapter);
                      addressAdapter.notifyDataSetChanged();
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
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
    }
}
