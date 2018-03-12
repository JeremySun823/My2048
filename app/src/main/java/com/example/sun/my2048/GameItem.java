package com.example.sun.my2048;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by sun on 17/8/31.
 */

public class GameItem extends FrameLayout {
    private int mCardShowNum;
    private TextView mTvNum;
    private LayoutParams mParams;

    public GameItem(Context context, int cardShowNum) {
        super(context);
        initCardItem(cardShowNum);
    }

    private void initCardItem(int cardShowNum) {
        setBackgroundColor(Color.GRAY);
        mTvNum = new TextView(getContext());
        setNum(cardShowNum);
        mTvNum.setTextSize(35);
        TextPaint tp = mTvNum.getPaint();
        tp.setFakeBoldText(true);
        mTvNum.setGravity(Gravity.CENTER);
        mParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mParams.setMargins(5, 5, 5, 5);
        addView(mTvNum, mParams);
    }

    public void setNum(int cardShowNum) {
        this.mCardShowNum = cardShowNum;
        if (cardShowNum == 0) {
            mTvNum.setText("");
        } else {
            mTvNum.setText("" + cardShowNum);
        }
        switch (cardShowNum) {
            case 0:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color1));
                break;
            case 2:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color2));
                break;
            case 4:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color3));
                break;
            case 8:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color4));
                break;
            case 16:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color5));
                break;
            case 32:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color6));
                break;
            case 64:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color7));
                break;
            case 128:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color8));
                break;
            case 256:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color9));
                break;
            case 512:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color10));
                break;
            case 1024:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color11));
                break;
            case 2048:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color12));
                break;
            case 4096:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color13));
                break;
            case 8192:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color14));
                break;
            case 16384:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color15));
                break;
            case 32768:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color16));
                break;
            case 65536:
                mTvNum.setBackgroundColor(getResources().getColor(R.color.color17));
                break;
            default:
                mTvNum.setBackgroundColor(0x00112233);
        }
    }

    public int getNum() {
        return mCardShowNum;
    }

    public View getItemView() {
        return mTvNum;
    }
}
