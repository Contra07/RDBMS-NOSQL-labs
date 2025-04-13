package ru.ssau.lab1.view.custom;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import org.cosinus.swing.context.SwingComponent;
import org.cosinus.swing.form.Panel;
import org.springframework.context.annotation.Scope;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class CRUDPanel<E> extends Panel
{
    private final CRUDTable<E> CRUDTable;
    private Button addButton;
    private Button reloadButton;
    private Button editButton;
    private Button deleteButton;
    private boolean dialogOpen = false;

    {
        addButton = new Button("Добавить");
        addButton.addActionListener(e -> addAction(e));
        addButton.setSize(100, 50);
        editButton = new Button("Изменить");
        editButton.addActionListener(e -> editAction(e));
        editButton.setSize(100, 50);
        reloadButton = new Button("Обновить");
        reloadButton.addActionListener(e -> reloadAction(e));
        reloadButton.setSize(100, 50);
        deleteButton = new Button("Удалить");
        deleteButton.addActionListener(e -> deleteAction(e));
        deleteButton.setSize(100, 50);
    }

    @Override
    public void initComponents() 
    {
        super.initComponents();
        setLayout(new GridLayout(2,1));

        CRUDTable.initComponents();
        this.add(new JScrollPane(CRUDTable));

        var buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(reloadButton);
        buttonPanel.add(deleteButton);
        this.add(buttonPanel);
    }

    protected void addAction(ActionEvent e) 
    {
        if(!dialogOpen)
        {
            var item = CRUDTable.getNewItem();
            var dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), false);
            var pane = new EditItemPanel<>(item);
            pane.initComponents();
            dialog.setContentPane(pane);
            dialog.pack();
            pane.addSaveListener(
                newItem ->
                {
                    CRUDTable.add(newItem);
                    dialog.dispose();
                    dialogOpen = false;
                }
            );
            dialog.addWindowListener(new WindowAdapter() 
            {
                public void windowClosing(WindowEvent e)
                {
                    dialogOpen = false;
                }
            });
            dialogOpen = true;
            dialog.setVisible(true);
        }
    }

    protected void editAction(ActionEvent e) 
    {
        if(!dialogOpen)
        {
            var rowIndex = CRUDTable.getSelectedIndex();
            if(rowIndex >= 0 && rowIndex < CRUDTable.getRowCount())
            {
                var item = CRUDTable.getSelectedItem();
                var dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), false);
                var pane = new EditItemPanel<>(item);
                pane.initComponents();
                dialog.setContentPane(pane);
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                dialog.pack();
                pane.addSaveListener(
                    newItem ->
                    {
                        CRUDTable.edit(newItem, rowIndex);
                        dialog.dispose();
                        dialogOpen = false;
                    }
                );
                dialog.addWindowListener(new WindowAdapter() 
                {
                    public void windowClosing(WindowEvent e)
                    {
                        dialogOpen = false;
                    }
                });
                dialogOpen = true;
                dialog.setVisible(true);
            }
        }
    }

    protected void reloadAction(ActionEvent e) 
    {
        CRUDTable.reload();
    }

    protected void deleteAction(ActionEvent e) 
    {
        if(!dialogOpen)
        {
            CRUDTable.delete(CRUDTable.getSelectedIndex());
        }
    }
}
