import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Producer implements Runnable{
    private final Queue<ArrayList<Integer>> queue;
    private final AtomicBoolean stop;
    private final int delay;

    Producer(Queue<ArrayList<Integer>> queue, AtomicBoolean stop, int delay) {
        this.queue = queue;
        this.stop = stop;
        this.delay = delay;
    }

    @Override
    public void run() {
        while (!stop.get()) {
            synchronized (queue) {
                Random random = new Random(System.currentTimeMillis());
                ArrayList<Integer> list = new ArrayList<>();
                for (int j = 0; j < 10; j++) {
                    list.add(random.nextInt());
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