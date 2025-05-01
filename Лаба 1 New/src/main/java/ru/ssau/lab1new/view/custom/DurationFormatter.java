package ru.ssau.lab1new.view.custom;

import java.time.Duration;

import org.springframework.shell.table.Formatter;

public class DurationFormatter  implements Formatter 
{
    @Override
    public String[] format(Object value) {
        Duration duration = (Duration) value;
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds();
        return new String[] {String.format("%d:%02d:%02d", hours, minutes, seconds)};
    }

}
