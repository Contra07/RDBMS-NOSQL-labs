package ru.ssau.lab1.view;

import org.cosinus.swing.context.SwingComponent;
import org.springframework.context.annotation.Scope;

import ru.ssau.lab1.pojo.ItemPojo;
import ru.ssau.lab1.view.custom.CRUDPanel;
import ru.ssau.lab1.view.custom.CRUDTable;

@SwingComponent
@Scope("prototype")
public class ItemPanel extends CRUDPanel<ItemPojo>
{
    public ItemPanel(CRUDTable<ItemPojo> CRUDTable) 
    {
        super(CRUDTable);
    }
}
