package ru.ssau.lab1.pojo;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.*;
import ru.ssau.lab1.model.*;
import ru.ssau.lab1.view.custom.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Scope("prototype")
public class ItemPojo 
{
    @EditTransient
    private UUID id;
    @ColumnName(name = "Имя")
    private String name;
    @ColumnName(name = "Является директорией")
    private boolean isFolder;
    @ColumnName(name = "Родительская директория")
    private UUID parent;
    @ColumnName(name = "Дата создания")
    private LocalDateTime createdAt = LocalDateTime.now();
    @ColumnName(name = "Дата изменения")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @ColumnName(name = "Размер")
    private Long size;
    
    public static ItemPojo fromEntity(Item entity)
    {
        return new ItemPojo(entity.getId(), entity.getName(), entity.isFolder(), entity.getParentId(), entity.getCreatedAt(), entity.getUpdatedAt(), entity.getSize());
    }

    public static Item toEntity(ItemPojo pojo)
    {
        var item = new Item(pojo.getId(), pojo.getName(), pojo.isFolder(), null, pojo.getParent(), pojo.getCreatedAt(), pojo.getUpdatedAt(), pojo.getSize());
        if(pojo.getParent() != null)
        {
            var parent = new Item();
            parent.setId(pojo.getParent());
            item.setParent(parent);
        }
        return item;
    }
}