package ru.ssau.lab1new.view.custom;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableHeader 
{
	String name();
}
