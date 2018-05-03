package com.raindus.pymdo.focus;


import com.raindus.pymdo.R;

/**
 * Created by Raindus on 2018/4/12.
 */

public class FocusLayer {

    public static final int LAYER_SIZE = 3;

    public static final int[] LAYER_COLOR = {R.color.focus_pymdo, R.color.focus_love, R.color.focus_positive};

    public static final String[] MUSIC_DESCRIBE = {"晨露的森林", "爱在人间", "Positive Outlook"};

    public interface OnLayerChangerListener {
        void onLayerSelected(int position);
    }

    public interface OnLayerScrolledListener {
        void onLayerScrolled(int position, float positionOffset);
    }

    public interface OnLayerStaticListener {
        void onLayerStatic(boolean isStatic);
    }
}
