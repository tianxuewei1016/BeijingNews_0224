package com.atguigu.beijingnews_0224.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.baselibrary.BitmapCacheUtils;
import com.atguigu.baselibrary.Constants;
import com.atguigu.baselibrary.NetCacheUtils;
import com.atguigu.beijingnews_0224.R;
import com.atguigu.beijingnews_0224.activity.PicassoSampleActivity;
import com.atguigu.beijingnews_0224.domain.PhotosMenuDetailPagerBean;

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
    private final RecyclerView recyclerview;
    private BitmapCacheUtils bitmapCacheUtils;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetCacheUtils.SECUSS://图片请求成功
                    //位置
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (recyclerview.isShown()) {
                        ImageView ivIcon = (ImageView) recyclerview.findViewWithTag(position);
                        if (ivIcon != null && bitmap != null) {
                            Log.e("TAG", "网络缓存图片显示成功" + position);
                            ivIcon.setImageBitmap(bitmap);
                        }
                    }
                    break;
                case NetCacheUtils.FAIL://图片请求失败
                    position = msg.arg1;
                    Log.e("TAG", "网络缓存失败" + position);
                    break;
            }
        }
    };

    public PhotosMenuDetailPagerAdapater(Context mContext, List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> datas, RecyclerView recyclerview) {
        this.mContext = mContext;
        this.datas = datas;
        this.recyclerview = recyclerview;
        bitmapCacheUtils = new BitmapCacheUtils(handler);
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
        //使用Glide联网请求图片
//        String url = Constants.BASE_URL + newsEntity.getListimage();
//        Glide.with(mContext)
//                .load(imageUrl)
//                .placeholder(R.drawable.news_pic_default)
//                .error(R.drawable.news_pic_default)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.ivIcon);

        //使用自定义的方式请求图片
        holder.ivIcon.setTag(position);
        Bitmap bitmap = bitmapCacheUtils.getBitmapFromNet(Constants.BASE_URL + newsEntity.getListimage(), position);
        if (bitmap != null) {//内存或者本地
            holder.ivIcon.setImageBitmap(bitmap);
        }

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
                    String imageUrl = Constants.BASE_URL + datas.get(getLayoutPosition()).getListimage();
                    Intent intent = new Intent(mContext, PicassoSampleActivity.class);
                    intent.setData(Uri.parse(imageUrl));
                    mContext.startActivity(intent);
//                    Intent intent = new Intent(mContext, PicassoSampleActivity.class);
//                    intent.putExtra("url", Constants.BASE_URL + datas.get(getLayoutPosition()).getListimage());
//                    mContext.startActivity(intent);
                }
            });
        }
    }
}
