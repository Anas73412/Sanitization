package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;

import static in.sanitization.sanitization.Config.BaseUrl.SIGN_UP;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    EditText et_name,et_number,et_email,et_address,et_pass,et_con_pass,et_pin ;
    AutoCompleteTextView et_city ,et_state;
    Button btn_reg;
    Module module;
   HashMap<String,Object> hashMap = new HashMap<>();
    ArrayList<String> state_list;
    ArrayList<String> city_list ;
    Activity ctx=RegistrationActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
    }

    private void initViews() {
        et_name=findViewById(R.id.et_name);
        et_number=findViewById(R.id.et_number);
        et_email=findViewById(R.id.et_email);
        et_address=findViewById(R.id.et_address);
        et_state=findViewById(R.id.et_state);
        et_city=findViewById(R.id.et_city);
        et_pin=findViewById(R.id.et_pincode);
        et_pass=findViewById(R.id.et_pass);
        et_con_pass=findViewById(R.id.et_con_pass);
        btn_reg=findViewById(R.id.btn_reg);
        module=new Module(ctx);
        city_list = new ArrayList<>();
        state_list = new ArrayList<>();
        btn_reg.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_reg)
        {
            String name=et_name.getText().toString();
            String number=et_number.getText().toString();
            String email=et_email.getText().toString();
            String address=et_address.getText().toString();
            String pass=et_pass.getText().toString();
            String cpass=et_con_pass.getText().toString();
            String city=et_city.getText().toString();
            String state=et_state.getText().toString();
            String pin=et_pin.getText().toString();

            if(name.isEmpty())
            {
                et_name.setError("Enter Name");
                et_name.requestFocus();
            }
            else if(number.isEmpty())
            {
                et_number.setError("Enter Mobile Number");
                et_number.requestFocus();
            } else if(number.length()!=10)
            {
                et_number.setError("Invalid Mobile Number");
                et_number.requestFocus();
            }else if(email.isEmpty())
            {
                et_email.setError("Enter Email Address");
                et_email.requestFocus();
            }else if(!email.contains("@"))
            {
                et_email.setError("Invalid Email Address");
                et_email.requestFocus();
            }else if(address.isEmpty())
            {
                et_address.setError("Enter Address");
                et_address.requestFocus();
            }else if(pass.isEmpty())
            {
                et_pass.setError("Enter Password");
                et_pass.requestFocus();
            }
            else if(pass.length()<5)
            {
                et_pass.setError("Minimum 6 characters allow");
                et_pass.requestFocus();
            }else if(cpass.isEmpty())
            {
                et_con_pass.setError("Enter Password");
                et_con_pass.requestFocus();
            }
            else if(cpass.length()<5)
            {
                et_con_pass.setError("Minimum 6 characters allow");
                et_con_pass.requestFocus();
            }
            else if(cpass.length()<5)
            {
                et_con_pass.setError("Minimum 6 characters allow");
                et_con_pass.requestFocus();
            }
            else if(cpass.length()<5)
            {
                et_con_pass.setError("Minimum 6 characters allow");
                et_con_pass.requestFocus();
            }
            else
            {
               if(pass.equals(cpass))
               {
                   registerUser(name,number,email,address,pass);
               }
               else {
                   module.showToast("Password must be matched");
               }
            }
            
        }
    }

    private void registerUser(String name, String number, String email, String address, String pass) {

        HashMap<String,String> params=new HashMap<>();
        params.put("user_name",name);
        params.put("user_mobile",number);
        params.put("user_email",email);
        params.put("address",address);
        params.put("password",pass);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, SIGN_UP, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        module.showToast(""+response.getString("message"));
                       finish();
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
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    module.showToast(""+msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
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
                                RegistrationActivity.this, android.R.layout.simple_list_item_1,state_list);
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
                    Toast.makeText(RegistrationActivity.this,""+msg,Toast.LENGTH_LONG).show();
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
                                RegistrationActivity.this, android.R.layout.simple_list_item_1,city_list);
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
                    Toast.makeText(RegistrationActivity.this,""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");

    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if(view.getId()==R.id.et_state )
//        {
//            Toast.makeText(RegistrationActivity.this,""+et_state.getText(),Toast.LENGTH_LONG).show();
////            getcities(et_state.getText().toString());
//        }
////       getcities(parent.getSelectedItem().toString());
//    }


}
