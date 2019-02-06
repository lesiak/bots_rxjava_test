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

    private int x, y;

    private final Subject<String> positions = PublishSubject.create();
    private final Observable<Timed<String>> positionsFiltered = positions.timestamp().throttleLatest(1, TimeUnit.SECONDS);

    public Observable<Timed<String>> getPositionsFilteredObservable() {
        return positionsFiltered;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        positions.onNext(x + "-" + y);
    }

}

