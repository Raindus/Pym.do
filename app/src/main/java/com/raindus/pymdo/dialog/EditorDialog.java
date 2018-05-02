package com.raindus.pymdo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.widget.EditText;

import com.raindus.pymdo.R;

/**
 * Created by Raindus on 2018/5/1.
 */

public class EditorDialog extends BaseDialog {

    public static final int TAG_NICKNAME = 1;

    private int mTag;
    private EditText mEtEditor;

    public EditorDialog(@NonNull Context context, int tag) {
        super(context);
        mTag = tag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_editor);
        mEtEditor = findViewById(R.id.editor_et);

        if (mTag == TAG_NICKNAME) {
            initTitleBar(getContext().getResources().getString(R.string.main_nickname), true);
            mEtEditor.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        }

    }

    @Override
    protected void onPositive() {
        if (mListener != null) {
            mListener.onEditor(mEtEditor.getText().toString());
        }
    }

    private OnEditorListener mListener;

    public void setOnEditorListener(OnEditorListener listener) {
        mListener = listener;
    }

    public interface OnEditorListener {
        void onEditor(String text);
    }
}
