package com.davidllorca.gesturetest;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.TextView;


public class MainActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {

    // Handlers
    private GestureDetector mDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.f;

    //Views
    private TextView gestureTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Reference views
        gestureTv = (TextView) findViewById(R.id.gesture_tv);
        // Parameters -> Context and class manager of gestures
        mDetector = new GestureDetector(MainActivity.this, MainActivity.this);
        mScaleGestureDetector = new ScaleGestureDetector(MainActivity.this, MainActivity.this);
        // This detector will listen double tap event
        mDetector.setOnDoubleTapListener(MainActivity.this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Call detector
        mDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
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
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mScaleFactor *= detector.getScaleFactor();
        // Set min and max of scale
        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
        setTextView("onScale");
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        setTextView("onScaleBegin");
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
