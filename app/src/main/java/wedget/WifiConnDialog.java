package wedget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.example.jrm.wifidemo.R;

/**
 * Created by jrm on 2017-4-10.
 */

public class WifiConnDialog extends Dialog implements View.OnClickListener{
    private Button confirm;

    public WifiConnDialog(Context paramContext) {
        this(paramContext, R.style.GuideDefDialog);
    }

    public WifiConnDialog(Context paramContext, int paramInt) {
        super(paramContext, R.style.GuideDefDialog);
        setContentView(R.layout.conn_fail_item);
        confirm = ((Button)findViewById(R.id.btn_confirm));
        confirm.setOnClickListener(this);
    }

    public void setWifiConnFailure(WifiConnFailure failure) {
        this.failure= failure;
    }

    public void onClick(View paramView) {
        if ((paramView.getId() == R.id.btn_confirm) && (failure != null) && (failure.faliure(this)) && (isShowing()))
            dismiss();
    }

    private WifiConnFailure failure;
    public   interface WifiConnFailure {
        boolean faliure(WifiConnDialog paramc);
    }
}
