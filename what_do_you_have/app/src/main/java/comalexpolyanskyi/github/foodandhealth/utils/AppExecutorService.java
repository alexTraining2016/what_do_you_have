package comalexpolyanskyi.github.foodandhealth.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutorService {

    private static ExecutorService executorService;

    public static void install() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
    }

    public static ExecutorService getAppExecutor() throws NullPointerException {
        if (executorService != null) {
            return executorService;
        } else {
            throw new NullPointerException();
        }
    }
}
