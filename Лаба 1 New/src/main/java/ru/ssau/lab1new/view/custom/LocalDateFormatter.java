package ru.ssau.lab1new.view.custom;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.shell.table.Formatter;

public class LocalDateFormatter implements Formatter 
{
    private static final String defaultPattern = "dd.MM.yyyy HH:mm:ss";
    private String pattern;

    public LocalDateFormatter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String[] format(Object value) {
        LocalDateTime localDate = (LocalDateTime) value;
        DateTimeFormatter dateFormatter = (new DateTimeFormatterFactory(pattern)).createDateTimeFormatter();
        return new String[] {dateFormatter.format(localDate)};
    }

    public static LocalDateFormatter create()
    {
       return new LocalDateFormatter(defaultPattern);
    }

    public static DateTimeFormatter createDatetime()
    {
        return(new DateTimeFormatterFactory(defaultPattern)).createDateTimeFormatter();
    }
}
