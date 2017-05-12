package utils;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

/**
 * Created by jrm on 2017-4-7.
 * 获取wifi相关信息在连接页面
 */

public class WifiManagerUtils {

    private WifiManager wifiManager;

    public WifiManagerUtils(final WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    public int getWifiManangerInfos(final String ssid, final boolean hiddenSSID, final String s, final String s2) {
        Log.i("tag-0----",ssid+""+hiddenSSID+""+s+""+s2+""+wifiManager.isWifiEnabled()); //WPA
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
         WifiConfiguration wifiConfiguration = new WifiConfiguration();
         WifiConfigurationUtils.type type = null;
        if (s == null || TextUtils.isEmpty(s)){
            type = WifiConfigurationUtils.type.NONE;
            if (type.equals( WifiConfigurationUtils.type.NONE) && !(TextUtils.isEmpty(s2))){
                type = WifiConfigurationUtils.type.WPA;
            }
            switch (type.ordinal()){
                default:
                case 1:
                case 2:
                case 3:
            }
        }
        wifiConfiguration.SSID = ssid;
        wifiConfiguration.status = 2;
        wifiConfiguration.hiddenSSID = hiddenSSID;
        while (true) {
            while (true) {
                try {
                    type = Enum.valueOf(WifiConfigurationUtils.type.class, s.toUpperCase(Locale.US));
                    Log.i("tag-->", type.toString());
                    switch (type.ordinal()) {
                        case 1:
                            this.get_WPE_Info(wifiConfiguration, s2);
                            break;
                        case 2:
                            Log.i("tag--,,,,", s2);
                            this.get_WPAN_Info(wifiConfiguration, s2);
                            break;
                        case 3:
                            Log.i("tag--...", s2);
                            wifiConfiguration.allowedKeyManagement.set(0);
                            wifiConfiguration.allowedAuthAlgorithms.set(0);
                            break;
                    }
                } catch (Exception e) {
                    return -1;
                }
                int netId = wifiManager.addNetwork(wifiConfiguration);
                if (netId != -1) {
                    wifiManager.enableNetwork(netId, true);
                    wifiManager.saveConfiguration();
                }
                Log.i("tag---", netId + "  " + wifiConfiguration.SSID + "  " + wifiManager.getConnectionInfo().getSSID());
                return netId;
            }
        }
     /* //  Label_0183: {
            Enum<WifiConfigurationUtils.type> enum1;
       //   Label_0044: {
                if (s != null && !TextUtils.isEmpty(s)) {
                    try {
                        enum1 = Enum.valueOf(WifiConfigurationUtils.type.class, s.toUpperCase(Locale.US));
                      //  break ;//Label_0044;
                    }
                    catch (IllegalArgumentException ex) {
                        return -1;
                    }
                  // break;// Label_0183;
                }else {
                    enum1 = WifiConfigurationUtils.type.NONE;
                }
          // }
            if (enum1.equals(WifiConfigurationUtils.type.NONE) && !TextUtils.isEmpty(s2)) {
                enum1 = WifiConfigurationUtils.type.WPA;
            }
            wifiConfiguration.SSID = ssid;
            wifiConfiguration.status = 2;
            wifiConfiguration.hiddenSSID = hiddenSSID;
            switch (enum1.ordinal()) {
                case 1:
                    this.get_WPE_Info(wifiConfiguration, s2);
                    break;
                case 2:
                    Log.i("tag--,,,",s2);
                    this.get_WPAN_Info(wifiConfiguration, s2);
                    break;
                case 3:
                    wifiConfiguration.allowedKeyManagement.set(0);
                    wifiConfiguration.allowedAuthAlgorithms.set(0);
                    break;
            }
       // }
        final int addNetwork = wifiManager.addNetwork(wifiConfiguration);
        if (addNetwork != -1) {
            wifiManager.enableNetwork(addNetwork, true);
            wifiManager.saveConfiguration();
        }
        Log.i("tagjrm",addNetwork+"  " +wifiConfiguration.SSID+"  "+wifiManager.getConnectionInfo().getSSID());
        return addNetwork;*/
    }

    protected void get_WPAN_Info(final WifiConfiguration wifiConfiguration, final String s) {
        wifiConfiguration.allowedKeyManagement.set(1);
        wifiConfiguration.allowedAuthAlgorithms.set(0);
        wifiConfiguration.allowedProtocols.set(0);
        wifiConfiguration.allowedProtocols.set(1);
        wifiConfiguration.allowedPairwiseCiphers.set(1);
        wifiConfiguration.allowedPairwiseCiphers.set(2);
        wifiConfiguration.allowedGroupCiphers.set(2);
        wifiConfiguration.allowedGroupCiphers.set(3);
        if (!TextUtils.isEmpty(s)) {
            wifiConfiguration.preSharedKey = "\"" + s + "\"";
        }
        Log.i("tag888",wifiConfiguration.toString());
    }

    protected void get_WPE_Info(final WifiConfiguration wifiConfiguration, final String s) {
        wifiConfiguration.allowedKeyManagement.set(0);
        wifiConfiguration.allowedAuthAlgorithms.set(0);
        wifiConfiguration.allowedAuthAlgorithms.set(1);
        wifiConfiguration.allowedGroupCiphers.set(0);
        wifiConfiguration.allowedGroupCiphers.set(1);
        wifiConfiguration.allowedGroupCiphers.set(2);
        wifiConfiguration.allowedGroupCiphers.set(3);
        final int length = s.length();
        if ((length == 10 || length == 26 || length == 58) && s.matches("[0-9A-Fa-f]*")) {
            wifiConfiguration.wepKeys[0] = s;
        }
        else {
            wifiConfiguration.wepKeys[0] = '\"' + s + '\"';
        }
        wifiConfiguration.wepTxKeyIndex = 0;
    }
}
