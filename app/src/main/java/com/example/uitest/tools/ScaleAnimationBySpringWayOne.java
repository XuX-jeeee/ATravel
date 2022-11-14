package com.example.uitest.tools;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class ScaleAnimationBySpringWayOne {
    public static void onScaleAnimationBySpringWayOne(View view){
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view,"scaleX",1.1f,1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view,"scaleY",1.1f,1.0f);
        AnimatorSet set =new AnimatorSet();
        set.setDuration(2000);
        set.setInterpolator(new SpringScaleInterpolator(0.2f));
        set.playTogether(animatorX,animatorY);
        set.start();
    }
    public static void onScaleAnimationBySpringWayOneMiddle(View view){
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view,"scaleX",1.03f,1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view,"scaleY",1.03f,1.0f);
        AnimatorSet set =new AnimatorSet();
        set.setDuration(2000);
        set.setInterpolator(new SpringScaleInterpolator(0.2f));
        set.playTogether(animatorX,animatorY);
        set.start();
    }
}
