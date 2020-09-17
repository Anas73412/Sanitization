package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.artjimlop.altex.AltexImageDownloader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.util.Session_management;

import static in.sanitization.sanitization.Config.Constants.KEY_ID;
import static in.sanitization.sanitization.Config.Constants.KEY_NAME;

public class ThanksActivity extends AppCompatActivity {

    TextView tv_msg;
    Module module;
    Session_management session_management;
    Button btn_home,btn_download;
    String url="";
    private static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tv_msg=findViewById(R.id.tv_msg);
        module=new Module(ThanksActivity.this);
        session_management=new Session_management(ThanksActivity.this);
        btn_home=findViewById(R.id.btn_home);
        btn_download=findViewById(R.id.btn_download);
       getPermission();
        String type=getIntent().getStringExtra("type");
        if(type.equalsIgnoreCase("pay"))
        {
            btn_download.setVisibility(View.GONE);
        }
        else if(type.equalsIgnoreCase("donate"))
        {
           url=getIntent().getStringExtra("url");
        }

        String msg=getIntent().getStringExtra("msg");
        tv_msg.setText(Html.fromHtml(msg));
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             go_home();
            }
        });
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadCertificate(url+".jpg",session_management.getUserDetails().get(KEY_NAME),session_management.getUserDetails().get(KEY_ID));
            }
        });

    }

    @Override
    public void onBackPressed() {
        go_home();
    }

    public void go_home()
    {
        Intent intent=new Intent(ThanksActivity.this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    public void getPermission()
    {
        if (!checkPermission()) {
            requestPermission(); // Code for permission
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    private void downloadCertificate(final String mediaUrl, final String name, final String id) {

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                downloadFile(mediaUrl, name,id);
            }
        };
        handler.post(runnable);


    }

    private void downloadFile(String mediaUrl, final String s,final String p_id) {
        final ProgressDialog dialog = new ProgressDialog(ThanksActivity.this);
        dialog.setMessage("Loading..");
        dialog.setCanceledOnTouchOutside(false);

        Log.e("adsdasdasd",""+mediaUrl.toString()+" - "+s);

        final String appName = getResources().getString(R.string.app_name);
        // creating the download directory
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/" + appName + "/Certificates");
        if (!direct.exists()) {
            direct.mkdirs();
        }

        String pdfFileName = s + ".jpg";

        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(mediaUrl));
        request1.setTitle("File Doo");
        request1.setDescription("Downloading.....");
//        request1.setVisibleInDownloadsUi(false);
        request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request1.setShowRunningNotification(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request1.allowScanningByMediaScanner();
            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
//        if (getFileExists(s,vvExt))
//        {
//            new ToastMsg(activity).toastIconError("File is already downloaded!");
//            return;
//        }
        request1.setDestinationInExternalPublicDir("/" + appName + "/Certificates", pdfFileName);
        final DownloadManager manager1 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        // start the download
        final long downloadId = manager1.enqueue(request1);
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            module.showToast("Download will be started soon.");
        }

        BroadcastReceiver mCompleted = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId == id) {
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:MM:ss");
                    String ss = simpleDateFormat.format(date);

//                    File movieFile = new File(Environment.getExternalStorageDirectory()
//                            +movieFileName);
//                    String appName22 = getResources().getString(R.string.app_namee)+getResources().getString(R.string.original_file_path);
                    module.showToast("Certificate Download Successfully.");
                    go_home();
//                    File direct = new File(Environment.getExternalStorageDirectory()
//                            + "/" + appName + "/Orders/"+"FW_ID"+id+".pdf");
                    File direct = new File(Environment.getExternalStorageDirectory()
                            + "/" + appName + "/Certificates/"+appName+p_id.toUpperCase()+".jpg");
//                    createDialogPdfViewer(direct);

                }
            }
        };
        registerReceiver(mCompleted, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}
