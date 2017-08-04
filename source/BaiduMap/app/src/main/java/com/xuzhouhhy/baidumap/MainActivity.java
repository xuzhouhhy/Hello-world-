package com.xuzhouhhy.baidumap;

import android.content.Intent;
import android.net.Uri;
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
import com.xuzhouhhy.baidumap.data.Block;
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
    private ImageButton mIbtnInput;
    private ImageButton mIbtnStartBaidu;

    private BaiduMap.OnMarkerClickListener mOnMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Bundle bundle = marker.getExtraInfo();
            String clickBlockNum = bundle.getString("mark_key");
            if (clickBlockNum == null || clickBlockNum.isEmpty()) {
                return false;
            }
            mController.setBlockNumber(clickBlockNum);
            Toast.makeText(MainActivity.this, clickBlockNum, LENGTH_LONG).show();
            marker.setIcon(getClickBitmapDescriptor());
            for (int i = 0; i < mController.getBaiduBlockMarks().size(); i++) {
                String num = mController.getBaiduBlockMarks().get(i).getBlock().getBlockNumber();
                if (clickBlockNum.equalsIgnoreCase(num)) {
                    ((Text) mController.getBaiduBlockMarks().get(i).getTitleMark()).setBgColor(
                            getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    ((Marker) mController.getBaiduBlockMarks().get(i).getPointMark())
                            .setIcon(getBitmapDescriptor());
                    ((Text) mController.getBaiduBlockMarks().get(i).getTitleMark()).setBgColor(
                            getResources().getColor(R.color.navinput));
                }
            }
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
                case R.id.btnInput:
                    onInput();
                    break;
                case R.id.btnBaiduMap:
                    onStartBaidu();
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
        mController = new BaiduMapController(mMapView.getMap(), getBlocks());
        mIbtnPackage = (ImageButton) findViewById(R.id.btnPackage);
        mIbtnNavigate = (ImageButton) findViewById(R.id.btnNavigate);
        mIbtnDelete = (ImageButton) findViewById(R.id.btnDelete);
        mIbtnInput = (ImageButton) findViewById(R.id.btnInput);
        mIbtnStartBaidu = (ImageButton) findViewById(R.id.btnBaiduMap);
        mIbtnPackage.setOnClickListener(mOnClickListener);
        mIbtnNavigate.setOnClickListener(mOnClickListener);
        mIbtnDelete.setOnClickListener(mOnClickListener);
        mIbtnInput.setOnClickListener(mOnClickListener);
        mIbtnStartBaidu.setOnClickListener(mOnClickListener);
        mController.setOnMarkerClickListener(mOnMarkerClickListener);
        LatLng latLng = UtilBaidu.coorConverter84ToBaidu(new LatLng(31, 121));
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        mController.getBaiduMap().animateMapStatus(update);
        //隐藏缩放按钮
        mMapView.showZoomControls(false);
    }

    private List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block(new Point3DMutable(31, 121, 0), "S_0123456_01"));
        blocks.add(new Block(new Point3DMutable(31.1, 121.1, 0), "S_0123456_02"));
        blocks.add(new Block(new Point3DMutable(31.2, 121.2, 0), "S_0123456_03"));
        blocks.add(new Block(new Point3DMutable(30.9, 120.9, 0), "S_0123456_04"));
        blocks.add(new Block(new Point3DMutable(30.8, 120.8, 0), "S_0123456_05"));
        blocks.add(new Block(new Point3DMutable(33, 120.8, 0), "S_0123456_06"));
        return blocks;
    }

    private void onDelete() {
        mController.deleteSelect();
    }

    private void onNavigate() {
        //打开百度地图导航intent
        Intent intent = mController.getNavigationIntent();
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void onShowPackage() {

    }

    private void onStartBaidu() {
        // TODO: 2017/8/4 show confirm dialog
        Intent i1 = new Intent();
        i1.setData(Uri.parse("baidumap://map?"));
        startActivity(i1);
    }

    private void onInput() {

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
