package ru.ssau.lab1new.view.custom;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.CellMatchers;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;

public class CustomUtils 
{
    public static <T> Map<Field, Object> getFieldsWithValue(T item)
    {
        Map<Field, Object> map = new LinkedHashMap<>();
        Class cl = item.getClass();
        while (cl != null) 
        {
            for (var field : List.of(cl.getDeclaredFields()).reversed()) 
            {
                try 
                {
                    field.setAccessible(true);
                    map.put(field, field.get(item));
                    field.setAccessible(false);
                } 
                catch (IllegalArgumentException | IllegalAccessException e) 
                {
                    e.printStackTrace();
                }
            }
            cl = cl.getSuperclass();
        }
        return map;
    }

    public static <T> Table createTable(List<T> items, T item, int lastToFirst)
    {
        var fields = CustomUtils.getFieldsWithValue(item);
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        for (var field : fields.entrySet()) 
        {
            var header = field.getKey().getAnnotation(TableHeader.class);
            if(header != null)
            {
                headers.putFirst(field.getKey().getName(), header.name());
            }
        }
        for (int i = 0; i < lastToFirst; i++) 
        {
            var f = headers.pollLastEntry();
            headers.putFirst(f.getKey(), f.getValue());
            
        }
        var model = new BeanListTableModel<>(items, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
        tableBuilder.on(CellMatchers.ofType(LocalDateTime.class)).addFormatter(LocalDateFormatter.create());
        tableBuilder.on(CellMatchers.ofType(Duration.class)).addFormatter(new DurationFormatter());
        return tableBuilder.build();
    }

    public static <T> Table createItemTable(T item)
    {
        LinkedHashMap<String, Object> entries = new LinkedHashMap<>();
        for (var field : getFieldsWithValue(item).entrySet()) 
        {
            var header = field.getKey().getAnnotation(TableHeader.class);
            if(header != null)
            {
                entries.putFirst(header.name(), field.getValue() == null ? "NULL" : field.getValue());
            }
        }
        var data = new Object[entries.size()][2];
        int i = 0;
        for (var entry : entries.entrySet()) 
        {
            data[i][0] = entry.getKey();
            data[i][1] = entry.getValue();
            i++;
        }
        var model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
        tableBuilder.on(CellMatchers.ofType(LocalDateTime.class)).addFormatter(LocalDateFormatter.create());
        return tableBuilder.build();
    }

}
