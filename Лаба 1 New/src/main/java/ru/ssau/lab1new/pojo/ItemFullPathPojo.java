package ru.ssau.lab1new.pojo;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.ssau.lab1new.projection.ItemFullPathProjection;
import ru.ssau.lab1new.view.custom.TableHeader;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Component
@Scope("prototype")
public class ItemFullPathPojo extends ItemPojo
{
    @TableHeader(name = "Полный путь")
    private String fullPath;

    public ItemFullPathPojo(UUID id, String name, Boolean isFolder, UUID parent, LocalDateTime createdAt, LocalDateTime updatedAt, Long size, String fullPath) {
        super(id, name, isFolder, parent, createdAt, updatedAt, size);
        this.fullPath = fullPath;
    }

    public static ItemFullPathPojo fromProjection(ItemFullPathProjection fromProjection)
    {
        return new ItemFullPathPojo(fromProjection.getId(), fromProjection.getName(), fromProjection.getIsFolder(), fromProjection.getParentId(), fromProjection.getCreatedAt(), fromProjection.getUpdatedAt(), fromProjection.getSize(), String.join("/", fromProjection.getFullPath()));
    }
    
}
