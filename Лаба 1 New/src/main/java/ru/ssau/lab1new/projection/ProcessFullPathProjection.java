package ru.ssau.lab1new.projection;

import java.util.UUID;

public interface ProcessFullPathProjection extends ProcessProjection
{
    String[] getFullPath();
    UUID[] getPath();
}