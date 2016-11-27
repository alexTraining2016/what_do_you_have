package comalexpolyanskyi.github.foodandhealth.utils.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

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
    private static final String DELETED = "deleted";
    private File cacheDir;
    private static FileCache fileCache;
    private ConcurrentHashMap<String, Long> map = null;
    private long cacheMaxSize = 150 * 1024 * 1024;
    private final Object lock = new Object();
    private final Object deleteLock = new Object();

    private FileCache(Context context, long cacheMaxSize) {
        cacheDir = new File(context.getCacheDir(), TEMP_IMAGES);

        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        if (cacheDir.getFreeSpace() < cacheMaxSize) {
            this.cacheMaxSize = cacheDir.getFreeSpace();
        } else {
            this.cacheMaxSize = cacheMaxSize;
        }
    }


    private void save(String filename, Bitmap bitmap) {
        map.put(filename, System.currentTimeMillis());
        final File f = getFile(filename);
        final File mappingFile = getFile(MAPPING_FILE);
        ObjectOutputStream out = null;
        FileOutputStream outStream = null;

        try {
            if (f != null) {
                outStream = new FileOutputStream(f);
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);

            if (mappingFile != null) {
                outStream = new FileOutputStream(mappingFile);
            }

            out = new ObjectOutputStream(outStream);
            out.writeObject(map);
        } catch (IOException e) {
            if (f != null) {
                boolean isSuccess = f.delete();
                Log.e("", DELETED + isSuccess);
            }

            if (mappingFile != null) {
                boolean isSuccess = mappingFile.delete();
                Log.e("", DELETED + isSuccess);
            }
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }

                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteOldFile() {
        final List<Map.Entry<String, Long>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        final int currentCacheSize = map.size();

        for (Map.Entry<String, Long> entry : map.entrySet()) {
            File f = getFile(entry.getKey());

            if (f != null) {
                boolean isSuccess = f.delete();
                Log.i("", DELETED + isSuccess);
            }
            map.remove(entry.getKey());

            if (currentCacheSize * 0.8 >= map.size()) {
                break;
            }
        }
    }

    private long getFolderSize() {
        long length = 0;

        for (File file : cacheDir.listFiles()) {
            length += file.length();
        }

        return length;
    }

    private boolean isCacheOverfull() {
        return getFolderSize() >= cacheMaxSize;
    }

    public void put(String url, Bitmap bitmap) {
        if (bitmap == null) {
            return;

        }

        synchronized (lock) {
            try {
                getMappingFile();
            } catch (Exception e) {
                int DEFAULT_COUNT = 70;
                map = new ConcurrentHashMap<>(DEFAULT_COUNT);
            }
        }

        if (isCacheOverfull()) {
            synchronized (deleteLock) {
                deleteOldFile();
            }
        }
        save(url, bitmap);
    }

    public static FileCache initialFileCache(Context context, long cacheMaxSize) {
        if (fileCache == null) {
            fileCache = new FileCache(context, cacheMaxSize);

        }

        return fileCache;
    }

    public Bitmap get(String url) {
        try {
            getMappingFile();

            if (map == null || map.get(url) == null) {
                return null;
            } else {
                File f = getFile(url);

                if (f != null) {
                    return BitmapFactory.decodeStream(new FileInputStream(f));
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    private File getFile(String url) {
        final String filename = String.valueOf(url.hashCode());

        return new File(cacheDir, filename);
    }

    @SuppressWarnings("unchecked")
    private void getMappingFile() throws IOException, ClassNotFoundException {
        final File f = getFile(MAPPING_FILE);

        if (f == null) {
            return;
        }

        FileInputStream fIn = new FileInputStream(f);
        ObjectInputStream oIn = new ObjectInputStream(fIn);
        map = (ConcurrentHashMap<String, Long>) oIn.readObject();

        fIn.close();
        oIn.close();
    }
}