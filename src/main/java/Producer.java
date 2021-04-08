import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Producer implements Runnable{
    private final Queue<ArrayList<Integer>> queue;
    private final AtomicBoolean stop;
    private final int delay;
    private final int capacity;

    public Producer(Queue<ArrayList<Integer>> queue, AtomicBoolean stop, int capacity, int delay) {
        this.queue = queue;
        this.stop = stop;
        this.delay = delay;
        this.capacity = capacity;
    }

    @Override
    public void run() {
        while (!stop.get()) {
            synchronized (queue) {
                while (queue.size() == capacity) {
                    try { queue.wait(); }
                    catch (Exception e) { e.printStackTrace(); }
                }

                Random random = new Random(System.currentTimeMillis());
                ArrayList<Integer> list = new ArrayList<>();
                for (int j = 0; j < 10; j++) {
                    list.add(random.nextInt());
                }
                queue.add(list);
                queue.notifyAll();

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}