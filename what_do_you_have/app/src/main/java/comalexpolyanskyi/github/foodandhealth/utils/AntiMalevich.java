package comalexpolyanskyi.github.foodandhealth.utils;

import android.widget.ImageView;

/**
 * Created by Алексей on 26.10.2016.
 */

public interface AntiMalevich {

    void loadImageFromUrl(final String imageUrl, final ImageView imageView);

    class Impl {
        public static AntiMalevich newInstance() {
            return new ImageLoader();
        }
    }
}
