package com.milly.pic;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.image_back)
    ImageView image_back;
    @BindView(R.id.tv_preview)
    TextView tv_preview;
    @BindView(R.id.tv_allPic)
    TextView tv_allPic;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;
    @BindView(R.id.rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.view)
    View view;
    //存储每个目录下的图片路径
    private Map<String, List<String>> groupMap = new HashMap<>();
    private List<AllImageBean> list = new ArrayList<>();
    //当前文件夹显示的图片路径
    private List<String> listPath = new ArrayList<>();
    //选择的图片路径集合
    private ArrayList<String> listSelectPath = new ArrayList<String>();
    private SelectImageAdapter adapter;
    private SelectPicAdapter selectPicAdapter;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //获取所有图片
        initViews();
        //设置数据
        initData();
    }


    @OnClick({R.id.tv_preview, R.id.tv_allPic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_preview://预览
                PreviewActivity.newInstance(this, listSelectPath);
                break;
            case R.id.tv_allPic://所有图片
                showPopup();
                break;
        }
    }

    private void showPopup() {
        final View popupView = LayoutInflater.from(this).inflate(R.layout.popup_all_pic, null);
        View view = popupView.findViewById(R.id.view);
        View view2 = popupView.findViewById(R.id.view2);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popDissmiss();
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popDissmiss();
            }
        });
        RecyclerView recycler_view = (RecyclerView) popupView.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, 4, Color.parseColor("#dcdcdc")));
        selectPicAdapter = new SelectPicAdapter(list, this);
        recycler_view.setAdapter(selectPicAdapter);
        popupWindow = new PopupWindow(popupView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(mRecyclerView, Gravity.BOTTOM, 0, dp2px(50, MainActivity.this));
        popupWindow.update();

        selectPicAdapter.setOnItemClickListener(new SelectPicAdapter.OnItemClickListener() {
            @Override
            public void setItemOnClick(String name) {
                //获取点击条目的图片路劲集合
                List<String> selectLists = groupMap.get(name);
                //清空原来的数据
                listSelectPath.clear();
                listPath.clear();
                listPath.addAll(selectLists);
                //更新显示
                adapter.upDate(listPath);
                tv_allPic.setText(name);
                setUnDisable();
                popDissmiss();
            }
        });
    }

    private void popDissmiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    //监听系统的返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        popDissmiss();
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new SelectImageAdapter(this, listPath);
        mRecyclerView.setAdapter(adapter);
        //adapter设置监听图片选择变化的监听
        adapter.setOnCheckedChangedListener(new SelectImageAdapter.OnCheckedChangedListener() {
            @Override
            public void onChanged(boolean isChecked, String path, List<Boolean> flags, int pos) {
                if (isChecked) {
                    //选择
                    if (listSelectPath.size() == 9) {
                        Toast.makeText(MainActivity.this, "最多选择9张", Toast.LENGTH_SHORT).show();
                        flags.set(pos, false);
                        return;
                    }
                    //不够9张时，添加到集合中
                    if (!listSelectPath.contains(path)) {
                        listSelectPath.add(path);
                    }
                } else {
                    //取消选择,从集合中移除
                    if (listSelectPath.contains(path)) {
                        listSelectPath.remove(path);
                    }
                }
                //改变颜色
                if (listSelectPath.size() == 0) {
                    setUnDisable();
                } else {
                    setDisable();
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    //设置预览不可以点击
    private void setUnDisable() {
        tv_confirm.setBackgroundColor(Color.parseColor("#A4A4A4"));
        tv_confirm.setTextColor(Color.parseColor("#676767"));
        tv_confirm.setText("确定");
        tv_confirm.setEnabled(false);

        tv_preview.setEnabled(false);
        tv_preview.setTextColor(Color.parseColor("#bebebe"));
    }

    //设置可以点击
    private void setDisable() {
        tv_confirm.setBackgroundColor(Color.parseColor("#00ff00"));
        tv_confirm.setTextColor(Color.parseColor("#ffffff"));
        tv_confirm.setText("确定 " + listSelectPath.size() + "/9");
        tv_confirm.setEnabled(true);

        tv_preview.setEnabled(true);
        tv_preview.setTextColor(Color.parseColor("#ff0000"));
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            listPath.clear();
            listPath.addAll(groupMap.get("所有图片"));
            adapter.upDate(listPath);

            getGalleryList();
        }
    };

    /**
     * 使用ContentProvider扫描手机助手的图片
     */
    private void initViews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = MainActivity.this.getContentResolver();
                Cursor cursor = contentResolver.query(
                        mImageUri,
                        null,
                        null,
                        null,
                        MediaStore.Images.Media.DATE_MODIFIED);

                if (cursor == null) {
                    return;
                }

                //存放所有图片的路径
                List<String> listAllPic = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    //获取图片的路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    listAllPic.add(path);
                    //获取图片的父路径名称
                    String parentPath = new File(path).getParentFile().getName();

                    if (!groupMap.containsKey(parentPath)) {
                        List<String> childList = new ArrayList<String>();
                        childList.add(path);
                        groupMap.put(parentPath, childList);
                    } else {
                        groupMap.get(parentPath).add(path);
                    }
                }
                //添加所有图片
                groupMap.put("所有图片", listAllPic);
                //通知handler
                mHandler.sendEmptyMessage(0);
                cursor.close();
            }
        }) {
        }.start();
    }

    private void getGalleryList() {
        Iterator<Map.Entry<String, List<String>>> iterator = groupMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> next = iterator.next();
            AllImageBean imageBean = new AllImageBean();
            imageBean.setShowName(next.getKey());
            imageBean.setShowFirstImage(next.getValue().get(0));
            imageBean.setCount(next.getValue().size());
            if (next.getKey().equals("所有图片")) {
                list.add(0, imageBean);
            } else {
                list.add(imageBean);
            }
        }
    }

    /**
     * dp转px
     */
    public int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
