package ru.ssau.lab1new.pojo;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.*;
import ru.ssau.lab1new.model.*;
import ru.ssau.lab1new.projection.ItemProjection;
import ru.ssau.lab1new.view.custom.TableHeader;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Component
@Primary
@Scope("prototype")
public class ItemPojo extends IdPojo
{
    @TableHeader(name = "Имя")
    private String name;
    @TableHeader(name = "Каталог")
    private Boolean isFolder;
    @TableHeader(name = "Родительский каталог")
    private UUID parent;
    @TableHeader(name = "Дата создания")
    private LocalDateTime createdAt;
    @TableHeader(name = "Дата обновления")
    private LocalDateTime updatedAt;
    @TableHeader(name = "Размер")
    private Long size;
    
    public ItemPojo(UUID id, String name, Boolean isFolder, UUID parent, LocalDateTime createdAt,
            LocalDateTime updatedAt, Long size) {
        super(id);
        this.name = name;
        this.isFolder = isFolder;
        this.parent = parent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.size = size;
    }

    public static ItemPojo fromEntity(Item entity)
    {
        return new ItemPojo(entity.getId(), entity.getName(), entity.getIsFolder(), entity.getParentId(), entity.getCreatedAt(), entity.getUpdatedAt(), entity.getSize());
    }

    public static ItemPojo fromProjection(ItemProjection projection)
    {
        return new ItemPojo(projection.getId(), projection.getName(), projection.getIsFolder(), projection.getParentId(), projection.getCreatedAt(), projection.getUpdatedAt(), projection.getSize());
    }

    public static Item toEntity(ItemPojo pojo)
    {
        var item = new Item(pojo.getId(), pojo.getName(), pojo.getIsFolder(), pojo.getParent(), pojo.getCreatedAt(), pojo.getUpdatedAt(), pojo.getSize());
        return item;
    }

    @Override
    public ItemPojo clone() 
    {
        return new ItemPojo(id, name, isFolder, parent, createdAt, updatedAt, size);
    }
}