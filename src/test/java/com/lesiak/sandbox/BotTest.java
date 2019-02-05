package com.lesiak.sandbox;

import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.time.Instant;


public class BotTest {

    @Test
    public void testSetLocation() {
        Bot bot = new Bot(null);
        TestObserver subscriber = bot.getPositionsObservable().test();
        Instant now = Instant.now();
        bot.setLocation(1, 2, now);

        bot.setLocation(1, 2, now);
        subscriber.assertValues("1-2");

        Instant time = now.plusMillis(Bot.REPORT_STATUS_INTERVAL + 1);
        bot.setLocation(5, 6, time);

    }

}