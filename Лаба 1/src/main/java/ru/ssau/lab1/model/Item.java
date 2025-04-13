package ru.ssau.lab1.model;

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
    private boolean isFolder;

    @ManyToOne
    @JoinColumn(nullable = true, name = "parentid", insertable=false, updatable=false)
    private Item parent;

    @Column(nullable = true, name = "parentid")
    private UUID parentId;

    @Column(nullable = false, name = "createdat")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "updatedat")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(nullable = true)
    private Long size;
}