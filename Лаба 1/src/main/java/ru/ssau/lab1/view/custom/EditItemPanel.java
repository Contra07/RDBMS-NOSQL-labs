package ru.ssau.lab1.view.custom;

import java.awt.Button;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;

import org.cosinus.swing.form.Panel;


public class EditItemPanel<E> extends Panel{
    private final E model;
    private final List<Field> fields;
    private Button saveButton;
    private List<ReadFieldListener> readFieldListeners = new ArrayList<>();
    private List<ItemActionListener<E>> saveListeners = new ArrayList<>();

    {
        saveButton = new Button("Сохранить");
        saveButton.addActionListener(e -> saveAction(e));
    }

    public interface ReadFieldListener extends EventListener
    {
        public void actionPerformed(ActionEvent event) throws ReadFieldException;
    }

    public interface ItemActionListener<E> extends EventListener
    {
        public void actionPerformed(E e);
    }

    public EditItemPanel(E model) {
        super();
        this.model = model;
        this.fields = setFields(model);
    }

    @Override
    public void initComponents() 
    {
        super.initComponents();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //Для каждого поля надо добавить свой компонент
        //Наверное лучше привязать ивенты?
        for (Field field : fields) 
        {
            try 
            {
                // 1. Panel для каждого поля (с нормальным текстом ошибки?)
                // 2. Аннотация EditTransient, но при этом Date = now
                ColumnName colName = field.getAnnotation(ColumnName.class);
                var columnName = colName != null ? colName.name() : field.getName();
                
                if (field.getAnnotation(EditTransient.class) == null) {
                    Type type = field.getType();
                    JComponent component = null;
                    if (type.equals(Long.class)) 
                    {
                        component = createLongPanel(columnName, field, (Long)getItemValue(model, field));
                    }
                    else if (type.equals(UUID.class)) 
                    {
                        component = createUUIDPanel(columnName, field, (UUID)getItemValue(model, field));
                    }
                    else if (type.equals(LocalDateTime.class)) 
                    {
                        component = createLocalDateTimePanel(columnName, field, (LocalDateTime)getItemValue(model, field));
                    }
                    else if (type.equals(String.class)) 
                    {
                        component = createTextPanel(columnName, field, (String)getItemValue(model, field));
                    }
                    if(component != null)
                        add(component);
                }
            }
            catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) 
            {
                ErrorPane.showErrorDialog(this, ex);
            }
        }
        add(saveButton);
    }

    public void addSaveListener(ItemActionListener<E> listener)
    {
        saveListeners.add(listener);
    }

    private static <E> List<Field> setFields(E model) 
    {
        var fields = new ArrayList<Field>();
        Field[] allFields = model.getClass().getDeclaredFields();
        for (int i = 0; i < allFields.length; i++) 
        {
            if (allFields[i].getAnnotation(EditTransient.class) == null) 
            {
                fields.add(allFields[i]);
            }
        }
        return fields;
    }

    private Object getItemValue(Object item, Field field) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        Object value = null;
        Field f = item.getClass().getDeclaredField(field.getName());
        if(f.canAccess(item))
        {
            value = f.get(item);
        }
        else
        {
            f.setAccessible(true);
            value = f.get(item);
            f.setAccessible(false);
        }
        return value;
    }

    private void setItemValue(Object item, Field field, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        Field f = item.getClass().getDeclaredField(field.getName());
        if(f.canAccess(item))
        {
            f.set(item, value);
        }
        else
        {
            f.setAccessible(true);
            f.set(item, value);
            f.setAccessible(false);
        }
    }

    private void saveAction(ActionEvent event)
    {
        try 
        {
            for (var fieldListener : readFieldListeners) 
            {
                fieldListener.actionPerformed(event);
            } 
            for (var saveListener : saveListeners) {
                saveListener.actionPerformed(model);
            }
        }
        catch (ReadFieldException e) 
        {
            ErrorPane.showErrorDialog(this, e);
        }
    }

    private JPanel createTextPanel(String name, Field field, String value)
    {
        var panel = new JPanel(new GridLayout(3,1));
        var label = new JLabel();
        var textField = new JTextField();
        var nullSwitch = new JCheckBox();

        label.setText(name);
        nullSwitch.setSelected(value == null);
        nullSwitch.setText("NULL");
        textField.setText(value == null ? "" : value.toString());
        textField.setEnabled(value != null);
        
        nullSwitch.addItemListener(
            e ->
            {
                Object source = e.getItemSelectable();
                if(source == nullSwitch)
                {
                    textField.setEnabled(e.getStateChange() == ItemEvent.DESELECTED);
                }
            }
        );

        readFieldListeners.add(
            e -> 
            {
                try
                {
                    var text = nullSwitch.isSelected() ? null : textField.getText();
                    setItemValue(model, field, text);
                }
                catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) 
                {
                    throw new ReadFieldException(ex);
                }
            }
        );
        panel.add(label);
        panel.add(textField);
        panel.add(nullSwitch);
        return panel;
    }

    private JPanel createLongPanel(String name, Field field, Long value)
    {
        var panel = new JPanel(new GridLayout(2,1));
        var label = new JLabel(name);
        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        integerFormat.setGroupingUsed(false);

        NumberFormatter numberFormatter = new NumberFormatter(integerFormat);
        numberFormatter.setValueClass(Long.class); 
        numberFormatter.setAllowsInvalid(true); 

        var textField = new JFormattedTextField(numberFormatter);
        textField.setText(value == null ? "" : value.toString());
        readFieldListeners.add(
            e -> 
            {
                try
                {
                    var text = textField.getText();
                    if(text.equals(""))
                        setItemValue(model, field, null);
                    else
                        setItemValue(model, field, Long.parseLong(text));
                }
                catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) 
                {
                    throw new ReadFieldException(ex);
                }
            }
        );
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    private JPanel createUUIDPanel(String name, Field field, UUID value)
    {
        var panel = new JPanel(new GridLayout(2,1));
        var label = new JLabel(name);
        DefaultFormatter uuidFormatter = new DefaultFormatter() 
        {
            @Override
            public Object stringToValue(String text) throws java.text.ParseException {
                try {
                    // Проверяем, является ли введенное значение допустимым UUID
                    return UUID.fromString(text);
                } catch (IllegalArgumentException e) {
                    throw new java.text.ParseException("Неверный формат UUID", 0);
                }
            }

            @Override
            public String valueToString(Object value) throws java.text.ParseException {
                if (value instanceof UUID) {
                    return value.toString(); // Преобразование UUID в строку
                }
                return super.valueToString(value);
            }
        };
        var textField = new JFormattedTextField(uuidFormatter);
        textField.setText(value == null ? "" : value.toString());
        readFieldListeners.add(
            e -> 
            {
                try
                {
                    var text = textField.getText();
                    if(text.equals(""))
                        setItemValue(model, field, null);
                    else
                        setItemValue(model, field, UUID.fromString(text));
                }
                catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) 
                {
                    throw new ReadFieldException(ex);
                }
            }
        );
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    private JPanel createLocalDateTimePanel(String name, Field field, LocalDateTime value)
    {
        var panel = new JPanel(new GridLayout(2,1));
        var label = new JLabel(name);
        DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendLiteral(' ')
                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                .toFormatter(Locale.ENGLISH);

        var textField = new JFormattedTextField(dateFormatter);
        textField.setText(value == null ? "" : value.toString());
        readFieldListeners.add(
            e -> 
            {
                try
                {
                    var text = textField.getText();
                    if(text.equals(""))
                        setItemValue(model, field, null);
                    else
                        setItemValue(model, field, LocalDateTime.parse(text));
                }
                catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) 
                {
                    throw new ReadFieldException(ex);
                }
            }
        );
        panel.add(label);
        panel.add(textField);
        return panel;
    }
}
