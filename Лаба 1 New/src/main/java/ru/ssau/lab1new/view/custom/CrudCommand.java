package ru.ssau.lab1new.view.custom;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.component.flow.ComponentFlow;
import org.springframework.shell.component.flow.ComponentFlow.ComponentFlowResult;
import org.springframework.shell.table.*;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab1new.pojo.IdPojo;
import ru.ssau.lab1new.service.CRUDService;
import ru.ssau.lab1new.service.ServiceException;

@RequiredArgsConstructor
public class CrudCommand<T extends IdPojo> 
{
    private final Terminal terminal;
    private final ComponentFlow.Builder componentFlowBuilder;
    private final T entity;
    private final CRUDService<T, UUID> service;
    private Iterable<T> items = new ArrayList<T>();
    private static final String NULL = "NULL";
    
    @Command(command = {"list"})
    public void list() throws ServiceException
    {
        items = service.ReadAll();
        printTable(entity, items);
    }

    @Command(command = {"show"})
    public void show(UUID id) throws ServiceException 
    {
        var item = service.Read(id);
        if(item == null)
        {
            terminal.writer().println("Не удалось найти запись с идентификатором: " + id);
        }
        else
        {
            printEntity(item);
        }
    }

    @Command(command = {"delete"})
    public void delete(UUID id) throws ServiceException 
    {
        service.Delete(id);
    }

    @Command(command = {"new"})
    public void create() throws ServiceException 
    {
        var item = askForItem((T)entity.clone());
        var newItem = service.Create(item);
        printEntity(newItem);
    }

    @Command(command = {"edit"})
    public void edit(UUID id) throws ServiceException 
    {
        var item = askForItem(service.Read(id));
        var newItem = service.Update(item);
        printEntity(newItem);
    }

    private void printTable(T item, Iterable<T> items)
    {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        for (var field : getFieldsWithValue(item).entrySet()) 
        {
            var header = field.getKey().getAnnotation(TableHeader.class);
            if(header != null)
            {
                headers.putFirst(field.getKey().getName(), header.name());
            }
        }
        var model = new BeanListTableModel<>(items, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
        tableBuilder.on(CellMatchers.ofType(LocalDateTime.class)).addFormatter(LocalDateFormatter.create());
        terminal.writer().println(tableBuilder.build().render(80));
    }

    private void printEntity(T item)
    {
        LinkedHashMap<String, Object> entries = new LinkedHashMap<>();
        for (var field : getFieldsWithValue(item).entrySet()) 
        {
            var header = field.getKey().getAnnotation(TableHeader.class);
            if(header != null)
            {
                entries.putFirst(header.name(), field.getValue() == null ? NULL : field.getValue());
            }
        }
        var data = new Object[entries.size()][2];
        int i = 0;
        for (var entry : entries.entrySet()) 
        {
            data[i][0] = entry.getKey();
            data[i][1] = entry.getValue();
            i++;
        }
        var model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
        tableBuilder.on(CellMatchers.ofType(LocalDateTime.class)).addFormatter(LocalDateFormatter.create());
        terminal.writer().println(tableBuilder.build().render(80));
    }

    private T askForItem(T item)
    {
        var builder = componentFlowBuilder.clone().reset();
        var fields = getFieldsWithValue(item).entrySet().stream().toList().reversed();
        for (var field :fields) 
        {
            var header = field.getKey().getAnnotation(TableHeader.class);
            var ignore = field.getKey().getAnnotation(IgnoreEdit.class) != null;
            if(header != null && !ignore)
            {   
                if(field.getKey().getType().equals(LocalDateTime.class))
                {
                    builder = builder
                    .withStringInput(field.getKey().getName())
                    .name(header.name())
                    .defaultValue(
                        field.getValue() == null 
                        ? LocalDateTime.now().format(LocalDateFormatter.createDatetime())
                        : ((LocalDateTime)field.getValue()).format(LocalDateFormatter.createDatetime())
                    )
                    .postHandler(
                        ctx -> {
                            var text = ctx.getResultValue();
                            if(text.equals(NULL))
                                setItemValue(item, field.getKey(), null);
                            else
                                setItemValue(item, field.getKey(), LocalDateTime.parse(text, LocalDateFormatter.createDatetime()));
                        }
                    )
                    .and();
                }
                else if(field.getKey().getType().equals(Boolean.class))
                {
                    builder = builder
                    .withConfirmationInput(field.getKey().getName())
                    .name(header.name())
                    .defaultValue(
                        field.getValue() == null 
                        ? false
                        : (Boolean)field.getValue()
                    )
                    .postHandler(
                        ctx ->
                        {
                            var value = ctx.getResultValue();
                            setItemValue(item, field.getKey(), value);
                        }
                    )
                    .and();
                }
                else if (field.getKey().getType().equals(List.class) && ((ParameterizedType)(field.getKey().getGenericType())).getActualTypeArguments()[0].equals(String.class)) 
                {
                    builder = builder
                    .withStringInput(field.getKey().getName())
                    .name(header.name())
                    .defaultValue(
                        field.getValue() == null 
                        ? NULL 
                        : ((List<String>)field.getValue()).stream().collect(Collectors.joining(", "))
                    )
                    .postHandler(
                        ctx -> 
                        {
                            String text = ctx.getResultValue();
                            if(text.equals(NULL))
                                setItemValue(item, field.getKey(), null);
                            else
                            {
                                String[] args = text.split(",");
                                for (String arg : args) {
                                    arg = arg.trim();
                                }
                                setItemValue(item, field.getKey(), List.of(args));
                            }
                        }
                    )
                    .and();
                }
                else
                {
                    builder = builder
                    .withStringInput(field.getKey().getName())
                    .name(header.name())
                    .defaultValue(
                        field.getValue() == null 
                        ? NULL 
                        : field.getValue().toString()
                    )
                    .postHandler(
                        ctx -> {
                            var text = ctx.getResultValue();
                            if(text.equals(NULL))
                                setItemValue(item, field.getKey(), null);
                            else
                            {
                                if (field.getKey().getType().equals(UUID.class))
                                {
                                    setItemValue(item, field.getKey(), UUID.fromString(text));
                                }
                                else if (field.getKey().getType().equals(Long.class))
                                {
                                    setItemValue(item, field.getKey(), Long.parseLong(text));
                                }
                                else if (field.getKey().getType().equals(String.class)) 
                                {
                                    setItemValue(item, field.getKey(), text);
                                }
                            }
                        }
                    )
                    .and();
                }
            }
        }
        ComponentFlow flow = builder.build();
        ComponentFlowResult result = flow.run();
        return item;
    }

    private Map<Field, Object> getFieldsWithValue(T item)
    {
        Map<Field, Object> map = new LinkedHashMap<>();
        Class cl = item.getClass();
        while (cl != null) 
        {
            for (var field : List.of(cl.getDeclaredFields()).reversed()) 
            {
                try 
                {
                    field.setAccessible(true);
                    map.put(field, field.get(item));
                    field.setAccessible(false);
                } 
                catch (IllegalArgumentException | IllegalAccessException e) 
                {
                    e.printStackTrace();
                }
            }
            cl = cl.getSuperclass();
        }
        return map;
    }

    private void setItemValue(T item, Field field, Object value)
    {
        try 
        {
            field.setAccessible(true);
            field.set(item, value);
            field.setAccessible(false);
        } 
        catch (IllegalArgumentException | IllegalAccessException e) 
        {
            e.printStackTrace();
        }
    }
}
