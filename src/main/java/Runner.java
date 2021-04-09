import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Runner {
    private final Queue<ArrayList<Integer>> queue;
    private final int delay;
    private final int consumerNumber;
    private final int producerNumber;
    private final int queueCapacity;

    private final ExecutorService pool;

    public Runner(int delay, int consumerNumber, int producerNumber, int queueCapacity) {
        this.queue = new LinkedList<>();
        this.delay = delay;
        this.consumerNumber = consumerNumber;
        this.producerNumber = producerNumber;
        this.queueCapacity = queueCapacity;

        pool = Executors.newCachedThreadPool();
    }

    public void start() {
        for(int i = 0; i < consumerNumber; i++) {
            pool.execute(new Consumer(queue));
        }
        for(int i = 0; i < producerNumber; i++) {
            pool.execute(new Producer(queue, queueCapacity, delay));
        }
    }

    public static void main(String[] args) {
        new Runner(300, 2, 5, 5).start();
    }
}