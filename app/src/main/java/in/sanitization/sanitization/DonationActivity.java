package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.json.JSONObject;

import java.util.HashMap;

import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.payment.ServiceWrapper;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;
import retrofit2.Call;
import retrofit2.Callback;

import static in.sanitization.sanitization.Config.BaseUrl.ADD_DONATION_URL;
import static in.sanitization.sanitization.Config.Constants.KEY_EMAIL;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;
import static in.sanitization.sanitization.Config.Constants.KEY_MOBILE;
import static in.sanitization.sanitization.Config.Constants.KEY_NAME;
import static in.sanitization.sanitization.Fragments.HomeFragment.gst;
import static in.sanitization.sanitization.Fragments.HomeFragment.min_donation_amount;

public class DonationActivity extends AppCompatActivity implements View.OnClickListener {
    Activity ctx=DonationActivity.this;
    Module module;
    LoadingBar loadingBar;
    ImageView iv_back;
    TextView tv_title;
    ToastMsg toastMsg;
    String user_id="";
    Session_management session_management;
    EditText et_name,et_phone,et_amount;
    Button btn_pay;

    //payments
    PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
    //declare paymentParam object
    PayUmoneySdkInitializer.PaymentParam paymentParam = null;

    String TAG ="PaymentActivity", txnid ="", amount ="", phone ="",
            prodname ="", firstname ="", email ="",
            merchantId ="7123249", merchantkey="FEM9jhFe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        initViews();
    }

    private void initViews() {
        module=new Module(ctx);
        loadingBar=new LoadingBar(ctx);
        iv_back=findViewById(R.id.iv_back);
        tv_title=findViewById(R.id.tv_title);
        toastMsg=new ToastMsg(ctx);
        session_management=new Session_management(ctx);

        tv_title.setText("Donate Now");
        et_amount=findViewById(R.id.et_amount);
        et_phone=findViewById(R.id.et_phone);
        et_name=findViewById(R.id.et_name);
        btn_pay=findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        user_id=session_management.getUserDetails().get(KEY_ID);
       et_name.setText(session_management.getUserDetails().get(KEY_NAME));
        et_phone.setText(session_management.getUserDetails().get(KEY_MOBILE));

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_back)
        {
            finish();
        }
        else if(v.getId() == R.id.btn_pay)
        {
            boolean valid=true;
            String name=et_name.getText().toString().trim();
            String mobile=et_phone.getText().toString().trim();
            String amt=et_amount.getText().toString().trim();
            if(name == null || name.isEmpty())
            {
                valid=false;
                et_name.setError("Enter Name");
                et_name.requestFocus();
            }
            if(mobile == null || mobile.isEmpty())
            {
                valid=false;
                et_phone.setError("Enter Mobile Number");
                et_phone.requestFocus();
            }
            if(mobile.length()<10)
            {
                valid=false;
                et_phone.setError("Invalid Mobile Number");
                et_phone.requestFocus();
            }
            if(amt == null || amt.isEmpty()||Integer.parseInt(amt)==0)
            {
                valid=false;
                et_amount.setError("Enter Some amount");
                et_amount.requestFocus();
            }

            if(valid)
            {
                if(ConnectivityReceiver.isConnected())
                {
                    if(min_donation_amount>Integer.parseInt(amt))
                    {
                        toastMsg.toastIconError("Minimum Amount is "+amt);
                    }
                    else
                    {
                        float tot=Float.parseFloat(et_amount.getText().toString().trim());
                        module.showToast("Please wait..");
                        user_id=session_management.getUserDetails().get(KEY_ID);
                        txnid=module.getUniqueId("a2z");
                        amount=String.valueOf(tot);
//            amount="12";
                        phone=mobile;
                        prodname="A2z INDIA COVID-19 FUND";
                        firstname=name;
                        email=session_management.getUserDetails().get(KEY_EMAIL);

//            attemptOrder(user_id,"paid",loc_id,txnid,plan_id,plan_name,mrp,price,gst, String.valueOf(tot),plan_expiry,module.getCurrentDate(),working_days);
                        startpay();
//                        addDonationRequest(user_id,name,mobile,amt,txnid);
                    }

                }
                else{
                    Intent intent=new Intent(ctx, NoInternetConnection.class);
                    startActivity(intent);
                }

            }

        }
    }

    private void addDonationRequest(String user_id, String name, final String mobile, String amt,String txnid) {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("name",name);
        params.put("mobile",mobile);
        params.put("amount",amt);
        params.put("txn_id",txnid);
        Log.e(TAG,""+params.toString());

        CustomVolleyJsonRequest jsonRequest=new CustomVolleyJsonRequest(Request.Method.POST,  ADD_DONATION_URL, params,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loadingBar.dismiss();
              try {
                  boolean resp=response.getBoolean("responce");
                  if(resp)
                  {
                      toastMsg.toastIconSuccess(response.getString("message"));
                      String url=response.getString("url");
                      Intent intent=new Intent(ctx,ThanksActivity.class);
                      intent.putExtra("type","donate");
                      intent.putExtra("msg","Thank You for your contribution");
                      intent.putExtra("url",url);
                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(intent);
                  }
                  else
                  {
                      toastMsg.toastIconSuccess(response.getString("error"));

                  }

              }catch (Exception ex)
              {
                  ex.printStackTrace();
              }
            }
        }     , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.errMessage(error);
            }
        });

        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    private void startpay() {
        builder.setAmount(amount)                          // Payment amount
                .setTxnId(txnid)                     // Transaction ID
                .setPhone(phone)                   // User Phone number
                .setProductName(prodname)                   // Product Name or description
                .setFirstName(firstname)                              // User First name
                .setEmail(email)              // User Email ID
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")     // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")     //Failure URL (furl)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(false)                              // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantkey)                        // Merchant key
                .setMerchantId(merchantId);
        try {
            paymentParam = builder.build();
            // generateHashFromServer(paymentParam );
            getHashkey();

        } catch (Exception e) {
            Log.e(TAG, " error s "+e.toString());
        }

    }

    public void getHashkey(){
        ServiceWrapper service = new ServiceWrapper(null);
        Call<String> call = service.newHashCall(merchantkey, txnid, amount, prodname,
                firstname, email);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e(TAG, "hash res "+response.body());
                String merchantHash= response.body();
                if (merchantHash.isEmpty() || merchantHash.equals("")) {
                    Toast.makeText(ctx, "Could not generate hash", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "hash empty");
                } else {
                    // mPaymentParams.setMerchantHash(merchantHash);
                    paymentParam.setMerchantHash(merchantHash);
                    // Invoke the following function to open the checkout page.
                    // PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, StartPaymentActivity.this,-1, true);
                    PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, ctx, R.style.AppTheme_Green, false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "hash error "+ t.toString());
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
// PayUMoneySdk: Success -- payuResponse{"id":225642,"mode":"CC","status":"success","unmappedstatus":"captured","key":"9yrcMzso","txnid":"223013","transaction_fee":"20.00","amount":"20.00","cardCategory":"domestic","discount":"0.00","addedon":"2018-12-31 09:09:43","productinfo":"a2z shop","firstname":"kamal","email":"kamal.bunkar07@gmail.com","phone":"9144040888","hash":"b22172fcc0ab6dbc0a52925ebbd0297cca6793328a8dd1e61ef510b9545d9c851600fdbdc985960f803412c49e4faa56968b3e70c67fe62eaed7cecacdfdb5b3","field1":"562178","field2":"823386","field3":"2061","field4":"MC","field5":"167227964249","field6":"00","field7":"0","field8":"3DS","field9":" Verification of Secure Hash Failed: E700 -- Approved -- Transaction Successful -- Unable to be determined--E000","payment_source":"payu","PG_TYPE":"AXISPG","bank_ref_no":"562178","ibibo_code":"VISA","error_code":"E000","Error_Message":"No Error","name_on_card":"payu","card_no":"401200XXXXXX1112","is_seamless":1,"surl":"https://www.payumoney.com/sandbox/payment/postBackParam.do","furl":"https://www.payumoney.com/sandbox/payment/postBackParam.do"}
//PayUMoneySdk: Success -- merchantResponse438104
// on successfull txn
        //  request code 10000 resultcode -1
        //tran {"status":0,"message":"payment status for :438104","result":{"postBackParamId":292490,"mihpayid":"225642","paymentId":438104,"mode":"CC","status":"success","unmappedstatus":"captured","key":"9yrcMzso","txnid":"txt12345","amount":"20.00","additionalCharges":"","addedon":"2018-12-31 09:09:43","createdOn":1546227592000,"productinfo":"a2z shop","firstname":"kamal","lastname":"","address1":"","address2":"","city":"","state":"","country":"","zipcode":"","email":"kamal.bunkar07@gmail.com","phone":"9144040888","udf1":"","udf2":"","udf3":"","udf4":"","udf5":"","udf6":"","udf7":"","udf8":"","udf9":"","udf10":"","hash":"0e285d3a1166a1c51b72670ecfc8569645b133611988ad0b9c03df4bf73e6adcca799a3844cd279e934fed7325abc6c7b45b9c57bb15047eb9607fff41b5960e","field1":"562178","field2":"823386","field3":"2061","field4":"MC","field5":"167227964249","field6":"00","field7":"0","field8":"3DS","field9":" Verification of Secure Hash Failed: E700 -- Approved -- Transaction Successful -- Unable to be determined--E000","bank_ref_num":"562178","bankcode":"VISA","error":"E000","error_Message":"No Error","cardToken":"","offer_key":"","offer_type":"","offer_availed":"","pg_ref_no":"","offer_failure_reason":"","name_on_card":"payu","cardnum":"401200XXXXXX1112","cardhash":"This field is no longer supported in postback params.","card_type":"","card_merchant_param":null,"version":"","postUrl":"https:\/\/www.payumoney.com\/mobileapp\/payumoney\/success.php","calledStatus":false,"additional_param":"","amount_split":"{\"PAYU\":\"20.0\"}","discount":"0.00","net_amount_debit":"20","fetchAPI":null,"paisa_mecode":"","meCode":"{\"vpc_Merchant\":\"TESTIBIBOWEB\"}","payuMoneyId":"438104","encryptedPaymentId":null,"id":null,"surl":null,"furl":null,"baseUrl":null,"retryCount":0,"merchantid":null,"payment_source":null,"pg_TYPE":"AXISPG"},"errorCode":null,"responseCode":null}---438104

        // Result Code is -1 send from Payumoney activity
        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra( PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE );

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if(transactionResponse.getTransactionStatus().equals( TransactionResponse.TransactionStatus.SUCCESSFUL )){

                    Log.e("taransactionsdsadasd",""+transactionResponse.getTransactionDetails().toString());
                    addDonationRequest(user_id,et_name.getText().toString().trim(),et_phone.getText().toString().trim(),amount,txnid);
//                    addTranscation(tv_points.getText().toString(),tv_amt.getText().toString(),sessionManagment.getUserDetails().get(KEY_ID),txnid,"success");
                    //Success Transaction
                } else{
                    //Failure Transaction
                    toastMsg.toastIconError("Transaction Failed. Try again later");
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                Log.e(TAG, "tran "+payuResponse+"---"+ merchantResponse);
            } /* else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }*/
        }
    }


}

