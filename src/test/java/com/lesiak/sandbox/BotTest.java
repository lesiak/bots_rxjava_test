package com.lesiak.sandbox;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.concurrent.TimeUnit;


public class BotTest {

    private TestScheduler testScheduler;

    @Before
    public void before() {
        testScheduler = new TestScheduler();
    }

    @Test
    public void testSetLocation() {
        Bot bot = new Bot(null, testScheduler);
        TestObserver<String> subscriber = bot.getPositionsFilteredObservable().test();
        bot.setLocation(1, 2);
        bot.setLocation(3, 4);
        subscriber.assertValues("1-2");
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        subscriber.assertValues("1-2", "3-4");
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        subscriber.assertValues("1-2", "3-4");
        bot.setLocation(5, 6);
        subscriber.assertValues("1-2", "3-4", "5-6");

    }

}