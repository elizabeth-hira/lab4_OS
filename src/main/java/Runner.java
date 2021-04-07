import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Runner {
    private final AtomicBoolean stop;
    private final Queue<ArrayList<Integer>> queue;
    private final int delay;
    private final int number;
    private final int deathTime;

    private final ExecutorService pool;

    Runner(int delay, int number, int deathTime) {
        this.stop = new AtomicBoolean(false);
        this.queue = new LinkedList<>();
        this.delay = delay;
        this.number = number;

        pool = Executors.newFixedThreadPool(2 * number);
        this.deathTime = deathTime;
    }

    public void start() {
        for(int i = 0; i < number; i++) {
            pool.execute(new Consumer(queue, stop));
            pool.execute(new Producer(queue, stop, delay));
        }

        new Thread(() -> {
            try {
                Thread.sleep(deathTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop.set(true);
            pool.shutdown();
        }).start();
    }

    public static void main(String[] args) {
        new Runner(150, 3, 10000).start();
    }
}