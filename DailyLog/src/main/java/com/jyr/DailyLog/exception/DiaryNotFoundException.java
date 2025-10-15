package com.jyr.DailyLog.exception;

public class DiaryNotFoundException extends RuntimeException{
    public DiaryNotFoundException(String message){
        super(message);
    }
}
