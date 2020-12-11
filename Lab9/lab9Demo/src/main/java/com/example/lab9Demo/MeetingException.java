package com.example.lab9Demo;

public class MeetingException extends RuntimeException{
    public MeetingException(){
        super("Operation could not be completed.");
    }
}
