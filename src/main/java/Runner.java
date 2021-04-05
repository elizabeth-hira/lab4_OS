import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Runner {
    final private AtomicBoolean stop;
    final private Queue<ArrayList<Integer>> queue;
    final private Integer seconds;

    Runner(Integer seconds) {
        this.stop = new AtomicBoolean(false);
        this.queue = new LinkedList<>();
        this.seconds = seconds;
    }

    public void start() {
        final Consumer consumer = new Consumer(queue, stop);
        final Producer producer = new Producer(queue, stop, seconds);
        producer.producing();
        consumer.consuming();
        final Timer timer = new Timer();
        timer.schedule(new Stopper(), 10000);
    }

    private class Stopper extends TimerTask {
        @Override
        public void run() {
            try {
                stop.set(true);
                Thread.sleep(2000);
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Runner runner = new Runner(150);
        runner.start();
    }
}
