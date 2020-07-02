package in.sanitization.sanitization.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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

import in.sanitization.sanitization.Adapter.Socity_adapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.Model.BlockModel;
import in.sanitization.sanitization.Model.DistrictModel;
import in.sanitization.sanitization.Model.Socity_model;
import in.sanitization.sanitization.Model.StateModel;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.SubscriptionActivity;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.Config.BaseUrl.ADD_ADDRESS_URL;
import static in.sanitization.sanitization.Config.BaseUrl.EDIT_ADDRESS_URL;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;
import static in.sanitization.sanitization.Config.Constants.KEY_SOCITY_ID;
import static in.sanitization.sanitization.Config.Constants.KEY_SOCITY_NAME;
import static in.sanitization.sanitization.Config.Constants.KEY_SOCITY_PINCODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAddressFragment extends Fragment implements View.OnClickListener{

    Module module;
    String eName="",elocation_id="",eMobile="",eDistrict="",eBlock="",
            eSocictyId="",ePincode="",eAddress="",eState="",eCity="",eDesc="",eAddType="";
    String is_edit="";
    String city_name="",distict_name="",block_name="";
    Spinner spin_block;
    LoadingBar loadingBar;
    Button btn_home,btn_shop,btn_other,btn_office;
    int flagType=0;

    ArrayList<StateModel> stateModelList;
    ArrayList<DistrictModel> districtModelList;
    ArrayList<BlockModel> blockModelList;
    EditText et_name,et_number,et_address,et_details,et_pincode;
    Button btn_submit;
    AutoCompleteTextView et_state,et_district;
    ArrayList<String> state_list,city_list,temp_list,district_list,block_list,tempDisList,tempBlockList;
    ToastMsg toastMsg;
    ArrayList<Socity_model> socity_modelList;
    Session_management session_management;
    String sPin="",sScId="",sScNm="";
    public AddAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_address, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View v) {
        module=new Module(getActivity());
        toastMsg=new ToastMsg(getActivity());
        btn_home=v.findViewById(R.id.btn_home);
        btn_office=v.findViewById(R.id.btn_office);
        btn_shop=v.findViewById(R.id.btn_shop);
        btn_other=v.findViewById(R.id.btn_other);
        et_district=v.findViewById(R.id.et_district);
        spin_block=v.findViewById(R.id.spin_block);
        btn_home.setOnClickListener(this);
        btn_office.setOnClickListener(this);
        btn_shop.setOnClickListener(this);
        btn_other.setOnClickListener(this);
        session_management=new Session_management(getActivity());
        loadingBar=new LoadingBar(getActivity());
        et_name=v.findViewById(R.id.et_name);
        et_number=v.findViewById(R.id.et_number);
        et_pincode=v.findViewById(R.id.et_pincode);
        et_address=v.findViewById(R.id.et_address);
        et_details=v.findViewById(R.id.et_details);
        btn_submit=v.findViewById(R.id.btn_submit);
        et_state=v.findViewById(R.id.et_state);
        state_list=new ArrayList<>();
        city_list=new ArrayList<>();
        temp_list=new ArrayList<>();
        tempDisList=new ArrayList<>();
        tempBlockList=new ArrayList<>();
        stateModelList=new ArrayList<>();
        districtModelList=new ArrayList<>();
        blockModelList=new ArrayList<>();
        district_list=new ArrayList<>();
        socity_modelList=new ArrayList<>();
        block_list=new ArrayList<>();
        btn_submit.setOnClickListener(this);
        getstates();
        is_edit=getArguments().getString("is_edit");
        elocation_id=getArguments().getString("location_id");
        eName=getArguments().getString("name");
        eMobile=getArguments().getString("mobile");
        eSocictyId=getArguments().getString("socity_id");
        ePincode=getArguments().getString("pincode");
        eAddress=getArguments().getString("address");
        eState=getArguments().getString("state");
        eDistrict=getArguments().getString("district");
        eBlock=getArguments().getString("block");
        eDesc=getArguments().getString("desc");
        eAddType=getArguments().getString("add_type");
        ((SubscriptionActivity)getActivity()).setTitle("Add Address");
        et_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String state=et_state.getText().toString().trim();
                et_district.setText("");
                if(!state.isEmpty())
                {

                    getDistrict(module.getStateId(stateModelList,state),true,"");

                }
            }
        });

        et_district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dis=et_district.getText().toString().trim();
                if(!dis.isEmpty())
                {
//                    Log.e("district_id",""+module.getDistrictId(districtModelList,dis));
                    getBlock(module.getDistrictId(districtModelList,dis),true,"");
                }
            }
        });
        btn_submit.setText("Add Address");
        flagType=1;
        module.setDrawableBackground(btn_home);
        if(is_edit.equalsIgnoreCase("true"))
        {
            btn_submit.setText("Update Address");
            ((SubscriptionActivity)getActivity()).setTitle("Update Address");
            et_name.setText(eName);
            et_number.setText(eMobile);
            et_state.setText(eState);
//            et_district.setText(eDistrict);
            et_pincode.setText(ePincode);


            if(!eState.isEmpty())
            {
                distict_name=eDistrict;
            }
            if(!eDistrict.isEmpty())
            {
                block_name=eBlock;


            }
            et_details.setText(eDesc);
            flagType=module.getFlagTypeOnAddress(eAddType);
            switch (flagType)
            {
                case 1:  module.setDrawableBackground(btn_home);
                         break;
                case 2: module.setDrawableBackground(btn_office);
                    break;
                case 3: module.setDrawableBackground(btn_shop);
                    break;
                case 4: module.setDrawableBackground(btn_other);
                    break;
                default: module.setDrawableBackground(btn_home);
                    break;
            }
//            sScId=eSocictyId;
            et_address.setText(eAddress);

            removeFlagType(flagType);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_home)
        {
            flagType=1;
            module.setDrawableBackground(btn_home);
            removeFlagType(flagType);
        }
        else if(v.getId() == R.id.btn_office)
        {
            flagType=2;
            module.setDrawableBackground(btn_office);
            removeFlagType(flagType);
        }else if(v.getId() == R.id.btn_shop)
        {
            flagType=3;
            module.setDrawableBackground(btn_shop);
            removeFlagType(flagType);
        }else if(v.getId() == R.id.btn_other)
        {
            flagType=4;
            module.setDrawableBackground(btn_other);
            removeFlagType(flagType);
        }
        else if(v.getId()==R.id.btn_submit)
        {

            validateData();
        }


    }

    private void validateData() {
        String name=et_name.getText().toString();
        String mobile=et_number.getText().toString();
        String state=et_state.getText().toString();
        String district=et_district.getText().toString();
        String pincode=et_pincode.getText().toString();
        String address=et_address.getText().toString();
        String details=et_details.getText().toString();


        if(name.isEmpty())
        {
            module.setErrorOnEditText(et_name,"Enter Name");
        }
        else if(mobile.isEmpty())
        {
            module.setErrorOnEditText(et_number,"Enter Mobile number");
        }
        else if(mobile.length()!=10)
        {
            module.setErrorOnEditText(et_number,"Invalid Mobile Number");
        }
        else if(state.isEmpty())
        {
            toastMsg.toastIconError("Select a State");
            et_state.requestFocus();
        }
        else if(!state_list.contains(state))
        {
            toastMsg.toastIconError("Invalid State");
            et_state.requestFocus();
        }
        else if(district.isEmpty())
        {
            toastMsg.toastIconError("Select a District");
            et_district.requestFocus();
        }
        else if(!district_list.contains(district))
        {
            toastMsg.toastIconError("Invalid District");
            et_district.requestFocus();
        }
        else if(pincode.isEmpty())
        {
            toastMsg.toastIconError("Enter pincode");

        } else if(address.isEmpty())
        {
            module.setErrorOnEditText(et_address,"Enter Address");
        }
        else {
            String block = spin_block.getSelectedItem().toString();
            if (block.isEmpty() || block.equalsIgnoreCase("Select City")) {
                toastMsg.toastIconError("Select Block");
            }
            String user_id = session_management.getUserDetails().get(KEY_ID).toString();
            if (ConnectivityReceiver.isConnected()) {
                if (is_edit.equalsIgnoreCase("true")) {
                    editAddress(elocation_id, user_id, name, mobile, state, district, block, pincode, address, details, module.getBuildingType(flagType));
                } else {
                    addAddress(user_id, name, mobile, state, district, block, pincode, address, details, module.getBuildingType(flagType));
                }
            }
            else
            {
                Intent intent = new Intent(getActivity(), NoInternetConnection.class);
                startActivity(intent);
            }
        }
    }

    private void addAddress(String user_id, String name, final String mobile, String state, String district,String block, String pincode, String address, String details, String buildingType) {
        loadingBar.show();
        String json_tag="json_add_address";
        HashMap<String,String> parmas=new HashMap<>();
        parmas.put("user_id",user_id);
        parmas.put("name",name);
        parmas.put("mobile",mobile);
        parmas.put("state",state);
        parmas.put("district",module.getDistrictId(districtModelList,district));
        parmas.put("block",module.getBlockId(blockModelList,block));
        parmas.put("pincode",pincode);
        parmas.put("address",address);
        parmas.put("details",details);
        parmas.put("building",buildingType);
        Log.e("add_address",""+parmas.toString());
        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, ADD_ADDRESS_URL, parmas, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        toastMsg.toastIconSuccess(""+response.getString("message"));
                        ((SubscriptionActivity) getActivity()).onBackPressed();
                    }
                    else
                    {
                        toastMsg.toastIconError(""+response.getString("error"));
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
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    module.showToast(""+msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
    }
    private void editAddress(String elocation_id,String user_id, String name, final String mobile, String state, String district,String block, String pincode, String address, String details, String buildingType) {
        loadingBar.show();
        String json_tag="json_add_address";
        HashMap<String,String> parmas=new HashMap<>();
        parmas.put("user_id",user_id);
        parmas.put("location_id",elocation_id);
        parmas.put("name",name);
        parmas.put("mobile",mobile);
        parmas.put("state",state);
        parmas.put("district_id",module.getDistrictId(districtModelList,district));
        parmas.put("block_id",module.getBlockId(blockModelList,block));
        parmas.put("pincode",pincode);
        parmas.put("address",address);
        parmas.put("details",details);
        parmas.put("building",buildingType);
        Log.e("edit_address",""+parmas.toString());

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, EDIT_ADDRESS_URL, parmas, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        toastMsg.toastIconSuccess(""+response.getString("message"));
                        ((SubscriptionActivity) getActivity()).onBackPressed();
                    }
                    else
                    {
                        toastMsg.toastIconError(""+response.getString("error"));
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
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    module.showToast(""+msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
    }

    public void removeFlagType(int flg)
    {
        switch (flg)
        {
            case 1:
                module.removeDrawableBackground(btn_office);
                module.removeDrawableBackground(btn_shop);
                module.removeDrawableBackground(btn_other);
                break;
            case 2:
                module.removeDrawableBackground(btn_home);
                module.removeDrawableBackground(btn_shop);
                module.removeDrawableBackground(btn_other);
                break;
            case 3:
                module.removeDrawableBackground(btn_office);
                module.removeDrawableBackground(btn_home);
                module.removeDrawableBackground(btn_other);
                break;
            case 4:
                module.removeDrawableBackground(btn_office);
                module.removeDrawableBackground(btn_shop);
                module.removeDrawableBackground(btn_home);
                break;
        }
    }

    private void getstates() {

        Map<String, String> params = new HashMap<String, String>();
        stateModelList.clear();
        state_list.clear();
        final CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.GET_STATES, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean status = response.getBoolean("responce");
                    if (status)
                    {
                        JSONArray data = response.getJSONArray("data");
                        Gson gsonState=new Gson();
                        Type listType=new TypeToken<ArrayList<StateModel>>(){}.getType();
                        stateModelList=gsonState.fromJson(data.toString(),listType);
                        for (int i = 0 ; i <stateModelList.size();i++)
                        {
                            state_list.add(stateModelList.get(i).getState_name().toString());

                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getActivity(), android.R.layout.simple_list_item_1,state_list);
                        et_state.setAdapter(arrayAdapter);
                        et_state.setThreshold(1);
                        if(is_edit.equalsIgnoreCase("true"))
                        {
                            getDistrict(module.getStateId(stateModelList,eState),false,eDistrict);
                        }

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
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");

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


    private void getDistrict(String stateId, final boolean flag,final String dis_name) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("state_id",stateId);
        districtModelList.clear();
        district_list.clear();
        final CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.GET_DISTRICT, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean status = response.getBoolean("responce");
                    if (status)
                    {
                        JSONArray data = response.getJSONArray("data");
                        Gson gsonState=new Gson();
                        Type listType=new TypeToken<ArrayList<DistrictModel>>(){}.getType();
                        districtModelList=gsonState.fromJson(data.toString(),listType);
                        for (int i = 0 ; i <districtModelList.size();i++)
                        {
                            district_list.add(districtModelList.get(i).getDistrict_name().toString());

                        }

                        tempDisList.clear();
                        tempDisList.addAll(district_list);
                        if(!flag)
                        {
                            if(!dis_name.isEmpty())
                            {


//                                et_district.setSelection(module.getStringListIndex(district_list,dis_name));
                                et_district.setText(district_list.get(module.getStringListIndex(district_list,dis_name)));
                            }
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getActivity(), android.R.layout.simple_list_item_1,district_list);
                        et_district.setAdapter(arrayAdapter);
                        et_district.setThreshold(1);

                        if(is_edit.equalsIgnoreCase("true"))
                        {
                            getBlock(module.getDistrictId(districtModelList,eDistrict),false,eBlock);
                        }

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
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");


    }

    private void getBlock(String districtId, final boolean flag,final String block_name) {
        loadingBar.show();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("dis_id",districtId);
        blockModelList.clear();
        block_list.clear();
        final CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.GET_BLOCK, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("block_details - "+flag,""+params.toString()+" - "+response.toString());
                loadingBar.dismiss();
                try {
                    boolean status = response.getBoolean("responce");
                    if (status)
                    {
                        JSONArray data = response.getJSONArray("data");
                        Gson gsonState=new Gson();
                        Type listType=new TypeToken<ArrayList<BlockModel>>(){}.getType();
                        blockModelList=gsonState.fromJson(data.toString(),listType);
                        for (int i = 0 ; i <blockModelList.size();i++)
                        {
                            block_list.add(blockModelList.get(i).getBlock_name().toString());

                        }
                        module.setSpinAdapter(block_list,spin_block,getActivity(),"Select Block");

                        tempBlockList.clear();
                        tempBlockList.addAll(block_list);
                        if(!flag)
                        {
                            if(!block_name.isEmpty())
                            {

                                spin_block.setSelection(module.getStringListIndex(block_list,block_name));
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
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");

    }

}
