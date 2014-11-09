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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        View pictureView = null;
        for(int i=0; i < childCount; i++){
            View childView = getChildAt(i);
            LayoutParams childParams = (LayoutParams)childView.getLayoutParams();

            if(childParams.isLayoutPicture) {

                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                pictureView = childView;

            }else if(childParams.isLayoutFrame) {

                int frameWidth = pictureView.getMeasuredWidth();
                int frameHeight = pictureView.getMeasuredHeight();
                int measureSpecWidth = MeasureSpec.makeMeasureSpec(frameWidth, MeasureSpec.EXACTLY);
                int measureSpecHeight = MeasureSpec.makeMeasureSpec(frameHeight, MeasureSpec.EXACTLY);
                measureChild(childView, measureSpecWidth, measureSpecHeight);
                
            }else{
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            }
        }

        // Note this size is being set by what is passed in, not what ProfileLayout is determining what it needs...
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean sizeChanged, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int heightTally=0;
        View pictureView = null;

        for(int i=0; i < childCount; i++){
            View childView = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams)childView.getLayoutParams();
            if(layoutParams.isLayoutPicture){
                int leftOffset = (getMeasuredWidth() - childView.getMeasuredWidth())/2;
                childView.layout(leftOffset, heightTally, leftOffset + childView.getMeasuredWidth(), heightTally+childView.getMeasuredHeight());
                heightTally += childView.getMeasuredHeight();

                pictureView = childView;
            }else if(layoutParams.isLayoutFrame) {
                childView.layout(pictureView.getLeft(), pictureView.getTop(), pictureView.getRight(), pictureView.getBottom());

            }else {


                //Note that at this point you will likely notice something is wrong, what is it?  Hint measureChild doesn't always do what you want
                childView.layout(0, heightTally, childView.getMeasuredWidth(), heightTally + childView.getMeasuredHeight());
                heightTally += childView.getMeasuredHeight();
            }
        }
    }


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
    }

    public static class LayoutParams extends ViewGroup.LayoutParams{

        public boolean isLayoutPicture = false;
        public boolean isLayoutFrame = false;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ProfileLayout_LayoutParams);

            isLayoutPicture = a.getBoolean(R.styleable.ProfileLayout_LayoutParams_layout_picture, false);
            isLayoutFrame = a.getBoolean(R.styleable.ProfileLayout_LayoutParams_layout_frame, false);

            a.recycle();
        }

        // used when creating via code
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

    }
}
