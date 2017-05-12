package com.example.jrm.wifidemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jrm on 2017-4-7.
 */

public class WifiAdapter extends BaseAdapter{

    private Context mContext;
    private List<LocalaBean> mWifi;
    private LayoutInflater mInflater;
    private boolean wifiOpen = true;//设置开关的状态
    private boolean wifiClose = false; //
    private myWifiCallBack myWifiCallBack;

    public WifiAdapter(Context context, List<LocalaBean> wifi,boolean paramBoolean){
        this.mContext = context;
        this.mWifi = wifi;
        this.wifiClose = paramBoolean;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setListLocalBean(final List<LocalaBean> local){
        this.mWifi = local;
    }

    public void setMyWifiCallBack(myWifiCallBack myWifiCallBack){
        this.myWifiCallBack = myWifiCallBack;
    }

    public void setOpenWifi(boolean wifiOpen){
        this.wifiOpen = wifiOpen;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if (this.mWifi == null)
            return 0;
      //  return 1 + this.mWifi.size();
        return this.mWifi.size();
    }

    @Override
    public LocalaBean getItem(int position) {
        /*if (position == 0) {
            return null;
        }*/
       // return this.mWifi.get(position - 1);
        return this.mWifi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       /* if (position == 0){ // 代表没有wifi,wifi开关关闭的状态下的布局
              WifiColseViewHolder holder = new WifiColseViewHolder();
              View view = mInflater.inflate(R.layout.wifi_close_item,null);
              holder.aSwitch = (Switch) view.findViewById(R.id.switch_close);
              holder.aSwitch .setOnCheckedChangeListener(null);
              holder.aSwitch.setChecked(this.wifiClose);
              holder.aSwitch.setEnabled(this.wifiOpen);
              holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                  @Override
                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                      wifiClose = isChecked;
                      if (wifiOpen && myWifiCallBack != null){ //打开开关并且存在wifi热点时
                            myWifiCallBack.isOpenWifi(isChecked);
                      }
                  }
              });
            return  view;
        }*/
        final LocalaBean localaBean = mWifi.get(position);
        ViewHolder holder = new ViewHolder();
        //此时加载带有wifi的布局
        View infalter = mInflater.inflate(R.layout.wifi_item,null);
        holder.ssidText = (TextView) infalter.findViewById(R.id.ssidName);
        holder.imgeView = (ImageView) infalter.findViewById(R.id.wifi_img);
        holder.title = (TextView) infalter.findViewById(R.id.wifi_title);
        holder.ssidText.setText(localaBean.getSsid());
        holder.title.setText(localaBean.getTitle());
        infalter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int mPosition = position;
                if (myWifiCallBack != null){
                    myWifiCallBack.openPositionWifi(mPosition,localaBean);
                }
            }
        });
       final  int level = localaBean.getLevel();
        if (level <= 0 && level >= -50) {
            if (localaBean.isRead()) { //自动连接的话，显示连接的状态
                holder.imgeView.setBackgroundResource(R.mipmap.ic_hw_wlan_status_open_0);
                return infalter;
            }
            //否则显示图片为加密的状态
            holder.imgeView.setBackgroundResource(R.mipmap.ic_hw_wlan_status_dense_0);
            return infalter;
        } else if (level < -50 && level >= -70) {
            if (localaBean.isRead()) {
                holder.imgeView.setBackgroundResource(R.mipmap.ic_hw_wlan_status_open_1);
                return infalter;
            }
            holder.imgeView.setBackgroundResource(R.mipmap.ic_hw_wlan_status_dense_1);
            return infalter;
        } else if (level < -70 && level >= -80) {
            if (localaBean.isRead()) {
                holder.imgeView.setBackgroundResource(R.mipmap.ic_hw_wlan_status_open_2);
                return infalter;
            }
            holder.imgeView.setBackgroundResource(R.mipmap.ic_hw_wlan_status_dense_2);
            return infalter;
        } else if (level < -80 && level >= -100) {
            if (localaBean.isRead()) {
                holder.imgeView.setBackgroundResource(R.mipmap.ic_hw_wlan_status_open_3);
                return infalter;
            }
            holder.imgeView.setBackgroundResource(R.mipmap.ic_hw_wlan_status_dense_3);
            return infalter;
        } else {
            if (localaBean.isRead()) {
                holder.imgeView.setBackgroundResource(R.mipmap.ic_hw_wlan_status_open_4);
                return infalter;
            }
            holder.imgeView.setBackgroundResource(R.mipmap.ic_hw_wlan_status_dense_4);
            return infalter;
        }
    }

    //打开开关并且有wifi时的布局
    public class ViewHolder{
        private TextView ssidText;
        private TextView title;
        private ImageView imgeView;
    }

    /*//关闭开关时的布局
    public class WifiColseViewHolder{
        private Switch aSwitch;
    }*/

    /**
     * 打开关闭开关所需的接口回调
     */
    public interface myWifiCallBack{
        void openPositionWifi(int position,LocalaBean localBean);
       // void isOpenWifi(boolean isOpen);
    }
}
