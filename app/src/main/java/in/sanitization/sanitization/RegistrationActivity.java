package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.Model.BlockModel;
import in.sanitization.sanitization.Model.DistrictModel;
import in.sanitization.sanitization.Model.StateModel;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.Config.BaseUrl.SIGN_UP;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    EditText et_name,et_number,et_email,et_address,et_pass,et_con_pass,et_pin,et_ref ;
    AutoCompleteTextView et_district ,et_state,et_block;
    TextView tv_back;
    Spinner spin_block;
    Button btn_reg;
    Module module;
    LoadingBar loadingBar ;
    ArrayList<String> state_list;
    ArrayList<String> district_list,block_list ;
    ArrayList<StateModel> stateModelList;
    ArrayList<DistrictModel> districtModelList;
    ArrayList<BlockModel> blockModelList;
    Activity ctx=RegistrationActivity.this;
    ToastMsg toastMsg;
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
        et_district=findViewById(R.id.et_district);
        spin_block=findViewById(R.id.spin_block);
        et_pin=findViewById(R.id.et_pincode);
        et_pass=findViewById(R.id.et_pass);
        et_ref=findViewById(R.id.et_ref);
        et_con_pass=findViewById(R.id.et_con_pass);
        btn_reg=findViewById(R.id.btn_reg);
       tv_back=findViewById(R.id.txt_back);
        module=new Module(ctx);
        toastMsg=new ToastMsg(ctx);
        loadingBar=new LoadingBar(ctx);
        district_list = new ArrayList<>();
        state_list = new ArrayList<>();
        block_list = new ArrayList<>();
        stateModelList = new ArrayList<>();
        districtModelList = new ArrayList<>();
        blockModelList = new ArrayList<>();

        btn_reg.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        et_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String state=et_state.getText().toString().trim();
                et_district.setText("");
                if(!state.isEmpty())
                {
                    getDistrict(module.getStateId(stateModelList,state));
                }
            }
        });
        et_district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dis=et_district.getText().toString().trim();
                if(!dis.isEmpty())
                {
                    getBlock(module.getDistrictId(districtModelList,dis));
                }
            }
        });

        if (ConnectivityReceiver.isConnected()) {
            getstates();
        }
        else
        {
            Intent intent = new Intent(RegistrationActivity.this, NoInternetConnection.class);
            startActivity(intent);
        }
        et_number.setText(getIntent().getStringExtra("number"));
        et_number.setEnabled(false);
    }

    private void getBlock(String districtId) {
        loadingBar.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("dis_id",districtId);
        blockModelList.clear();
        block_list.clear();
        final CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.GET_BLOCK, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
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
                        module.setSpinAdapter(block_list,spin_block,ctx,"Select Block");


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
                    Toast.makeText(ctx,""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");

    }

    private void getDistrict(String stateId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("state_id",stateId);
        Log.e("asdsdasd",""+params.toString());
     districtModelList.clear();
     district_list.clear();
        final CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.GET_DISTRICT, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("states", response.toString());
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
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                ctx, android.R.layout.simple_list_item_1,district_list);
                        et_district.setAdapter(arrayAdapter);
                        et_district.setThreshold(1);

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
            String district=et_district.getText().toString();
            String state=et_state.getText().toString();
            String pin=et_pin.getText().toString();
            String ref=et_ref.getText().toString();

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
            }
            else if(email.isEmpty())
            {
                et_email.setError("Enter Email Address");
                et_email.requestFocus();
            }
            else if(!email.contains("@"))
            {
                et_email.setError("Invalid Email Address");
                et_email.requestFocus();
            }
            else if(state.isEmpty())
            {
                et_state.setError("Select State");
                et_state.requestFocus();
            }
            else if(district.isEmpty())
            {
                et_district.setError("Select District");
                et_district.requestFocus();
            }
            else if(pin.isEmpty())
            {
                et_pin.setError("Enter Pincode");
                et_pin.requestFocus();
            }
            else if(pin.length()!=6)
            {
                et_pin.setError("Enter Valid Pincode");
                et_pin.requestFocus();
            }
            else if(address.isEmpty())
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

            else
            {
               if(pass.equals(cpass))
               {
                   String block=spin_block.getSelectedItem().toString();
                   if(block.equalsIgnoreCase("Select Block"))
                   {
                       module.showToast("Select Block");
                   }
                   else
                   {

                       registerUser(name,number,email,state,district,block,pin,address,pass,ref);
                   }

               }
               else {
                   module.showToast("Password must be same");
               }
            }
            
        }
        else if (v.getId() == R.id.txt_back)
        {
          Intent intent=new Intent(ctx,LoginActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
          finish();
        }
    }

    private void registerUser(String name, String number, String email,String state ,String district,String block ,String pincode, String address, String pass,String ref )
    {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_name",name);
        params.put("user_mobile",number);
        params.put("user_email",email);
        params.put("state",state);
        params.put("district",module.getDistrictId(districtModelList,district));
        params.put("block",module.getBlockId(blockModelList,block));
        params.put("pincode",pincode);
        params.put("address",address);
        params.put("password",pass);
        params.put("referral_by",ref);

        Log.e("adsdasd",""+params.toString());
        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, SIGN_UP, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    loadingBar.dismiss();
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        toastMsg.toastIconSuccess(response.getString("message"));

                        Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        toastMsg.toastIconError(response.getString("error"));
                    }
                }
                catch (Exception ex)
                {
                    loadingBar.dismiss();
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
                        Log.e("asdasdasd",""+stateModelList.size()+" - "+state_list.size());
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                ctx, android.R.layout.simple_list_item_1,state_list);
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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ctx,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
