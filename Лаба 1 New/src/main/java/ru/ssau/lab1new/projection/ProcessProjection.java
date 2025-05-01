package ru.ssau.lab1new.projection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ProcessProjection 
{
    UUID getId();
    UUID getParentId();
    String getName();
    LocalDateTime getStartedAt();
    UUID getSourceFile();
    List<String> getArguments();
}