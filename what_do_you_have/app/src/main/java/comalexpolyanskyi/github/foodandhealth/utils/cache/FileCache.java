package comalexpolyanskyi.github.foodandhealth.utils.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class FileCache {

    private static final String TEMP_IMAGES = "TempImages";
    private static final String MAPPING_FILE = "mapping";
    private File cacheDir;
    private static int DEFAULT_COUNT = 30;
    private int maxCountFile = DEFAULT_COUNT;
    private static FileCache fileCache;
    private ConcurrentHashMap<String, Long> map = null;
    private final Object lock = new Object();
    private final Object deleteLock = new Object();

    private FileCache(Context context, int maxCountFile){
        this.maxCountFile = maxCountFile;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), TEMP_IMAGES);
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }


    private void save(String filename, Bitmap bitmap){
        map.put(filename, System.currentTimeMillis());
        File f = getFile(filename);
        File mappingFile = getFile(MAPPING_FILE);
        ObjectOutputStream out = null;
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream = new FileOutputStream(mappingFile);
            out = new ObjectOutputStream(outStream);
            out.writeObject(map);
        } catch (IOException e) {
            f.delete();
            mappingFile.delete();
        }finally {
            try {
                outStream.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteOldFile() {
        List<Map.Entry<String, Long>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        for(Map.Entry<String, Long> entry : map.entrySet()) {
            File f = getFile(entry.getKey());
            f.delete();
            map.remove(entry.getKey());
            if(maxCountFile/2 >= map.size()) break;
        }
    }

    private boolean isCacheOverfull(){
        return map.size() >= maxCountFile;
    }

    public void put(String url, Bitmap bitmap){
        if (bitmap == null) {
            return;
        }
        synchronized (lock) {
            try {
                getMappingFile();
            } catch (Exception e) {
                map = new ConcurrentHashMap<>(maxCountFile);
            }
        }
        if (isCacheOverfull()) {
            synchronized (deleteLock) {
                deleteOldFile();
            }
            save(url, bitmap);
        } else {
            save(url, bitmap);
        }
    }

    public static FileCache initialFileCache(Context context, int maxCountFile){
        if(fileCache == null){
            fileCache = new FileCache(context, maxCountFile);
        }
        return fileCache;
    }

    @Override
    protected void finalize() throws Throwable {
        clear();
        super.finalize();
    }

    public Bitmap get(String url) {
        try {
            getMappingFile();
            if(map == null || map.get(url) == null){
                return null;
            }else{
                File f = getFile(url);
                return BitmapFactory.decodeStream(new FileInputStream(f));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    private File getFile(String url){
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
    }

    private void getMappingFile() throws IOException, ClassNotFoundException {
        File f = getFile(MAPPING_FILE);
        if (f == null) return;
        FileInputStream fIn = new FileInputStream(f);
        ObjectInputStream oIn = new ObjectInputStream(fIn);
        map = (ConcurrentHashMap<String, Long>) oIn.readObject();
        fIn.close();
        oIn.close();
    }

    public void clear(){
        File[] files = cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files){
            if(map != null)
            map.clear();
            f.delete();
        }
    }

}