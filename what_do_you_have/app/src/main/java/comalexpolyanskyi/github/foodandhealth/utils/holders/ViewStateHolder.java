package comalexpolyanskyi.github.foodandhealth.utils.holders;


import android.graphics.drawable.Drawable;

public class ViewStateHolder {

    private Drawable drawable;
    private String label;
    private boolean isLike;

    public ViewStateHolder(Drawable drawable, String label, boolean isLike){
        this.drawable = drawable;
        this.label = label;
        this.isLike = isLike;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getLabel() {
        return label;
    }

    public boolean isLike() {
        return isLike;
    }


}
