package comalexpolyanskyi.github.foodandhealth.utils.holders;

import android.content.Context;

public enum ContextHolder {

    INSTANCE;

    private Context mContext;

    public static Context getContext() {
        return INSTANCE.mContext;
    }

    public static void setContext(final Context pContext) {
        INSTANCE.mContext = pContext;
    }

}