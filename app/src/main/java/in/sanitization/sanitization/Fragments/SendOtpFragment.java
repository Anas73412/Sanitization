package in.sanitization.sanitization.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.MainActivity;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendOtpFragment extends Fragment {

    EditText et_gen_otp;
    Button btn_otp_verify;
    public String otp="";
    LoadingBar loadingBar ;
    Module module ;
    String type = "";
    public SendOtpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_send_otp, container, false);
        btn_otp_verify=view.findViewById(R.id.btn_otp_verify);
        et_gen_otp=view.findViewById(R.id.et_gen_otp);
        loadingBar=new LoadingBar(getActivity());
        type = getActivity().getIntent().getStringExtra("type");


     btn_otp_verify.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String number=et_gen_otp.getText().toString().trim();
            if (number.isEmpty())
            {
                et_gen_otp.setError("Enter Mobile Number");
                et_gen_otp.requestFocus();
            }
            else if (et_gen_otp.length()!=10)
            {
                et_gen_otp.setError("Enter Valid Mobile Number");
                et_gen_otp.requestFocus();
            }

            else {
                otp = getRandomKey(6);
                Log.e("otp", otp);

                if (type.equals("r"))
                {
                    sendCodeR(number,otp);
                }
                else if (type.equals("f"))
                {
                    sendCodeF(number,otp);
                }
            }

        }
    });

        return  view ;
    }

    private void sendCodeR(final String number, final String otp) {
        loadingBar.show();
        String json_tag="json_otp";
        HashMap<String,String> map=new HashMap<>();
        map.put("mobile",number);
        map.put("otp",otp);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.URL_SEND_OTP_R, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    boolean responce=response.getBoolean("responce");
                    Log.d("send_otp",response.toString());
                    if(responce)
                    {
                        loadingBar.dismiss();
                        Fragment fm=new VerifyOtpFragment();
                        Bundle bundle=new Bundle();
                        bundle.putString("type",getActivity().getIntent().getStringExtra("type"));
                        bundle.putString("number",number);
                        bundle.putString("otp",otp);
                        fm.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().add(R.id.varify_container, fm)
                                .commit();

                    }
                    else
                    {
                        loadingBar.dismiss();
                      Toast.makeText(getActivity(),response.getString("error"),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ex)
                {        loadingBar.dismiss();
                    ex.printStackTrace();
                    Toast.makeText(getActivity(),""+ex.getMessage(),Toast.LENGTH_LONG).show();
                }

                // Toast.makeText(OtpActivity.this,""+response,Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);


    }

    private void sendCodeF(final String number, final String otp) {
        loadingBar.show();
        String json_tag="json_otp";
        HashMap<String,String> map=new HashMap<>();
        map.put("mobile",number);
        map.put("otp",otp);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.URL_SEND_OTP_F, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    boolean responce=response.getBoolean("responce");
                    Log.d("send_otp",response.toString());
                    if(responce)
                    {
                        loadingBar.dismiss();
                        Fragment fm=new VerifyOtpFragment();
                        Bundle bundle=new Bundle();
                        bundle.putString("type",getActivity().getIntent().getStringExtra("type"));
                        bundle.putString("number",number);
                        bundle.putString("otp",otp);
                        fm.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().add(R.id.varify_container, fm)
                                .commit();

                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(getActivity(),response.getString("error"),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ex)
                {        loadingBar.dismiss();
                    ex.printStackTrace();
                    Toast.makeText(getActivity(),""+ex.getMessage(),Toast.LENGTH_LONG).show();
                }

                // Toast.makeText(OtpActivity.this,""+response,Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);


    }


    public static String getRandomKey(int i)
    {
        final String characters="0123456789";
        StringBuilder stringBuilder=new StringBuilder();
        while (i>0)
        {
            Random ran=new Random();
            stringBuilder.append(characters.charAt(ran.nextInt(characters.length())));
            i--;
        }
        return stringBuilder.toString();
    }
}
