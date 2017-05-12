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
 * 当wifi被连接的时候，此时点击时 弹出的一个wifi信息弹出窗
 */

public class WifiInfomationDialog extends Dialog implements View.OnClickListener{

    private TextView wifi_title;
    private TextView wifi_statue;
    private TextView wifi_strong;
    private TextView wifi_speed;
    private TextView wifi_frency;
    private TextView wifi_caps;
    private Button cancle;
    private Button unSave;

    public WifiInfomationDialog(Context context) {
        this(context,R.style.GuideDefDialog);
    }

    public WifiInfomationDialog(Context context, int themeResId) {
        super(context, R.style.GuideDefDialog);
        this.setContentView(R.layout.wifi_dialog);
        this.wifi_title = (TextView) findViewById(R.id.wifi_title);
        this.wifi_statue = (TextView) findViewById(R.id.wifi_statue);
        this.wifi_strong = (TextView) findViewById(R.id.wifi_strong);
        this.wifi_speed = (TextView) findViewById(R.id.wifi_speed);
        this.wifi_frency = (TextView) findViewById(R.id.wifi_frency);
        this.wifi_caps = (TextView) findViewById(R.id.wifi_caps);
        this.cancle = (Button) findViewById(R.id.wifi_save_cancle);
        this.unSave = (Button) findViewById(R.id.wifi_unSave);
        this.cancle.setOnClickListener(this);
        this.unSave.setOnClickListener(this);
    }

    /**
     * 获取实例的同时给布局填充数据
     * @param localaBean
     * @return
     */
    public WifiInfomationDialog getDialog(LocalaBean localaBean){
        this.wifi_title.setText(localaBean.getSsid());
        this.wifi_statue.setText(localaBean.getTitle());
        this.wifi_strong.setText(WifiConfigurationUtils.isWeakOrStrong(localaBean.getLevel()));
        this.wifi_speed.setText(localaBean.getLinkedSpeed());
        this.wifi_frency.setText(localaBean.getFrequency());
        this.wifi_caps.setText(WifiConfigurationUtils.getWifiOne(WifiConfigurationUtils.getWifiTypeOne(localaBean.getCaps())));
        return this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.wifi_save_cancle: //取消
                if (this.dialogCallBack != null && this.dialogCallBack.cancle(this) && this.isShowing()){
                    this.dismiss();
                }
                break;

            case R.id.wifi_unSave: //不保存
                if (this.dialogCallBack != null && this.dialogCallBack.unSave(this) && this.isShowing()){
                    this.dismiss();
                }
                break;
        }
    }

    /**
     * 弹出窗按钮事件的回调
     */
    private DialogCallBack  dialogCallBack;
    public void setDialogCallBack(DialogCallBack  mDialogCallBack){
        this.dialogCallBack = mDialogCallBack;
    }

    public  interface DialogCallBack {
        public abstract boolean unSave(WifiInfomationDialog paramb);
        public abstract boolean cancle(WifiInfomationDialog paramb);
    }
}
