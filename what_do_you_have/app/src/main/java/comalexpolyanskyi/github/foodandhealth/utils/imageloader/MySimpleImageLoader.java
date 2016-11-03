package comalexpolyanskyi.github.foodandhealth.utils.imageloader;

import android.widget.ImageView;

/**
 * Created by Алексей on 26.10.2016.
 */

public interface MySimpleImageLoader {

    void loadImageFromUrl(final String imageUrl, final ImageView imageView);

    class Impl {
        public static MySimpleImageLoader newInstance() {
            return new ImageLoader();
        }
    }
}
