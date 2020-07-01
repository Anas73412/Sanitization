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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.MainActivity;
import in.sanitization.sanitization.Model.BlockModel;
import in.sanitization.sanitization.Model.DistrictModel;
import in.sanitization.sanitization.Model.StateModel;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.RegistrationActivity;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;

import static in.sanitization.sanitization.Config.BaseUrl.SIGN_UP;
import static in.sanitization.sanitization.Config.BaseUrl.UPDATE_PROFILE;
import static in.sanitization.sanitization.Config.Constants.KEY_ADDRESS;
import static in.sanitization.sanitization.Config.Constants.KEY_AREA_MANAGER;
import static in.sanitization.sanitization.Config.Constants.KEY_CITY;
import static in.sanitization.sanitization.Config.Constants.KEY_DISTRICT;
import static in.sanitization.sanitization.Config.Constants.KEY_DISTRICT_MANAGER;
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
    AutoCompleteTextView et_state ,et_district;
    Spinner spin_block ;
    Button btn_update ;
    Module module;
    LoadingBar loadingBar;
    JSONArray disArr,areaArr;
    HashMap<String,Object> hashMap = new HashMap<>();
    ArrayList<String> state_list;
    ArrayList<String> district_list,block_list ;
    ArrayList<StateModel> stateModelList;
    ArrayList<DistrictModel> districtModelList;
    ArrayList<BlockModel> blockModelList;
    Session_management session_management;
    String id , name , add , state ,district,pin ,email;
    CircleImageView img_dis,img_area;
    TextView tv_dis_name,tv_dis_mobile,tv_area_name,tv_area_mobile;
    LinearLayout lin_area,lin_dis;
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
        img_dis=v.findViewById(R.id.img_dis);
        img_area=v.findViewById(R.id.img_area);
        tv_dis_name=v.findViewById(R.id.tv_dis_name);
        tv_dis_mobile=v.findViewById(R.id.tv_dis_mobile);
        tv_area_name=v.findViewById(R.id.tv_area_name);
        tv_area_mobile=v.findViewById(R.id.tv_area_mobile);
        et_name=v.findViewById(R.id.et_name);
        et_mobile=v.findViewById(R.id.et_phone);
        et_email=v.findViewById(R.id.et_email);
        et_address=v.findViewById(R.id.et_address);
        et_state=v.findViewById(R.id.et_state);
        et_district=v.findViewById(R.id.et_district);
        spin_block=v.findViewById(R.id.spin_block);

        et_pin=v.findViewById(R.id.et_pincode);
        lin_dis=v.findViewById(R.id.lin_dis);
        lin_area=v.findViewById(R.id.lin_area);

        btn_update=v.findViewById(R.id.btn_updt);
        loadingBar = new LoadingBar(getActivity());
        module=new Module(getActivity());
        session_management = new Session_management(getActivity());
        district_list = new ArrayList<>();
        state_list = new ArrayList<>();
        block_list = new ArrayList<>();
        stateModelList = new ArrayList<>();
        districtModelList = new ArrayList<>();
        blockModelList = new ArrayList<>();

        btn_update.setOnClickListener(this);
        lin_dis.setOnClickListener(this);
        lin_area.setOnClickListener(this);

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

        getstates();
        getDistrict(module.getStateId(stateModelList,et_state.getText().toString()));
        getBlock(module.getDistrictId(districtModelList,et_district.getText().toString()));
        et_mobile.setText(session_management.getUserDetails().get(KEY_MOBILE));
        et_name.setText(session_management.getUserDetails().get(KEY_NAME));
        et_email.setText(session_management.getUserDetails().get(KEY_EMAIL));
        et_state.setText(session_management.getUserDetails().get(KEY_STATE));
        et_district.setText(session_management.getUserDetails().get(KEY_DISTRICT));
        et_pin.setText(session_management.getUserDetails().get(KEY_PINCODE));
        et_address.setText(session_management.getUserDetails().get(KEY_ADDRESS));
        id =  session_management.getUserDetails().get(KEY_ID);
        getmanagers(session_management.getUserDetails().get(KEY_DISTRICT_MANAGER).toString(),session_management.getUserDetails().get(KEY_AREA_MANAGER).toString());

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_updt) {
            String name = et_name.getText().toString();
            String number = et_mobile.getText().toString();
            String email = et_email.getText().toString();
            String address = et_address.getText().toString();
           String district = et_district.getText().toString();
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
            }
            else if(district.isEmpty())
            {
                et_district.setError("Select District");
                et_district.requestFocus();
            }
            else if (pin.isEmpty()) {
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

                String block=spin_block.getSelectedItem().toString();
                if(block.equalsIgnoreCase("Select Block"))
                {
                    module.showToast("Select Block");
                }
                else
                {
                    updateProfile(name,number,email,state,district,block,pin,address);
                }

            }
        }
        else if(v.getId()==R.id.lin_area)
        {
            Fragment fm=new ManagerDetailsFragment();
            Bundle bundle=new Bundle();
            bundle.putString("arr",areaArr.toString());
            bundle.putString("type","area");
            loadFragment(fm,bundle);
        }else if(v.getId()==R.id.lin_dis)
        {
            Fragment fm=new ManagerDetailsFragment();
            Bundle bundle=new Bundle();
            bundle.putString("arr",disArr.toString());
            bundle.putString("type","dis");
            loadFragment(fm,bundle);
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
                        Log.e("asdasdasd",""+stateModelList.size()+" - "+state_list.size());
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
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
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
                                getActivity(), android.R.layout.simple_list_item_1,district_list);
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
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");


    }
//    private void getcities(String state) {
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("state",state);
//        city_list.clear();
//        final CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
//                BaseUrl.GET_CITY, params, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.e("city", response.toString());
//                try {
//                    boolean status = response.getBoolean("responce");
//                    if (status)
//                    {
//                        JSONArray data = response.getJSONArray("data");
//                        for (int i = 0 ; i <data.length();i++)
//                        {
//                            JSONObject object = data.getJSONObject(i);
//                            city_list.add(object.get("city_name").toString());
//
//                        }
//                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                                getActivity(), android.R.layout.simple_list_item_1,city_list);
//                        et_city.setAdapter(arrayAdapter);
//                        et_city.setThreshold(1);
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                String msg=module.VolleyErrorMessage(error);
//                if(!msg.equals(""))
//                {
//                    Toast.makeText(ctx,""+msg,Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");
//
//    }

    private void updateProfile(String id , final String name, final String email, final String state , final String district, final String block , final String pincode, final String address )
    {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",id);
        params.put("name",name);
        params.put("email",email);
        params.put("state",state);
        params.put("district",module.getDistrictId(districtModelList,district));
        params.put("block",module.getBlockId(blockModelList,block));
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
                        session_management.updateProfile(name,email,state,district,block,pincode,address);

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

    public void loadFragment(Fragment fm,Bundle args)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fm.setArguments(args);
        fragmentManager.beginTransaction()
                .replace( R.id.frame,fm)
                .addToBackStack(null)
                .commit();
    }
    public void getmanagers(String dis_id,String area_id)
    {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("dis_id",dis_id);
        params.put("area_id",area_id);
        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.GET_MANAGERS_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    boolean resp=response.getBoolean("responce");
                    Log.e("managers",response.toString());
                    if(resp)
                    {
                         disArr=response.getJSONArray("district");

                         if (disArr.length()==0)
                         {
                             lin_dis.setVisibility(View.GONE);
                         }
                         else
                        {
                            lin_dis.setVisibility(View.VISIBLE);
                        Glide.with(getActivity())
                                .load( BaseUrl.IMG_DISTRICT_URL + disArr.getJSONObject(0).getString("user_photo").toString())
                                .placeholder( R.drawable.logo)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .dontAnimate()
                                .into(img_dis);
                            tv_dis_name.setText(disArr.getJSONObject(0).getString("user_name").toString());
                            tv_dis_mobile.setText(disArr.getJSONObject(0).getString("user_mobile").toString());
                        }

                        areaArr=response.getJSONArray("area");
                         if (areaArr.length()==0)
                         { lin_area.setVisibility(View.GONE);
                         }
                         else {
                             lin_area.setVisibility(View.VISIBLE);
                             Glide.with(getActivity())
                                     .load(BaseUrl.IMG_AREA_URL + areaArr.getJSONObject(0).getString("user_photo").toString())
                                     .placeholder(R.drawable.logo)
                                     .crossFade()
                                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                                     .dontAnimate()
                                     .into(img_area);
                             tv_area_name.setText(areaArr.getJSONObject(0).getString("user_name").toString());
                             tv_area_mobile.setText(areaArr.getJSONObject(0).getString("user_mobile").toString());
                         }



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
                module.errMessage(error);
            }
        });
        AppController.getInstance().addToRequestQueue(request);
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
                        module.setSpinAdapter(block_list,spin_block,getActivity(),"Select Block");


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
