package in.sanitization.sanitization.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sanitization.sanitization.Adapter.Order_adapter;
import in.sanitization.sanitization.Adapter.PagerOrderAdapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.Model.OrderModel;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.RecyclerTouchListener;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.Config.BaseUrl.GET_ORDER_URL;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrders extends Fragment {
    ViewPager viewPager ;
    TabLayout tabLayout ;
    LoadingBar loadingBar ;

    Module module;
    private RecyclerView rv_myorder;

    RelativeLayout rel_no ;

    private List<OrderModel> my_order_modelList = new ArrayList<>();

    public MyOrders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_my_orders, container, false);
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

    if (ConnectivityReceiver.isConnected())

    {
      makeGetOrderRequest(user_id);
    }  else
    {
        Intent intent = new Intent(getActivity(), NoInternetConnection.class);
        startActivity(intent);
    }

        // recyclerview item click listener
        rv_myorder.addOnItemTouchListener(new

                RecyclerTouchListener(getActivity(), rv_myorder, new RecyclerTouchListener.OnItemClickListener()

        {
            @Override
            public void onItemClick(View view, int position) {
                OrderModel model = my_order_modelList.get(position);
            Bundle bundle = new Bundle();
            Log.e("pack_id",model.getPackage_id());
//        Intent intent=new Intent(getContext(), MyOrderDetail.class);
        bundle.putString("order_id",model.getOrder_id());
        bundle.putString("user_id",model.getUser_id());
        bundle.putString("worker_id",model.getWorker_id());
        bundle.putString("package_id",model.getPackage_id());
        bundle.putString("package_name",model.getPackage_name());
        bundle.putString("package_price",model.getPackage_price());
        bundle.putString("package_duration",model.getPackage_duration());
        bundle.putString("date", model.getCreated_at());
        bundle.putString("gst", model.getGst());
        bundle.putString("total", model.getGross_amount());
//        bundle.putString("time", model.getCreated_at().substring(11,18));
        bundle.putString("status",model.getStatus());
        bundle.putString("duration",model.getPackage_duration());
        bundle.putString("r_name",model.getReceiver_name());
        bundle.putString("r_mobile",model.getReceiver_mobile());
        bundle.putString("location_id",model.getLocation_id());
//        bundle.putString("description",model.getP);
        Fragment fm = new My_order_detail_fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fm.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace( R.id.frame,fm)
                        .addToBackStack(null)
                        .commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        ((HomeActivity) getActivity()).setTitle("My Orders");
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Recent Orders"));
        tabLayout.addTab(tabLayout.newTab().setText("Past Orders"));
        tabLayout.addTab(tabLayout.newTab().setText("Cancelled Orders"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)view.findViewById(R.id.pager);
        final PagerOrderAdapter adapter = new PagerOrderAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

      return view ;
    }

    private void makeGetOrderRequest(String userid) {
        loadingBar.show();
        my_order_modelList.clear();
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
                            model.setGst(obj.getString("gst"));
                            model.setGross_amount(obj.getString("gross_amount"));
                            model.setPackage_duration(obj.getString("package_duration"));
                            model.setWorker_id(obj.getString("worker_id"));
                            model.setOrder_date(obj.getString("order_date"));
                            model.setStatus(obj.getString("status"));
                            model.setCreated_at(obj.getString("created_at"));
                            model.setReceiver_mobile(obj.getString("receiver_mobile"));
                            model.setReceiver_name(obj.getString("receiver_name"));
                            model.setLocation_id(obj.getString("location_id"));

                                my_order_modelList.add(model);
//                Log.d("orderlist0", String.valueOf(my_order_modelList.size()));


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("exp",e.getMessage());
                }
                Log.d("orderlist", String.valueOf(my_order_modelList.size()));
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
