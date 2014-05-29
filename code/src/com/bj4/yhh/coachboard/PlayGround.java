
package com.bj4.yhh.coachboard;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class PlayGround extends FrameLayout {
    private int mPlayerPerTeam = 5;

    private Context mContext;

    private ArrayList<MovableItem> mTeam1, mTeam2;

    private MovableItem mBall;

    private Paint mRunPaint;

    private ArrayList<ArrayList<Point>> mAllRunningPoints = new ArrayList<ArrayList<Point>>();

    private ArrayList<Point> mRunningPoints = new ArrayList<Point>();

    private boolean mShowPen = true;

    public PlayGround(Context context) {
        this(context, null);
    }

    public PlayGround(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayGround(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mRunPaint = new Paint();
        mRunPaint.setColor(Color.WHITE);
        mRunPaint.setStyle(Paint.Style.FILL);
        mRunPaint.setStrokeWidth(5);
        resetPlayer();
    }

    public void setBlueTeamVisiblity(boolean isChecked) {
        if (mTeam1 == null) {
            return;
        }
        for (View v : mTeam1) {
            v.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setRedTeamVisiblity(boolean isChecked) {
        if (mTeam2 == null) {
            return;
        }
        for (View v : mTeam2) {
            v.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setBallVisiblity(boolean isChecked) {
        if (mBall == null) {
            return;
        }
        mBall.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
    }

    public void setPenVisiblity(boolean isChecked) {
        mShowPen = isChecked;
        invalidate();
    }

    public void resetPlayer() {
        removeAllViews();
        mTeam1 = new ArrayList<MovableItem>();
        mTeam2 = new ArrayList<MovableItem>();
        int playerWandH = (int)mContext.getResources().getDimension(R.dimen.movable_item_w_and_h);
        // team1
        for (int i = 0; i < mPlayerPerTeam; i++) {
            MovableItem player = new MovableItem(mContext);
            player.setBackgroundResource(R.drawable.blue_team);
            FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(playerWandH, playerWandH);
            fl.topMargin = playerWandH;
            fl.leftMargin = i * playerWandH;
            addView(player, fl);
            mTeam1.add(player);
        }
        // team2
        for (int i = 0; i < mPlayerPerTeam; i++) {
            MovableItem player = new MovableItem(mContext);
            player.setBackgroundResource(R.drawable.red_team);
            FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(playerWandH, playerWandH);
            fl.leftMargin = i * playerWandH;
            fl.topMargin = playerWandH * 3;
            addView(player, fl);
            mTeam2.add(player);
        }
        // ball
        mBall = new MovableItem(mContext);
        mBall.setBackgroundResource(R.drawable.ball);
        FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(playerWandH, playerWandH);
        fl.topMargin = playerWandH * 5;
        addView(mBall, fl);
    }

    public void setGroundImage(int resource) {
        setBackgroundResource(resource);
    }

    public void setPlayerPerTeam(int playerPerTeam) {
        mPlayerPerTeam = playerPerTeam;
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        Point p;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                p = new Point();
                p.set(x, y);
                mRunningPoints.add(p);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                p = new Point();
                p.set(x, y);
                mRunningPoints.add(p);
                mAllRunningPoints.add(mRunningPoints);
                invalidate();
                break;
            case MotionEvent.ACTION_DOWN:
                mRunningPoints = new ArrayList<Point>();
                break;
        }
        return true;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mShowPen) {
            Iterator<ArrayList<Point>> iter = mAllRunningPoints.iterator();
            while (iter.hasNext()) {
                ArrayList<Point> runningPoints = iter.next();
                for (int i = 0; i < runningPoints.size() - 1; i++) {
                    Point p = runningPoints.get(i);
                    Point p1 = runningPoints.get(i + 1);
                    canvas.drawLine(p.x, p.y, p1.x, p1.y, mRunPaint);
                }
            }
            for (int i = 0; i < mRunningPoints.size() - 1; i++) {
                Point p = mRunningPoints.get(i);
                Point p1 = mRunningPoints.get(i + 1);
                canvas.drawLine(p.x, p.y, p1.x, p1.y, mRunPaint);
            }
        }
    }

    private class MovableItem extends View {

        private int mDeltaX, mDeltaY;

        public MovableItem(Context context) {
            super(context);
        }

        public boolean onTouchEvent(MotionEvent event) {

            int x = (int)event.getRawX();
            int y = (int)event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    setX(x - mDeltaX);
                    setY(y - mDeltaY);
                    break;

                case MotionEvent.ACTION_UP:
                    break;

                case MotionEvent.ACTION_DOWN:
                    mDeltaX = x - (int)getX();
                    mDeltaY = y - (int)getY();
                    break;
            }

            return true;
        }

    }
}
