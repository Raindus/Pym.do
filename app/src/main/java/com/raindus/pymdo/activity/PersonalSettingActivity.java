package com.raindus.pymdo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raindus.pymdo.R;
import com.raindus.pymdo.common.FileUtils;
import com.raindus.pymdo.common.ImageFilter;
import com.raindus.pymdo.common.Utils;
import com.raindus.pymdo.dialog.EditorDialog;
import com.raindus.pymdo.personal.CoverParam;
import com.raindus.pymdo.personal.PersonalPopup;

public class PersonalSettingActivity extends BaseActivity implements PersonalPopup.OnPersonalOperateListener {

    // 不计算图片密度。图片宽高乘积不超过500k
    static final int BITMAP_MAX_SIZE = 512 * 1024;

    public static final int TAG_NICKNAME = 1;
    public static final int TAG_COVER = 2;
    public static final int TAG_BANNER = 3;
    public static final int TAG_REPORT = 4;

    // 昵称
    private RelativeLayout mRlNickname;
    private TextView mTvNickname;

    // 封面
    private RelativeLayout mRlCover;
    private ImageView mIvCover;

    // banner
    private RelativeLayout mRlBanner;
    private ImageView mIvBanner;

    // 报告
    private RelativeLayout mRlReport;
    private ImageView mIvReport;

    private PersonalPopup mPopup;

    private boolean mIsUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        setTransitionAnimation();
        initTitleBar(getResources().getString(R.string.main_personal), false);
        initView();
        initData();
    }

    private void initView() {
        mRlNickname = findViewById(R.id.personal_rl_nickname);
        mRlNickname.setOnClickListener(this);
        mTvNickname = findViewById(R.id.personal_tv_nickname);

        mRlCover = findViewById(R.id.personal_rl_cover);
        mRlCover.setOnClickListener(this);
        mIvCover = findViewById(R.id.personal_iv_cover);
        mIvCover.setTag(TAG_COVER);

        mRlBanner = findViewById(R.id.personal_rl_banner);
        mRlBanner.setOnClickListener(this);
        mIvBanner = findViewById(R.id.personal_iv_banner);
        mIvBanner.setTag(TAG_BANNER);

        mRlReport = findViewById(R.id.personal_rl_report);
        mRlReport.setOnClickListener(this);
        mIvReport = findViewById(R.id.personal_iv_report);
        mIvReport.setTag(TAG_REPORT);

        mPopup = new PersonalPopup(this, mRlNickname);
        mPopup.setOnPersonalOperateListener(this);
    }

    private void initData() {
        FileUtils.createDir(FileUtils.DIR_PERSONAL);
        setNickName(CoverParam.getNickname(), CoverParam.isNicknameDefault(), false);

        setImage(mIvCover, CoverParam.getCover(), CoverParam.isCoverDefault(), false);
        setImage(mIvBanner, CoverParam.getBanner(), CoverParam.isBannerDefault(), false);
        setImage(mIvReport, CoverParam.getReport(), CoverParam.isReportDefault(), false);
    }

    private void setNickName(String name, boolean isDefault, boolean update) {
        mTvNickname.setText(Utils.getStringByKey(name, isDefault));
        if (update) {
            mIsUpdate = true;
            CoverParam.setNickname(name);
        }
    }

    // 展示图片,控制图片大小
    private void setImage(ImageView imageView, String path, boolean isDefault, boolean update) {
        Bitmap temp = Utils.scaleBitmap(path, isDefault, BITMAP_MAX_SIZE);
        if (temp != null) {
            imageView.setImageBitmap(temp);
        } else { // 恢复默认
            temp = Utils.scaleBitmap(recover((int) imageView.getTag()), true, BITMAP_MAX_SIZE);
            imageView.setImageBitmap(temp);
        }
        if (update) {
            mIsUpdate = true;
            switch ((int) imageView.getTag()) {
                case TAG_COVER:
                    CoverParam.setCover(path);
                    break;
                case TAG_BANNER:
                    CoverParam.setBanner(path);
                    break;
                case TAG_REPORT:
                    CoverParam.setReport(path);
                    break;
            }
        }
    }

    // recover and return default
    private String recover(int tag) {
        switch (tag) {
            case TAG_COVER:
                CoverParam.setCover(CoverParam.DEFAULT_COVER);
                return CoverParam.DEFAULT_COVER;
            case TAG_BANNER:
                CoverParam.setBanner(CoverParam.DEFAULT_BANNER);
                return CoverParam.DEFAULT_BANNER;
            case TAG_REPORT:
                CoverParam.setReport(CoverParam.DEFAULT_REPORT);
                return CoverParam.DEFAULT_REPORT;
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.personal_rl_nickname:
                mPopup.show(TAG_NICKNAME, CoverParam.isNicknameDefault());
                break;
            case R.id.personal_rl_cover:
                mPopup.show(TAG_COVER, CoverParam.isCoverDefault());
                break;
            case R.id.personal_rl_banner:
                mPopup.show(TAG_BANNER, CoverParam.isBannerDefault());
                break;
            case R.id.personal_rl_report:
                mPopup.show(TAG_REPORT, CoverParam.isReportDefault());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mIsUpdate)
            setResult(RESULT_OK);
    }

    @Override
    public void onUpdateText(int tag) {
        EditorDialog editorDialog = new EditorDialog(this, EditorDialog.TAG_NICKNAME);
        editorDialog.setOnEditorListener(new EditorDialog.OnEditorListener() {
            @Override
            public void onEditor(String text) {
                if (TextUtils.isEmpty(text))
                    toast("昵称不能为空");
                else
                    setNickName(text, false, true);
            }
        });
        editorDialog.show();
    }

    @Override
    public void onChoosePhoto(int tag) {
        choosePhoto(tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK)
            return;

        String imagePath = Utils.handleImageOnKitKat(data);
        if (TextUtils.isEmpty(imagePath))
            return;

        String filter;

        switch (requestCode) {// 过滤
            case TAG_COVER:
                filter = ImageFilter.PersonalFilter.cover(imagePath);
                if (filter != null) {
                    toast(filter);
                    return;
                }
                if (FileUtils.copyPicture(imagePath, FileUtils.PERSONAL_COVER_PATH)) {
                    setImage(mIvCover, FileUtils.PERSONAL_COVER_PATH, false, true);
                }
                break;
            case TAG_BANNER:
                filter = ImageFilter.PersonalFilter.banner(imagePath);
                if (filter != null) {
                    toast(filter);
                    return;
                }
                if (FileUtils.copyPicture(imagePath, FileUtils.PERSONAL_BANNER_PATH)) {
                    setImage(mIvBanner, FileUtils.PERSONAL_BANNER_PATH, false, true);
                }
                break;
            case TAG_REPORT:
                filter = ImageFilter.PersonalFilter.banner(imagePath);
                if (filter != null) {
                    toast(filter);
                    return;
                }
                if (FileUtils.copyPicture(imagePath, FileUtils.PERSONAL_REPORT_PATH)) {
                    setImage(mIvReport, FileUtils.PERSONAL_REPORT_PATH, false, true);
                }
                break;
        }
    }

    @Override
    public void onRecover(int tag) {
        switch (tag) {
            case TAG_NICKNAME:
                setNickName(CoverParam.DEFAULT_NICKNAME, true, true);
                break;
            case TAG_COVER:
                setImage(mIvCover, CoverParam.DEFAULT_COVER, true, true);
                break;
            case TAG_BANNER:
                setImage(mIvBanner, CoverParam.DEFAULT_BANNER, true, true);
                break;
            case TAG_REPORT:
                setImage(mIvReport, CoverParam.DEFAULT_REPORT, true, true);
                break;
        }
    }
}
