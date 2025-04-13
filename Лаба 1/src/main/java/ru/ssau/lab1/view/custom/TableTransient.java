package ru.ssau.lab1.view.custom;

import java.lang.annotation.*;

/**
 * Specifies that the property or field is not to be considered as a column.
 * 
 * @author Sewwandi
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableTransient {}
