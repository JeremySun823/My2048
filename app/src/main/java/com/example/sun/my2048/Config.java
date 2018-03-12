package com.example.sun.my2048;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by sun on 17/8/31.
 */

public class Config extends Application {
    private static final String TAG = "sunwillfly Config";

    public static SharedPreferences mSp;
    private static int mGoal; // 目标 2048
    private static int mRecord; // 最高记录
    private static int mScore; // 当前分数

    private static int mLine;
    private static int mItemSize;

    public static String SP_GAME_2048 = "SP_GAME_2048";

    public static String KEY_LINE = "KEY_LINE";
    public static String KEY_GOAL = "KEY_GOAL";
    public static String KEY_RECORD = "KEY_RECORD";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mSp = getSharedPreferences(SP_GAME_2048, MODE_PRIVATE);
        mLine = mSp.getInt(KEY_LINE, 4);
        mGoal = mSp.getInt(KEY_GOAL, 2048);
        mRecord = mSp.getInt(KEY_RECORD, 0);
        initScore();
        Log.d(TAG, "onCreate mGoal = " + mGoal + ", mRecord = " + mRecord + ", mScore = " + mScore);

    }

    public static void setRecord() {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(KEY_RECORD, mRecord);
        editor.commit();
    }

    public static void setGoal() {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(KEY_GOAL, mGoal);
        editor.commit();
    }

    public static void initScore() {
        mScore = 0;
    }

    public static int getGoal() {
        return mGoal;
    }

    public static int getRecord() {
        return mRecord;
    }

    public static int getScore() {
        return mScore;
    }

    public static int getLine() {
        return mLine;
    }

    public static int getItemSize() {
        Log.d(TAG, "mItemSize = " + mItemSize);
        return mItemSize;
    }

    public static void setItemSize(int widthPixels) {
        mItemSize = widthPixels / mLine;
    }

    public static void updateGoal() {
        mGoal = mGoal * 2;
        Log.d(TAG, "updateGoal mGoal = " + mGoal);
        setGoal();
    }

    public static boolean checkRecord() {
        if (mScore > mRecord) {
            mRecord = mScore;
            Log.d(TAG, "checkRecord mRecord = " + mRecord);
            return true;
        } else {
            // do nothing
            return false;
        }
    }

    public static void updateScore(int addScore) {
        mScore = mScore + 2 * addScore;
        Log.d(TAG, "updateScore mScore = " + mScore);
    }

    public static void setScore(int score) {
        mScore = score;
    }
}
