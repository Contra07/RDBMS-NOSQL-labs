package ru.ssau.shared.shell;

import java.lang.reflect.ParameterizedType;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.shell.component.flow.ComponentFlow;
import org.springframework.shell.component.flow.ComponentFlow.ComponentFlowResult;
import org.springframework.shell.component.flow.SelectItem;
import org.springframework.shell.table.*;

import ru.ssau.shared.data.DataUtils;

public class ShellUtils 
{
    public static final String NULL = "NULL";

    public static <T> Table createTable(List<T> items, T item, int lastToFirst)
    {
        var fields = DataUtils.getFieldsWithValue(item);
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        for (var field : fields.entrySet()) 
        {
            var header = field.getKey().getAnnotation(TableHeader.class);
            if(header != null)
            {
                headers.putFirst(field.getKey().getName(), header.name());
            }
        }
        for (int i = 0; i < lastToFirst; i++) 
        {
            var f = headers.pollLastEntry();
            headers.putFirst(f.getKey(), f.getValue());
            
        }
        var model = new BeanListTableModel<>(items, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
        tableBuilder.on(CellMatchers.ofType(LocalDateTime.class)).addFormatter(LocalDateFormatter.create());
        tableBuilder.on(CellMatchers.ofType(Duration.class)).addFormatter(new DurationFormatter());
        return tableBuilder.build();
    }

    public static <T> Table createCollectionTable(Collection<T> items)
    {
        return createCollectionTable(items, false);
    }

    public static <T> Table createCollectionTable(Collection<T> items, boolean numbers)
    {
        Object[][] data;
        if(numbers)
        {
            data = new Object[items.size()][2];
            int i = 0;
            for (var item : items) 
            {
                data[i][0] = i;
                data[i][1] = item == null ? "NULL" : item;
                i++;
            }
        }
        else
        {
            data = new Object[items.size()][1];
            int i = 0;
            for (var item : items) 
            {
                data[i][0] = item == null ? "NULL" : item;
                i++;
            }
        }
        var model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
        return tableBuilder.build();
    }

    public static <T> Table createItemFieldsTable(T item)
    {
        LinkedHashMap<String, Object> entries = new LinkedHashMap<>();
        for (var field : DataUtils.getFieldsWithValue(item).entrySet()) 
        {
            var header = field.getKey().getAnnotation(TableHeader.class);
            if(header != null)
            {
                entries.putFirst(header.name(), field.getValue() == null ? "NULL" : field.getValue());
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
        return tableBuilder.build();
    }

    public static <T> T askForItem(ComponentFlow.Builder componentFlowBuilder, T item)
    {
        var builder = componentFlowBuilder.clone().reset();
        var fields = DataUtils.getFieldsWithValue(item).entrySet().stream().toList().reversed();
        for (var field :fields) 
        {
            var header = field.getKey().getAnnotation(TableHeader.class);
            var ignore = field.getKey().getAnnotation(IgnoreEdit.class) != null;
            if(header != null && !ignore)
            {   
                if(field.getKey().getType().isEnum())
                {
                    var c = (Enum[])field.getKey().getType().getEnumConstants();
                    List<SelectItem> single1SelectItems = new ArrayList<SelectItem>();
                    for (Enum e : c) {
                        single1SelectItems.add(SelectItem.of(e.name(), e.name()));
                    }
                    var s = builder
                        .withSingleItemSelector(field.getKey().getName());
                    
                    if(field.getValue() != null)
                    {
                        s = s.defaultSelect(field.getValue().toString());
                    }
                    builder = s
                        .name(header.name())
                        .selectItems(single1SelectItems)
                        .postHandler(
                            ctx -> {
                                var text = ctx.getResultItem().get().getItem();
                                if(text.equals(NULL))
                                    DataUtils.setItemValue(item, field.getKey(), null);
                                else
                                {
                                    for (Enum e : c) 
                                    {
                                        if(e.name().equals(text))
                                            DataUtils.setItemValue(item, field.getKey(), e);
                                    }
                                }
                            }
                        )
                        .and();
                }
                else if(field.getKey().getType().equals(LocalDateTime.class))
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
                                DataUtils.setItemValue(item, field.getKey(), null);
                            else
                                DataUtils.setItemValue(item, field.getKey(), LocalDateTime.parse(text, LocalDateFormatter.createDatetime()));
                        }
                    )
                    .and();
                }
                else if(field.getKey().getType().equals(LocalDate.class))
                {
                    builder = builder
                    .withStringInput(field.getKey().getName())
                    .name(header.name())
                    .defaultValue(
                        field.getValue() == null 
                        ? LocalDate.now().format(LocalDateFormatter.createDate())
                        : ((LocalDate)field.getValue()).format(LocalDateFormatter.createDate())
                    )
                    .postHandler(
                        ctx -> {
                            var text = ctx.getResultValue();
                            if(text.equals(NULL))
                                DataUtils.setItemValue(item, field.getKey(), null);
                            else
                                DataUtils.setItemValue(item, field.getKey(), LocalDate.parse(text, LocalDateFormatter.createDate()));
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
                            DataUtils.setItemValue(item, field.getKey(), value);
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
                                DataUtils.setItemValue(item, field.getKey(), null);
                            else
                            {
                                String[] args = text.split(",");
                                for (String arg : args) {
                                    arg = arg.trim();
                                }
                                DataUtils.setItemValue(item, field.getKey(), List.of(args));
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
                                DataUtils.setItemValue(item, field.getKey(), null);
                            else
                            {
                                if (field.getKey().getType().equals(UUID.class))
                                {
                                    DataUtils.setItemValue(item, field.getKey(), UUID.fromString(text));
                                }
                                else if (field.getKey().getType().equals(Long.class))
                                {
                                    DataUtils.setItemValue(item, field.getKey(), Long.parseLong(text));
                                }
                                else if (field.getKey().getType().equals(Integer.class))
                                {
                                    DataUtils.setItemValue(item, field.getKey(), Integer.parseInt(text));
                                }
                                else if (field.getKey().getType().equals(Double.class))
                                {
                                    DataUtils.setItemValue(item, field.getKey(), Double.parseDouble(text));
                                }
                                else if (field.getKey().getType().equals(String.class)) 
                                {
                                    DataUtils.setItemValue(item, field.getKey(), text);
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
}
