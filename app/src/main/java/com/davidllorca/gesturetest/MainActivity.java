package com.davidllorca.gesturetest;

import android.app.Activity;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {

    private static final int INVALID_POINTER_ID = -1;
    // Handlers
    private GestureDetector mDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.f;

    private int mActivePointerId;

    //Views
    private TextView gestureTv;
    private ImageView imageView;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mPosX;
    private float mPosY;
    private MyImageView mMyImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference views
        gestureTv = (TextView) findViewById(R.id.gesture_tv);
        //imageView = (ImageView) findViewById(R.id.imageView);
        mMyImageView = (MyImageView) findViewById(R.id.imageView);

        // Parameters -> Context and class manager of gestures
        //mDetector = new GestureDetector(MainActivity.this, MainActivity.this);
        //mScaleGestureDetector = new ScaleGestureDetector(MainActivity.this, MainActivity.this);
        // This detector will listen double tap event
        //mDetector.setOnDoubleTapListener(MainActivity.this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Call detector
//        mDetector.onTouchEvent(event);
//        mScaleGestureDetector.onTouchEvent(event);

        mActivePointerId = event.getPointerId(0);
        final int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:

            {
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final float x = MotionEventCompat.getX(event, pointerIndex);
                final float y = MotionEventCompat.getY(event, pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(event, mActivePointerId);

                final float x = MotionEventCompat.getX(event, pointerIndex);
                final float y = MotionEventCompat.getY(event, pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY = dy;

                imageView.invalidate();

                // Remember this touch position for the next move event
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

                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(event, newPointerIndex);
                    mLastTouchY = MotionEventCompat.getY(event, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                }
                Log.i("OnTouchEvent", "mLastTouchX: " + mLastTouchX + "|mLastTouchY:" + mLastTouchY);
                Log.i("OnTouchEvent", "mPosX: " + mPosX + "|mPosY:" + mPosY);
                break;
            }
        }

        // Always call parent class
        return super.onTouchEvent(event);
    }

    /*
        OnDoubleTapListener
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        setTextView("onSingleTapConfirmed");
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        setTextView("onDoubleTap");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        setTextView("onDoubleTapEvent");
        return true;
    }

    /*
        OnGestureListener
     */
    @Override
    public boolean onDown(MotionEvent e) {
        setTextView("onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        setTextView("onShowPress");

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        setTextView("onSingleTapUp");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        imageView.scrollBy((int) distanceX, (int) distanceY);
        setTextView("onScroll");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        setTextView("onLongPress");

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        setTextView("onFling");
        return true;
    }

    /*
        OnScaleGestureListener
     */

    private float lastSpanX;
    private float lastSpanY;


    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        setTextView("onScaleBegin");
        lastSpanX = detector.getCurrentSpanX();
        lastSpanY = detector.getCurrentSpanY();
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mScaleFactor *= detector.getScaleFactor();
        // Set min and max of scale
        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
//        imageView.setScaleX(detector.getCurrentSpanX());
//        imageView.setScaleY(detector.getCurrentSpanY());

        float spanX = detector.getCurrentSpanX();
        float spanY = detector.getCurrentSpanY();

        float newWidth = lastSpanX /spanX * imageView.getWidth();
        float newHeight = lastSpanY / spanY * imageView.getHeight();

        float focusX = detector.getFocusX();
        float focusY = detector.getFocusY();

        imageView.setScaleX(imageView.getX() - newWidth * (focusX));
        imageView.setScaleY(imageView.getY() - newHeight * (focusY));

        setTextView("onScale");
        lastSpanX = spanX;
        lastSpanY = spanY;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        setTextView("onScaleEnd");
    }

    private void setTextView(String gesture) {
        gestureTv.setText(gesture);
    }

}
