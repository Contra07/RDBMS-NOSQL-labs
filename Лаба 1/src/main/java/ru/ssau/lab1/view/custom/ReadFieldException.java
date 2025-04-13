package ru.ssau.lab1.view.custom;

public class ReadFieldException extends Exception
{
    public ReadFieldException() {super();}
    public ReadFieldException(String message) {super(message);}
    public ReadFieldException(String message, Throwable ex) {super(message, ex);}
    public ReadFieldException(Throwable ex) {super(ex);}
}
