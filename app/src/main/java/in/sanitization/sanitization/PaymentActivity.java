package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.payment.ServiceWrapper;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;
import retrofit2.Call;
import retrofit2.Callback;

import static in.sanitization.sanitization.Config.Constants.KEY_EMAIL;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;
import static in.sanitization.sanitization.Config.Constants.KEY_MOBILE;
import static in.sanitization.sanitization.Config.Constants.KEY_NAME;
import static in.sanitization.sanitization.Fragments.HomeFragment.gst;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener{
    Activity ctx=PaymentActivity.this;
    Module module;
    LoadingBar loadingBar;
    ImageView iv_back;
    TextView tv_title;
    ToastMsg toastMsg;
    String user_id="";
    TextView tv_rev_name,tv_rev_mobile,tv_rev_pincode,tv_rev_address,tvItems,tvprice,tvMrp,tvDiscount,tvSubTotal,tvGst;
    RelativeLayout rel_order;
    float tot ;
    String loc_id="",rev_name,rev_mobile,rev_address,rev_state,rev_city,rev_pincode,rev_soc_id,plan_id,plan_name,mrp,price,working_days,plan_expiry;
    Session_management session_management;
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
        setContentView(R.layout.activity_payment);
        initViews();
    }

    private void initViews() {
        module=new Module(ctx);
        toastMsg=new ToastMsg(ctx);

        loadingBar=new LoadingBar(ctx);
        iv_back=findViewById(R.id.iv_back);
        tv_title=findViewById(R.id.tv_title);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_title.setText("Order Details");
        tv_rev_name=findViewById(R.id.tv_rev_name);
        tv_rev_mobile=findViewById(R.id.tv_rev_mobile);
        tv_rev_pincode=findViewById(R.id.tv_rev_pincode);
        tv_rev_address=findViewById(R.id.tv_rev_address);
        tvItems=findViewById(R.id.tvItems);
        tvprice=findViewById(R.id.tvprice);
        tvMrp=findViewById(R.id.tvMrp);
        tvGst=findViewById(R.id.tvGst);
        session_management=new Session_management(ctx);
        tvDiscount=findViewById(R.id.tvDiscount);
        tvSubTotal=findViewById(R.id.tvSubTotal);
        rel_order=findViewById(R.id.rel_order);
        loc_id=getIntent().getStringExtra("loc_id");
        rev_name=getIntent().getStringExtra("name");
        rev_mobile=getIntent().getStringExtra("mobile");
        rev_soc_id=getIntent().getStringExtra("socity_id");
        rev_pincode=getIntent().getStringExtra("pincode");
        rev_address=getIntent().getStringExtra("address");
        rev_state=getIntent().getStringExtra("state");
        rev_city=getIntent().getStringExtra("city");
        plan_id=getIntent().getStringExtra("plan_id");
        plan_name=getIntent().getStringExtra("plan_name");
        working_days=getIntent().getStringExtra("working_days");
        plan_expiry=getIntent().getStringExtra("plan_expiry");
        mrp=getIntent().getStringExtra("mrp");
        price=getIntent().getStringExtra("price");
        rel_order.setOnClickListener(this);
        module=new Module(ctx);
        tv_rev_name.setText(rev_name);
        tv_rev_mobile.setText(rev_mobile);
        tv_rev_pincode.setText(rev_pincode);
        String address=rev_address+"\n "+rev_state+" ("+rev_pincode+")";
        tv_rev_address.setText(address);

        tvItems.setText("1");
        tvprice.setText(getResources().getString(R.string.currency)+" "+price);
        tvMrp.setText(getResources().getString(R.string.currency)+" "+mrp);
        float dmrp=Float.parseFloat(mrp);
        float dprice=Float.parseFloat(price);
        int dis=(int)(dmrp-dprice);
        if(dis<=0)
        {
            tvDiscount.setText("No Discount");
        }
        else
        {
            tvDiscount.setText("-"+getResources().getString(R.string.currency)+" "+dis);
        }

        DecimalFormat precision = new DecimalFormat("0.0");
        tot =  Float.parseFloat(precision.format(dprice+module.getGSt(gst,price))+"0");

        tvSubTotal.setText(getResources().getString(R.string.currency)+" "+ tot+"0");
        tvGst.setText(getResources().getString(R.string.currency)+" "+precision.format(module.getGSt(gst,price))+"0");



    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rel_order)
        {
            module.showToast("Please wait..");
             user_id=session_management.getUserDetails().get(KEY_ID);
            txnid=module.getUniqueId("a2z");
            amount=String.valueOf(tot);
//            amount="12";
            phone=session_management.getUserDetails().get(KEY_MOBILE);
            prodname=plan_name;
            firstname=session_management.getUserDetails().get(KEY_NAME);
            email=session_management.getUserDetails().get(KEY_EMAIL);

            attemptOrder(user_id,"paid",loc_id,txnid,plan_id,plan_name,mrp,price,gst, String.valueOf(tot),plan_expiry,module.getCurrentDate(),working_days);
//            startpay();

        }
    }

    private void attemptOrder(String user_id, String payment_status,String loc_id, String trans_id, String plan_id, String plan_name, String mrp, String price,String gst,String gross, String plan_expiry, String currentDate,String working_days) {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("payment",payment_status);
        params.put("trans_id",trans_id);
        params.put("package_id",plan_id);
        params.put("package_name",plan_name);
        params.put("package_mrp",mrp);
        params.put("package_price",price);
        params.put("gst",gst);
        params.put("gross_amount",gross);
        params.put("package_duration",plan_expiry);
        params.put("order_date",currentDate);
        params.put("location_id",loc_id);
        params.put("no_of_working_days",working_days);
        Log.e("paramssss",""+params.toString());
        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.ATTEMPT_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        String msg=response.getString("data");
                        Intent intent=new Intent(ctx,ThanksActivity.class);
                        intent.putExtra("msg",msg);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                    else
                    {
                         toastMsg.toastIconError("Something Went Wrong");
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    toastMsg.toastIconError("Something Went Wrong");
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
                    Toast.makeText(PaymentActivity.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "hash empty");
                } else {
                    // mPaymentParams.setMerchantHash(merchantHash);
                    paymentParam.setMerchantHash(merchantHash);
                    // Invoke the following function to open the checkout page.
                    // PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, StartPaymentActivity.this,-1, true);
                    PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, PaymentActivity.this, R.style.AppTheme_Green, false);
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
                    attemptOrder(user_id,"paid",loc_id,txnid,plan_id,plan_name,mrp,price,gst, String.valueOf(tot),plan_expiry,module.getCurrentDate(),working_days);

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
