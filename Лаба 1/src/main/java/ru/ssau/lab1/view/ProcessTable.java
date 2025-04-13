package ru.ssau.lab1.view;

import java.util.UUID;

import org.cosinus.swing.context.SwingComponent;
import org.springframework.context.annotation.Scope;

import ru.ssau.lab1.pojo.ProcessPojo;
import ru.ssau.lab1.service.CRUDService;
import ru.ssau.lab1.view.custom.CRUDTable;

@SwingComponent
@Scope("prototype")
public class ProcessTable extends CRUDTable<ProcessPojo>
{
    public ProcessTable(CRUDService<ProcessPojo, UUID> crudService, ProcessPojo entity) 
    {
        super(crudService, entity);
    }
}
