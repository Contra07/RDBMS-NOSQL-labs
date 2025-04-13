package ru.ssau.lab1.model;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Process 
{
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name",nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parentid", nullable = true, insertable=false, updatable=false)
    private Process parent;

    @Column(name = "parentid", nullable = true)
    private UUID parentId;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "arguments", nullable = false)
    private List<String> arguments;

    @ManyToOne
    @JoinColumn(name = "sourcefile", nullable = false, insertable=false, updatable=false)
    private Item sourceFile;

    @Column(name = "sourcefile", nullable = false)
    private UUID sourceFileId;
}
