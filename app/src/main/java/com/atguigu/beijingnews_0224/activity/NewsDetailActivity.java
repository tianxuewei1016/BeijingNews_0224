package com.atguigu.beijingnews_0224.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.beijingnews_0224.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NewsDetailActivity extends AppCompatActivity {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ib_menu)
    ImageButton ibMenu;
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.ib_textsize)
    ImageButton ibTextsize;
    @InjectView(R.id.ib_share)
    ImageButton ibShare;
    @InjectView(R.id.webview)
    WebView webview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    private Uri url;

    /**
     * 缓存的
     */
    private int tempSize = 2;
    private WebSettings settings;
    /**
     * 真实大小
     */
    private int realSize =  tempSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.inject(this);
        setView();
        //网页地址
        url = getIntent().getData();

        settings = webview.getSettings();
        //设置相关配置
        //设置支持javaScript
        settings.setJavaScriptEnabled(true);
        //设置双击页面变大变小
        settings.setUseWideViewPort(true);

        //添加变大变小按钮
        settings.setBuiltInZoomControls(true);

        //设置加载页面完成的监听
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressbar.setVisibility(View.GONE);
            }
        });
        //加载网页地址
        webview.loadUrl(url.toString());
    }

    private void setView() {
        tvTitle.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.ib_back, R.id.ib_textsize, R.id.ib_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_textsize:
//                Toast.makeText(NewsDetailActivity.this, "设置文字大小", Toast.LENGTH_SHORT).show();
                shouChangeTextSizeDialog();
                break;
            case R.id.ib_share:
                Toast.makeText(NewsDetailActivity.this, "分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void shouChangeTextSizeDialog() {
        String[] items = {"超大字体","大字体","正常字体","小字体","超小字体"};
        new AlertDialog.Builder(this)
                .setTitle("设置文字大小")
                .setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tempSize = which;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realSize = tempSize;
                changeTextSize(realSize);
            }
        }).setNegativeButton("取消", null).show();
    }

    private void changeTextSize(int realSize) {
        switch (realSize) {
            case 0:
                settings.setTextZoom(200);
                break;
            case 1:
                settings.setTextZoom(150);
                break;
            case 2:
                settings.setTextZoom(100);
                break;
            case 3:
                settings.setTextZoom(75);
                break;
            case 4:
                settings.setTextZoom(50);
                break;
        }
    }
}
