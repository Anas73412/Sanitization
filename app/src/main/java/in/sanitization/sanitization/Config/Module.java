package in.sanitization.sanitization.Config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.sanitization.sanitization.Model.BlockModel;
import in.sanitization.sanitization.Model.DistrictModel;
import in.sanitization.sanitization.Model.StateModel;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.util.ToastMsg;

public class Module {

    Context context;
  ToastMsg toastMsg;
    public Module(Context context) {
        this.context = context;
        toastMsg=new ToastMsg(context);
    }


    public String VolleyErrorMessage(VolleyError error)
    {
        String str_error ="";
        if (error instanceof TimeoutError) {
            str_error="Connection Timeout";
        } else if (error instanceof AuthFailureError) {
            str_error="Session Timeout";
            //TODO
        } else if (error instanceof ServerError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof NetworkError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof ParseError) {
            //TODO
            str_error="An Unknown error occur";
        }else if(error instanceof NoConnectionError){
            str_error="No Internet Connection";
        }

        return str_error;
    }

    public void errMessage(VolleyError error)
    {
        String msg=VolleyErrorMessage(error);
        if(!msg.isEmpty())
        {
            showToast(""+msg);
        }
    }
    public void preventMultipleClick(final View view) {
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 1000);
    }
    public void showToast(String s)
    {
        Toast.makeText(context,""+s,Toast.LENGTH_SHORT).show();
    }


    public void setErrorOnEditText(EditText et, String msg)
    {
        toastMsg.toastIconError(""+msg);
        et.requestFocus();
    }

    public void setDrawableBackground(Button btn)
    {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.button_background) );
        } else {
            btn.setBackground(ContextCompat.getDrawable(context, R.drawable.button_background));
        }
        btn.setTextColor(context.getResources().getColor(R.color.white));
    }
    public void removeDrawableBackground(Button btn)
    {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.button_white_background) );
        } else {
            btn.setBackground(ContextCompat.getDrawable(context, R.drawable.button_white_background));
        }
        btn.setTextColor(context.getResources().getColor(R.color.black));
    }

    public String getBuildingType(int flg)
    {
        String str="";
        switch (flg)
        {
            case 1:str="Home";
               break;
            case 2:str="Office";
               break;
            case 3:str="Shop";
                break;
            case 4:str="Other";
                break;
            default:str="Home";
                break;
        }
        return str;
    }

    public int getDiscount(String price, String mrp)
    {
        double mrp_d=Double.parseDouble(mrp);
        double price_d=Double.parseDouble(price);
        double per=((mrp_d-price_d)/mrp_d)*100;
        double df=Math.round(per);
        int d=(int)df;
        return d;
    }

    public boolean checkCityExist(ArrayList<String> list,String str)
    {
        boolean flag=false;
        for(int i=0;  i<list.size();i++)
        {
            if(list.get(i).toString().equalsIgnoreCase(str))
            {
                flag=true;
            }
        }
        return flag;
    }

    public void setSpinAdapter(ArrayList<String> list, Spinner spin, Activity activity,String select) {
        list.add(0,select);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, list);
        spin.setAdapter(adapter);

    }

    public void printLogE(String TAG,String msg)
    {
        Log.e(TAG,""+msg);
    }

    public int getFlagTypeOnAddress(String str)
    {
        int flag=0;
        switch (str)
        {
            case "Home":flag=1;
                        break;
            case "Office":flag=2;
                        break;
             case "Shop":flag=3;
                        break;
              case "Other":flag=4;
                        break;
        }
        return flag;
    }

    @SuppressLint("NewApi")
    public void whatsapp( String phone,String message) {
        String phoneNo=phone;

        Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.setAction(Intent.ACTION_VIEW);
        sendIntent.setPackage("com.whatsapp");
        String url = "https://api.whatsapp.com/send?phone=" + phoneNo + "&text=" + message;
        sendIntent.setDataAndType(Uri.parse(url),"text/plain");


        if(sendIntent.resolveActivity(context.getPackageManager()) != null){
            context.startActivity(sendIntent);
        }else{
            Toast.makeText(context,"Please Install Whatsapp Massnger App in your Devices",Toast.LENGTH_LONG).show();
        }
    }
    public void callOnNumber(String num)
    {
        Intent intent=new Intent(Intent.ACTION_DIAL);
        String number=num.trim();
        intent.setData(Uri.parse("tel: "+number));
        context.startActivity(intent);
    }

    public String getStateId(ArrayList<StateModel> list,String state_name)
    {
        int id=0;
        for(int i=0; i<list.size();i++)
        {
              if(list.get(i).getState_name().toString().equalsIgnoreCase(state_name))
              {
                  id=i;
                  break;
              }
        }
        return String.valueOf(id+1);
    }

    public String getDistrictId(ArrayList<DistrictModel> list, String dis_name)
    {
        int id=0;
        for(int i=0; i<list.size();i++)
        {
            if(list.get(i).getDistrict_name().toString().equalsIgnoreCase(dis_name))
            {
                id=i;
                break;
            }
        }
        return String.valueOf(id+1);
    }

    public String getBlockId(ArrayList<BlockModel> list, String block_name)
    {
        int id=0;
        for(int i=0; i<list.size();i++)
        {
            if(list.get(i).getBlock_name().toString().equalsIgnoreCase(block_name))
            {
                id=i;
                break;
            }
        }
        return String.valueOf(id+1);
    }

    public String getCurrentDate()
    {
        String c_date="";
        try
        {
            Date date=new Date();
            c_date=new SimpleDateFormat("dd/MM/yyyy").format(date).toString();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return c_date;
    }
    public String getUniqueId(String type)
    {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("ddMMyyyyhhmmss");
        return (type+simpleDateFormat.format(date).toString());
    }
}

