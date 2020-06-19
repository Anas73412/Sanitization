package in.sanitization.sanitization.Fragments;

import android.app.Activity;
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
import in.sanitization.sanitization.Model.Socity_model;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.SubscriptionActivity;
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
    String eName="",elocation_id="",eMobile="",eSocictyId="",ePincode="",eAddress="",eState="",eCity="",eDesc="",eAddType="";
    String is_edit="";
    String city_name="";
    LoadingBar loadingBar;
    Button btn_home,btn_shop,btn_other,btn_office;
    int flagType=0;
    Spinner spin_city;
    EditText et_name,et_number,et_address,et_details;
    Button btn_submit;
    AutoCompleteTextView et_state;
    TextView tv_pincode;
    ArrayList<String> state_list,city_list,temp_list;
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
        spin_city=v.findViewById(R.id.spin_city);
        btn_home.setOnClickListener(this);
        btn_office.setOnClickListener(this);
        btn_shop.setOnClickListener(this);
        btn_other.setOnClickListener(this);
        session_management=new Session_management(getActivity());
        loadingBar=new LoadingBar(getActivity());
        et_name=v.findViewById(R.id.et_name);
        et_number=v.findViewById(R.id.et_number);
        tv_pincode=v.findViewById(R.id.tv_pincode);
        et_address=v.findViewById(R.id.et_address);
        et_details=v.findViewById(R.id.et_details);
        btn_submit=v.findViewById(R.id.btn_submit);
        et_state=v.findViewById(R.id.et_state);
        state_list=new ArrayList<>();
        city_list=new ArrayList<>();
        temp_list=new ArrayList<>();
        socity_modelList=new ArrayList<>();
        tv_pincode.setOnClickListener(this);
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
        eCity=getArguments().getString("city");
        eDesc=getArguments().getString("desc");
        eAddType=getArguments().getString("add_type");
        et_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String state=et_state.getText().toString().trim();
                if(!state.isEmpty())
                {
                    getcities(state,true,"");
                }
            }
        });
        btn_submit.setText("Add Address");
        flagType=1;
        module.setDrawableBackground(btn_home);
        if(is_edit.equalsIgnoreCase("true"))
        {
            btn_submit.setText("Update Address");
            et_name.setText(eName);
            et_number.setText(eMobile);
            et_state.setText(eState);
            if(!eState.isEmpty())
            {
                city_name=eCity;
            }
            if(!eCity.isEmpty())
            {
                String sId=session_management.getSocityDetails().get(KEY_SOCITY_ID).toString();
                if(sId.isEmpty())
                {
                    makeGetSocityRequest(eCity.toString(),eSocictyId);
                }
                else
                {
                    makeGetSocityRequest(eCity.toString(),sId);
                }


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



            if (!(session_management.getSocityDetails().get(KEY_SOCITY_ID).isEmpty())) {
                sPin = session_management.getSocityDetails().get(KEY_SOCITY_PINCODE);
                sScId = session_management.getSocityDetails().get(KEY_SOCITY_ID);
                sScNm = session_management.getSocityDetails().get(KEY_SOCITY_NAME);
                tv_pincode.setText(sScNm + " (" + sPin + ")");
            }

            spin_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    city_name=city_list.get(position).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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
        else if(v.getId()== R.id.tv_pincode)
        {
            String cty=spin_city.getSelectedItem().toString();
            if(cty.isEmpty())
            {
                toastMsg.toastIconError("Select a city");

            }
            else if(cty.equalsIgnoreCase("Select City"))
            {
                toastMsg.toastIconError("Invalid city");
            }
            else
            {
                SocietyFragment fm=new SocietyFragment();
                Bundle bundle=new Bundle();
                bundle.putString("city",cty);
                loadFragment(fm,bundle);
            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();

 if(!et_state.getText().toString().isEmpty())
 {
     getcities(et_state.getText().toString(),false,city_name);
 }
    }

    private void validateData() {
        String name=et_name.getText().toString();
        String mobile=et_number.getText().toString();
        String state=et_state.getText().toString();
        String pincode=tv_pincode.getText().toString();
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
        else if(pincode.isEmpty())
        {
            toastMsg.toastIconError("Select a pincode");

        } else if(address.isEmpty())
        {
            module.setErrorOnEditText(et_address,"Enter Address");
        }
        else
        {
            String city=spin_city.getSelectedItem().toString();
       if(city.isEmpty() || city.equalsIgnoreCase("Select City"))
       {
          toastMsg.toastIconError("Select City");
       }
            String user_id=session_management.getUserDetails().get(KEY_ID).toString();
            if(is_edit.equalsIgnoreCase("true"))
            {
                editAddress(elocation_id,name,mobile,state,city,sPin,address,details,module.getBuildingType(flagType),sScId);
            }
            else
            {
                addAddress(user_id,name,mobile,state,city,sPin,address,details,module.getBuildingType(flagType),sScId);
            }
        }
    }

    private void addAddress(String user_id, String name, final String mobile, String state, String city, String pincode, String address, String details, String buildingType, String socity_id) {
        loadingBar.show();
        String json_tag="json_add_address";
        HashMap<String,String> parmas=new HashMap<>();
        parmas.put("user_id",user_id);
        parmas.put("name",name);
        parmas.put("mobile",mobile);
        parmas.put("state",state);
        parmas.put("city",city);
        parmas.put("pincode",pincode);
        parmas.put("address",address);
        parmas.put("details",details);
        parmas.put("building",buildingType);
        parmas.put("socity_id",socity_id);
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
                        module.showToast(""+response.getString("error"));
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
    private void editAddress(String elocation_id, String name, final String mobile, String state, String city, String pincode, String address, String details, String buildingType, String socity_id) {
        loadingBar.show();
        String json_tag="json_add_address";
        HashMap<String,String> parmas=new HashMap<>();
        parmas.put("location_id",elocation_id);
        parmas.put("name",name);
        parmas.put("mobile",mobile);
        parmas.put("state",state);
        parmas.put("city",city);
        parmas.put("pincode",pincode);
        parmas.put("address",address);
        parmas.put("description",details);
        parmas.put("address_type",buildingType);
        parmas.put("socity_id",socity_id);
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
                        module.showToast(""+response.getString("error"));
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
        loadingBar.show();
        Map<String, String> params = new HashMap<String, String>();
        final CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.GET_STATES, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    boolean status = response.getBoolean("responce");
                    if (status)
                    {
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0 ; i <data.length();i++)
                        {
                            JSONObject object = data.getJSONObject(i);
                            state_list.add(object.get("city_state").toString());

                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getActivity(), android.R.layout.simple_list_item_1,state_list);
                        et_state.setAdapter(arrayAdapter);
                        et_state.setThreshold(1);

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
    private void getcities(String state, final boolean flag,final String city_name) {
        loadingBar.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("state",state);
        city_list.clear();
        final CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.GET_CITY, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    boolean status = response.getBoolean("responce");
                    if (status)
                    {
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0 ; i <data.length();i++)
                        {
                            JSONObject object = data.getJSONObject(i);
                            city_list.add(object.get("city_name").toString());

                        }
                        temp_list.clear();
                        temp_list.addAll(city_list);
                            module.setSpinAdapter(city_list,spin_city,getActivity());

                            if(!flag)
                            {
                                if(!city_name.isEmpty())
                                {
                                    int idx=-1;
                                    for(int i=0; i<city_list.size();i++)
                                    {
                                        if(city_list.get(i).toString().equalsIgnoreCase(city_name))
                                        {
                                            idx=i;
                                            break;
                                        }
                                    }
                                    spin_city.setSelection(idx);
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
                    module.showToast(""+msg);
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

    private void makeGetSocityRequest(final String city, final String SocId) {
        loadingBar.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_socity_req";
        HashMap<String,String> params=new HashMap<>();
        params.put("city",city);

        Log.e("get_socitiesss",""+SocId+" - "+city);
        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.GET_SOCITY_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        JSONArray object=response.getJSONArray("data");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Socity_model>>() {
                        }.getType();

                        socity_modelList = gson.fromJson(object.toString(), listType);
                       if(socity_modelList.size()<=0)
                        {
                            toastMsg.toastIconError("No Records Found");
                        }
                       else
                       {
                           int indx=-1;
                           for(int i=0; i<socity_modelList.size();i++)
                           {
                               if(socity_modelList.get(i).getSociety_id().toString().equalsIgnoreCase(SocId))
                               {

                                   indx=i;
                                   break;
                               }
                           }
                           tv_pincode.setText(socity_modelList.get(indx).getSociety().toString() + " (" + socity_modelList.get(indx).getPincode().toString() + ")");
                       }
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest, tag_json_obj);
    }
}
