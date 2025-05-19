package ru.ssau.shared.pojo;

import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UUIDPojo extends IdPojo<UUID>
{
    public UUIDPojo() {
        super();
    }
    public UUIDPojo(UUID id) {
        super(id);
    }
    
    @Override
    public UUIDPojo clone()
    {
        return new UUIDPojo(id);
    }
}