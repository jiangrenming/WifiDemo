package wedget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jrm.wifidemo.LocalaBean;
import com.example.jrm.wifidemo.R;

import utils.WifiConfigurationUtils;

/**
 * Created by jrm on 2017-4-7.
 * 保存，连接wifi的弹出窗
 */

public class WifiInformationSaveDialog extends Dialog implements View.OnClickListener{


    private TextView wifi_save_title;
    private TextView wifi_save_strong;
    private TextView wifi_save_caps;
    private Button wifi_save_cancle;
    private Button wifi_save_unSave;
    private Button wifi_save_wlan;

    public WifiInformationSaveDialog(Context context) {
        this(context,R.style.GuideDefDialog);
    }

    public WifiInformationSaveDialog(Context context, int themeResId) {
        super(context, R.style.GuideDefDialog);
        this.setContentView(R.layout.wifi_save_dialog);
        this.wifi_save_title = (TextView) findViewById(R.id.wifi_save_ssid);
        this.wifi_save_strong = (TextView)findViewById(R.id.wifi_save_strong);
        this.wifi_save_caps = (TextView) findViewById(R.id.wifi_save_cap);
        this.wifi_save_cancle =(Button)findViewById(R.id.wifi_save_cancle);
        this.wifi_save_unSave = (Button)findViewById(R.id.wifi_save_unSave);
        this.wifi_save_wlan = (Button)findViewById(R.id.wifi_save_wlan);
        this.wifi_save_cancle.setOnClickListener(this);
        this.wifi_save_unSave.setOnClickListener(this);
        this.wifi_save_wlan.setOnClickListener(this);

    }

    public WifiInformationSaveDialog getSaveDialog(LocalaBean localaBean){
        this.wifi_save_title.setText(localaBean.getSsid());
        this.wifi_save_strong.setText(WifiConfigurationUtils.isWeakOrStrong(localaBean.getLevel()));
        this.wifi_save_caps.setText(WifiConfigurationUtils.getWifiOne(WifiConfigurationUtils.getWifiTypeOne(localaBean.getCaps())));
        return  this;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wifi_save_cancle:
                if (this.callBack != null && this.callBack.save_Cancle_Dialog(this) && this.isShowing()){
                    this.dismiss();
                }
                break;

            case R.id.wifi_save_unSave:
                if (this.callBack != null && this.callBack.save_unSave_Dialog(this) && this.isShowing()){
                    this.dismiss();
                }
                break;

            case R.id.wifi_save_wlan:
                if (this.callBack != null && this.callBack.save_wlan_Dialog(this) && this.isShowing()){
                    this.dismiss();
                }
                break;
        }
    }

    /**
     * 三个按钮的事件回调
     */
    private SaveDialogCallBack callBack;
    public void setSaveDialogCallBack(SaveDialogCallBack mCallBack){
        this.callBack = mCallBack;
    }
    public interface SaveDialogCallBack{
        boolean save_Cancle_Dialog(WifiInformationSaveDialog dialog);
        boolean save_unSave_Dialog(WifiInformationSaveDialog dialog);
        boolean save_wlan_Dialog(WifiInformationSaveDialog dialog);
    }
}
