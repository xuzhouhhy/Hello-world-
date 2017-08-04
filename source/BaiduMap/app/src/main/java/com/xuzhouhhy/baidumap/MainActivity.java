package com.xuzhouhhy.baidumap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.xuzhouhhy.baidumap.data.Point3DMutable;
import com.xuzhouhhy.baidumap.util.UtilBaidu;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static com.xuzhouhhy.baidumap.util.UtilBaidu.getBitmapDescriptor;
import static com.xuzhouhhy.baidumap.util.UtilBaidu.getClickBitmapDescriptor;

public class MainActivity extends AppCompatActivity {

    private BaiduMapController mController;

    private MapView mMapView;

    private ImageButton mIbtnPackage;
    private ImageButton mIbtnNavigate;
    private ImageButton mIbtnDelete;

    private BaiduMap.OnMarkerClickListener mOnMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Bundle bundle = marker.getExtraInfo();
            String markInfo = bundle.getString("mark_key");
            Toast.makeText(MainActivity.this, markInfo, LENGTH_LONG).show();
            marker.setIcon(getClickBitmapDescriptor());
            for (int i = 0; i < mController.getBaiduBlockMarks().size(); i++) {
                if (i != marker.getZIndex()) {
                    ((Marker) mController.getBaiduBlockMarks().get(i).getPointMark())
                            .setIcon(getBitmapDescriptor());
                    ((Text) mController.getBaiduBlockMarks().get(i).getTitleMark()).setBgColor(
                            getResources().getColor(R.color.navinput));
                } else {
                    ((Text) mController.getBaiduBlockMarks().get(i).getTitleMark()).setBgColor(
                            getResources().getColor(R.color.colorPrimaryDark));
                }
            }
//            marker.remove();
//            OverlayOptions option = new MarkerOptions()
//                    .position(marker.getPosition())
//                    .icon(getClickBitmapDescriptor())
//                    .title("s_12345_01");
//            //在地图上添加Marker，并显示
//            Overlay newmarker = mBaiduMap.addOverlay(option);
//            Bundle newbundle = new Bundle();
//            newbundle.putString("mark_key", "31&121");
//            newmarker.setExtraInfo(newbundle);
//            Bundle newbundle = new Bundle();
//            newbundle.putString("mark_key", "31&121");
//            newmarker.setExtraInfo(newbundle);
            return false;
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnPackage:
                    onShowPackage();
                    break;
                case R.id.btnNavigate:
                    onNavigate();
                    break;
                case R.id.btnDelete:
                    onDelete();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
    }

    private void initview() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mController = new BaiduMapController(mMapView.getMap(), getPoints());
        mIbtnPackage = (ImageButton) findViewById(R.id.btnPackage);
        mIbtnNavigate = (ImageButton) findViewById(R.id.btnNavigate);
        mIbtnDelete = (ImageButton) findViewById(R.id.btnDelete);
        mIbtnPackage.setOnClickListener(mOnClickListener);
        mIbtnNavigate.setOnClickListener(mOnClickListener);
        mIbtnDelete.setOnClickListener(mOnClickListener);
        mController.setOnMarkerClickListener(mOnMarkerClickListener);
        LatLng latLng = UtilBaidu.coorConverter84ToBaidu(new LatLng(31, 121));
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        mController.getBaiduMap().animateMapStatus(update);
        //隐藏缩放按钮
        mMapView.showZoomControls(false);
    }

    private List<Point3DMutable> getPoints() {
        List<Point3DMutable> points = new ArrayList<>();
        points.add(new Point3DMutable(31, 121, 0));
        points.add(new Point3DMutable(31.1, 121.1, 0));
        points.add(new Point3DMutable(31.2, 121.2, 0));
        points.add(new Point3DMutable(30.9, 120.9, 0));
        points.add(new Point3DMutable(30.8, 120.8, 0));
        points.add(new Point3DMutable(33, 120.8, 0));
        return points;
    }

    private void onDelete() {
    }

    private void onNavigate() {

    }

    private void onShowPackage() {

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
    }
}
