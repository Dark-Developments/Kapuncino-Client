package coffee.client.feature.utils.Jinx;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JinxExecutor {
    public static ExecutorService executor;

    @PreInit
    public static void init() {
        executor = Executors.newSingleThreadExecutor();
    }

    public static void execute(Runnable task) {
        executor.execute(task);
    }
}
