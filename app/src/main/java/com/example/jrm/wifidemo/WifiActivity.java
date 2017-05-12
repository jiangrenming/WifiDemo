package com.example.jrm.wifidemo;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import utils.WifiConfigurationUtils;
import utils.WifiManagerUtils;
import utils.WindowUtils;
import wedget.MyListView;
import wedget.WifiInfomationDialog;
import wedget.WifiInformationSaveDialog;

/**
 * Created by jrm on 2017-4-7.
 */

public class WifiActivity  extends BaseWifiActivity implements View.OnClickListener,WifiAdapter.myWifiCallBack{

    private MyListView listView;
    private WifiAdapter adapter;
    private Button btn_next;
    private Switch wifi_switch;
    private boolean switchParams = false;
    private boolean wifiOpen = true;//设置开关的状态
    //第一步
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          initWifiManager();
          WindowUtils.isViseable(this.getWindow());
          setContentView(R.layout.wifi_activity);
          listView = (MyListView) findViewById(R.id.wifi_list);
          btn_next = (Button) findViewById(R.id.btn_next);
          wifi_switch = (Switch)findViewById(R.id.switch_close);
          adapter = new WifiAdapter(this,this.localList,wifiManager.isWifiEnabled());
          adapter.setMyWifiCallBack(this);
          listView.setAdapter(adapter);
        if (wifiManager.isWifiEnabled()){ //这里得到结果后会发送一个消息
            this.getWifiResult();
          }

        switchParams = wifiManager.isWifiEnabled();
        wifi_switch.setChecked(switchParams);
        wifi_switch.setEnabled(wifiOpen);
        wifi_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchParams = isChecked;
                if (isChecked && wifiOpen){
                    handler.sendEmptyMessage(99);
                    return;
                }
                handler.sendEmptyMessage(-99);
                btn_next.setText("跳过");
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void postMessage(Message paramMessage) {
        switch (paramMessage.what) {
            default: {}
            case 99:
                //adapter.setOpenWifi(false);
                wifiOpen = false;
                adapter.notifyDataSetChanged();
                this.wifiManager.setWifiEnabled(true);
                WindowUtils.isViseable(this.getWindow());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        WifiActivity.this.changeWifiStatue(true);
                    }
                }).start();
            break;
            case -99:
                //adapter.setOpenWifi(false);
                wifiOpen = false;
                adapter.notifyDataSetChanged();
                this.wifiManager.setWifiEnabled(false);
                WindowUtils.isViseable(this.getWindow());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        WifiActivity.this.changeWifiStatue(false);
                    }
                }).start();
            break;
            case 90:
             //   adapter.setOpenWifi(true);
                wifiOpen = true;
                adapter.notifyDataSetChanged();
            break;
            case 101:
                wifiOpen = true;
                adapter.notifyDataSetChanged();
               // adapter.setOpenWifi(true);
            break;
            case 102:
                wifiOpen = true;
                adapter.notifyDataSetChanged();
               // adapter.setOpenWifi(true);
                adapter.setListLocalBean(null);
                adapter.notifyDataSetChanged();
            break;
        }
    }

    //第二步:这里得到消息来更新适配器等数据
    @Override
    protected void postResult() {
        adapter.setListLocalBean(this.localList);
        adapter.notifyDataSetChanged();
        Log.i("TAG???",localList.size()+"  "+localList.toString());
        if (this.wifiManager.isWifiEnabled()) {
            if (this.wifiInfo.getSupplicantState() != SupplicantState.COMPLETED) {
                btn_next.setText("跳过");
                return;
            }
            btn_next.setText("下一步");
        }
    }

    /**
     * 实现适配器里的接口回调
     * @param position
     * @param localBean
     * 这里主要是做弹出窗处理和连接界面的处理
     */
    @Override
    public void openPositionWifi(int position, final LocalaBean localBean) {
        if (localBean != null) {
            final String ssid = localBean.getSsid();
            if (localBean.isUntrusted()) { //-->是否保存
                switch (localBean.getDistance()) {
                    case 1000:   //當wifi連接的時候點擊會走此操作
                        final WifiInfomationDialog dialog = new WifiInfomationDialog(this);
                        dialog.getDialog(localBean);
                        dialog.setDialogCallBack(new WifiInfomationDialog.DialogCallBack() {
                            @Override
                            public boolean unSave(WifiInfomationDialog paramb) { //不保存此wifi热点
                                    WifiActivity.this.wifiManager.disconnect(); //断连
                                    WifiActivity.this.wifiManager.removeNetwork(localBean.getNewId()); //移除网络id
                                    WifiActivity.this.wifiManager.saveConfiguration(); //保存配置
                                    WifiActivity.this.getWifiResult(); //重新获取wifi热点
                                    WindowUtils.isViseable(WifiActivity.this.getWindow()); //弹出窗消失
                                return true;
                            }
                            @Override
                            public boolean cancle(WifiInfomationDialog paramb) { //弹出窗的取消
                                 WindowUtils.isViseable(WifiActivity.this.getWindow());
                                return true;
                            }
                        });
                        dialog.show();
                    break;
                    case 1002:
                    case 1003:  //--->當wifi處於 保存的狀態的時候，點擊界面會執行這裏的操作
                        final WifiInformationSaveDialog saveDialog = new WifiInformationSaveDialog(this);
                        saveDialog.getSaveDialog(localBean);
                        saveDialog.setSaveDialogCallBack(new WifiInformationSaveDialog.SaveDialogCallBack() {
                            @Override
                            public boolean save_Cancle_Dialog(WifiInformationSaveDialog dialog) {
                                WindowUtils.isViseable(WifiActivity.this.getWindow());
                                return true;
                            }

                            @Override
                            public boolean save_unSave_Dialog(WifiInformationSaveDialog dialog) {
                                WifiActivity.this.wifiManager.disconnect();
                                WifiActivity.this.wifiManager.removeNetwork(localBean.getNewId());
                                WifiActivity.this.wifiManager.saveConfiguration();
                                WifiActivity.this.getWifiResult();
                                WindowUtils.isViseable(WifiActivity.this.getWindow()); //弹出窗消失
                                return true;
                            }

                            @Override
                            public boolean save_wlan_Dialog(WifiInformationSaveDialog dialog) {
                                for (final WifiConfiguration wifiConfiguration : WifiActivity.this.wifiConfigurations) {
                                    if (wifiConfiguration.SSID.equals("\"" + ssid + "\"")) {
                                        WifiActivity.this.wifiManager.updateNetwork(wifiConfiguration);
                                        WifiActivity.this.wifiManager.enableNetwork(wifiConfiguration.networkId, true);
                                        WindowUtils.isViseable(WifiActivity.this.getWindow()); //弹出窗消失
                                        break;
                                    }
                                }
                                WifiActivity.this.getWifiResult();
                                return true;
                            }
                        });
                        saveDialog.show();
                    break;
                }
            } else {
                if (2000 == localBean.getDistance()) { //-->處於 開放 不需要密碼的情況~
                    new WifiManagerUtils(wifiManager).getWifiManangerInfos(localBean.getSsid(), false, WifiConfigurationUtils.getWifiTypeTwo(localBean.getCaps()), null);
                    getWifiResult();
                    return;
                }
                for (final ScanResult scanResult : result) {
                    if (scanResult.SSID.equals(ssid)) { //--->跳转到打开需要输入密码的界面，连接wifi
                        Intent intent = new Intent(WifiActivity.this,WordPageActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("level",localBean.getLevel());
                        bundle.putParcelable("scanResult",scanResult);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                    }
                }
            }
        }
    }

  /*  @Override
    public void isOpenWifi(boolean isOpen) {
        if (isOpen) {
            this.handler.sendEmptyMessage(99);
            return;
        }
        this.handler.sendEmptyMessage(-99);
        btn_next.setText("跳过");
    }*/
}
