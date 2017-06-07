package com.atguigu.beijingnews_0224.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.baselibrary.Constants;
import com.atguigu.beijingnews_0224.R;
import com.atguigu.beijingnews_0224.activity.PicassoSampleActivity;
import com.atguigu.beijingnews_0224.domain.PhotosMenuDetailPagerBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：田学伟 on 2017/6/6 15:05
 * QQ：93226539
 * 作用：图组的适配器
 */

public class PhotosMenuDetailPagerAdapater extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapater.MyViewHolder> {

    private final Context mContext;
    private final List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> datas;

    public PhotosMenuDetailPagerAdapater(Context mContext, List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(mContext, R.layout.item_photos, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //1.根据位置得到对应的数据
        PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsEntity = datas.get(position);
        //2.绑定数据
        holder.tvTitle.setText(newsEntity.getTitle());
        String imageUrl = Constants.BASE_URL + newsEntity.getListimage();
        Glide.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.news_pic_default)
                .error(R.drawable.news_pic_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivIcon);

        //3.设置点击事件

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String imageUrl = Constants.BASE_URL + datas.get(getLayoutPosition()).getListimage();
//                    Intent intent = new Intent(mContext, PicassoSampleActivity.class);
//                    intent.setData(Uri.parse(imageUrl));
//                    mContext.startActivity(intent);
                    Intent intent = new Intent(mContext, PicassoSampleActivity.class);
                    intent.putExtra("url", Constants.BASE_URL + datas.get(getLayoutPosition()).getListimage());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
