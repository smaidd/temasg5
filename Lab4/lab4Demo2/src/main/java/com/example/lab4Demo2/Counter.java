package com.example.lab4Demo2;

public class Counter {
    private int counter;

    public synchronized int count(){
        return ++counter;
    }
}
