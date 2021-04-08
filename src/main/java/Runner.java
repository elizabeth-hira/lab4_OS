import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Runner {
    private final AtomicBoolean stop;
    private final Queue<ArrayList<Integer>> queue;
    private final int delay;
    private final int consumerNumber;
    private final int producerNumber;
    private final int deathTime;
    private final int queueCapacity;

    private final ExecutorService pool;

    public Runner(int delay, int consumerNumber, int producerNumber, int queueCapacity, int deathTime) {
        this.stop = new AtomicBoolean(false);
        this.queue = new LinkedList<>();
        this.delay = delay;
        this.consumerNumber = consumerNumber;
        this.producerNumber = producerNumber;
        this.queueCapacity = queueCapacity;

        pool = Executors.newCachedThreadPool();
        this.deathTime = deathTime;
    }

    public void start() {
        for(int i = 0; i < consumerNumber; i++) {
            pool.execute(new Consumer(queue, stop));
        }
        for(int i = 0; i < producerNumber; i++) {
            pool.execute(new Producer(queue, stop, queueCapacity, delay));
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
        new Runner(150, 3, 2, 5,10000).start();
    }
}