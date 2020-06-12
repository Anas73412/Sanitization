package in.sanitization.sanitization.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.MainActivity;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.RegistrationActivity;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;

import static in.sanitization.sanitization.Config.BaseUrl.SIGN_UP;
import static in.sanitization.sanitization.Config.BaseUrl.UPDATE_PROFILE;
import static in.sanitization.sanitization.Config.Constants.KEY_ADDRESS;
import static in.sanitization.sanitization.Config.Constants.KEY_CITY;
import static in.sanitization.sanitization.Config.Constants.KEY_EMAIL;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;
import static in.sanitization.sanitization.Config.Constants.KEY_MOBILE;
import static in.sanitization.sanitization.Config.Constants.KEY_NAME;
import static in.sanitization.sanitization.Config.Constants.KEY_PINCODE;
import static in.sanitization.sanitization.Config.Constants.KEY_STATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {
    EditText et_name ,et_mobile ,et_email ,et_address ,et_pin ;
    AutoCompleteTextView et_city ,et_state ;
    Button btn_update ;
    Module module;
    LoadingBar loadingBar ;
    HashMap<String,Object> hashMap = new HashMap<>();
    ArrayList<String> state_list;
    ArrayList<String> city_list ;
    Session_management session_management;
    Activity ctx= getActivity();
    String id , name , add , state , city ,pin ,email;
    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ((HomeActivity) getActivity()).setTitle("Edit Profile");

       initViews(view);
       return  view ;
    }

    private void initViews(View v) {
        et_name=v.findViewById(R.id.et_name);
        et_mobile=v.findViewById(R.id.et_phone);
        et_email=v.findViewById(R.id.et_email);
        et_address=v.findViewById(R.id.et_address);
        et_state=v.findViewById(R.id.et_state);
        et_city=v.findViewById(R.id.et_city);
        et_pin=v.findViewById(R.id.et_pincode);

        btn_update=v.findViewById(R.id.btn_updt);
        loadingBar = new LoadingBar(getActivity());
        module=new Module(getActivity());
        session_management = new Session_management(getActivity());
        city_list = new ArrayList<>();
        state_list = new ArrayList<>();
        btn_update.setOnClickListener(this);

        et_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String state=et_state.getText().toString().trim();
                et_city.setText("");
                if(!state.isEmpty())
                {
                    getcities(state);
                }
            }
        });
        getstates();
        et_mobile.setText(session_management.getUserDetails().get(KEY_MOBILE));
        et_name.setText(session_management.getUserDetails().get(KEY_NAME));
        et_email.setText(session_management.getUserDetails().get(KEY_EMAIL));
        et_state.setText(session_management.getUserDetails().get(KEY_STATE));
        et_city.setText(session_management.getUserDetails().get(KEY_CITY));
        et_pin.setText(session_management.getUserDetails().get(KEY_PINCODE));
        et_address.setText(session_management.getUserDetails().get(KEY_ADDRESS));
        id =  session_management.getUserDetails().get(KEY_ID);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_updt) {
            String name = et_name.getText().toString();
            String number = et_mobile.getText().toString();
            String email = et_email.getText().toString();
            String address = et_address.getText().toString();
            String city = et_city.getText().toString();
            String state = et_state.getText().toString();
            String pin = et_pin.getText().toString();

            if (name.isEmpty()) {
                et_name.setError("Enter Name");
                et_name.requestFocus();
            } else if (number.isEmpty()) {
                et_mobile.setError("Enter Mobile Number");
                et_mobile.requestFocus();
            } else if (number.length() != 10) {
                et_mobile.setError("Invalid Mobile Number");
                et_mobile.requestFocus();
            } else if (email.isEmpty()) {
                et_email.setError("Enter Email Address");
                et_email.requestFocus();
            } else if (!email.contains("@")) {
                et_email.setError("Invalid Email Address");
                et_email.requestFocus();
            } else if (state.isEmpty()) {
                et_state.setError("Select State");
                et_state.requestFocus();
            } else if (city.isEmpty()) {
                et_city.setError("Select City");
                et_city.requestFocus();
            } else if (pin.isEmpty()) {
                et_pin.setError("Enter Pincode");
                et_pin.requestFocus();
            } else if (pin.length() != 6) {
                et_pin.setError("Enter Valid Pincode");
                et_pin.requestFocus();
            } else if (address.isEmpty()) {
                et_address.setError("Enter Address");
                et_address.requestFocus();
            }
            else {

                   updateProfile(id,name, email, state, city, pin, address);

            }
        }
    }

    private void getstates() {

        Map<String, String> params = new HashMap<String, String>();

        final CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.GET_STATES, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("states", response.toString());
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

                String msg=module.VolleyErrorMessage(error);

                if(!msg.equals(""))
                {
                    Toast.makeText(ctx,""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");

    }
    private void getcities(String state) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("state",state);
        city_list.clear();
        final CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.GET_CITY, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("city", response.toString());
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
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getActivity(), android.R.layout.simple_list_item_1,city_list);
                        et_city.setAdapter(arrayAdapter);
                        et_city.setThreshold(1);

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
                    Toast.makeText(ctx,""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");

    }

    private void updateProfile(String id , final String name, final String email, final String state , final String city , final String pincode, final String address )
    {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",id);
        params.put("name",name);
        params.put("email",email);
        params.put("state",state);
        params.put("city",city);
        params.put("pincode",pincode);
        params.put("address",address);

        Log.e("update",params.toString());


        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, UPDATE_PROFILE, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    loadingBar.dismiss();
                    boolean resp=response.getBoolean("responce");
                    Log.e("update_response",response.toString());
                    if(resp)
                    {
                      module.showToast("Profile Details Updated Successfully");
                       Fragment fm = new HomeFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frame, fm)
                                .addToBackStack(null).commit();
                        session_management.updateProfile(name,email,state,city,pincode,address);

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
}
