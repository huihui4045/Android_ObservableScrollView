package com.alizhezi.observablescrollview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alizhezi.observablescrollview.view.ObservableScrollView;

public class MainActivity extends AppCompatActivity implements OnObservableScrollViewListener{

    private ObservableScrollView mObservableScrollView;
    private ImageView mImageView;
    private LinearLayout mHeaderContent;

    private int mHeight;

    private LinearLayout mlyBottom;

    int touchSlop = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置透明状态栏
        StatusbarUtils.enableTranslucentStatusbar(this);
        setContentView(R.layout.activity_main);

        touchSlop= ViewConfiguration.get(this).getScaledTouchSlop();


        //初始化控件
        mObservableScrollView =  findViewById(R.id.sv_main_content);
        mImageView =  findViewById(R.id.tv_main_topContent);
        mHeaderContent =  findViewById(R.id.ll_header_content);

        mlyBottom=findViewById(R.id.ly_bottom);


        mObservableScrollView.setOnObservableScrollViewListener(MainActivity.this);

        /*//获取标题栏高度
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
        });*/


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){

            mHeight=mImageView.getHeight();

            Log.e("MainActivity","===============");
        }
    }

    AnimatorSet hideAnimatorSet=new AnimatorSet();//这是隐藏头尾元素使用的动画

    AnimatorSet showAnimatorSet=new AnimatorSet();//显示底部元素的动画



    private void showBottom(){


        if (hideAnimatorSet!=null&&hideAnimatorSet.isRunning()){

            hideAnimatorSet.cancel();
        }

        if (showAnimatorSet!=null &&showAnimatorSet.isRunning()){

            return;
        }else {

            ObjectAnimator bottomTranslationAnimator = ObjectAnimator.ofFloat(mlyBottom, "translationY", mlyBottom.getTranslationY(), mHeight);



            ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(mlyBottom, "alpha", mlyBottom.getAlpha(), 0f);


            showAnimatorSet.playTogether(bottomTranslationAnimator,bottomAnimator);
            showAnimatorSet.setDuration(300);

            showAnimatorSet.start();
        }

    }

    /****
     * 隐藏动画
     */
    private void hideBottom(){

        if (showAnimatorSet!=null &&showAnimatorSet.isRunning()){

            showAnimatorSet.cancel();
        }

        if (hideAnimatorSet!=null && hideAnimatorSet.isRunning()){

            return;
        }else {

            ObjectAnimator bottomTranslationAnimator = ObjectAnimator.ofFloat(mlyBottom, "translationY", mlyBottom.getTranslationY(), 0);



           ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(mlyBottom, "alpha", mlyBottom.getAlpha(), 1f);


            hideAnimatorSet.playTogether(bottomTranslationAnimator,bottomAnimator);

            hideAnimatorSet.setDuration(300);

            hideAnimatorSet.start();
        }
    }

    @Override
    public void onObservableScrollViewListener(int l, int t, int oldl, int oldt) {



        if (oldt>t&&oldt-t>touchSlop){

            Log.e("MainActivity:","向上滑动");
            hideBottom();

        }else if (oldt<t&& t-oldt>touchSlop){

            Log.e("MainActivity:","向下滑动");
            showBottom();
        }




        //Log.e("MainActivity:",""+t);

        if (t <= 0) {
            //顶部图处于最顶部，标题栏透明   181,180,51
            mHeaderContent.setBackgroundColor(Color.argb(0, 48, 63, 159));
           // mImageView.setBackgroundColor(Color.argb(255,181,180,51));
        } else if (t > 0 && t < mHeight) {
            //滑动过程中，渐变
            float scale = (float) t / mHeight;//算出滑动距离比例
            float alpha = (255 * scale);//得到透明度
            mHeaderContent.setBackgroundColor(Color.argb((int) alpha, 48, 63, 159));


            float alphax=(255/scale);

           // mImageView.setBackgroundColor(Color.argb((int) alphax, 181, 180, 51));


        } else {
            //过顶部图区域，标题栏定色
            mHeaderContent.setBackgroundColor(Color.argb(255, 48, 63, 159));

           // mImageView.setBackgroundColor(Color.argb(0,181,180,51));
        }
    }
}
