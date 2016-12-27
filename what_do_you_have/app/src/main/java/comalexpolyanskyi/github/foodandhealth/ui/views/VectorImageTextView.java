package comalexpolyanskyi.github.foodandhealth.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import comalexpolyanskyi.github.foodandhealth.R;

public class VectorImageTextView extends TextView {

    public VectorImageTextView(final Context context) {
        super(context, null, R.attr.icTextViewStyle);
    }

    public VectorImageTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs, R.attr.icTextViewStyle);

        init(attrs, R.attr.icTextViewStyle);
    }

    public VectorImageTextView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, R.attr.icTextViewStyle);

        init(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VectorImageTextView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, R.attr.icTextViewStyle, defStyleRes);

        init(attrs, defStyleAttr);
    }

    public void setRightDrawable(int drawableId) {
        final AppCompatDrawableManager drawableManager = AppCompatDrawableManager.get();
        final Drawable drawable = drawableManager.getDrawable(getContext(), drawableId);

        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    private void init(final AttributeSet attrs, final int defStyleAttr) {
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.icTextViewStyle, defStyleAttr, 0);

        try {
            final AppCompatDrawableManager drawableManager = AppCompatDrawableManager.get();
            Drawable leftIcon = a.getDrawableIfKnown(R.styleable.icTextViewStyle_leftIcon);

            int id = a.getResourceId(R.styleable.icTextViewStyle_leftIcon, -1);
            if (id != -1) {
                leftIcon = drawableManager.getDrawable(getContext(), id);
            }
            Drawable rightIcon = a.getDrawableIfKnown(R.styleable.icTextViewStyle_rightIcon);

            id = a.getResourceId(R.styleable.icTextViewStyle_rightIcon, -1);
            if (id != -1) {
                rightIcon = drawableManager.getDrawable(getContext(), id);
            }
            Drawable bottomIcon = a.getDrawableIfKnown(R.styleable.icTextViewStyle_bottomIcon);
            id = a.getResourceId(R.styleable.icTextViewStyle_bottomIcon, -1);
            if (id != -1) {
                bottomIcon = drawableManager.getDrawable(getContext(), id);
            }
            setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, rightIcon, bottomIcon);

        } finally {
            a.recycle();
        }
    }

}