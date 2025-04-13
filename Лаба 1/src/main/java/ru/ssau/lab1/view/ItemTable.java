package ru.ssau.lab1.view;

import java.util.UUID;

import org.cosinus.swing.context.SwingComponent;
import org.springframework.context.annotation.Scope;

import ru.ssau.lab1.pojo.ItemPojo;
import ru.ssau.lab1.service.CRUDService;
import ru.ssau.lab1.view.custom.CRUDTable;

@SwingComponent
@Scope("prototype")
public class ItemTable extends CRUDTable<ItemPojo>
{
    public ItemTable(CRUDService<ItemPojo, UUID> crudService, ItemPojo entity) 
    {
        super(crudService, entity);
    }
}