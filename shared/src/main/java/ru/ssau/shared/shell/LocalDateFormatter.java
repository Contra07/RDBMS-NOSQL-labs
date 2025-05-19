package ru.ssau.shared.shell;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.shell.table.Formatter;

public class LocalDateFormatter implements Formatter 
{
    private static final String defaultPattern = "dd.MM.yyyy HH:mm:ss";
    private static final String defaultDPattern = "dd.MM.yyyy";
    private String pattern;

    public LocalDateFormatter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String[] format(Object value) {
        LocalDateTime localDate = (LocalDateTime) value;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(defaultPattern);
        // (new DateTimeFormatterFactory(pattern)).createDateTimeFormatter();
        return new String[] {dateFormatter.format(localDate)};
    }

    public static LocalDateFormatter create()
    {
       return new LocalDateFormatter(defaultPattern);
    }

    public static DateTimeFormatter createDatetime()
    {
        return DateTimeFormatter.ofPattern(defaultPattern);
    }

    public static DateTimeFormatter createDate()
    {
        return DateTimeFormatter.ofPattern(defaultDPattern);
    }
}
