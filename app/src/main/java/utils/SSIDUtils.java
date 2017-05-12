package utils;

/**
 * Created by jrm on 2017-4-7.
 */

public class SSIDUtils {
    public static boolean isExitSSID(final String ssid) {
        return ssid == null || ssid.trim().length() == 0;
    }

    public static boolean isLength(final String s, final String... array) {
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                if (s.equals(array[i])) {
                    return true;
                }
            }
        }
        return false;
    }
}
