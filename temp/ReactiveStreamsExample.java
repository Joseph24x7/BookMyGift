package com.bookmygift.service;

import java.util.concurrent.SubmissionPublisher;

import java.util.concurrent.SubmissionPublisher;

public class ReactiveStreamsExample {
    public static void main(String[] args) {
        // Create a Publisher
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        // Register a Subscriber
        publisher.subscribe(new MySubscriber());

        // Publish items to the Publisher
        publisher.submit("Item 1");
        publisher.submit("Item 2");
        publisher.submit("Item 3");

        // Close the Publisher
        publisher.close();
    }
}

class MySubscriber implements java.util.concurrent.Flow.Subscriber<String> {
    private java.util.concurrent.Flow.Subscription subscription;

    @Override
    public void onSubscribe(java.util.concurrent.Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        System.out.println("Received: " + item);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Completed");
    }
}

