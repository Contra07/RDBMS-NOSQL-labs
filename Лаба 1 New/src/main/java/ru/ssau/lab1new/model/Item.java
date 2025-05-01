package ru.ssau.lab1new.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item 
{
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "isfolder")
    private Boolean isFolder;

    @Column(nullable = true, name = "parentid")
    private UUID parentId;

    @Column(nullable = false, name = "createdat")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updatedat")
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private Long size;
}