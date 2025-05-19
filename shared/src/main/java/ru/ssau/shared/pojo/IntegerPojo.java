package ru.ssau.shared.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class IntegerPojo extends IdPojo<Integer>
{
    public IntegerPojo() {
        super();
    }
    public IntegerPojo(Integer id) {
        super(id);
    }
    
    @Override
    public IntegerPojo clone()
    {
        return new IntegerPojo(id);
    }
}