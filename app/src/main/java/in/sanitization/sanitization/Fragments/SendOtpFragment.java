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
                Fragment fm = new VerifyOtpFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.varify_container, fm)
                        .commit();
            sendCode(number,otp);
            }

        }
    });

        return  view ;
    }

    private void sendCode(final String number, final String otp) {
        loadingBar.show();
        String json_tag="json_otp";
        HashMap<String,String> map=new HashMap<>();
        map.put("mobile",number);
        map.put("otp",otp);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.URL_SEND_OTP, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    boolean responce=response.getBoolean("responce");
                    if(responce==true)
                    {
                        loadingBar.dismiss();
                        Fragment fm=new VerifyOtpFragment();
                        Bundle bundle=new Bundle();
                        bundle.putString("type",getActivity().getIntent().getStringExtra("type"));
                        bundle.putString("number",number);
                        fm.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().add(R.id.varify_container, fm)
                                .commit();

                        //   Toast.makeText(OtpGenerateActivity.this,"true",Toast.LENGTH_LONG).show();


                    }
                    else
                    {
                        loadingBar.dismiss();
//                        Toast.makeText(getActivity(),"false",Toast.LENGTH_LONG).show();
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
