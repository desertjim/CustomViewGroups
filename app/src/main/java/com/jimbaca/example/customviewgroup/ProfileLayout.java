package com.jimbaca.example.customviewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by jamesbaca on 5/17/14.
 */
public class ProfileLayout extends ViewGroup{

    ArrayList<View> mChainViews = new ArrayList<View>();

    public ProfileLayout(Context context) {
        super(context);
    }

    public ProfileLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfileLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }*/

    @Override
    protected void onLayout(boolean sizeChanged, int left, int top, int right, int bottom) {
    }

    /*
    @Override // Checks if the exiting layout params for a View are correct for ProfileLayout
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    // if the existing layout params aren't the right type lets see if we can
    // convert the info from one layout param into another
    // we do this by taking the width and height of one and moving it into another
    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {

        return new LayoutParams(p.width, p.height);
    }*/

    public static class LayoutParams extends ViewGroup.LayoutParams{

        public boolean isLayoutPicture = false;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ProfileLayout_LayoutParams);

            isLayoutPicture = a.getBoolean(R.styleable.ProfileLayout_LayoutParams_layout_picture, false);

            a.recycle();
        }
    }
}
