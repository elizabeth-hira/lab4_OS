import java.util.ArrayList;
import java.util.Queue;

public class Consumer implements Runnable{
    private final Queue<ArrayList<Integer>> queue;

    public Consumer(Queue<ArrayList<Integer>> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try { queue.wait(); }
                    catch (Exception e) { e.printStackTrace(); }
                }

                ArrayList<Integer> poll = queue.poll();
                double sum = 0;
                for (int item : poll) {
                    sum += item;
                }

                System.out.println(sum / poll.size());
                queue.notify();
            }
        }
    }
}