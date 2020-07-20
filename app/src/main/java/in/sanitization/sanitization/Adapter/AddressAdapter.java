package in.sanitization.sanitization.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.Fragments.AddAddressFragment;
import in.sanitization.sanitization.MainActivity;
import in.sanitization.sanitization.Model.AddressModel;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.SubscriptionActivity;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 19,June,2020
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    String TAG=AddressAdapter.class.getSimpleName();
    Activity activity;
    ArrayList<AddressModel> modelList;
    Module module;
    LoadingBar loadingBar;
    ToastMsg toastMsg;
    private String location_id = "";
    private String getsocityid, getstate,getcity, getphone, getpin, getname, getdesc ,getaddress,getaddresstype;
    private static int lastCheckedPos = 0;
    private static RadioButton lastChecked = null;
    private boolean ischecked = false;

    public AddressAdapter(Activity activity, ArrayList<AddressModel> modelList) {
        this.activity = activity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.row_delivery_time_rv,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_name.setText(modelList.get(position).getReceiver_name());
        holder.tv_phone.setText(modelList.get(position).getReceiver_mobile());
        holder.tv_address.setText(modelList.get(position).getAddress());

        holder.rb_select.setChecked(modelList.get(position).isIscheckd());
        holder.rb_select.setTag(new Integer(position));

        //for default check in first item
        if (position == 0 /*&& mList.getIscheckd() && holder.rb_select.isChecked()*/) {
            holder.rb_select.setChecked(true);
            modelList.get(position).setIscheckd(true);

            lastChecked = holder.rb_select;
            lastCheckedPos = 0;

            location_id = modelList.get(0).getLocation_id();
            getname = modelList.get(0).getReceiver_name();
            getphone = modelList.get(0).getReceiver_mobile();
            getsocityid = modelList.get(0).getSocity_id();
            getpin = modelList.get(0).getPincode();
            getaddress = modelList.get(0).getAddress();
            getstate = modelList.get(0).getState();
            getdesc = modelList.get(0).getDescription();
            getaddresstype = modelList.get(0).getAddress_type();

            ischecked = true;

        }


        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure want to delete this address?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(ConnectivityReceiver.isConnected()){
                            makeDeleteAddressRequest(modelList.get(position).getLocation_id(),position);
                        }

                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog=builder.create();
                dialog.show();


            }
        });
        holder.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                Fragment fm = new AddAddressFragment();
                args.putString("location_id",modelList.get(position).getLocation_id());
                args.putString("name", modelList.get(position).getReceiver_name());
                args.putString("mobile", modelList.get(position).getReceiver_mobile());
                args.putString("socity_id", modelList.get(position).getSocity_id());
                args.putString("pincode", modelList.get(position).getPincode());
                args.putString("address", modelList.get(position).getAddress());
                args.putString("state", modelList.get(position).getState());
                args.putString("desc", modelList.get(position).getDescription());
                args.putString("district", modelList.get(position).getDistrict_name());
                args.putString("block", modelList.get(position).getBlock_name());
                args.putString("add_type", modelList.get(position).getAddress_type());
                args.putString("is_edit", "true");
                //  args.putString( "address",getaddress );

                fm.setArguments(args);
                FragmentManager fragmentManager = ((SubscriptionActivity) activity).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fm)
                        .addToBackStack(null).commit();
//
            }
        });


    }

    private void makeDeleteAddressRequest(String location_id, final int position) {
        loadingBar.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_delete_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("location_id", location_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.DELETE_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");

                        Toast.makeText(activity, ""+msg, Toast.LENGTH_SHORT).show();

                        modelList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, modelList.size());
                        //  mItemManger.closeAllItems();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
               module.errMessage(error);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_address, tv_name, tv_phone, tv_charges,txtEdit,txtDelete;
        public RadioButton rb_select;
        public ViewHolder(@NonNull View view) {
            super(view);
            tv_address = (TextView) view.findViewById(R.id.tv_adres_addres);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_phone = (TextView) view.findViewById(R.id.tv_adres_phone);
            tv_charges = (TextView) view.findViewById(R.id.tv_adres_charge);
            txtEdit = (TextView) view.findViewById(R.id.txtEdit);
            txtDelete = (TextView) view.findViewById(R.id.txtDelete);
            rb_select = (RadioButton) view.findViewById(R.id.rb_adres);
            module=new Module(activity);
            loadingBar=new LoadingBar(activity);
            toastMsg=new ToastMsg(activity);
            rb_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RadioButton cb = (RadioButton) view;
                    int clickedPos = getAdapterPosition();

                    location_id = modelList.get(clickedPos).getLocation_id();
                    getname = modelList.get(clickedPos).getReceiver_name();
                    getphone = modelList.get(clickedPos).getReceiver_mobile();
                    getsocityid=modelList.get(clickedPos).getSocity_id();
                    getpin=modelList.get(clickedPos).getPincode();
                    getaddress=modelList.get(clickedPos).getAddress();
                    getstate=modelList.get(clickedPos).getState();
                    getdesc=modelList.get(clickedPos).getDescription();
                    getaddresstype=modelList.get(clickedPos).getAddress_type();

                    if (modelList.size() > 1) {
                        if (cb.isChecked()) {
                            if (lastChecked != null) {
                                lastChecked.setChecked(false);
                                modelList.get(lastCheckedPos).setIscheckd(false);
                            }

                            lastChecked = cb;
                            lastCheckedPos = clickedPos;
                        } else {
                            lastChecked = null;
                        }
                    }
                    modelList.get(clickedPos).setIscheckd(cb.isChecked());
//
                }
            });
        }
    }
    public boolean ischeckd() {
        return ischecked;
    }

    public HashMap<String,String> getAlladdress() {


        HashMap<String,String> map=new HashMap<String, String>(  );
        map.put("location_id",location_id);
        map.put("name",getname);
        map.put("phone",getphone);
        map.put("socity_id",getsocityid);
        map.put("pincode",getpin);
        map.put("address",getaddress);
        map.put("state",getstate);
        map.put("city",getcity);
        map.put("desc",getdesc);
        map.put("address_type",getaddresstype);

        return map;
    }
}
