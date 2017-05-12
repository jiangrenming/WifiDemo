package utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

/**
 * Created by jrm on 2017-4-6.
 */

public class WifiConfigurationUtils {

    /**
     * 是否存在网络信息
     * @param paramContext
     * @param paramString
     * @return
     */
    public static WifiConfiguration getConfig(Context paramContext, String paramString) {
        List localList = ((WifiManager)paramContext.getSystemService(Context.WIFI_SERVICE)).getConfiguredNetworks();
        Log.i("tag///",localList.size()+"");
        if (localList != null) {
            Iterator localIterator = localList.iterator();
            while (localIterator.hasNext()) {
                WifiConfiguration localWifiConfiguration = (WifiConfiguration)localIterator.next();
                Log.i("tag666",localWifiConfiguration.SSID+"   "+paramString);
                if (paramString.equals(getSSID(localWifiConfiguration.SSID)))
                    return localWifiConfiguration;
            }
        }
        return null;
    }

    /**
     * 创建一个wifi信息
     * @param ssid wifi名称
     * @param password wifi密码
     * @param parama wifi连接的方式：3种
     * @return
     */
    public static WifiConfiguration createConfiguration(String ssid, String password, type parama){

        WifiConfiguration localWifiConfiguration = new WifiConfiguration();
        localWifiConfiguration.allowedAuthAlgorithms.clear();
        localWifiConfiguration.allowedGroupCiphers.clear();
        localWifiConfiguration.allowedKeyManagement.clear();
        localWifiConfiguration.allowedPairwiseCiphers.clear();
        localWifiConfiguration.allowedProtocols.clear();
        localWifiConfiguration.SSID = ("\"" + ssid + "\"");
        Log.i("tag+-+-",parama.toString()+"  "+password+"  "+ssid);
        if (parama == type.NONE) { //没有密码
            localWifiConfiguration.wepKeys[0] = "";
            localWifiConfiguration.allowedKeyManagement.set(0);
            localWifiConfiguration.wepTxKeyIndex = 0;
        }
        if (parama == type.WEP) { //wep加密
            localWifiConfiguration.hiddenSSID = true;
            localWifiConfiguration.wepKeys[0] = ("\"" + password + "\"");
            localWifiConfiguration.allowedAuthAlgorithms.set(1);
            localWifiConfiguration.allowedGroupCiphers.set(3);
            localWifiConfiguration.allowedGroupCiphers.set(2);
            localWifiConfiguration.allowedGroupCiphers.set(0);
            localWifiConfiguration.allowedGroupCiphers.set(1);
            localWifiConfiguration.allowedKeyManagement.set(0);
            localWifiConfiguration.wepTxKeyIndex = 0;
        }
        if (parama == type.WPA) { // wap加密
            localWifiConfiguration.preSharedKey = ("\"" + password + "\"");
            localWifiConfiguration.hiddenSSID = true;
            localWifiConfiguration.allowedAuthAlgorithms.set(0);
            localWifiConfiguration.allowedKeyManagement.set(1);
            localWifiConfiguration.allowedGroupCiphers.set(2);
            localWifiConfiguration.allowedGroupCiphers.set(3);
            localWifiConfiguration.allowedPairwiseCiphers.set(1);
            localWifiConfiguration.allowedPairwiseCiphers.set(2);
            localWifiConfiguration.allowedProtocols.set(0);
            localWifiConfiguration.status = 2;
        }
        Log.i("tag()",localWifiConfiguration.toString()+"  "+ssid);
        return localWifiConfiguration;
    }

    /**
     * 判断网络是否可用
     * @param paramContext
     * @param paramString1 ssid
     * @param paramString2 加密类型
     * @param paramString3 输入的密码
     * @return
     */
    public static boolean isWifiNetWork(Context paramContext, String paramString1, String paramString2, String paramString3){
        Log.i("tag-+-+-",paramString1+"  "+paramString2 +"  "+paramString3);
        if (getConfig(paramContext, paramString1) == null) {
            Log.i("tag-+-+-two",paramString1+"  "+paramString2 +"  "+paramString3);
            WifiManager localWifiManager = (WifiManager)paramContext.getSystemService(Context.WIFI_SERVICE);
            new WifiConfigurationUtils();
            WifiConfiguration localWifiConfiguration = createConfiguration(paramString1, paramString3, getWifiTypeOne(paramString2));
            int i = localWifiManager.addNetwork(localWifiConfiguration);
            localWifiManager.saveConfiguration();
            if (localWifiManager.updateNetwork(localWifiConfiguration) == -1)
                localWifiManager.updateNetwork(localWifiConfiguration);
            localWifiManager.saveConfiguration();
            return localWifiManager.enableNetwork(i, true);
        }
        return false;
    }
    /**
     * 判断wifi的强弱
     * @param paramInt ：范围的大小
     * @return
     */
    public static String isWeakOrStrong(int paramInt){
        if (-1000 == paramInt)
            return "不在范围内";
        if ((paramInt <= 0) && (paramInt >= -50))
            return "强";
        if ((paramInt < -50) && (paramInt >= -70))
            return "较强";
        if ((paramInt < -70) && (paramInt >= -80))
            return "一般";
        if ((paramInt < -80) && (paramInt >= -100))
            return "弱";
        return "弱";
    }

    /**
     * 获取 wifi的类型
     * @param  paramString
     * @return
     */
    public static type getWifiTypeOne(String paramString) {
        String str = paramString.replace("[ESS]", "");
        if (str.contains("WEP"))
            return type.WEP;
        if (str.contains("WPA"))
            return type.WPA;
        return type.NONE;
    }
    /**
     *获取wifi 连接的方式
     * @param paramString
     * @return
     */
    public static String getWifiTypeTwo(String paramString) {
        String str = paramString.replace("[ESS]", "");
        if (str.contains("WEP"))
            return "WEP";
        if (str.contains("WPA"))
            return "WPA";
        return "NONE";
    }

    /**
     * 获取wifi
     * @param type
     * @return
     */
    public static String getWifiTwo(final type type) {
        switch (type.ordinal()) {
            default: {
                return "";
            }
            case 1: {
                return "WEP";
            }
            case 2: {
                return "WPA/WPA2 PSK";
            }
            case 3: {
                return "OPEN";
            }
        }
    }

    public static String getWifiOne(final type type) {
        switch (type.ordinal()) {
            default: {
                return "";
            }
            case 1: {
                return "wep";
            }
            case 2: {
                return "WPA/WPA2 PSK";
            }
            case 3: {
                return "开放";
            }
        }
    }


    /**
     * 获取wifi名称
     * @param ssid
     * @return
     */
    public static String getSSID(String ssid) {
        if (!TextUtils.isEmpty(ssid)) {
            final int length = ssid.length();
            if (length > 1 && ssid.charAt(0) == '\"' && ssid.charAt(length - 1) == '\"') {
                Log.i("tag----?",ssid.substring(1, length - 1));
                return ssid.substring(1, length - 1);
            }
        }
        return ssid;
    }

    /**
     * 网络连接方式：分别是：没有密码，wep加密，wap加密
     */
    public enum type {
        NONE,
        WEP,
        WPA
       // WIFICIPHER_NOPASS,
      //  WIFICIPHER_WEP,
       // WIFICIPHER_WPA
    }
}
