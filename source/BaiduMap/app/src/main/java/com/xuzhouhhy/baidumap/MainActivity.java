package com.xuzhouhhy.baidumap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.xuzhouhhy.baidumap.app.App;
import com.xuzhouhhy.baidumap.data.Block;
import com.xuzhouhhy.baidumap.data.Point3DMutable;
import com.xuzhouhhy.baidumap.db.NavigatePointManage;
import com.xuzhouhhy.baidumap.util.UtilBaidu;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static com.xuzhouhhy.baidumap.util.UtilBaidu.getBitmapDescriptor;
import static com.xuzhouhhy.baidumap.util.UtilBaidu.getClickBitmapDescriptor;

public class MainActivity extends AppCompatActivity {

    private BaiduMapController mController;

    private MapView mMapView;

    private Dialog mDialog;

    private ViewHolder mViewHolder;

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
                String num = mController.getBaiduBlockMarks().get(i).getBlock().getMarkTitle();
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

    private View.OnClickListener mOnViewClickListener = new View.OnClickListener() {
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

    private DialogInterface.OnClickListener mPositiveClickListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean isLocal = (mViewHolder.mSpinner.getSelectedItemId() == 0);
                    double x;
                    double y;
                    double z;
                    if (isLocal) {
                        x = Double.parseDouble(mViewHolder.mEdtLocalN.getText().toString());
                        y = Double.parseDouble(mViewHolder.mEdtLocalE.getText().toString());
                        z = Double.parseDouble(mViewHolder.mEdtLocalH.getText().toString());
                    } else {
//                        x = Math.toRadians(Double.parseDouble(mViewHolder.mEdtLocalN.getText().toString()));
//                        y = Math.toRadians(Double.parseDouble(mViewHolder.mEdtLocalE.getText().toString()));
//                        z = Math.toRadians(Double.parseDouble(mViewHolder.mEdtLocalH.getText().toString()));
                        x = Double.parseDouble(mViewHolder.mEdtLocalN.getText().toString());
                        y = Double.parseDouble(mViewHolder.mEdtLocalE.getText().toString());
                        z = Double.parseDouble(mViewHolder.mEdtLocalH.getText().toString());
                    }
                    String name = mViewHolder.mEdtName.getText().toString();
                    Point3DMutable point = new Point3DMutable(x, y, z);
                    if (NavigatePointManage.addPoint(isLocal, name, point)) {
                        Toast.makeText(MainActivity.this, "导航点保存成功", LENGTH_LONG).show();
                        mController.addBlock(new Block(point, name, true));
                    } else {
                        Toast.makeText(MainActivity.this, "导航点保存失败", LENGTH_LONG).show();
                    }
                }
            };

    private DialogInterface.OnClickListener mNegativeClickListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismissInputDialog();
                }
            };

    private AdapterView.OnItemSelectedListener mSpinerItemClickLinster =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    onSpinnerItemSelected(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
    }

    private void initview() {
        //set value
        mMapView = (MapView) findViewById(R.id.bmapView);
        mController = new BaiduMapController(mMapView.getMap(), getBlocks());
        ImageButton ibtnPackage = (ImageButton) findViewById(R.id.btnPackage);
        ImageButton ibtnNavigate = (ImageButton) findViewById(R.id.btnNavigate);
        ImageButton ibtnDelete = (ImageButton) findViewById(R.id.btnDelete);
        ImageButton ibtnInput = (ImageButton) findViewById(R.id.btnInput);
        ImageButton ibtnStartBaidu = (ImageButton) findViewById(R.id.btnBaiduMap);
        //set listener
        ibtnPackage.setOnClickListener(mOnViewClickListener);
        ibtnNavigate.setOnClickListener(mOnViewClickListener);
        ibtnDelete.setOnClickListener(mOnViewClickListener);
        ibtnInput.setOnClickListener(mOnViewClickListener);
        ibtnStartBaidu.setOnClickListener(mOnViewClickListener);
        mController.setOnMarkerClickListener(mOnMarkerClickListener);
        //map center
        LatLng latLng = UtilBaidu.coorConverter84ToBaidu(new LatLng(31, 121));
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        mController.getBaiduMap().animateMapStatus(update);
        //隐藏缩放按钮
        mMapView.showZoomControls(false);
    }

    private List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block(new Point3DMutable(31, 121, 0), "S_0123456_01", false));
        blocks.add(new Block(new Point3DMutable(31.1, 121.1, 0), "S_0123456_02", false));
        blocks.add(new Block(new Point3DMutable(31.2, 121.2, 0), "S_0123456_03", false));
        blocks.add(new Block(new Point3DMutable(30.9, 120.9, 0), "S_0123456_04", false));
        blocks.add(new Block(new Point3DMutable(30.8, 120.8, 0), "S_0123456_05", false));
        blocks.add(new Block(new Point3DMutable(33, 120.8, 0), "S_0123456_06", false));
        List<Block> inputBlock = NavigatePointManage.queryAll();
//        blocks.add(new Block(new Point3DMutable(31.3, 121.3, 0), "输入点1", true));
//        blocks.add(new Block(new Point3DMutable(31.4, 121.4, 0), "输入点2", true));
//        blocks.add(new Block(new Point3DMutable(31.5, 121.5, 0), "输入点3", true));
//        blocks.add(new Block(new Point3DMutable(30.6, 120.6, 0), "输入点4", true));
//        blocks.add(new Block(new Point3DMutable(30.5, 120.5, 0), "输入点5", true));
//        blocks.add(new Block(new Point3DMutable(30.4, 120.4, 0), "输入点6", true));
        if (inputBlock != null) {
            blocks.addAll(inputBlock);
        }
        return blocks;
    }

    private void onDelete() {
        if (mController.deleteSelect()) {
            Toast.makeText(App.getInstance(), "删除成功", LENGTH_LONG);
        } else {
            Toast.makeText(App.getInstance(), "删除失败", LENGTH_LONG);
        }
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
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_input_navigation_point, null);
        mViewHolder = new ViewHolder();
        mViewHolder.mEdtName = (EditText) view.findViewById(R.id.edtPointName);
        mViewHolder.mSpinner = (Spinner) view.findViewById(R.id.spPointType);
        mViewHolder.mTvLocalN = (TextView) view.findViewById(R.id.tvLocalN);
        mViewHolder.mTvLocalE = (TextView) view.findViewById(R.id.tvLocalE);
        mViewHolder.mTvLocalH = (TextView) view.findViewById(R.id.tvLocalH);
        mViewHolder.mEdtLocalN = (EditText) view.findViewById(R.id.edtLocalN);
        mViewHolder.mEdtLocalE = (EditText) view.findViewById(R.id.edtLocalE);
        mViewHolder.mEdtLocalH = (EditText) view.findViewById(R.id.edtLocalH);
        mViewHolder.mSpinner.setOnItemSelectedListener(mSpinerItemClickLinster);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view)
                .setMessage("目的地坐标")
                .setPositiveButton("确定", mPositiveClickListener)
                .setNegativeButton("取消", mNegativeClickListener)
                .setCancelable(false);
        mDialog = builder.create();
        mDialog.show();
    }

    private void onSpinnerItemSelected(int position) {
        switch (position) {
            case 0:
                mViewHolder.mTvLocalN.setText("本地北");
                mViewHolder.mTvLocalE.setText("本地东");
                mViewHolder.mTvLocalH.setText("本地高");
                break;
            case 1:
                mViewHolder.mTvLocalN.setText("WGS84 纬度");
                mViewHolder.mTvLocalE.setText("WGS84 经度");
                mViewHolder.mTvLocalH.setText("WGS84 海拔");
                break;
            default:
                break;
        }
    }

    private void dismissInputDialog() {
        if (mDialog != null) {
            mDialog.cancel();
        }
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

    private class ViewHolder {
        EditText mEdtName;
        TextView mTvLocalN;
        TextView mTvLocalE;
        TextView mTvLocalH;
        Spinner mSpinner;
        EditText mEdtLocalN;
        EditText mEdtLocalE;
        EditText mEdtLocalH;
    }
}
