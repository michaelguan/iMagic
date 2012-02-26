package com.guantao;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.Overlay;

public class BMapActivity extends MapActivity 
{
	private BMapManager mapManager;
	private MapView mapView;
	private MapController mapController;
    private MKSearch mkSearch;
    private MKLocationManager mkLocationManager;
    private LocationListener locationListener;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidumap);
		// 初始化MapActivity
		mapManager = new BMapManager(getApplication());
		// init方法的第一个参数需填入申请的API Key
		mapManager.init("D17D72725746793F37D7D03CFE29CF129575BCEB", new MKGeneralListener()
		{
			public void onGetPermissionState(int arg0)
			{
				Toast.makeText(BMapActivity.this, "验证失败", Toast.LENGTH_LONG).show();
			}
			
			public void onGetNetworkState(int arg0)
			{
				Toast.makeText(BMapActivity.this, "无法连接网络", Toast.LENGTH_LONG).show();
			}
		});
		mapManager.start();
		super.initMapActivity(mapManager);

		mapView = (MapView) findViewById(R.id.map_view);
		// 设置地图模式为交通地图
		//mapView.setTraffic(true);
		// 设置启用内置的缩放控件
		mapView.setBuiltInZoomControls(true);
		mkLocationManager = mapManager.getLocationManager();
		mkSearch  = new MKSearch();
		mkSearch.init(mapManager, new MKSearchListener()
		{
			
			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1)
			{
				// TODO Auto-generated method stub
				
			}
			
			public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1)
			{
				// TODO Auto-generated method stub
				
			}
			
			public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2)
			{
				// TODO Auto-generated method stub
				
			}
			
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1)
			{
				// TODO Auto-generated method stub
				
			}
			
			public void onGetAddrResult(MKAddrInfo arg0, int arg1)
			{
				Toast.makeText(getApplication(), arg0.strAddr, Toast.LENGTH_LONG).show();
			}
		});
		
		GeoPoint fGeoPoint = new GeoPoint((int) (30.292122 * 1E6), (int) (120.170388 * 1E6));
		mkSearch.reverseGeocode(fGeoPoint);
		
		// 创建标记maker
		//Drawable marker = this.getResources().getDrawable(R.drawable.iconmarka);
		// 为maker定义位置和边界
		//marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());

		// 取得地图控制器对象，用于控制MapView
		mapController = mapView.getController();
		// 设置地图的中心
		mapController.setCenter(fGeoPoint);
		// 设置地图默认的缩放级别
		mapController.setZoom(18);
		mapView.getOverlays().add(new WorkOverlay());
		
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, mapView);
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
		mapView.getOverlays().add(myLocationOverlay);
		
		locationListener = new LocationListener()
		{
			public void onLocationChanged(Location arg0)
			{
				// 用给定的经纬度构造一个GeoPoint（纬度，经度）
				GeoPoint point = new GeoPoint((int)(arg0.getLatitude()*1E6), (int)(arg0.getLongitude()*1E6));
				mapController.setCenter(point);
				mkSearch.reverseGeocode(point);
			}
		};
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onDestroy() {
		if (mapManager != null) {
			mapManager.destroy();
			mapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mapManager != null) {
			mkLocationManager.removeUpdates(locationListener);
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mapManager != null) {
			mapManager.start();
			mkLocationManager.requestLocationUpdates(locationListener);
		}
		super.onResume();
	}
	

	}
	class WorkOverlay extends Overlay
	{
		@Override
		public void draw(Canvas canvas,MapView mapView,boolean shadow)
		{
			Point point = mapView.getProjection().toPixels(new GeoPoint((int) (30.292122 * 1E6), (int) (120.170388 * 1E6)), null);
			canvas.drawText("上班的地方", point.x, point.y, new Paint());
		}
}
