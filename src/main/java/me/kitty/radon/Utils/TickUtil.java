package me.kitty.radon.Utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TickUtil {

    private static final Queue<Runnable> TASKS = new ConcurrentLinkedQueue<>();

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Runnable task;

            while ((task = TASKS.poll()) != null) {
                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void runNextTick(Runnable task) {
        TASKS.add(task);
    }

    public static void runLater(int ticks, Runnable task) {
        TASKS.add(new Runnable() {
            int remaining = ticks;

            @Override
            public void run() {
                if (--remaining <= 0) {
                    task.run();
                } else {
                    TASKS.add(this);
                }
            }
        });
    }
}