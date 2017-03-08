package com.milly.pic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectPicAdapter extends RecyclerView.Adapter<SelectPicAdapter.ViewHolder> {
    private List<AllImageBean> lists;
    private Context context;

    public SelectPicAdapter(List<AllImageBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AllImageBean allImageBean = lists.get(position);
        int count = allImageBean.getCount();
        String showFirstImage = allImageBean.getShowFirstImage();
        final String showName = allImageBean.getShowName();

        holder.tv_name.setText(showName);
        holder.tv_number.setText(count + "");
        Glide.with(context)
                .load("file://" + showFirstImage)
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.setItemOnClick(showName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_number)
        TextView tv_number;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    interface OnItemClickListener {
        void setItemOnClick(String name);
    }
}
