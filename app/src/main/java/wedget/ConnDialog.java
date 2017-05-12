package wedget;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jrm.wifidemo.R;

/**
 * Created by jrm on 2017-4-10.
 */

public class ConnDialog extends Dialog {

    private  TextView textView;
    private ImageView img ;
    public ConnDialog(Context paramContext) {
        this(paramContext, R.style.GuideDefDialog);
    }

    public ConnDialog(Context paramContext, int paramInt) {
        super(paramContext, R.style.GuideDefDialog);
        setContentView(R.layout.conn_dialog);
        textView = ((TextView)findViewById(R.id.tv_conn_for_ssid));
        img= ((ImageView)findViewById(R.id.iv_wifi_conn));
        img.setAnimation(getAnimation());
    }

    public static RotateAnimation getAnimation() {
        RotateAnimation localRotateAnimation = new RotateAnimation(0.0F, -3600.0F, 1, 0.5F, 1, 0.5F);
        localRotateAnimation.setInterpolator(new LinearInterpolator());
        localRotateAnimation.setFillAfter(true);
        localRotateAnimation.setDuration(5000L);
        localRotateAnimation.setStartOffset(0L);
        localRotateAnimation.setRepeatCount(1000);
        return localRotateAnimation;
    }

    public ConnDialog getDiaolg(String paramString) {
        textView.setText("正在连接\"" + paramString + "\"....");
        return this;
    }
}
