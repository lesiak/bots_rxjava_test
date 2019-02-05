package com.lesiak.sandbox;

import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.time.Instant;


public class BotTest {

    @Test
    public void testSetLocation() throws InterruptedException {
        Bot bot = new Bot(null);
        TestObserver<String> subscriber = bot.getPositionsFilteredObservable().test();
        Instant now = Instant.now();
        bot.setLocation(1, 2, now);

        bot.setLocation(3, 4, now);
        subscriber.assertValues("1-2");
        Thread.sleep(1200);
        subscriber.assertValues("1-2", "3-4");
        Thread.sleep(1200);
        subscriber.assertValues("1-2", "3-4");
        Instant time = now.plusMillis(Bot.REPORT_STATUS_INTERVAL + 1);
        bot.setLocation(5, 6, time);
        subscriber.assertValues("1-2", "3-4", "5-6");

    }

}