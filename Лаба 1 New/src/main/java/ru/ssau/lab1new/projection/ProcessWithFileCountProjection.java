package ru.ssau.lab1new.projection;

import ru.ssau.lab1new.model.FileAccessType;

public interface ProcessWithFileCountProjection
{
    String getProcessId();
    String getProcessName();
    FileAccessType getAccess();
    int getFilesNumber();
}
