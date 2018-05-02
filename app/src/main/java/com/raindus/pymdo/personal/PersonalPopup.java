package com.raindus.pymdo.personal;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.raindus.pymdo.R;
import com.raindus.pymdo.activity.PersonalSettingActivity;

/**
 * Created by Raindus on 2018/5/1.
 */

public class PersonalPopup implements View.OnClickListener {

    private Context mContext;

    private PopupWindow mPopupWindow;
    private View mPopupView;
    private View mParent;

    private RelativeLayout mRlUpdateNickname;
    private RelativeLayout mRlChoosePhoto;
    private RelativeLayout mRlRecover;

    private int mCurTag;
    private OnPersonalOperateListener mListener;

    public PersonalPopup(Context context, View parent) {
        mContext = context;
        mParent = parent;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupView = inflater.inflate(R.layout.popup_personal, null);

        mRlUpdateNickname = mPopupView.findViewById(R.id.personal_rl_update_nickname);
        mRlUpdateNickname.setOnClickListener(this);
        mRlChoosePhoto = mPopupView.findViewById(R.id.personal_rl_choose_photo);
        mRlChoosePhoto.setOnClickListener(this);
        mRlRecover = mPopupView.findViewById(R.id.personal_rl_recover);
        mRlRecover.setOnClickListener(this);

        mPopupWindow = new PopupWindow(mPopupView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.PopupAnim);
    }

    public void show(int tag, boolean isDefault) {
        if (tag == PersonalSettingActivity.TAG_NICKNAME) {
            mRlUpdateNickname.setVisibility(View.VISIBLE);
            mRlChoosePhoto.setVisibility(View.GONE);
        } else {
            mRlUpdateNickname.setVisibility(View.GONE);
            mRlChoosePhoto.setVisibility(View.VISIBLE);
        }
        mRlRecover.setVisibility(isDefault ? View.GONE : View.VISIBLE);
        mCurTag = tag;
        mPopupWindow.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_rl_update_nickname:
                if (mListener != null)
                    mListener.onUpdateText(mCurTag);
                break;
            case R.id.personal_rl_choose_photo:
                if (mListener != null)
                    mListener.onChoosePhoto(mCurTag);
                break;
            case R.id.personal_rl_recover:
                if (mListener != null)
                    mListener.onRecover(mCurTag);
                break;
        }
        mPopupWindow.dismiss();
    }

    public void setOnPersonalOperateListener(OnPersonalOperateListener listener) {
        mListener = listener;
    }

    public interface OnPersonalOperateListener {
        void onUpdateText(int tag);

        void onChoosePhoto(int tag);

        void onRecover(int tag);
    }
}
