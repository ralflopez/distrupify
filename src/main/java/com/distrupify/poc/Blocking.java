package com.distrupify.poc;


import java.util.concurrent.*;

// windows run: mvn clean install exec:java "-Dexec.mainClass=com.distrupify.poc.Blocking" "-Dexec.classpathScope=test"
// run: mvn exec:java -Dexec.mainClass=org.acme.security.jwt.GenerateToken -Dexec.classpathScope=test -Dsmallrye.jwt.sign.key.location=privateKey.pem
public class Blocking {
//    public static void main(String[] args) {
//        final BlockingQueue<String> queue = new LinkedBlockingQueue<>(1);
//
//        final var t1 = new Thread(() -> {
//            for (int i = 0; i < 5; i++) {
//                try {
//                    final var msg = "hello" + i;
//                    queue.put(msg);
//                    System.out.println("Sent: " + msg);
//                    Thread.sleep(2000L);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        t1.start();
//
//        while (true) {
//            try {
//                final var msg = queue.take();
//                System.out.println("Received: " + msg);
//                Thread.sleep(500L);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    // ** FUTURE

    private static final ExecutorService executor
            = Executors.newFixedThreadPool(5);

    public static Future<Void> produce(BlockingQueue<String> queue) {
        System.out.println("Producer Queue: " + Integer.toHexString(System.identityHashCode(queue)));
        return executor.submit(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    final var msg = "hello" + i;
                    queue.put(msg);
                    System.out.println("Sent: " + msg);
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            return null;
        });
    }

    public static void main(String[] args) {
        System.out.println("update");
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(1);
        System.out.println("Main Queue: " + Integer.toHexString(System.identityHashCode(queue)));
        produce(queue);

        while (true) {
            try {
                final var msg = queue.take();
                System.out.println("Received: " + msg);
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}