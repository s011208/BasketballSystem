
package com.bj4.yhh.coachboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bj4.yhh.coachboard.basketball.R;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class PlayGround extends FrameLayout {
    private static final String JSON_KEY_TEAM_BLUE = "json_team_blue";

    private static final String JSON_KEY_TEAM_RED = "json_team_red";

    private static final String JSON_KEY_BALL = "json_ball";

    private static final String JSON_KEY_PEN = "json_pen";

    private static final String JSON_KEY_X = "json_x";

    private static final String JSON_KEY_Y = "json_y";

    public static final String JSON_KEY_TITLE = "title";

    public static final String JSON_KEY_FILE_NAME = "json_file_name";

    private static final int REPLAY_POINT_TIME = 40;

    private static final int DRAWING_MODE_NORMAL = 0;

    private static final int DRAWING_MODE_REPLAY = 1;

    private int mCurrentDrawingMode = DRAWING_MODE_NORMAL;

    private static final int MAX_PLAYER_PER_ROW = 6;

    private int mPlayerPerTeam;

    private Context mContext;

    private ArrayList<MovableItem> mTeamBlue, mTeamRed;

    private MovableItem mBall;

    private Paint mRunPaint, mTeamNumberPaint, mHandHoloPaint;

    private boolean mShowNumber = true;

    private ValueAnimator mHandHoloAnimator;

    private boolean mDrawHandHolo = false;

    private int mHandHoloRadius = 0;

    private int mHandHoloX, mHandHoloY;

    private int mPlayerNumberPaintTextSize;

    private ValueAnimator mReplayAnimator;

    public interface MainActivityCallback {
        public void replayDone();
    }

    private MainActivityCallback mCallback;

    public void setCallback(MainActivityCallback cb) {
        mCallback = cb;
    }

    private ArrayList<ArrayList<Point>> mAllRunningPoints = new ArrayList<ArrayList<Point>>();

    private ArrayList<ArrayList<Point>> mAnimatorRunningPoints = new ArrayList<ArrayList<Point>>();

    private ArrayList<Point> mRunningPoints = new ArrayList<Point>();

    private ArrayList<MoveStep> mMoveStepsList = new ArrayList<MoveStep>();

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
        initPaints();
        onSportTypeChanged(CoachBoardApplication.getSettingManager(mContext).getSportType());
        mHandHoloAnimator = ValueAnimator.ofInt(30, 80);
        mHandHoloAnimator.setRepeatMode(ValueAnimator.INFINITE);
        mHandHoloAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mHandHoloAnimator.setDuration(500);
        mHandHoloAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mHandHoloRadius = (Integer)animation.getAnimatedValue();
                invalidate();
            }
        });
        mHandHoloAnimator.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub
                mDrawHandHolo = true;
                mHandHoloRadius = 0;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mDrawHandHolo = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub
                mDrawHandHolo = true;
            }
        });
    }

    private void initPaints() {
        mPlayerNumberPaintTextSize = (int)getResources()
                .getDimension(R.dimen.team_number_text_size);
        mRunPaint = new Paint();
        mRunPaint.setColor(Color.WHITE);
        mRunPaint.setStyle(Paint.Style.FILL);
        mRunPaint.setStrokeWidth(5);
        mTeamNumberPaint = new Paint();
        mTeamNumberPaint.setColor(Color.WHITE);
        mTeamNumberPaint.setStyle(Paint.Style.FILL);
        mTeamNumberPaint.setTextSize(mPlayerNumberPaintTextSize);
        mTeamNumberPaint.setTextAlign(Paint.Align.CENTER);
        mHandHoloPaint = new Paint();
        mHandHoloPaint.setColor(Color.WHITE);
        mHandHoloPaint.setStyle(Paint.Style.STROKE);
        mHandHoloPaint.setStrokeWidth(5);
        mHandHoloPaint.setAntiAlias(true);
    }

    public void onSportTypeChanged(int sportType) {
        switch (sportType) {
            case SettingManager.SPORT_TYPE_BASEBALL:
                setPlayerPerTeam(9);
                resetAll();
                mBall.setImageResource(R.drawable.basketball_ball);
                break;
            case SettingManager.SPORT_TYPE_BASKETBALL:
                setPlayerPerTeam(5);
                resetAll();
                mBall.setImageResource(R.drawable.basketball_ball);
                break;
            case SettingManager.SPORT_TYPE_FOOTBALL:
                setPlayerPerTeam(15);
                resetAll();
                mBall.setImageResource(R.drawable.basketball_ball);
                break;
            case SettingManager.SPORT_TYPE_SOCCER:
                setPlayerPerTeam(11);
                resetAll();
                mBall.setImageResource(R.drawable.basketball_ball);
                break;
            case SettingManager.SPORT_TYPE_TENNIS:
                setPlayerPerTeam(2);
                resetAll();
                mBall.setImageResource(R.drawable.basketball_ball);
                break;
            case SettingManager.SPORT_TYPE_VOLLEYBALL:
                setPlayerPerTeam(6);
                resetAll();
                mBall.setImageResource(R.drawable.basketball_ball);
                break;
        }
    }

    public void setBlueTeamVisiblity(boolean isChecked) {
        if (mTeamBlue == null) {
            return;
        }
        for (View v : mTeamBlue) {
            v.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setRedTeamVisiblity(boolean isChecked) {
        if (mTeamRed == null) {
            return;
        }
        for (View v : mTeamRed) {
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

    public void resetAll() {
        removeAllViews();
        erasePen();
        mMoveStepsList.clear();
        mTeamBlue = new ArrayList<MovableItem>();
        mTeamRed = new ArrayList<MovableItem>();
        int playerWandH = (int)mContext.getResources().getDimension(R.dimen.movable_item_w_and_h);
        int playerPadding = (int)mContext.getResources().getDimension(R.dimen.movable_item_padding);
        if (mPlayerPerTeam <= 9) {
            mTeamNumberPaint.setTextSize(mPlayerNumberPaintTextSize);
        }
        if (mPlayerPerTeam > 9) {
            playerWandH *= 0.8f;
            playerPadding *= 0.8f;
            mTeamNumberPaint.setTextSize(mPlayerNumberPaintTextSize * 0.7f);
        }
        int row = 1;
        // team blue
        for (int i = 0; i < mPlayerPerTeam; i++) {
            MovableItem player = new MovableItem(mContext, MoveStep.TAG_TEAM_BLUE, i);
            player.setImageResource(R.drawable.team_blue);
            player.setScaleType(ScaleType.CENTER_INSIDE);
            if (mShowNumber)
                player.setNumber(String.valueOf(i + 1));
            player.setPadding(playerPadding, playerPadding, playerPadding, playerPadding);
            FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(playerWandH, playerWandH);
            fl.topMargin = (row + i / MAX_PLAYER_PER_ROW) * playerWandH;
            fl.leftMargin = (i % MAX_PLAYER_PER_ROW) * playerWandH;
            addView(player, fl);
            mTeamBlue.add(player);
        }
        row += mPlayerPerTeam / MAX_PLAYER_PER_ROW + 1;
        // team red
        for (int i = 0; i < mPlayerPerTeam; i++) {
            MovableItem player = new MovableItem(mContext, MoveStep.TAG_TEAM_RED, i);
            player.setImageResource(R.drawable.team_red);
            player.setScaleType(ScaleType.CENTER_INSIDE);
            if (mShowNumber)
                player.setNumber(String.valueOf(i + 1));
            player.setPadding(playerPadding, playerPadding, playerPadding, playerPadding);
            FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(playerWandH, playerWandH);
            fl.topMargin = (row + i / MAX_PLAYER_PER_ROW) * playerWandH;
            fl.leftMargin = (i % MAX_PLAYER_PER_ROW) * playerWandH;
            addView(player, fl);
            mTeamRed.add(player);
        }
        // ball
        row += mPlayerPerTeam / MAX_PLAYER_PER_ROW + 1;
        if (mBall == null) {
            mBall = new MovableItem(mContext, MoveStep.TAG_BALL, 0);
            mBall.setImageResource(R.drawable.basketball_ball);
            mBall.setScaleType(ScaleType.CENTER_INSIDE);
        }
        mBall.setPadding(playerPadding, playerPadding, playerPadding, playerPadding);
        FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(playerWandH, playerWandH);
        fl.topMargin = playerWandH * row;
        addView(mBall, fl);
    }

    public void replay() {
        if (mCurrentDrawingMode != DRAWING_MODE_NORMAL) {
            return;
        }
        if (mAllRunningPoints.isEmpty()) {
            Toast.makeText(mContext, "you have to draw lines to run!", Toast.LENGTH_LONG).show();
            return;
        }
        final ArrayList<ArrayList<Point>> allRunningPoints = new ArrayList<ArrayList<Point>>();
        mAllRunningPoints.clone();
        Iterator<ArrayList<Point>> iter = mAllRunningPoints.iterator();
        int totalEvents = 0;
        while (iter.hasNext()) {
            ArrayList<Point> points = iter.next();
            totalEvents += points.size();
            ArrayList<Point> cPoints = new ArrayList<Point>();
            for (Point p : points) {
                Point cp = new Point(p);
                cPoints.add(cp);
            }
            allRunningPoints.add(cPoints);
        }
        ArrayList<Point> cPoints = new ArrayList<Point>();
        for (Point p : mRunningPoints) {
            Point cp = new Point(p);
            cPoints.add(cp);
            ++totalEvents;
        }
        allRunningPoints.add(cPoints);

        final int duration = totalEvents * REPLAY_POINT_TIME;
        mReplayAnimator = ValueAnimator.ofInt(0, totalEvents);
        mReplayAnimator.setDuration(duration);
        mReplayAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (allRunningPoints.isEmpty() == false) {
                    ArrayList<Point> pointList = allRunningPoints.get(0);
                    if (pointList.isEmpty() == false) {
                        if (mAnimatorRunningPoints.isEmpty()) {
                            mAnimatorRunningPoints.add(new ArrayList<Point>());
                        }
                        mAnimatorRunningPoints.get(mAnimatorRunningPoints.size() - 1).add(
                                pointList.remove(0));
                    } else {
                        allRunningPoints.remove(0);
                        mAnimatorRunningPoints.add(new ArrayList<Point>());
                    }
                } else {
                    mReplayAnimator.cancel();
                }
                invalidate();
            }
        });
        mReplayAnimator.addListener(new AnimatorListener() {

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentDrawingMode = DRAWING_MODE_NORMAL;
                mAnimatorRunningPoints.clear();
                if (mCallback != null) {
                    mCallback.replayDone();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorRunningPoints.clear();
                mCurrentDrawingMode = DRAWING_MODE_REPLAY;
            }
        });
        mReplayAnimator.start();
    }

    public void cancelReplay() {
        if (mReplayAnimator != null && mReplayAnimator.isRunning()) {
            mReplayAnimator.cancel();
        }
    }

    public String saveData(String title) {
        JSONObject jSaveData = new JSONObject();
        try {
            // team blue
            JSONArray jTeamBlue = new JSONArray();
            for (View v : mTeamBlue) {
                JSONObject j = new JSONObject();
                j.put(JSON_KEY_X, v.getX());
                j.put(JSON_KEY_Y, v.getY());
                jTeamBlue.put(j);
            }
            // team red
            JSONArray jTeamRed = new JSONArray();
            for (View v : mTeamRed) {
                JSONObject j = new JSONObject();
                j.put(JSON_KEY_X, v.getX());
                j.put(JSON_KEY_Y, v.getY());
                jTeamRed.put(j);
            }
            // ball
            JSONArray jBall = new JSONArray();
            JSONObject j = new JSONObject();
            j.put(JSON_KEY_X, mBall.getX());
            j.put(JSON_KEY_Y, mBall.getY());
            jBall.put(j);

            // pen
            JSONArray jPen = new JSONArray();
            Iterator<ArrayList<Point>> iter = mAllRunningPoints.iterator();
            while (iter.hasNext()) {
                ArrayList<Point> runningPoints = iter.next();
                JSONArray points = new JSONArray();
                for (int i = 0; i < runningPoints.size() - 1; i++) {
                    JSONObject point = new JSONObject();
                    Point p = runningPoints.get(i);
                    point.put(JSON_KEY_X, p.x);
                    point.put(JSON_KEY_Y, p.y);
                    points.put(point);
                }
                jPen.put(points);
            }
            JSONArray points = new JSONArray();
            for (int i = 0; i < mRunningPoints.size() - 1; i++) {
                JSONObject point = new JSONObject();
                Point p = mRunningPoints.get(i);
                point.put(JSON_KEY_X, p.x);
                point.put(JSON_KEY_Y, p.y);
                points.put(point);
            }
            jPen.put(points);
            jSaveData.put(JSON_KEY_TEAM_BLUE, jTeamBlue);
            jSaveData.put(JSON_KEY_TEAM_RED, jTeamRed);
            jSaveData.put(JSON_KEY_BALL, jBall);
            jSaveData.put(JSON_KEY_TITLE, title);
            jSaveData.put(JSON_KEY_PEN, jPen);

        } catch (JSONException e) {
        }
        String result = jSaveData.toString();
        String fileName = CoachBoardApplication.getSettingManager(mContext).generateFileName();
        File file = new File(mContext.getFilesDir() + File.separator + fileName);
        try {
            file.createNewFile();
            writeToFile(file.getAbsolutePath(), result);
        } catch (IOException e) {
            Log.w("QQQQ", "failed", e);
        }

        return result;
    }

    public static boolean writeToFile(final String filePath, final String data) {
        Writer writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),
                    "utf-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            Log.w("QQQQ", "writeToFile failed: " + filePath, e);
            return false;
        }
    }

    public static String readFromFile(String filePath) {
        String ret = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            ret = sb.toString();
            br.close();
        } catch (IOException e) {
            Log.w("QQQQ", "readFromFile failed: " + filePath, e);
        }
        return ret;
    }

    public void restoreData(JSONObject jObject) {
        try {
            JSONArray blueTeam = jObject.getJSONArray(JSON_KEY_TEAM_BLUE);
            for (int i = 0; i < blueTeam.length(); i++) {
                JSONObject k = blueTeam.getJSONObject(i);
                View v = mTeamBlue.get(i);
                int x = k.getInt(JSON_KEY_X);
                int y = k.getInt(JSON_KEY_Y);
                v.setX(x);
                v.setY(y);
            }

            JSONArray redTeam = jObject.getJSONArray(JSON_KEY_TEAM_RED);
            for (int i = 0; i < redTeam.length(); i++) {
                JSONObject k = redTeam.getJSONObject(i);
                View v = mTeamRed.get(i);
                int x = k.getInt(JSON_KEY_X);
                int y = k.getInt(JSON_KEY_Y);
                v.setX(x);
                v.setY(y);
            }

            JSONArray ball = jObject.getJSONArray(JSON_KEY_BALL);
            JSONObject b = ball.getJSONObject(0);
            int x = b.getInt(JSON_KEY_X);
            int y = b.getInt(JSON_KEY_Y);
            mBall.setX(x);
            mBall.setY(y);
            erasePen();
            JSONArray jPen = jObject.getJSONArray(JSON_KEY_PEN);
            for (int i = 0; i < jPen.length(); i++) {
                JSONArray jPoints = jPen.getJSONArray(i);
                ArrayList<Point> pointList = new ArrayList<Point>();
                for (int j = 0; j < jPoints.length(); j++) {
                    JSONObject jPoint = jPoints.getJSONObject(j);
                    Point p = new Point();
                    p.x = jPoint.getInt(JSON_KEY_X);
                    p.y = jPoint.getInt(JSON_KEY_Y);
                    pointList.add(p);
                }
                mAllRunningPoints.add(pointList);
            }
            invalidate();
        } catch (JSONException e) {
            Log.e("QQQQ", "failed", e);
        }
    }

    public void sharePlayGround() {
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap bmp = getDrawingCache();
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator
                + "coachboard");
        if (directory.exists() == false)
            directory.mkdir();
        File png = new File(Environment.getExternalStorageDirectory() + File.separator
                + "coachboard" + File.separatorChar + java.util.UUID.randomUUID().toString()
                + ".png");
        Uri fileUri = Uri.fromFile(png);
        FileOutputStream out = null;
        try {
            png.createNewFile();
            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            scanIntent.setData(fileUri);
            mContext.sendBroadcast(scanIntent);
            out = new FileOutputStream(png);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException ignore) {
            }
        }
        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.share_text));
        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.share_title));
        intent.setType("image/png");
        mContext.startActivity(intent);
    }

    public void erasePen() {
        if (mAllRunningPoints != null) {
            mAllRunningPoints.clear();
            mRunningPoints.clear();
            mAnimatorRunningPoints.clear();
            if (mReplayAnimator != null && mReplayAnimator.isRunning())
                mReplayAnimator.cancel();
            invalidate();
        }
    }

    public void setGroundImage(int resource) {
        setBackgroundResource(resource);
    }

    public void setPlayerPerTeam(int playerPerTeam) {
        mPlayerPerTeam = playerPerTeam;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mCurrentDrawingMode != DRAWING_MODE_NORMAL)
            return super.onTouchEvent(event);
        int x = mHandHoloX = (int)event.getX();
        int y = mHandHoloY = (int)event.getY();
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
                mHandHoloAnimator.cancel();

                invalidate();
                break;
            case MotionEvent.ACTION_DOWN:
                mRunningPoints = new ArrayList<Point>();
                p = new Point();
                p.set(x, y);
                mRunningPoints.add(p);
                mHandHoloAnimator.start();
                mMoveStepsList.add(new MoveStep(MoveStep.TAG_DRAW_LINE, MoveStep.NOT_USED,
                        MoveStep.NOT_USED, MoveStep.NOT_USED));
                break;
        }
        return true;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mCurrentDrawingMode == DRAWING_MODE_NORMAL) {
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
                if (mDrawHandHolo) {
                    canvas.drawCircle(mHandHoloX, mHandHoloY, mHandHoloRadius, mHandHoloPaint);
                }
            }
        } else {
            Iterator<ArrayList<Point>> iter = mAnimatorRunningPoints.iterator();
            while (iter.hasNext()) {
                ArrayList<Point> runningPoints = iter.next();
                for (int i = 0; i < runningPoints.size() - 1; i++) {
                    Point p = runningPoints.get(i);
                    Point p1 = runningPoints.get(i + 1);
                    canvas.drawLine(p.x, p.y, p1.x, p1.y, mRunPaint);
                }
            }
        }
    }

    public boolean isReplaying() {
        return mCurrentDrawingMode == DRAWING_MODE_REPLAY;
    }

    public void undo() {
        if (mMoveStepsList.isEmpty())
            return;
        // for(MoveStep step : mMoveStepsList){
        // Log.e("QQQQ", step.toString());
        // }
        MoveStep step = mMoveStepsList.remove(mMoveStepsList.size() - 1);
        final int tag = step.mTag;
        final int index = step.mIndex;
        final int x = step.mX;
        final int y = step.mY;
        switch (tag) {
            case MoveStep.TAG_TEAM_RED:
                if (mTeamRed.size() > index) {
                    MovableItem item = mTeamRed.get(index);
                    item.setX(x);
                    item.setY(y);
                } else {
                    // wrong
                }
                break;
            case MoveStep.TAG_TEAM_BLUE:
                if (mTeamBlue.size() > index) {
                    MovableItem item = mTeamBlue.get(index);
                    item.setX(x);
                    item.setY(y);
                } else {
                    // wrong
                }
                break;
            case MoveStep.TAG_BALL:
                MovableItem item = mBall;
                item.setX(x);
                item.setY(y);
                break;
            case MoveStep.TAG_DRAW_LINE:
                if (mRunningPoints.isEmpty() == false) {
                    mRunningPoints.clear();
                    mAllRunningPoints.remove(mAllRunningPoints.size() - 1);
                } else {
                    if (mAllRunningPoints.isEmpty()) {
                        // do nothing
                    } else {
                        mAllRunningPoints.remove(mAllRunningPoints.size() - 1);
                    }
                }
                postInvalidate();
                break;
        }

    }

    class MoveStep {
        public static final int TAG_TEAM_RED = 0;

        public static final int TAG_TEAM_BLUE = 1;

        public static final int TAG_BALL = 2;

        public static final int TAG_DRAW_LINE = 3;

        public static final int NOT_USED = -1;

        public int mIndex;

        public int mTag;

        public int mX, mY;

        public MoveStep(int tag, int index, int x, int y) {
            mTag = tag;
            mIndex = index;
            mX = x;
            mY = y;
        }

        public String toString() {
            return "tag: " + mTag + ", index: " + mIndex + ", x: " + mX + ", y: " + mY;
        }

    }

    private class MovableItem extends ImageView {

        private int mDeltaX, mDeltaY;

        private String mNumber = "";

        public int mTag, mIndex;

        public MovableItem(Context context, int tag, int index) {
            super(context);
            mTag = tag;
            mIndex = index;
        }

        public void setNumber(String number) {
            mNumber = number;
        }

        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int xPos = (canvas.getWidth() / 2);
            int yPos = (int)((canvas.getHeight() / 2) - ((mTeamNumberPaint.descent() + mTeamNumberPaint
                    .ascent()) / 2));
            canvas.drawText(mNumber, xPos, yPos, mTeamNumberPaint);
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (mCurrentDrawingMode != DRAWING_MODE_NORMAL)
                return super.onTouchEvent(event);
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
                    mMoveStepsList.add(new MoveStep(mTag, mIndex, (int)getX(), (int)getY()));
                    break;
            }

            return true;
        }
    }
}
