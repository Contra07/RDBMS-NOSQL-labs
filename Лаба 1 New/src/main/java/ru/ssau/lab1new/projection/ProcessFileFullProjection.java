package ru.ssau.lab1new.projection;

public interface ProcessFileFullProjection extends ProcessFileProjection
{
    String getProcessName();
    String[] getFullPath();
}