package ru.ssau.lab1.view;

import org.cosinus.swing.context.SwingComponent;
import org.springframework.context.annotation.Scope;

import ru.ssau.lab1.pojo.ProcessPojo;
import ru.ssau.lab1.view.custom.CRUDPanel;
import ru.ssau.lab1.view.custom.CRUDTable;

@SwingComponent
@Scope("prototype")
public class ProcessPanel extends CRUDPanel<ProcessPojo>
{
    public ProcessPanel(CRUDTable<ProcessPojo> CRUDTable) 
    {
        super(CRUDTable);
    }
}
