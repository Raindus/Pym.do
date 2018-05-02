package com.raindus.pymdo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.raindus.pymdo.R;

/**
 * Created by Raindus on 2018/5/1.
 */

public class BaseDialog extends Dialog implements View.OnClickListener {

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    protected void initTitleBar(String title, boolean positive) {
        findViewById(R.id.title_bar_negative).setOnClickListener(this);
        ((TextView) findViewById(R.id.title_bar_title)).setText(title);
        if (positive) {
            findViewById(R.id.title_bar_positive).setVisibility(View.VISIBLE);
            findViewById(R.id.title_bar_positive).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_negative:
                dismiss();
                break;
            case R.id.title_bar_positive:
                onPositive();
                dismiss();
                break;
        }
    }

    protected void onPositive() {

    }
}
