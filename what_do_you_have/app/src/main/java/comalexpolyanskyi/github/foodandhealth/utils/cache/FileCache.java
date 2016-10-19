package comalexpolyanskyi.github.foodandhealth.utils.cache;

import android.content.Context;
import java.io.File;

public class FileCache {

    private static final String TEMP_IMAGES = "TempImages";
    private File cacheDir;
    private static FileCache fileCache;

    private FileCache(Context context){
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(), TEMP_IMAGES);
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public static FileCache initialFileCache(Context context){
        if(fileCache == null){
            fileCache = new FileCache(context);
        }
        return fileCache;
    }

    @Override
    protected void finalize() throws Throwable {
        clear();
        super.finalize();
    }

    public static FileCache getFileCache(){
        return fileCache;
    }

    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}