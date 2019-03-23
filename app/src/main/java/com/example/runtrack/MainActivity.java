package com.example.runtrack;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.TimeUnit;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.maps2d.model.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity //implements LocationSource, AMapLocationListener
{
    private MapView mapView = null;
    private AMap aMap = null;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    private AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private LocationSource.OnLocationChangedListener mListener = null;
    private AMapLocation privLocation = null;
    //跑步距离
    private double distance = 0.0;
    //判断状态
    private boolean isRun = false;
    private Timer timer = new Timer();
    private int time = 0;
    private Handler mHandler = new Handler();

    private TextView tv_Time;
    private TextView tv_Distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.amap);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setMapLanguage(AMap.ENGLISH);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        }
        setUpMap();
        tv_Time = (TextView) findViewById(R.id.time);
        tv_Distance = (TextView) findViewById(R.id.dist);
        mHandler.postDelayed(TimerRunnable, 1000);
    }

    public void btnStart(View v){
        Button btnRun = (Button)findViewById(R.id.btnStart);
        if(!isRun){
            isRun = true;
            btnRun.setBackgroundResource(R.drawable.btn_circle_pressed);
            btnRun.setText("Stop"); btnRun.setTextColor(Color.parseColor("#ffffff"));
            timer.setStart(true);
        }
        else {
            isRun = false;
            btnRun.setBackgroundResource(R.drawable.btn_circle);
            btnRun.setText("Run"); btnRun.setTextColor(Color.parseColor("#254F6E"));
            timer.setStart(false);
        }
    }

    private Runnable TimerRunnable = new Runnable() {
        @Override
        public void run() {
            if(timer.getStateOfTimer()){
                time++;
                tv_Time.setText(timer.secToTime(time));
                DecimalFormat df=new DecimalFormat("00.00");
                tv_Distance.setText(String.valueOf(df.format(distance)));
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    private void setUpMap() {
        /**         * 设置一些amap的属性         */
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);// 设置缩放按钮是否显示
        uiSettings.setScaleControlsEnabled(true);// 设置比例尺是否显示
        uiSettings.setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        /** 自定义系统定位小蓝点         *         */
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 自定义精度范围的圆形边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//圆圈的颜色,设为透明的时候就可以去掉园区区域了

        aMap.setLocationSource(mLocationSource);// 设置定位监听
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    public LocationSource mLocationSource = new LocationSource() {
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            mListener = onLocationChangedListener;
            initAmapLocation();
        }

        @Override
        public void deactivate() {
            mListener = null;
            if (mLocationClient != null) {
                mLocationClient.stopLocation();
                mLocationClient.onDestroy();
            }
            mLocationClient = null;
        }
    };

    private void initAmapLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，设备定位模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.startLocation();
        }
    }

    public AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    mListener.onLocationChanged(amapLocation);// 显示系统小蓝点,不写这一句无法显示到当前位置
                    Log.e("MainActivty", "获取经纬度集合" + amapLocation);//打Log记录点是否正确
                    Log.e("MainActivty", "获取精度信息" + amapLocation.getAccuracy());
                    Log.e("MainActivty", "获取方向角信息" + amapLocation.getBearing());
                    //Toast.makeText(this, "speed"+ amapLocation.getSpeed(), Toast.LENGTH_SHORT).show();//获取速度信息m/s
                    amapLocation.getLocationType();//查看是什么类型的点
                    Log.e("MainActivty", "获取点的类型" + amapLocation.getLocationType());
                    if(isRun)
                        drawLines(amapLocation);//一边定位一边连线
                    Log.e("DDDDDDDDD", String.valueOf(distance));
                    privLocation = amapLocation;
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
                }
            }
        }
    };

    public void drawLines(AMapLocation curLocation) {
        if (null == privLocation) {
            return;
        }
        PolylineOptions options = new PolylineOptions();
        //之前的经纬度和当前的经纬度
        LatLng preLat = new LatLng(privLocation.getLatitude(), privLocation.getLongitude());
        LatLng postLat = new LatLng(curLocation.getLatitude(), curLocation.getLongitude());
        options.add(preLat);
        options.add(postLat);
        options.width(10).geodesic(true).color(Color.GREEN);
        aMap.addPolyline(options);
        //距离的计算
        distance = distance + AMapUtils.calculateLineDistance(preLat, postLat);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mLocationClient){
            mLocationClient.onDestroy();
        }
        mHandler.removeCallbacks(TimerRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
