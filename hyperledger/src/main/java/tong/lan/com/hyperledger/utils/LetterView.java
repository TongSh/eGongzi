package tong.lan.com.hyperledger.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LetterView extends LinearLayout {
    private Context mContext;
    private CharacterClickListener mListener;

    public LetterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        setOrientation(VERTICAL);

        initView();
    }

    private void initView() {
        for (char i = 'A'; i <= 'Z'; i++) {
            final String character = i + "";
            TextView tv = buildTextLayout(character);
            addView(tv);
        }

        addView(buildTextLayout("#"));
    }

    private TextView buildTextLayout(final String character) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);

        TextView tv = new TextView(mContext);
        tv.setLayoutParams(layoutParams);
        tv.setGravity(Gravity.CENTER);
        tv.setClickable(true);

        tv.setText(character);

        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.clickCharacter(character);
                }
            }
        });
        return tv;
    }



    public void setCharacterListener(CharacterClickListener listener) {
        mListener = listener;
    }

    public interface CharacterClickListener {
        void clickCharacter(String character);
    }
}

