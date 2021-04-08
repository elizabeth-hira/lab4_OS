import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer implements Runnable{
    private final Queue<ArrayList<Integer>> queue;
    private  final AtomicBoolean stop;

    public Consumer(Queue<ArrayList<Integer>> queue, AtomicBoolean stop) {
        this.queue = queue;
        this.stop = stop;
    }

    @Override
    public void run() {
        while (!stop.get()) {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try { queue.wait(); }
                    catch (Exception e) { e.printStackTrace(); }
                }

                ArrayList<Integer> poll = queue.poll();
                int sum = 0;
                for (int item : poll) {
                    sum += item;
                }

                System.out.println(sum / poll.size());
                queue.notifyAll();
            }
        }
    }
}