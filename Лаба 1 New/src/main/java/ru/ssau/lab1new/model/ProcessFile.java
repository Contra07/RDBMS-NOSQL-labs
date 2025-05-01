package ru.ssau.lab1new.model;

import java.util.UUID;

import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "processfile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessFile 
{
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "processid", nullable = false)
    private UUID processId;

    @Column(nullable = false, name = "itemid")
    private UUID itemId;

    @Column(nullable = false, name = "access")
    @Enumerated(value = EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private FileAccessType access;
}
