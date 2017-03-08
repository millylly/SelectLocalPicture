package com.milly.pic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.ViewHolder> {
    private Context context;
    private List<String> listPath = new ArrayList<>();
    private OnCheckedChangedListener onCheckedChangedListener;
    private List<Boolean> listChecked = new ArrayList<>();

    public SelectImageAdapter(Context context, List<String> listPath) {
        this.context = context;
        this.listPath.addAll(listPath);
        setListChecked(listPath);
    }

    //更新数据
    public void upDate(List<String> list) {
        this.listPath.clear();
        this.listPath.addAll(list);
        setListChecked(list);
        notifyDataSetChanged();
    }

    //设置checked的初始值
    public void setListChecked(List<String> list) {
        listChecked.clear();
        for (int i = 0; i < list.size(); i++) {
            listChecked.add(false);
        }
    }

    public void setOnCheckedChangedListener(OnCheckedChangedListener onCheckedChangedListener) {
        this.onCheckedChangedListener = onCheckedChangedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_select_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).
                load("file://" + listPath.get(position)).
                into(holder.image);
        if (listChecked.get(position)) {
            holder.image_flag.setBackgroundResource(R.mipmap.image_select);
        } else {
            holder.image_flag.setBackgroundResource(R.mipmap.image_default);
        }

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listChecked.set(position, !listChecked.get(position));
                if (listChecked.get(position)) {
                    listChecked.set(position, true);
                } else {
                    listChecked.set(position, false);
                }
                if (onCheckedChangedListener != null) {
                    onCheckedChangedListener.onChanged(
                            listChecked.get(position),
                            listPath.get(position),
                            listChecked,
                            position
                    );
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPath.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.iv_itemImageSelect)
        ImageView image;
        @BindView(R.id.image_itemImageSelect)
        ImageView image_flag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnCheckedChangedListener {
        /**
         * @param isChecked 是否选中
         * @param path      点击图片的路径
         * @param pos       点击的位置
         */
        void onChanged(boolean isChecked, String path, List<Boolean> flags, int pos);
    }
}
