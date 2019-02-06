package com.lesiak.sandbox;

import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.schedulers.Timed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


public class BotTest {

    private TestScheduler testScheduler;

    @Before
    public void before() {
        testScheduler = new TestScheduler();
        // set calls to Schedulers.computation() to use our test scheduler
        RxJavaPlugins.setComputationSchedulerHandler(ignore -> testScheduler);
    }

    @After
    public void after() {
        // reset it
        RxJavaPlugins.setComputationSchedulerHandler(null);
    }

    @Test
    public void testSetLocation() {
        Bot bot = new Bot();
        TestObserver<Timed<String>> subscriber = bot.getPositionsFilteredObservable().test();
        bot.setLocation(1, 2);
        testScheduler.advanceTimeBy(10, MILLISECONDS);
        bot.setLocation(3, 4);
        subscriber.assertValues(new Timed<>("1-2", 0, MILLISECONDS));
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        subscriber.assertValues(
                new Timed<>("1-2", 0, MILLISECONDS),
                new Timed<>("3-4", 10, MILLISECONDS));
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        subscriber.assertValues(
                new Timed<>("1-2", 0, MILLISECONDS),
                new Timed<>("3-4", 10, MILLISECONDS));
        bot.setLocation(5, 6);
        subscriber.assertValues(
                new Timed<>("1-2", 0, MILLISECONDS),
                new Timed<>("3-4", 10, MILLISECONDS),
                new Timed<>("5-6", 2010, MILLISECONDS));

    }

}