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

public class PlayGround extends FrameLayout {
	private static final String JSON_KEY_TEAM_BLUE = "json_team_blue";

	private static final String JSON_KEY_TEAM_RED = "json_team_red";

	private static final String JSON_KEY_BALL = "json_ball";

	private static final String JSON_KEY_PEN = "json_pen";

	private static final String JSON_KEY_X = "json_x";

	private static final String JSON_KEY_Y = "json_y";

	public static final String JSON_KEY_TITLE = "title";

	public static final String JSON_KEY_FILE_NAME = "json_file_name";

	private int mPlayerPerTeam = 5;

	private Context mContext;

	private ArrayList<MovableItem> mTeamBlue, mTeamRed;

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
		resetAll();
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
		mTeamBlue = new ArrayList<MovableItem>();
		mTeamRed = new ArrayList<MovableItem>();
		int playerWandH = (int) mContext.getResources().getDimension(
				R.dimen.movable_item_w_and_h);
		int playerPadding = (int) mContext.getResources().getDimension(
				R.dimen.movable_item_padding);
		// team1
		for (int i = 0; i < mPlayerPerTeam; i++) {
			MovableItem player = new MovableItem(mContext);
			player.setImageResource(R.drawable.blue_team);
			player.setScaleType(ScaleType.CENTER_INSIDE);
			player.setPadding(playerPadding, playerPadding, playerPadding,
					playerPadding);
			FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(
					playerWandH, playerWandH);
			fl.topMargin = playerWandH;
			fl.leftMargin = i * playerWandH;
			addView(player, fl);
			mTeamBlue.add(player);
		}
		// team2
		for (int i = 0; i < mPlayerPerTeam; i++) {
			MovableItem player = new MovableItem(mContext);
			player.setImageResource(R.drawable.red_team);
			player.setScaleType(ScaleType.CENTER_INSIDE);
			player.setPadding(playerPadding, playerPadding, playerPadding,
					playerPadding);
			FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(
					playerWandH, playerWandH);
			fl.leftMargin = i * playerWandH;
			fl.topMargin = playerWandH * 3;
			addView(player, fl);
			mTeamRed.add(player);
		}
		// ball
		mBall = new MovableItem(mContext);
		mBall.setImageResource(R.drawable.ball);
		mBall.setScaleType(ScaleType.CENTER_INSIDE);
		mBall.setPadding(playerPadding, playerPadding, playerPadding,
				playerPadding);
		FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(playerWandH,
				playerWandH);
		fl.topMargin = playerWandH * 5;
		addView(mBall, fl);
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
		String fileName = java.util.UUID.randomUUID().toString();
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
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filePath), "utf-8"));
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
		File png = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "coachboard" + File.separatorChar
				+ java.util.UUID.randomUUID().toString() + ".png");
		Uri fileUri = Uri.fromFile(png);
		FileOutputStream out = null;
		try {
			png.createNewFile();
			Intent scanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
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
		int x = (int) event.getX();
		int y = (int) event.getY();
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

	private class MovableItem extends ImageView {

		private int mDeltaX, mDeltaY;

		public MovableItem(Context context) {
			super(context);
		}

		public boolean onTouchEvent(MotionEvent event) {

			int x = (int) event.getRawX();
			int y = (int) event.getRawY();
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				setX(x - mDeltaX);
				setY(y - mDeltaY);
				break;

			case MotionEvent.ACTION_UP:
				break;

			case MotionEvent.ACTION_DOWN:
				mDeltaX = x - (int) getX();
				mDeltaY = y - (int) getY();
				break;
			}

			return true;
		}

	}
}
