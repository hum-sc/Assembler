package org.itzhum.exceptions;

public class ProcessCanceledException extends Exception{
    public  ProcessCanceledException(){
        super();
    }
    public ProcessCanceledException(String message){
        super(message);
    }
}
