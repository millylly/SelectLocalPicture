package com.milly.pic;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PreviewAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> selectPath = new ArrayList<>();

    public PreviewAdapter(Context context, ArrayList<String> selectPath) {
        this.context = context;
        this.selectPath.clear();
        this.selectPath.addAll(selectPath);
    }

    @Override
    public int getCount() {
        return selectPath.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        PhotoView photoView = new PhotoView(context);
        Glide.with(context)
                .load("file://" + selectPath.get(position))
                .into(photoView);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (onPhotoSetOnClickListener != null) {
                    onPhotoSetOnClickListener.onClickListener(position);
                }
            }
        });
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private OnPhotoSetOnClickListener onPhotoSetOnClickListener;

    public void setOnPhotoSetOnClickListener(OnPhotoSetOnClickListener onPhotoSetOnClickListener) {
        this.onPhotoSetOnClickListener = onPhotoSetOnClickListener;
    }

    interface OnPhotoSetOnClickListener {
        void onClickListener(int position);
    }
}
