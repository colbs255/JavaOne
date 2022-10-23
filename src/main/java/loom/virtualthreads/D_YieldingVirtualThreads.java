package loom.virtualthreads;

import java.time.Duration;
import java.util.stream.IntStream;

public class D_YieldingVirtualThreads {

    public static void main(String[] args) throws InterruptedException {

        // Let us now create a bunch of virtual threads and see
        // how they can jump from one platform thread to the other.
        // This is a feature that is unique to Loom virtual threads.
        // Because blocking a Loom virtual thread is so cheap, trying
        // to pool them becomes useless.

        var threads =
                IntStream.range(0, 100)
                        .mapToObj(index ->
                                index == 0 ? Thread.ofVirtual().unstarted(() -> {
                                    try {
                                        System.out.println("First");
                                        System.out.println(Thread.currentThread());
                                        Thread.sleep(Duration.ofMillis(20));

                                        System.out.println("second");
                                        System.out.println(Thread.currentThread());
                                        Thread.sleep(Duration.ofMillis(20));

                                        System.out.println("third");
                                        System.out.println(Thread.currentThread());
                                    } catch (Exception e) {

                                    }
                                })
                                        : Thread.ofVirtual().unstarted(() -> {})
                        )
                        .toList();

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }
}