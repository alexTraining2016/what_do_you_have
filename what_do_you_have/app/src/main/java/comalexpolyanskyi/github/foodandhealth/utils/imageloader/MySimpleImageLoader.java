package comalexpolyanskyi.github.foodandhealth.utils.imageloader;

import android.widget.ImageView;

public interface MySimpleImageLoader {

    void loadImageFromUrl(final String imageUrl, final ImageView imageView);

    class Impl {

        public static MySimpleImageLoader newInstance() {
            return new ImageLoader();
        }

    }
}
