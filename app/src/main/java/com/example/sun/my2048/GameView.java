package com.example.sun.my2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun on 17/8/31.
 */

public class GameView extends GridLayout implements View.OnTouchListener {
    private static final String TAG = "sunwillfly GameView";

    private GameItem[][] mGameMatrix;
    private List<Point> mBlanks;
    private int mGameLines;
    private int mStartX, mStartY, mEndX, mEndY;
    private List<Integer> mCalList;
    private int mKeyItemNum = -1;
    private int[][] mGameMatrixHistory;
    private int mScoreHistory;

    private int mDensity;


    public GameView(Context context) {
        super(context);
        initGameMatrix();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameMatrix();
    }

    public void startGame() {
        initGameMatrix();
    }

    private void initGameMatrix() {
        Log.d(TAG, "initGameMatrix");
        Config.initScore();
        Game.getGameActivity().setScore();
        mScoreHistory = 0;
        removeAllViews();
        mGameLines = Config.getLine();
        mGameMatrix = new GameItem[mGameLines][mGameLines];
        mGameMatrixHistory = new int[mGameLines][mGameLines];
        mCalList = new ArrayList<Integer>();
        mBlanks = new ArrayList<Point>();

        setColumnCount(mGameLines);
        setRowCount(mGameLines);
        setOnTouchListener(this);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        Config.setItemSize(metrics.widthPixels);
        mDensity = (int) metrics.density;
        Log.d(TAG, "mDensity = " + mDensity);
        initGameView(Config.getItemSize());
    }

    private void initGameView(int cardSize) {
        GameItem card;
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                card = new GameItem(getContext(), 0);
                addView(card, cardSize, cardSize);
                mGameMatrix[i][j] = card;
                mBlanks.add(new Point(i, j));
            }
        }
        addRandomNum();
        addRandomNum();

    }

    private void addRandomNum() {
        updateBlanks();
        if (mBlanks.size() > 0) {
            int randomNum = (int) (Math.random() * mBlanks.size());
            Point randomPoint = mBlanks.get(randomNum);
            mGameMatrix[randomPoint.x][randomPoint.y].setNum(Math.random() > 0.2d ? 2 : 4);
            animCreate(mGameMatrix[randomPoint.x][randomPoint.y]);
        }
    }

    private void animCreate(GameItem target) {
        ScaleAnimation sa = new ScaleAnimation(0.1f, 1, 0.1f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(100);
        target.setAnimation(null);
        target.getItemView().setAnimation(sa);
    }

    private void updateBlanks() {
        mBlanks.clear();
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                if (mGameMatrix[i][j].getNum() == 0) {
                    mBlanks.add(new Point(i, j));
                }
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouch ACTION_DOWN");
                mStartX = (int) event.getX();
                mStartY = (int) event.getY();
                saveHistoryMatrix();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouch ACTION_UP");
                mEndX = (int) event.getX();
                mEndY = (int) event.getY();
                judgeDirection(mEndX - mStartX, mEndY - mStartY);
                if (isMoved()) {
                    addRandomNum();
                    Game.getGameActivity().setScore();
                }
                checkRecord();
                checkCompleted();
                break;
            default:
                break;
        }
        return true;
    }

    // 判断是否超过Record
    private void checkRecord() {
        if (Config.checkRecord()) {
            Game.getGameActivity().setNewRecord();
        }
    }


    // 判断是否完成
    private void checkCompleted() {
        int result = checkNums();
        if (result == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Game Over").setPositiveButton("Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).create().show();
        } else if (result == 2) {
            Game.getGameActivity().setMissionComplete();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Mission Complete").setPositiveButton("Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }

    }

    // 0 : game over
    // 1 : normal
    // 2 : mission complete
    private int checkNums() {
        updateBlanks();
        // 如果没有空格了，判断横滑竖滑能否能合并，不能合并，则Game Failed。能合并则Normal
        if (mBlanks.size() == 0) {
            for (int i = 0; i < mGameLines; i++) {
                for (int j = 0; j < mGameLines; j++) {
                    if (j < mGameLines - 1) {
                        if (mGameMatrix[i][j].getNum() == mGameMatrix[i][j + 1].getNum()) {
                            return 1;
                        }
                    }
                    if (i < mGameLines - 1) {
                        if (mGameMatrix[i][j].getNum() == mGameMatrix[i + 1][j].getNum()) {
                            return 1;
                        }
                    }
                }
            }
            return 0;
        }
        // 判断最高分的格子有没有达到目标，有就update Goal
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                if (mGameMatrix[i][j].getNum() == Config.getGoal()) {
                    return 2;
                }
            }
        }
        return 1;
    }

    private void saveHistoryMatrix() {
        mScoreHistory = Config.getScore();
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                mGameMatrixHistory[i][j] = mGameMatrix[i][j].getNum();
            }
        }
    }

    private void judgeDirection(int offsetX, int offsetY) {
        Log.d(TAG, "judgeDirection offsetX = " + offsetX + ", offsetY = " + offsetY);
        if (Math.abs(offsetX) > Math.abs(offsetY)) {
            if (offsetX > mDensity * 10) {
                swipeRight();
            } else if (offsetX < mDensity * (-10)){
                swipeLeft();
            }
        } else {
            if (offsetY > mDensity * 10) {
                swipeDown();
            } else if (offsetY < mDensity * (-10)){
                swipeUp();
            }
        }
    }

    private boolean isMoved() {
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                if (mGameMatrixHistory[i][j] != mGameMatrix[i][j].getNum()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void swipeUp() {
        Log.d(TAG, "swipeUp");
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                int currentNum = mGameMatrix[j][i].getNum();
                if (currentNum != 0) {
                    if (mKeyItemNum == -1) {
                        // this case
                        mKeyItemNum = currentNum;
                    } else {
                        if (mKeyItemNum == currentNum) {
                            mCalList.add(mKeyItemNum * 2);
                            Config.updateScore(mKeyItemNum);
                            mKeyItemNum = -1;
                        } else {
                            mCalList.add(mKeyItemNum);
                            mKeyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            // for this case, add number
            if (mKeyItemNum != -1) {
                mCalList.add(mKeyItemNum);
            }
            // update item which with not-zero number
            for (int j = 0; j < mCalList.size(); j++) {
                mGameMatrix[j][i].setNum(mCalList.get(j));
            }
            // update item which should be zero
            for (int k = mCalList.size(); k < mGameLines; k++) {
                mGameMatrix[k][i].setNum(0);
            }

            mKeyItemNum = -1;
            mCalList.clear();
        }

    }

    private void swipeDown() {
        Log.d(TAG, "swipeDown");
        for (int i = mGameLines - 1; i >= 0; i--) {
            for (int j = mGameLines - 1; j >= 0; j--) {
                int currentNum = mGameMatrix[j][i].getNum();
                if (currentNum != 0) {
                    if (mKeyItemNum == -1) {
                        mKeyItemNum = currentNum;
                    } else {
                        if (mKeyItemNum == currentNum) {
                            mCalList.add(mKeyItemNum * 2);
                            Config.updateScore(mKeyItemNum);
                            mKeyItemNum = -1;
                        } else {
                            mCalList.add(mKeyItemNum);
                            mKeyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (mKeyItemNum != -1) {
                mCalList.add(mKeyItemNum);
            }
            for (int j = 0; j < mGameLines - mCalList.size(); j++) {
                mGameMatrix[j][i].setNum(0);
            }
            int index = mCalList.size() - 1;
            for (int k = mGameLines - mCalList.size(); k < mGameLines; k++) {
                mGameMatrix[k][i].setNum(mCalList.get(index));
                index--;
            }
            mKeyItemNum = -1;
            mCalList.clear();
        }
    }

    private void swipeLeft() {
        Log.d(TAG, "swipeLeft");
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                int currentNum = mGameMatrix[i][j].getNum();
                if (currentNum != 0) {
                    if (mKeyItemNum == -1) {
                        mKeyItemNum = currentNum;
                    } else {
                        if (mKeyItemNum == currentNum) {
                            mCalList.add(mKeyItemNum * 2);
                            Config.updateScore(mKeyItemNum);
                            mKeyItemNum = -1;
                        } else {
                            mCalList.add(mKeyItemNum);
                            mKeyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (mKeyItemNum != -1) {
                mCalList.add(mKeyItemNum);
            }
            for (int j = 0; j < mCalList.size(); j++) {
                mGameMatrix[i][j].setNum(mCalList.get(j));
            }
            for (int k = mCalList.size(); k < mGameLines; k++) {
                mGameMatrix[i][k].setNum(0);
            }
            mKeyItemNum = -1;
            mCalList.clear();
        }
    }

    private void swipeRight() {
        Log.d(TAG, "swipeRight");
        for (int i = mGameLines - 1; i >= 0; i--) {
            for (int j = mGameLines - 1; j >= 0; j--) {
                int currentNum = mGameMatrix[i][j].getNum();
                if (currentNum != 0) {
                    if (mKeyItemNum == -1) {
                        mKeyItemNum = currentNum;
                    } else {
                        if (mKeyItemNum == currentNum) {
                            mCalList.add(mKeyItemNum * 2);
                            Config.updateScore(mKeyItemNum);
                            mKeyItemNum = -1;
                        } else {
                            mCalList.add(mKeyItemNum);
                            mKeyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (mKeyItemNum != -1) {
                mCalList.add(mKeyItemNum);
            }
            for (int j = 0; j < mGameLines - mCalList.size(); j++) {
                mGameMatrix[i][j].setNum(0);
            }
            int index = mCalList.size() - 1;
            for (int k = mGameLines - mCalList.size(); k < mGameLines; k++) {
                mGameMatrix[i][k].setNum(mCalList.get(index));
                index--;
            }
            mKeyItemNum = -1;
            mCalList.clear();
        }
    }

    public void revertGame() {
        Log.d(TAG, "revertGame");
        int sum = 0;
        for(int[] element : mGameMatrixHistory) {
            for (int i : element) {
                sum += i;
            }
        }
        if (sum != 0) {
            Config.setScore(mScoreHistory);
            Game.getGameActivity().setScore();
            for (int i = 0; i < mGameLines; i++) {
                for (int j = 0; j < mGameLines; j++) {
                    mGameMatrix[i][j].setNum(mGameMatrixHistory[i][j]);
                }
            }
        }
    }

}
