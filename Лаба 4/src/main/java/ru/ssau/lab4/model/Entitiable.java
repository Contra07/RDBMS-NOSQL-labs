package ru.ssau.lab4.model;

import java.util.Map;

public interface Entitiable<T>
{
    public abstract T fromStringValues(Map<String, String> values);
    public abstract T clone();
}