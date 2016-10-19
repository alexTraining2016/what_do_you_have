package comalexpolyanskyi.github.foodandhealth.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.core.deps.guava.io.ByteStreams;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader extends AbstractImageLoader {

    private Drawable defaultDrawable;

    public ImageLoader(Drawable defaultDrawable) {
        super();
        this.defaultDrawable = defaultDrawable;
    }

    @Override
    public void onDestroy() {
       // memoryCache.evictAll();
    }

    @Override
    protected Bitmap doInBackground(String url) {
        Bitmap bitmap;
        File f=fileCache.getFile(url);
        try {
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            OutputStream os = new FileOutputStream(f);
            os.write(ByteStreams.toByteArray(inputStream));
            os.close();
            bitmap = decodeFile(f);
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap response, ImageView imageView) {
        if(response != null){
            imageView.setImageBitmap(response);
        }else{
            imageView.setImageDrawable(defaultDrawable);
        }
    }
}