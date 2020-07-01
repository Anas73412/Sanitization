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
import com.google.gson.JsonArray;
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
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.MainActivity;
import in.sanitization.sanitization.Model.OrderModel;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.RecyclerTouchListener;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.Config.BaseUrl.GET_ORDER_URL;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;

public class My_Pending_Order extends Fragment {

  private static String TAG = My_Pending_Order.class.getSimpleName();
  Module module;
  private RecyclerView rv_myorder;
  RelativeLayout rel_no ;
  private List<OrderModel> my_order_modelList = new ArrayList<>();
  TabHost tHost;
 LoadingBar loadingBar;

  public My_Pending_Order() {

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loadingBar = new LoadingBar(getActivity());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_my_pending_order, container, false);
    rel_no =view.findViewById( R.id.rel_no );
    loadingBar = new LoadingBar(getActivity());
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

    // check internet connection
//    if (ConnectivityReceiver.isConnected())
//
//    {
      makeGetOrderRequest(user_id);
//    } else

//    {
//      ((HomeActivity) getActivity()).onNetworkConnectionChanged(false);
//    }

    // recyclerview item click listener
    rv_myorder.addOnItemTouchListener(new
            RecyclerTouchListener(getActivity(), rv_myorder, new RecyclerTouchListener.OnItemClickListener()
    {
      @Override
      public void onItemClick(View view, int position) {
        Bundle args = new Bundle();


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
              if (obj.getString("status").equals("0"))
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
        Log.e("orderlist1111", String.valueOf(my_order_modelList.size()));
        if (my_order_modelList.size()<=0) {
          rel_no.setVisibility(View.VISIBLE);
          rv_myorder.setVisibility(View.GONE);
         new ToastMsg(getActivity()).toastIconError("No Records Found");
        }
        else
        {
            rel_no.setVisibility(View.GONE);
            rv_myorder.setVisibility(View.VISIBLE);
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
