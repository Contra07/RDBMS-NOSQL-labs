package ru.ssau.shared.data;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataUtils 
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

    public static <T> void setItemValue(T item, Field field, Object value)
    {
        try 
        {
            field.setAccessible(true);
            field.set(item, value);
            field.setAccessible(false);
        } 
        catch (IllegalArgumentException | IllegalAccessException e) 
        {
            e.printStackTrace();
        }
    }
}
