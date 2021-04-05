import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer {
    private final Queue<ArrayList<Integer>> queue;
    private final ExecutorService consumers;
    private  final AtomicBoolean stop;

    Consumer(final Queue<ArrayList<Integer>> queue, final AtomicBoolean stop) {
        this.consumers = Executors.newFixedThreadPool(3);
        this.queue = queue;
        this.stop = stop;
    }

    void consuming() {
        for (int i = 0; i < 3; i++) {
            final Thread consumer = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!stop.get()) {
                        synchronized (queue) {
                            while (queue.isEmpty()) {
                                try {
                                    queue.wait();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            final ArrayList<Integer> poll;
                            poll = queue.poll();
                            final Integer size = poll.size();
                            Integer sum = 0;
                            for (int j = 0; j < size; j++) {
                                sum += poll.get(j);
                            }
                            System.out.println(sum / size);
                            queue.notifyAll();
                        }
                    }
                }
            });
            this.consumers.submit(consumer);
        }
    }
}
