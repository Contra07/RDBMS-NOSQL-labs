package ru.ssau.lab1new.projection;

import java.util.UUID;

import ru.ssau.lab1new.model.FileAccessType;

public interface ProcessFileProjection 
{
    UUID getProcessId();
    UUID getId();
    UUID getItemId();
    FileAccessType getAccess();
}