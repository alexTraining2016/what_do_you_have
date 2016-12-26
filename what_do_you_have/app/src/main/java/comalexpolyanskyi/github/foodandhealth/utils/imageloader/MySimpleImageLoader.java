package comalexpolyanskyi.github.foodandhealth.utils.imageloader;

import android.support.v7.graphics.Palette;
import android.widget.ImageView;

public interface MySimpleImageLoader {

    void loadImageFromUrl(final String url, final ImageView imageView, final Palette.PaletteAsyncListener paletteListener);

    class Impl {

        public static MySimpleImageLoader newInstance() {
            return new ImageLoader();
        }

    }
}
