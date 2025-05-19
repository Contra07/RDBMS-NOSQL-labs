package ru.ssau.shared.pojo;

public class PojoConversionException extends Exception
{
    public PojoConversionException() {super();}
    public PojoConversionException(Exception ex) {super(ex);}
    public PojoConversionException(String message) {super(message);}
    public PojoConversionException(String message, Exception ex) {super(message, ex);}
}