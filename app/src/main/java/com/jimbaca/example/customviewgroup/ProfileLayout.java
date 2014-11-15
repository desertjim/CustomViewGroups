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

                int frameWidth = pictureView.getMeasuredWidth() + childParams.paddingLeft + childParams.paddingRight;
                int frameHeight = pictureView.getMeasuredHeight() + childParams.paddingTop + childParams.paddingBottom;
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
        View frameView = null;
        LayoutParams frameViewParams = null;

        for(int i=0; i < childCount; i++){
            View childView = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams)childView.getLayoutParams();
            if(layoutParams.isLayoutPicture){
                int leftOffset = (getMeasuredWidth() - childView.getMeasuredWidth())/2;
                int yOffset = heightTally + layoutParams.paddingTop;
                childView.layout(leftOffset, yOffset, leftOffset + childView.getMeasuredWidth(), yOffset+childView.getMeasuredHeight());
                heightTally += layoutParams.paddingTop + childView.getMeasuredHeight();

                pictureView = childView;
            }else if(layoutParams.isLayoutFrame) {

                int frameLeft = (getMeasuredWidth() - childView.getMeasuredWidth())/2;
                int frameRight = frameLeft + childView.getMeasuredWidth();
                int yOffset = 0;

                childView.layout(frameLeft, yOffset, frameRight, yOffset + childView.getMeasuredHeight());
                frameView = childView;
                frameViewParams = layoutParams;

            }else if(LayoutParams.FramePlacement.TOP_LEFT == layoutParams.framePlacement){

                int crownLeft = frameView.getLeft()+layoutParams.paddingLeft;
                int frameTop = frameView.getTop()+layoutParams.paddingTop;
                childView.layout(crownLeft, frameTop, crownLeft+childView.getMeasuredWidth(), frameTop + childView.getMeasuredHeight());

            }else if(LayoutParams.FramePlacement.TOP_RIGHT == layoutParams.framePlacement) {

                int frameLeft = frameView.getRight() - childView.getMeasuredWidth() - layoutParams.paddingRight;
                int frameTop = frameView.getTop()+layoutParams.paddingTop;
                childView.layout(frameLeft, frameTop, frameLeft+childView.getMeasuredWidth(), frameTop + childView.getMeasuredHeight());

            }else if(LayoutParams.FramePlacement.BOTTOM_CENTER == layoutParams.framePlacement){

                int nameLeft = (getMeasuredWidth() - childView.getMeasuredWidth())/2;
                int nameTop = frameView.getBottom() - frameViewParams.paddingBottom - layoutParams.paddingTop;
                childView.layout(nameLeft, nameTop, nameLeft + childView.getMeasuredWidth(), nameTop + childView.getMeasuredHeight());

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
        public int paddingLeft = 0;
        public int paddingTop = 0;
        public int paddingRight = 0;
        public int paddingBottom = 0;
        public int framePlacement = FramePlacement.NONE;


        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ProfileLayout_LayoutParams);

            isLayoutPicture = a.getBoolean(R.styleable.ProfileLayout_LayoutParams_layout_picture, false);
            isLayoutFrame = a.getBoolean(R.styleable.ProfileLayout_LayoutParams_layout_frame, false);

            paddingLeft = a.getDimensionPixelSize(R.styleable.ProfileLayout_LayoutParams_paddingLeft, 0);
            paddingTop = a.getDimensionPixelSize(R.styleable.ProfileLayout_LayoutParams_paddingTop, 0);
            paddingRight = a.getDimensionPixelSize(R.styleable.ProfileLayout_LayoutParams_paddingRight, 0);
            paddingBottom = a.getDimensionPixelSize(R.styleable.ProfileLayout_LayoutParams_paddingBottom, 0);

            framePlacement = a.getInt(R.styleable.ProfileLayout_LayoutParams_framePlacement, FramePlacement.NONE);

            a.recycle();
        }

        // used when creating via code
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }


        public static class FramePlacement{
            public static int NONE = -1;
            public static int TOP_LEFT = 0;
            public static int TOP_CENTER = 1;
            public static int TOP_RIGHT = 2;
            public static int BOTTOM_RIGHT = 3;
            public static int BOTTOM_CENTER = 4;
            public static int BOTTOM_LEFT = 5;
        }

    }
}
