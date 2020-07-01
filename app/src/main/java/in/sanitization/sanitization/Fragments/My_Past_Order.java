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

import in.sanitization.sanitization.Adapter.Order_adapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.Model.OrderModel;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.RecyclerTouchListener;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.Config.BaseUrl.GET_DELIVERD_ORDER_URL;
import static in.sanitization.sanitization.Config.BaseUrl.GET_ORDER_URL;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;


public class My_Past_Order extends Fragment {

  //  private static String TAG = binplus.Jabico.Fragment.My_Past_Order.class.getSimpleName();
  Module module;
  private RecyclerView rv_myorder;
  LoadingBar loadingBar;
  RelativeLayout rel_no ;

  private List<OrderModel> my_order_modelList = new ArrayList<>();
  TabHost tHost;

  public My_Past_Order() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loadingBar = new LoadingBar(getActivity());
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_my_past_order, container, false);

    // ((My_Order_activity) getActivity()).setTitle(getResources().getString(R.string.my_order));
    loadingBar = new LoadingBar(getActivity());
    rel_no=(RelativeLayout)view.findViewById(R.id.rel_no);
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

    rv_myorder = (RecyclerView) view.findViewById(R.id.rv_myorder);
    rv_myorder.setLayoutManager(new LinearLayoutManager(getActivity()));

    Session_management sessionManagement = new Session_management(getActivity());
    String user_id = sessionManagement.getUserDetails().get(KEY_ID);

//    if (ConnectivityReceiver.isConnected())
//
//    {
//      makeGetOrderRequest(user_id);
//    } else
//
//    {
//      ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
//    }

    // recyclerview item click listener
    rv_myorder.addOnItemTouchListener(new

            RecyclerTouchListener(getActivity(), rv_myorder, new RecyclerTouchListener.OnItemClickListener()

    {
      @Override
      public void onItemClick(View view, int position) {

//        Intent intent=new Intent(getContext(), MyOrderDetail.class);
//        intent.putExtra("sale_id", sale_id);
//        intent.putExtra("date", date);
//        intent.putExtra("time", time);
//        intent.putExtra("total", total);
//        intent.putExtra("status", status);
//        intent.putExtra("deli_charge", deli_charge);
//        intent.putExtra("type", "past");
//        startActivity(intent);

      }

      @Override
      public void onLongItemClick(View view, int position) {

      }
    }));

    return view;
  }


  /**
   * Method to make json array request where json response starts wtih
   */
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
              if (obj.getString("status").equals("1"))
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
        Log.d("orderlistconfirm", String.valueOf(my_order_modelList.size()));
        if (my_order_modelList.isEmpty()) {
          rel_no.setVisibility(View.VISIBLE);
          rv_myorder.setVisibility(View.GONE);
          new ToastMsg(getActivity()).toastIconError("No Records Found");
        }
        else
        {
          Order_adapter myPendingOrderAdapter = new Order_adapter(my_order_modelList,getActivity());
          rv_myorder.setAdapter(myPendingOrderAdapter);
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
