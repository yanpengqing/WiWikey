package com.wiwikeyandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.utils.PrefUtils;

public class Splash_Activity extends Activity {
    private RelativeLayout splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        
        splash = (RelativeLayout) findViewById(R.id.splash);
        //缩放动画
        ScaleAnimation animScale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScale.setDuration(1000);
        animScale.setFillAfter(true);
        //旋转动画
        RotateAnimation animRotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animRotate.setDuration(1000);
        animRotate.setFillAfter(true);
        //渐变动画
        AlphaAnimation animAlpha = new AlphaAnimation(0.2f, 1.0f);
        animAlpha.setDuration(2000);
        animAlpha.setFillAfter(true);
        //动画集合
        AnimationSet set = new AnimationSet(true);
        //set.addAnimation(animScale);
        //set.addAnimation(animRotate);
        set.addAnimation(animAlpha);
        splash.startAnimation(set);
        
        

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean isFirst = PrefUtils.getBoolean(Splash_Activity.this, "isFirst", true);
                boolean isLogin = PrefUtils.getBoolean(Splash_Activity.this, "isLogin", false);
                Intent intent; 
                if (isFirst) {
                    //第一次进入，向导页面
                    intent = new Intent(Splash_Activity.this,
                            GuideActivity.class);
                } else if(isLogin){
                	  intent = new Intent(Splash_Activity.this,
                              HomeAcivity.class);
						
					} else {
                    // 登录页面
                    intent = new Intent(Splash_Activity.this,
                            LoginActivity.class);
                }

                startActivity(intent);

                finish();// 结束当前页面
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
