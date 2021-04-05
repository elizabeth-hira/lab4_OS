import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Producer {
    private final Queue<ArrayList<Integer>> queue;
    private final ExecutorService producers;
    private final AtomicBoolean stop;
    private final Integer seconds;

    Producer(final Queue<ArrayList<Integer>> queue, AtomicBoolean stop, Integer seconds) {
        this.producers = Executors.newFixedThreadPool(3);
        this.queue = queue;
        this.stop = stop;
        this.seconds = seconds;
    }

    void producing() {
        for (int i = 0; i < 3; i++) {
            final Thread producer = new Thread(new Runnable() {
                private final Random random = new Random();
                @Override
                public void run() {
                    while (!stop.get()) {
                        synchronized (queue) {
                            ArrayList<Integer> list = new ArrayList<>();
                            for (int j = 0; j < 10; j++) {
                                list.add(this.random.nextInt());
                            }
                            queue.add(list);
                            queue.notifyAll();
                            try {
                                Thread.sleep(seconds);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            this.producers.submit(producer);
        }
    }
}
