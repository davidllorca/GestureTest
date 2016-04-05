package com.davidllorca.gesturetest;

/**
 * Created by coneptum on 4/04/16.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class MyImageView extends ImageView {

    private static final int INVALID_POINTER_ID = -1;

    //private Drawable mImage;
    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;

    private static float MAX_SCROLL_X;
    private static float MAX_SCROLL_Y;
    private static float MAX_SCROLL_RIGHT;
    private static float MAX_SCROLL_BOTTOM;
    private static float MAX_SCROLL_LEFT;
    private static float MAX_SCROLL_TOP;

    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    public MyImageView(Context context) {
        this(context, null, 0);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        // TODO SEGUIR AQUÍ
        super(context, attrs, defStyle);
        //mImage = getDrawable();

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                MAX_SCROLL_X = (getDrawable().getIntrinsicWidth()/2) - (getWidth()/2);
                MAX_SCROLL_Y = (getDrawable().getIntrinsicHeight()/2) - (getHeight()/2);

                MAX_SCROLL_RIGHT = MAX_SCROLL_X;
                MAX_SCROLL_BOTTOM =MAX_SCROLL_Y;
                MAX_SCROLL_LEFT = (MAX_SCROLL_X * -1);
                MAX_SCROLL_TOP = (MAX_SCROLL_Y * -1);
                // TODO remove listener
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);
                break;
            }

//            case MotionEvent.ACTION_MOVE: {
//                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
//                final float x = ev.getX(pointerIndex);
//                final float y = ev.getY(pointerIndex);
//
//                // Only move if the ScaleGestureDetector isn't processing a gesture.
//                if (!mScaleDetector.isInProgress()) {
//                    final float dx = x - mLastTouchX;
//                    final float dy = y - mLastTouchY;
//                    Log.d("DEBUG", "dx: " + dx + " dy: " + dy);
//
//                    mPosX += dx;
//                    mPosY += dy;
//
//                    invalidate();
//                }
//
//                mLastTouchX = x;
//                mLastTouchY = y;
//
//                break;
//            }
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    float dx = mLastTouchX - x;
                    float dy = mLastTouchY - y;
                    Log.d("DEBUG", "dx: " + dx + " dy: " + dy);

                    // scrolling to left side of image (pic moving to the right)
                    if (x > mLastTouchX)
                    {
                        if (mPosX == MAX_SCROLL_LEFT)
                        {
                            dx = 0;
                        }
                        if (mPosX > MAX_SCROLL_LEFT)
                        {
                            mPosX += dx;
                        }
                        if (mPosX < MAX_SCROLL_LEFT)
                        {
                            dx = MAX_SCROLL_LEFT - (mPosX - dx);
                            mPosX = MAX_SCROLL_LEFT;
                        }
                    }

                    // scrolling to right side of image (pic moving to the left)
                    if (x < mLastTouchX)
                    {
                        if (mPosX == MAX_SCROLL_RIGHT)
                        {
                            dx = 0;
                        }
                        if (mPosX < MAX_SCROLL_RIGHT)
                        {
                            mPosX += dx;
                        }
                        if (mPosX > MAX_SCROLL_RIGHT)
                        {
                            dx = MAX_SCROLL_RIGHT - (mPosX - dx);
                            mPosX = MAX_SCROLL_RIGHT;
                        }
                    }

                    // scrolling to top of image (pic moving to the bottom)
                    if (y > mLastTouchY)
                    {
                        if (mPosY == MAX_SCROLL_TOP)
                        {
                            dy = 0;
                        }
                        if (mPosY > MAX_SCROLL_TOP)
                        {
                            mPosY += dy;
                        }
                        if (mPosY < MAX_SCROLL_TOP)
                        {
                            dy = MAX_SCROLL_TOP - (mPosY - dy);
                            mPosY = MAX_SCROLL_TOP;
                        }
                    }

                    // scrolling to bottom of image (pic moving to the top)
                    if (y < mLastTouchY)
                    {
                        if (mPosY == MAX_SCROLL_BOTTOM)
                        {
                            dy = 0;
                        }
                        if (mPosY < MAX_SCROLL_BOTTOM)
                        {
                            mPosY += dy;
                        }
                        if (mPosY > MAX_SCROLL_BOTTOM)
                        {
                            dy = MAX_SCROLL_BOTTOM - (mPosY - dy);
                            mPosY = MAX_SCROLL_BOTTOM;
                        }
                    }
                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }
            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.save();
        canvas.translate(-mPosX, -mPosY);
        canvas.scale(mScaleFactor, mScaleFactor);
        //mImage.draw(canvas);
        getDrawable().draw(canvas);
        canvas.restore();
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            invalidate();
            return true;
        }
    }

}
