package com.lesiak.sandbox;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Bot {

    public static final long REPORT_STATUS_INTERVAL = 3_000;
    static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_INSTANT;

    private Optional<Radio> radio = Optional.empty();
    String message;
    private Instant sent = Instant.MIN;
    private int x, y;

    private Subject<String> positions = PublishSubject.create();
    private Observable<Timed<String>> positionsFiltered = positions.timestamp().throttleLatest(1, TimeUnit.SECONDS);

    public Bot(Double frq) {
        if (frq != null) { // convert MHz to Hz
            radio = Optional.of(
                    new Radio((long) (frq.doubleValue() * 1000000)));
        }
    }


    public Observable<Timed<String>> getPositionsFilteredObservable() {
        return positionsFiltered;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        positions.onNext(x + "-" + y);
    }

    private void reportStatus(Instant now) {
        if (sent.plusMillis(REPORT_STATUS_INTERVAL).isBefore(now)) {
            message = String.format("%s: (%d, %d)", DATE_FORMAT.format(now), x, y);
            radio.ifPresent(radio -> radio.send(message));
            sent = now;
        }
    }


}

