import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public class Producer implements Runnable{
    private final Queue<ArrayList<Integer>> queue;
    private final int delay;
    private final int capacity;

    public Producer(Queue<ArrayList<Integer>> queue, int capacity, int delay) {
        this.queue = queue;
        this.delay = delay;
        this.capacity = capacity;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (queue) {
                while (queue.size() == capacity) {
                    try { queue.wait(); }
                    catch (Exception e) { e.printStackTrace(); }
                }

                Random random = new Random(System.currentTimeMillis());
                ArrayList<Integer> list = new ArrayList<>();
                for (int j = 0; j < 10; j++) {
                    list.add(random.nextInt(11));
                }
                queue.add(list);
                queue.notify();

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}