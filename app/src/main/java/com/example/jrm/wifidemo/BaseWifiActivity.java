package com.example.jrm.wifidemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.Iterator;
import java.util.List;

import utils.SSIDUtils;
import utils.WifiConfigurationUtils;

/**
 * Created by jrm on 2017-4-6.
 */

public abstract  class BaseWifiActivity extends Activity {

   protected WifiManager wifiManager;
    protected WifiInfo wifiInfo;
    protected List<LocalaBean> localList;
    protected List<ScanResult> result;
    protected List<WifiConfiguration> wifiConfigurations;

    protected Handler handler = new Handler() {
        public void handleMessage(Message paramAnonymousMessage) {
            BaseWifiActivity.this.postMessage(paramAnonymousMessage);
        }
    };
    public BroadcastReceiver receiver = new BroadcastReceiver(){
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent){
            if (paramAnonymousIntent.getAction().equals("android.net.wifi.SCAN_RESULTS")) {
                if (wifiManager.isWifiEnabled())
                    BaseWifiActivity.this.getWifiResult();
                    wifiManager.startScan();
            }
        }
    };

    protected void initWifiManager() {
        wifiManager = ((WifiManager)this.getSystemService(Context.WIFI_SERVICE));
    }

    protected abstract void postMessage(Message paramMessage);

    protected void changeWifiStatue(boolean paramBoolean){
        int i = 1;
        while (i != 0) {
            switch (wifiManager.getWifiState()) {
                case 1:
                    handler.sendEmptyMessage(102);
                    if (!paramBoolean) {
                        i = 0;
                        break;
                    }
                    break;

                case 3:
                    handler.sendEmptyMessage(101);
                    if (paramBoolean) {
                        i = 0;
                        break;
                    }
                    break;

                case 4:
                    handler.sendEmptyMessage(103);
                    i = 0;
                    break;
            }
            try {
                Thread.sleep(200L);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        handler.sendEmptyMessage(90);
    }

    protected void getWifiResult() {
        /*主要是通过wifi 硬件的扫描来获取一些周边的wifi 热点的信息。
         在我们进行wifi 搜索的时候，一般会搜到这些信息，首先是接入点名字、接入点信息的强弱、还有接入点使用的安全模式，是WPA、WPE*/
        result = wifiManager.getScanResults();
        //在我们连通一个wifi 接入点的时候，需要获取到的一些信息
        wifiConfigurations= wifiManager.getConfiguredNetworks();

        //在我们的wifi 已经连通了以后，可以通过这个类获得一些已经连通的wifi 连接的信息获取当前链接的信息
        wifiInfo = wifiManager.getConnectionInfo();
        ArrayList localArrayList = new ArrayList();
        if (result != null) {
            for (final ScanResult scanResult : result) {   //--->主要是显示wifi列表界面上该展示的数据 给赋值
                final LocalaBean locala2 = new LocalaBean();
                locala2.setSsid(scanResult.SSID); // SSID代表网络的名字,类里的方法
                if ((!SSIDUtils.isExitSSID(scanResult.SSID)) && (LocalaBean.getInitLocalBean(scanResult.SSID, localArrayList) == null)) {
                    if (WifiConfigurationUtils.type.NONE == WifiConfigurationUtils.getWifiTypeOne(scanResult.capabilities)) {
                        locala2.setTitle("开放");
                        locala2.setDistance(2000);
                        locala2.setRead(true);
                    } else {
                        locala2.setDistance(2001);
                        locala2.setTitle("加密");
                        locala2.setRead(false);
                    }
                    locala2.setCaps(scanResult.capabilities);
                    locala2.setLevel(scanResult.level);
                    locala2.setUntrusted(false);
                    locala2.setFrequency(scanResult.frequency / 100 / 10.0 + "GHz");
                    localArrayList.add(locala2);
                }
            }
            if (wifiConfigurations != null) {
                final String ssid = getSSID();
                Log.i("TAG---",result.size()+"  "+wifiInfo.getNetworkId()+" "+ssid);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                final Iterator<WifiConfiguration> iterator2 = wifiConfigurations.iterator();
                int n = 0;
                while (iterator2.hasNext()){
                    WifiConfiguration localWifiConfiguration = (WifiConfiguration)iterator2.next();
                    Log.i("TAG+++",localArrayList.size()+"  "+localWifiConfiguration.SSID+"  "+wifiInfo.getNetworkId()+"  "+
                            connectionInfo.getSSID()+" "+wifiConfigurations.size()+"  "+localWifiConfiguration.toString());
                    final LocalaBean locala2 = LocalaBean.getInitLocalBean(localWifiConfiguration.SSID, localArrayList); //类里的方法,获取对象;
                    if (locala2 != null){
                        locala2.setUntrusted(true);
                        localArrayList.remove(locala2);
                        locala2.setNewId(localWifiConfiguration.networkId);
                        if ((localWifiConfiguration.SSID.equals(ssid)) && (-1 != wifiInfo.getNetworkId())){
                            locala2.setTitle(getWifiState());
                            if (SupplicantState.COMPLETED == wifiInfo.getSupplicantState()){
                                locala2.setDistance(1000);
                                final int ipAddress = wifiInfo.getIpAddress();
                                String string = "0.0.0.0";
                                if (ipAddress != 0) {
                                    string = (ipAddress & 0xFF) + "." + (0xFF & ipAddress >> 8) + "." + (0xFF & ipAddress >> 16) + "." + (0xFF & ipAddress >> 24);
                                }
                                locala2.setIpAddress(string);
                            }else {
                                locala2.setDistance(1002);
                            }
                            locala2.setLinkedSpeed(connectionInfo.getLinkSpeed() + "Mbps");
                            localArrayList.add(0, locala2);
                        }else {
                            locala2.setDistance(1003);
                            locala2.setTitle("已保存");
                            localArrayList.add(n, locala2);
                        }
                    }
                }

              /*  Label_0547_Outer:
                while (iterator2.hasNext()) {
                    final WifiConfiguration wifiConfiguration = iterator2.next();
                    Log.i("TAG+++",localArrayList.size()+"  "+wifiConfiguration.SSID+"  "+wifiInfo.getNetworkId());
                    final LocalaBean locala2 = LocalaBean.getInitLocalBean(wifiConfiguration.SSID, localArrayList); //类里的方法,获取对象;
                    while (true) {
                        Label_0591: {
                            if (locala2 == null) {
                                break Label_0591;
                            }
                            locala2.setUntrusted(true);
                            localArrayList.remove(locala2);
                            locala2.setNewId(wifiConfiguration.networkId);
                            Log.i("TAG+++",ssid+"  "+wifiConfiguration.SSID+"  "+wifiInfo.getNetworkId());
                            if (!wifiConfiguration.SSID.equals(ssid) || -1 == wifiInfo.getNetworkId()) {
                                locala2.setDistance(1003);
                                locala2.setTitle("已保存");
                                localArrayList.add(n, locala2);
                                break Label_0591;
                            }
                            locala2.setTitle(getWifiState());
                            if (SupplicantState.COMPLETED == connectionInfo.getSupplicantState()) {
                                locala2.setDistance(1000);
                                final int ipAddress = wifiInfo.getIpAddress();
                                String string = "0.0.0.0";
                                if (ipAddress != 0) {
                                    string = (ipAddress & 0xFF) + "." + (0xFF & ipAddress >> 8) + "." + (0xFF & ipAddress >> 16) + "." + (0xFF & ipAddress >> 24);
                                }
                                locala2.setIpAddress(string);
                            } else {
                                locala2.setDistance(1002);
                            }
                            locala2.setLinkedSpeed(connectionInfo.getLinkSpeed() + "Mbps");
                            localArrayList.add(0, locala2);
                            final int n2 = 1;
                            n = n2;
                            continue Label_0547_Outer;
                        }
                        Log.i("tag--->>>>",n+"");
                        final int n2 = n;
                        continue;
                    }
                }*/
            }
        }
        this.localList = localArrayList;
        this.postResult();
    }

    protected String getSSID(){
        WifiInfo localWifiInfo = wifiManager.getConnectionInfo();
        if (localWifiInfo != null)
            return localWifiInfo.getSSID();
        return null;
    }

    protected String getWifiState() {
        wifiInfo = wifiManager.getConnectionInfo();
        SupplicantState localSupplicantState = wifiInfo.getSupplicantState();
        switch (localSupplicantState.ordinal()) {
            case 3:
            default:
                return "";
            case 1:
                return "获取IP中";
            case 2:
                return "正在连接";
            case 9:
                return "已连接";
            case 5:
                return "断开连接";
            case 6:
                return "休眠";
            case 7:
                return "获取IP中";
            case 8:
                return "获取IP中";
            case 4:
                return "闲置";
            case 10:
                return "禁用";
            case 11:
                return "无效的";
            case 12:
                return "扫描网络";
            case 13:
        }
        return "未初始化";
    }

    protected abstract void postResult();

    public void onPause() {
        super.onPause();
        this.unregisterReceiver(receiver);
    }

    public void onResume() {
        super.onResume();
        IntentFilter localIntentFilter = new IntentFilter("android.net.wifi.SCAN_RESULTS");
        this.registerReceiver(receiver, localIntentFilter);
        wifiManager.startScan();
    }

}
