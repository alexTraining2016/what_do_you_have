package comalexpolyanskyi.github.foodandhealth.utils;

/**
 * Created by Алексей on 17.10.2016.
 */

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