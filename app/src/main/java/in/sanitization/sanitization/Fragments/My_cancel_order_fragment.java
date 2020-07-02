package in.sanitization.sanitization.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

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

import in.sanitization.sanitization.Adapter.My_Cancel_Order_adapter;
import in.sanitization.sanitization.Adapter.Order_adapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.Model.OrderModel;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.RecyclerTouchListener;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.Config.BaseUrl.GET_ORDER_URL;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;

public class My_cancel_order_fragment extends Fragment {

    private RecyclerView rv_mycancel;
    Module module;
    RelativeLayout rel_no;
    private List<OrderModel> my_order_modelList = new ArrayList<>();
    TabHost tHost;
   LoadingBar loadingBar;
    public My_cancel_order_fragment() {
    }

    private static String TAG = My_cancel_order_fragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_my_cancel_order, container, false);
        loadingBar=new LoadingBar(getActivity());

        module=new Module(getActivity());
        // handle the touch event if true
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back button or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

//                    binplus.Jabico.Fragment fm = new Home_fragment();
//                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        rv_mycancel = (RecyclerView) view.findViewById(R.id.rv_mycancel);
        rel_no = (RelativeLayout) view.findViewById(R.id.rel_no);
        rv_mycancel.setLayoutManager(new LinearLayoutManager(getActivity()));

        Session_management sessionManagement = new Session_management(getActivity());
        String user_id = sessionManagement.getUserDetails().get(KEY_ID);

        // check internet connection
//        if (ConnectivityReceiver.isConnected())
//
//        {
            //makeGetOrderRequest(user_id);
//        } else
//
//        {
//            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
//        }

        rv_mycancel.addOnItemTouchListener(new
                RecyclerTouchListener(getActivity(), rv_mycancel, new RecyclerTouchListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position) {

//                Intent intent=new Intent(getContext(), MyOrderDetail.class);
//                intent.putExtra("sale_id", sale_id);
//                intent.putExtra("date", date);
//                intent.putExtra("time", time);
//                intent.putExtra("total", total);
//                intent.putExtra("status", status);
//                intent.putExtra("deli_charge", deli_charge);
//                intent.putExtra("type", "cancel");
//                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }


    private void makeGetOrderRequest(String userid) {
        loadingBar.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                GET_ORDER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("orders", response.toString());
                loadingBar.dismiss();
                try {
                    boolean res = response.getBoolean("responce");
                    if (res)
                    {
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0 ; i <data.length();i++)
                        {
                            JSONObject obj = data.getJSONObject(i);
                            OrderModel model = new OrderModel();
                            model.setOrder_id(obj.getString("order_id"));
                            model.setUser_id(obj.getString("user_id"));
                            model.setPayment(obj.getString("payment"));
                            model.setTransaction_id(obj.getString("transaction_id"));
                            model.setPackage_id(obj.getString("package_id"));
                            model.setPackage_name(obj.getString("package_name"));
                            model.setPackage_mrp(obj.getString("package_mrp"));
                            model.setPackage_price(obj.getString("package_price"));
                            model.setPackage_duration(obj.getString("package_duration"));
                            model.setWorker_id(obj.getString("worker_id"));
                            model.setOrder_date(obj.getString("order_date"));
                            model.setStatus(obj.getString("status"));
                            model.setCreated_at(obj.getString("created_at"));
                            if (obj.getString("status").equals("3"))
                            {
                                my_order_modelList.add(model);
//                Log.d("orderlist0", String.valueOf(my_order_modelList.size()));
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("exp",e.getMessage());
                }
                Log.d("orderlist", String.valueOf(my_order_modelList.size()));
                if (my_order_modelList.isEmpty()) {
                    rel_no.setVisibility(View.VISIBLE);
                    rv_mycancel.setVisibility(View.GONE);
                    new ToastMsg(getActivity()).toastIconError("No Records Found");
                }
                else
                {
                    Order_adapter myPendingOrderAdapter = new Order_adapter(my_order_modelList,getActivity());
                    rv_mycancel.setAdapter(myPendingOrderAdapter);
                    myPendingOrderAdapter.notifyDataSetChanged();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    new ToastMsg(getActivity()).toastIconError(msg);
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

}
