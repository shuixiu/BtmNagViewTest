package com.test.sixpro.ui.home;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.test.sixpro.R;
import com.test.sixpro.base.BaseActivity;
import com.test.sixpro.http.OkhttpManager;
import com.test.sixpro.service.MockGpsService;
import com.test.sixpro.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.test.sixpro.utils.Utils.isNetworkAvailable;

public class LocationChangeActivity extends BaseActivity implements View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MockServiceReceiver mockServiceReceiver;

    @BindView(R.id.bt_start_location)
    Button mBt_start_location;


    public static String latLngInfo = "";
    private boolean isGPSOpen = false;
    private boolean isMockLocOpen = false;
    private boolean isNetworkConnected = true;
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;
    private boolean isMockServStart = false;
    private boolean isServiceRun = false;
    public static final int RunCode = 0x01;
    public static final int StopCode = 0x02;
    boolean isFirstLoc = true; // 是否首次定位

    public static LatLng currentPt = new LatLng(30.547743718042415, 104.07018449827267);
    public static BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location_change);
        ButterKnife.bind(this);
        mMapView = (MapView) findViewById(R.id.bmapView);

        initTitleView();
        title_font.setText("地图");
//        ll_back.setVisibility(View.VISIBLE);
//        ll_back.setOnClickListener(this);

        //网络是否可用
        if (!isNetworkAvailable()) {
            DisplayToast(LocationChangeActivity.this, "网络连接不可用,请检查网络连接设置");
            isNetworkConnected = false;
        }

        registRecevier();
        getPersimmions();
        //开启定位图层
        if (!isGpsOpened()) {
            //如果未打开GPS，跳转到定位设置界面
            showGpsDialog();
        } else {
            isGPSOpen = true;
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //如果GPS定位开启，则打开定位图层
            initMap();
        }
        // 是否开启位置模拟
        isMockLocOpen = isAllowMockLocation();
        //提醒用户开启位置模拟
        if (!isMockLocOpen) {
            setDialog();
        }

        initListener();
        //http init

//        116.29794250579144&40.05140172874632
    }

    @OnClick(R.id.bt_change_location)
    void changeLocation() {


        currentPt = new LatLng(40.0587767999885, 116.31048639417718);
        MapStatusUpdate mapstatusupdate = MapStatusUpdateFactory.newLatLng(currentPt);
        latLngInfo = "116.29794250579144&40.05140172874632";
        //对地图的中心点进行更新
        mBaiduMap.setMapStatus(mapstatusupdate);
        updateMapState();
    }


    @OnClick(R.id.bt_start_location)
    public void getStartLocation(View view) {
        if (!isGPSOpen) {
            //如果GPS未开启
            showGpsDialog();
        } else {
            if (!(isMockLocOpen = isAllowMockLocation())) {
                setDialog();
            } else {
                if (!isMockServStart && !isServiceRun && latLngInfo != null && !latLngInfo.equals("")) {
                    Log.d("DEBUG", "current pt is " + currentPt.longitude + "  " + currentPt.latitude);


//                    LatLng ll = new LatLng(location.getLatitude(),
////                            location.getLongitude());
////                    MapStatus.Builder builder = new MapStatus.Builder();
////                    builder.target(ll).zoom(18.0f);
////                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//
                    //start mock location service
                    Intent mockLocServiceIntent = new Intent(LocationChangeActivity.this, MockGpsService.class);
                    mockLocServiceIntent.putExtra("key", latLngInfo);
                    Log.d("DEBUG", "current pt is " + latLngInfo);
                    updateMapState();


//                    String latLngStr[] = latLngInfo.split("&");
//                    MapStatusUpdate mapstatusupdate = MapStatusUpdateFactory.newLatLng(new LatLng(Double.valueOf(latLngStr[0]), Double.valueOf(latLngStr[1])));
//                    mBaiduMap.setMapStatus(mapstatusupdate);
                    //isFisrtUpdate=false;
                    //save record
//                    updatePositionInfo();
                    //insert end
                    if (Build.VERSION.SDK_INT >= 26) {
                        startForegroundService(mockLocServiceIntent);
                        Log.d("DEBUG", "startForegroundService:MOCK_GPS");
                    } else {
                        startService(mockLocServiceIntent);
                        Log.d("DEBUG", "startService:MOCK_GPS");
                    }
                    isMockServStart = true;
                    Snackbar.make(view, "位置模拟已开启", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    mBt_start_location.setText("停止定位");
                } else {
//                    Snackbar.make(view, "位置模拟已在运行", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                    isMockServStart = true;
                    Intent mockLocServiceIntent = new Intent(LocationChangeActivity.this, MockGpsService.class);
                    stopService(mockLocServiceIntent);
                    Snackbar.make(view, "位置模拟服务终止", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    //service finish
                    isMockServStart = false;
                    //重新定位
                    mLocationClient.stop();
                    mLocationClient.start();
                    mBt_start_location.setText("开始定位");
                }
            }
        }

    }

    //注册广播
    private void registRecevier() {
        try {
            mockServiceReceiver = new MockServiceReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.test.sixpro.service.MockGpsService");
            this.registerReceiver(mockServiceReceiver, filter);
        } catch (Exception e) {
            Log.e("UNKNOWN", "registerReceiver error");
            e.printStackTrace();
        }
    }

    public class MockServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            assert bundle != null;
            int statusCode = bundle.getInt("statusCode");
//            Log.d("DEBUG", statusCode + "");
            if (statusCode == RunCode) {
                isServiceRun = true;
                mBt_start_location.setText("停止定位");

//                currentPt = new LatLng(40.0587767999885, 116.31048639417718);
//                MapStatusUpdate mapstatusupdate = MapStatusUpdateFactory.newLatLng(currentPt);
//                latLngInfo = "116.29794250579144&40.05140172874632";
//                //对地图的中心点进行更新
//                mBaiduMap.setMapStatus(mapstatusupdate);
//                updateMapState();

            } else if (statusCode == StopCode) {
                isServiceRun = false;
                mBt_start_location.setText("开启定位");
            }
        }
    }


    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    //显示开启GPS的提示
    private void showGpsDialog() {
        new AlertDialog.Builder(LocationChangeActivity.this)
                .setTitle("Tips")//这里是表头的内容
                .setMessage("是否开启GPS定位服务?")//这里是中间显示的具体信息
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 0);
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .show();
    }

    //初始化定位
    private void initMap() {
        mBaiduMap = mMapView.getMap();
        //开启地图的定位图层
        mBaiduMap.setMyLocationEnabled(true);
        locationStart();
    }

    //开始定位
    private void locationStart() {

        //定位初始化
        mLocationClient = new LocationClient(this);

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();
    }

    //构造地图数据
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(20.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

        }
    }


    public boolean isAllowMockLocation() {
        boolean canMockPosition = false;
        if (Build.VERSION.SDK_INT <= 22) {//6.0以下
            canMockPosition = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
        } else {
            try {
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);//获得LocationManager引用
                String providerStr = LocationManager.GPS_PROVIDER;
                LocationProvider provider = locationManager.getProvider(providerStr);
                if (provider != null) {
                    locationManager.addTestProvider(
                            provider.getName()
                            , provider.requiresNetwork()
                            , provider.requiresSatellite()
                            , provider.requiresCell()
                            , provider.hasMonetaryCost()
                            , provider.supportsAltitude()
                            , provider.supportsSpeed()
                            , provider.supportsBearing()
                            , provider.getPowerRequirement()
                            , provider.getAccuracy());
                } else {
                    locationManager.addTestProvider(
                            providerStr
                            , true, true, false, false, true, true, true
                            , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
                }
                locationManager.setTestProviderEnabled(providerStr, true);
                locationManager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
                // 模拟位置可用
                canMockPosition = true;
                locationManager.setTestProviderEnabled(providerStr, false);
                locationManager.removeTestProvider(providerStr);
            } catch (SecurityException e) {
                canMockPosition = false;
            }
        }
        return canMockPosition;
    }

    //提醒开启位置模拟的弹框
    private void setDialog() {
        //判断是否开启开发者选项
        new AlertDialog.Builder(this)
                .setTitle("启用位置模拟")//这里是表头的内容
                .setMessage("请在开发者选项->选择模拟位置信息应用中进行设置")//这里是中间显示的具体信息
                .setPositiveButton("设置",//这个string是设置左边按钮的文字
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    DisplayToast(LocationChangeActivity.this, "无法跳转到开发者选项,请先确保您的设备已处于开发者模式");
                                    e.printStackTrace();
                                }
                            }
                        })//setPositiveButton里面的onClick执行的是左边按钮
                .setNegativeButton("取消",//这个string是设置右边按钮的文字
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })//setNegativeButton里面的onClick执行的是右边的按钮的操作
                .show();
    }

    //对地图事件的消息响应
    private void initListener() {
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent event) {
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 单击地图
             */
            public void onMapClick(LatLng point) {
                currentPt = point;
//                DisplayToast("BD09\n[纬度:" + point.latitude + "]\n[经度:" + point.longitude + "]");
                //百度坐标系转wgs坐标系
                transformCoordinate(String.valueOf(point.longitude), String.valueOf(point.latitude));
                updateMapState();
            }

            /**
             * 单击地图中的POI点
             */
            public boolean onMapPoiClick(MapPoi poi) {
                currentPt = poi.getPosition();
//                DisplayToast("BD09\n[维度:" + poi.getPosition().latitude + "]\n[经度:" + poi.getPosition().longitude + "]");
                //百度坐标系转wgs坐标系
                transformCoordinate(String.valueOf(poi.getPosition().longitude), String.valueOf(poi.getPosition().latitude));
                updateMapState();
                return false;
            }
        });
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            /**
             * 长按地图
             */
            public void onMapLongClick(LatLng point) {
                currentPt = point;
//                DisplayToast("BD09\n[维度:" + point.latitude + "]\n[经度:" + point.longitude + "]");
                //百度坐标系转wgs坐标系
                transformCoordinate(String.valueOf(point.longitude), String.valueOf(point.latitude));
                updateMapState();
            }
        });
        mBaiduMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            /**
             * 双击地图
             */
            public void onMapDoubleClick(LatLng point) {
                currentPt = point;
//                DisplayToast("BD09\n[维度:" + point.latitude + "]\n[经度:" + point.longitude + "]");
                //百度坐标系转wgs坐标系
                transformCoordinate(String.valueOf(point.longitude), String.valueOf(point.latitude));
                updateMapState();
            }
        });
        /**
         * 地图状态发生变化
         */
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            public void onMapStatusChangeStart(MapStatus status) {
            }

            @Override
            public void onMapStatusChangeStart(MapStatus status, int reason) {
            }

            public void onMapStatusChangeFinish(MapStatus status) {
            }

            public void onMapStatusChange(MapStatus status) {
            }
        });
    }

    //更新地图状态显示面板
    private void updateMapState() {
        if (currentPt != null) {
            MarkerOptions ooA = new MarkerOptions().position(currentPt).icon(bdA);
            mBaiduMap.clear();
            mBaiduMap.addOverlay(ooA);
        }
    }

    private void transformCoordinate(final String longitude, final String latitude) {


        //参数坐标系：bd09
//        boolean isInCHN=false;
        final double error = 0.00000001;
//        final String mcode = "9D:8B:73:A5:DF:2A:36:3F:84:2D:38:55:BD:0A:57:C5:8F:50:44:69;com.example.mockgps";
        final String mcode = getResources().getString(R.string.safecode);
        final String ak = "KinZnjgjwPhsgEkZ4UvM3QaeNhEUekOm";
        //判断bd09坐标是否在国内
        String mapApiUrl = "http://api.map.baidu.com/geoconv/v1/?";

        String parm = "coords=" + longitude + "," + latitude + "&from=5&to=3&ak=" + ak + "&mcode=" + mcode;
        new OkhttpManager().get(mapApiUrl + parm, new OkhttpManager.HttpCallback() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(String data) {
                dismissLoadingDialog();

                try {
                    JSONObject getRetJson = new JSONObject(data);
                    //如果api接口转换成功
                    if (Integer.valueOf(getRetJson.getString("status")) == 0) {
                        Log.d("HTTP", "call api[bd09_to_gcj02] success");
                        JSONArray coordinateArr = getRetJson.getJSONArray("result");
                        JSONObject coordinate = coordinateArr.getJSONObject(0);
                        String gcj02Longitude = coordinate.getString("x");
                        String gcj02Latitude = coordinate.getString("y");

                        BigDecimal bigDecimalGcj02Longitude = new BigDecimal(Double.valueOf(gcj02Longitude));
                        BigDecimal bigDecimalGcj02Latitude = new BigDecimal(Double.valueOf(gcj02Latitude));

                        BigDecimal bigDecimalBd09Longitude = new BigDecimal(Double.valueOf(longitude));
                        BigDecimal bigDecimalBd09Latitude = new BigDecimal(Double.valueOf(latitude));

                        double gcj02LongitudeDouble = bigDecimalGcj02Longitude.setScale(9, BigDecimal.ROUND_HALF_UP).doubleValue();
                        double gcj02LatitudeDouble = bigDecimalGcj02Latitude.setScale(9, BigDecimal.ROUND_HALF_UP).doubleValue();
                        double bd09LongitudeDouble = bigDecimalBd09Longitude.setScale(9, BigDecimal.ROUND_HALF_UP).doubleValue();
                        double bd09LatitudeDouble = bigDecimalBd09Latitude.setScale(9, BigDecimal.ROUND_HALF_UP).doubleValue();
                        //如果bd09转gcj02 结果误差很小  认为该坐标在国外
                        if ((Math.abs(gcj02LongitudeDouble - bd09LongitudeDouble)) <= error && (Math.abs(gcj02LatitudeDouble - bd09LatitudeDouble)) <= error) {
                            //不进行坐标转换
                            latLngInfo = longitude + "&" + latitude;
                            Log.d("DEBUG", "OUT OF CHN, NO NEED TO TRANSFORM COORDINATE");
//                                    DisplayToast("OUT OF CHN, NO NEED TO TRANSFORM COORDINATE");
                        } else {
                            //离线转换坐标系
//                                    double latLng[] = Utils.bd2wgs(Double.valueOf(longitude), Double.valueOf(latitude));
                            double latLng[] = Utils.gcj02towgs84(Double.valueOf(gcj02Longitude), Double.valueOf(gcj02Latitude));
                            latLngInfo = latLng[0] + "&" + latLng[1];
                            Log.d("DEBUG", "IN CHN, NEED TO TRANSFORM COORDINATE");
//                                    DisplayToast("IN CHN, NEED TO TRANSFORM COORDINATE");
                        }
                    }
                    //api接口转换失败 认为在国内
                    else {
                        //离线转换坐标系
                        double latLng[] = Utils.bd2wgs(Double.valueOf(longitude), Double.valueOf(latitude));
                        latLngInfo = latLng[0] + "&" + latLng[1];
                        Log.d("DEBUG", "IN CHN, NEED TO TRANSFORM COORDINATE");
//                                DisplayToast("BD Map Api Return not Zero, ASSUME IN CHN, NEED TO TRANSFORM COORDINATE");
                    }

                } catch (JSONException e) {
                    Log.e("JSON", "resolve json error");
                    e.printStackTrace();
                    //离线转换坐标系
                    double latLng[] = Utils.bd2wgs(Double.valueOf(longitude), Double.valueOf(latitude));
                    latLngInfo = latLng[0] + "&" + latLng[1];
                    Log.d("DEBUG", "IN CHN, NEED TO TRANSFORM COORDINATE");
//                            DisplayToast("Resolve JSON Error, ASSUME IN CHN, NEED TO TRANSFORM COORDINATE");
                }

            }

            @Override
            public void onError(String msg) {
                super.onError(msg);
                dismissLoadingDialog();

                //http 请求失败
                Log.e("HTTP", "HTTP GET FAILED");
                //离线转换坐标系
                double latLng[] = Utils.bd2wgs(Double.valueOf(longitude), Double.valueOf(latitude));
                latLngInfo = latLng[0] + "&" + latLng[1];
                Log.d("DEBUG", "IN CHN, NEED TO TRANSFORM COORDINATE");
//                DisplayToast("HTTP Get Failed, ASSUME IN CHN, NEED TO TRANSFORM COORDINATE");
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        // 退出时销毁定位
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.ll_back:
//                this.finish();
//                break;

            default:
                break;

        }
    }
}
