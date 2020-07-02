package in.sanitization.sanitization.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.ForgotActivity;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.RegistrationActivity;
import in.sanitization.sanitization.SmsReceiver;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.SmsListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyOtpFragment extends Fragment implements OnOtpCompletionListener, View.OnClickListener {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private Button validateButton;
    private OtpView otpView;
    String otp_string ="" ,gen_otp ="";
    String type="";
    String number="";
    LoadingBar loadingBar ;
    Module module ;
    public static final String OTP_REGEX = "[0-9]{3,6}";
    public VerifyOtpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view= inflater.inflate(R.layout.fragment_verify_otp, container, false);
     initializeUi(view);
     return view ;
    }
    private void initializeUi(View view)
    {
    otpView =view.findViewById(R.id.otp_view);
        validateButton = view.findViewById(R.id.btn_otp_verify);
        otpView.setOtpCompletionListener(this);
        validateButton.setOnClickListener(this);
        loadingBar = new LoadingBar(getActivity());
        module = new Module(getActivity());
        checkAndRequestPermissions();
//        getSmsOtp();
        if (ConnectivityReceiver.isConnected()) {
            getMessageStatus();
        } else
        {
            Intent intent = new Intent(getActivity(), NoInternetConnection.class);
            startActivity(intent);
        }
       type=getArguments().getString("type");
       gen_otp=getArguments().getString("otp");
     number= getArguments().getString("number");
//     Toast.makeText(getActivity(),"num"+number,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onOtpCompleted(String otp) {
        otp_string = otp;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_otp_verify) {
            if(ConnectivityReceiver.isConnected())
            {
                verification();

            }

        }
    }
    private void verification() {
        if (otp_string.isEmpty())

            {
                otpView.setError("Enter Otp");
                otpView.requestFocus();
            }
            else if (otp_string.length()!= 6)
            {
                Toast.makeText(getActivity(), "Enter Valid Otp", Toast.LENGTH_LONG).show();
            }
            else
            {
                if(type.equals("f"))
                {
                    verifyMobileWithOtp(number,otp_string);
                }
                else if(type.equals("r"))
                {
                    verifyRegisterMobileWithOtp(number,otp_string);

                }

            }
        }



    private void verifyRegisterMobileWithOtp(final String number, String otp) {
        loadingBar.show();
        String json_tag="json_verification";
        HashMap<String,String> map=new HashMap<>();
        map.put("mobile",number);
        map.put("otp",otp);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.URL_VERIFY_REGISTER_OTP, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    Log.d("verify_register",response.toString());
                    boolean status=response.getBoolean("responce");
                    if(status)
                    {
                        loadingBar.dismiss();
                        Intent intent = new Intent( getActivity(), RegistrationActivity.class);
                        intent.putExtra( "number", number );
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity( intent );
                        getActivity().finish();

                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(getActivity(),""+response.getString("error").toString(),Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(getActivity(),""+ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);
    }


    private void verifyMobileWithOtp(final String number, String otp) {
        loadingBar.show();
        String json_tag="json_verification";
        HashMap<String,String> map=new HashMap<>();
        map.put("mobile",number);
        map.put("otp",otp);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.URL_VERIFY_OTP, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    Log.d("verify_forgot",response.toString());
                    boolean status=response.getBoolean("responce");
                    if(status)
                    {
                        String data=response.getString("data");

                        Intent intent = new Intent( getActivity(), ForgotActivity.class );
                        intent.putExtra( "mobile", number );
                        startActivity( intent );
                    

                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(getActivity(),""+response.getString("error").toString(),Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(getActivity(),""+ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);
    }

    private boolean checkAndRequestPermissions()
    {
        int sms = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);

        if (sms != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    public void getSmsOtp()
    {
        try
        {


            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {

                    //From the received text string you may do string operations to get the required OTP
                    //It depends on your SMS format
                    Log.e("Message",messageText);
                    // Toast.makeText(SmsVerificationActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();

                    // If your OTP is six digits number, you may use the below code

                    Pattern pattern = Pattern.compile(OTP_REGEX);
                    Matcher matcher = pattern.matcher(messageText);
                    String otp="";
                    while (matcher.find())
                    {
                        otp = matcher.group();
                    }

                    if(!(otp.isEmpty() || otp.equals("")))
                    {
                       otpView.setText(otp);

                        if(ConnectivityReceiver.isConnected())
                        {
                            verification();
                        }
                    }

                    //           Toast.makeText(SmsVerificationActivity.this,"OTP: "+ otp ,Toast.LENGTH_LONG).show();

                }
            });
        }
        catch (Exception ex)
        {
            // Toast.makeText(SmsVerificationActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

   public void getMessageStatus()
   {
       HashMap<String,String> map=new HashMap<>();
       CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.URL_UPDATER, map, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               try
               {
                   Log.d("updater_msg_status",response.toString());
                   boolean status=response.getBoolean("responce");
                   if(status)
                   {
                      JSONArray data=response.getJSONArray("data");
                      JSONObject obj = data.getJSONObject(0);
                      if (obj.getString("msg_status").equals("0"))
                      {
                         startcountdown();
                      }
                      else
                      {
                          getSmsOtp();
                      }

//                       Intent intent = new Intent( getActivity(), ForgotActivity.class );
//                       intent.putExtra( "mobile", number );
//                       startActivity( intent );


                   }
                   else
                   {
                       loadingBar.dismiss();
                       Toast.makeText(getActivity(),""+response.getString("error").toString(),Toast.LENGTH_LONG).show();
                   }

               }
               catch (Exception ex)
               {
                   ex.printStackTrace();
                   Toast.makeText(getActivity(),""+ex.getMessage(),Toast.LENGTH_LONG).show();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });
       AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,"app_updater");
   }


   public void startcountdown()
   {
       new CountDownTimer(5000, 1000) {

           public void onTick(long millisUntilFinished) {

           }

           public void onFinish() {
              otpView.setText(gen_otp);
           }

       }.start();
   }
}
