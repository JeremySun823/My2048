package com.example.sun.my2048;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Game extends Activity implements View.OnClickListener {
    private static final String TAG = "sunwillfly Game";


    private static Game mGame;
    private TextView mTvGoal;
    private TextView mTvScore;
    private TextView mTvRecord;

    private Button mBtnRevert;
    private Button mBtnRestart;
    private Button mBtnOptions;

    private GameView mGameView;

    public Game() {
        mGame = this;
    }

    public static Game getGameActivity() {
        return mGame;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mGameView = new GameView(this);
//        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.game_panel);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.game_panel_rl);
        relativeLayout.addView(mGameView);
    }

    private void initView() {
        mTvGoal = (TextView) findViewById(R.id.tv_goal);
        mTvScore = (TextView) findViewById(R.id.score);
        mTvRecord = (TextView) findViewById(R.id.record);

        mBtnRevert = (Button) findViewById(R.id.btn_revert);
        mBtnRestart = (Button) findViewById(R.id.btn_restart);
        mBtnOptions = (Button) findViewById(R.id.btn_options);

        mBtnRevert.setOnClickListener(this);
        mBtnRestart.setOnClickListener(this);
        mBtnOptions.setOnClickListener(this);

        setGoal();
        setRecord();
        setScore();

    }

    public void setScore() {
        mTvScore.setText("" + Config.getScore());
    }

    public void setRecord() {
        mTvRecord.setText("" + Config.getRecord());
    }

    public void setMissionComplete() {
        Config.updateGoal();
        setGoal();
    }

    public void setNewRecord() {
        Config.setRecord();
        setRecord();
    }

    public void setGoal() {
        mTvGoal.setText("" + Config.getGoal());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_revert:
                mGameView.revertGame();
                break;
            case R.id.btn_restart:
                mGameView.startGame();
                break;
            case R.id.btn_options:
                break;
        }

    }
}
