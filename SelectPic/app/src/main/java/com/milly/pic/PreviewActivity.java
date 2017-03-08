package com.milly.pic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//预览
public class PreviewActivity extends Activity {
    @BindView(R.id.image_back)
    ImageView image_back;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.vp)
    ViewPager mViewPager;
    private ArrayList<String> selectPath;
    private PreviewAdapter previewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
        initData();
    }

    @OnClick({R.id.image_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
        }
    }

    private void initData() {
        selectPath = getIntent().getStringArrayListExtra("select");
        tv_number.setText("1/" + selectPath.size());
        previewAdapter = new PreviewAdapter(this, selectPath);
        mViewPager.setAdapter(previewAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_number.setText((mViewPager.getCurrentItem() + 1) + "/" + selectPath.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        previewAdapter.setOnPhotoSetOnClickListener(new PreviewAdapter.OnPhotoSetOnClickListener() {
            @Override
            public void onClickListener(int position) {
                PreviewActivity.this.finish();
            }
        });
    }


    public static void newInstance(Context context, ArrayList<String> selectList) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putStringArrayListExtra("select", selectList);
        context.startActivity(intent);
    }
}
