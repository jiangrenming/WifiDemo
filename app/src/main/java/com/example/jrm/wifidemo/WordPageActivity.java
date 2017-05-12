package com.example.jrm.wifidemo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import utils.SSIDUtils;
import utils.WifiConfigurationUtils;
import utils.WifiManagerUtils;
import wedget.ConnDialog;
import wedget.WifiConnDialog;

/**
 * Created by jrm on 2017-4-10.
 * 連接的界面
 */

public class WordPageActivity  extends Activity implements View.OnClickListener{

    private Button wifi_page_wlan,wifi_page_cancle;
    private TextView wifi_page_name,wifi_page_title,wifi_page_strong,wifi_page_caps;
    private EditText wifi_page_txt;
    private ImageView iv_pwd_visviable;
    private ScanResult scanResult;
    private ConnDialog dialog;
    private WifiManager wifi;
    private String mSsid ="";
    boolean isConnection = false;
    private int netId = -1;
    int associatingCount = 0;
    int associatedCount = 0;
    int otherStatueCount = 0;

    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1000:
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    netId = msg.arg2;
                    dialog =  new ConnDialog(WordPageActivity.this).getDiaolg(scanResult.SSID);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        public void onDismiss(final DialogInterface dialogInterface) {
                            mHandle.removeMessages(1001);
                            if (!isConnection) {
                                dialog.dismiss();
                                wifi.disconnect();
                                Log.i("tag---","bbb");
                            }
                        }
                    });
                    dialog.show();
                    Log.i("tag---","ccc");
                    mHandle.sendEmptyMessage(1001);
                    break;

                case 1001:
                     WifiInfo connectionInfo = wifi.getConnectionInfo();
                     SupplicantState supplicantState = connectionInfo.getSupplicantState();
                      Log.i("tag>>",connectionInfo.getSSID());
                     String replaceAll = connectionInfo.getSSID().replaceAll("\"", "");
                     Log.i("tag--->",supplicantState.ordinal()+"  "+replaceAll+"  "+mSsid);
                    switch (supplicantState.ordinal()) {
                        default:
                            ++otherStatueCount;
                            if (otherStatueCount < 80) {
                                mHandle.sendEmptyMessageDelayed(1001, 400L);
                                return;
                            }
                            Log.i("tag---//",otherStatueCount+"");
                            connDiologShow();
                            return;
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            ++otherStatueCount;
                            if (otherStatueCount < 80) {
                                mHandle.sendEmptyMessageDelayed(1001, 400L);
                                return;
                            }
                            Log.i("tag---",otherStatueCount+"");
                            connDiologShow();
                            return;
                        case 6:
                        case 9:
                            Log.i("tag---","eee");
                            if (associatingCount > 10) {
                                connDiologShow();
                                return;
                            }
                            if (!mSsid.equals(replaceAll)) {
                                ++associatingCount;
                                mHandle.sendEmptyMessageDelayed(1001, 400L);
                                return;
                            }
                            isConnection = true;
                            mHandle.removeMessages(1001);
                            if (dialog != null &&dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            WordPageActivity.this.onBackPressed();
                            return;
                        case 7:
                            if (associatedCount <= 10 && !mSsid.equals(replaceAll)) {
                                ++associatedCount;
                                mHandle.sendEmptyMessageDelayed(1001, 400L);
                                return;
                            }
                            Log.i("tag---",associatedCount+"");
                            isConnection = false;
                            connDiologShow();
                            return;
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordpage_activity);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        initView();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null){
            scanResult = bundle.getParcelable("scanResult");
            int level = bundle.getInt("level");
            if (scanResult!= null){
                wifi_page_name.setText(scanResult.SSID);
                wifi_page_title.setText(scanResult.SSID);
                wifi_page_caps.setText(scanResult.capabilities);
            }
            if (level <= 0 && level >= -50) {
                wifi_page_strong.setText("强");
            } else if (level < -50 && level >= -70) {
                wifi_page_strong.setText("较强");
            } else if (level < -70 && level >= -80) {
                wifi_page_strong.setText("一般");
            } else if (level < -80 && level >= -100) {
                wifi_page_strong.setText("弱");
            }else {
                wifi_page_strong.setText("弱");
            }
        }
    }

    private void initView() {
        wifi_page_name = (TextView) findViewById(R.id.wifi_page_name);
        wifi_page_title = (TextView) findViewById(R.id.wifi_page_title);
        wifi_page_strong = (TextView) findViewById(R.id.wifi_page_strong);
        wifi_page_caps = (TextView) findViewById(R.id.wifi_page_caps);
        wifi_page_txt = (EditText) findViewById(R.id.password);
        wifi_page_cancle = (Button) findViewById(R.id.button);
        wifi_page_wlan = (Button) findViewById(R.id.btn_wifi_page_wlan);
        iv_pwd_visviable = (ImageView) findViewById(R.id.iv_pwd_visviable);

        wifi_page_wlan.setBackgroundDrawable(getResources().getDrawable(R.drawable.wifi_page_btn_disable));
        wifi_page_wlan.setTextColor(getResources().getColor(R.color.disable_txt));
        wifi_page_wlan.setEnabled(false);

        wifi_page_cancle.setOnClickListener(this);
        wifi_page_wlan.setOnClickListener(this);
        iv_pwd_visviable.setOnClickListener(this);

        wifi_page_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!SSIDUtils.isExitSSID(s.toString()) && s.length() >= 8) {
                    wifi_page_wlan.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_selector));
                    wifi_page_wlan.setTextColor(getResources().getColor(R.color.disable_txt_color));
                    wifi_page_wlan.setEnabled(true);
                    return;
                }
                wifi_page_wlan.setEnabled(false);
                wifi_page_wlan.setBackgroundDrawable(getResources().getDrawable(R.drawable.wifi_page_btn_disable));
                wifi_page_wlan.setTextColor(getResources().getColor(R.color.disable_txt));
                wifi_page_wlan.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WordPageActivity.this,WifiActivity.class));
        this.finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.button:
                this.onBackPressed();
                return;
            case R.id.btn_wifi_page_wlan:
                 this.mSsid = scanResult.SSID;
                 this.isConnection = false;
                 this.associatingCount = 0;
                 this.associatedCount = 0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int netId  = new WifiManagerUtils(wifi).getWifiManangerInfos(mSsid,false, WifiConfigurationUtils.getWifiTypeTwo(scanResult.capabilities),wifi_page_txt.getText().toString());
                        Log.i("tagjrm",netId+"");
                        new WifiConfigurationUtils().isWifiNetWork(WordPageActivity.this,scanResult.SSID,scanResult.capabilities,wifi_page_txt.getText().toString());
                        Message msg  = Message.obtain();
                        msg.what = 1000;
                        msg.arg2 = netId;
                        mHandle.sendMessage(msg);

                    }
                }).start();
            break;
            case R.id.iv_pwd_visviable:
                if (128 != wifi_page_txt.getInputType()) {
                    wifi_page_txt.setInputType(128);
                    return;
                }
                wifi_page_txt.setInputType(129);
                break;
        }
    }

    /**
     * 正在连接的弹出窗
     */
    private void connDiologShow() {
        mHandle.removeMessages(1001);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (-1 != netId) {
            wifi.disableNetwork(netId);
        }
        WifiConnDialog failure = new WifiConnDialog(this);
        failure.setWifiConnFailure(new WifiConnDialog.WifiConnFailure() {
            @Override
            public boolean faliure(WifiConnDialog paramc) {
                associatingCount = 0;
                associatedCount = 0;
                return true;
            }
        });
        failure.show();
    }
}
