package com.alizhezi.observablescrollview;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alizhezi.observablescrollview.view.ObservableScrollView;

public class MainActivity extends AppCompatActivity implements OnObservableScrollViewListener{

    private ObservableScrollView mObservableScrollView;
    private TextView mImageView;
    private LinearLayout mHeaderContent;

    private int mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置透明状态栏
        StatusbarUtils.enableTranslucentStatusbar(this);
        setContentView(R.layout.activity_main);


        //初始化控件
        mObservableScrollView = (ObservableScrollView) findViewById(R.id.sv_main_content);
        mImageView = (TextView) findViewById(R.id.tv_main_topContent);
        mHeaderContent = (LinearLayout) findViewById(R.id.ll_header_content);

        //获取标题栏高度
        ViewTreeObserver viewTreeObserver = mImageView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {

                mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mHeight = mImageView.getHeight() - mHeaderContent.getHeight();//这里取的高度应该为图片的高度-标题栏
                //注册滑动监听
                mObservableScrollView.setOnObservableScrollViewListener(MainActivity.this);
            }
        });


    }

    @Override
    public void onObservableScrollViewListener(int l, int t, int oldl, int oldt) {

        if (t <= 0) {
            //顶部图处于最顶部，标题栏透明
            mHeaderContent.setBackgroundColor(Color.argb(0, 48, 63, 159));
        } else if (t > 0 && t < mHeight) {
            //滑动过程中，渐变
            float scale = (float) t / mHeight;//算出滑动距离比例
            float alpha = (255 * scale);//得到透明度
            mHeaderContent.setBackgroundColor(Color.argb((int) alpha, 48, 63, 159));
        } else {
            //过顶部图区域，标题栏定色
            mHeaderContent.setBackgroundColor(Color.argb(255, 48, 63, 159));
        }
    }
}
