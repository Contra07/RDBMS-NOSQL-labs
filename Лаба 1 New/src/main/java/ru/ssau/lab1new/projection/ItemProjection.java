package ru.ssau.lab1new.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ItemProjection 
{
    UUID getId();
    String getName();
    Boolean getIsFolder();
    UUID getParentId();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    Long getSize();
}
