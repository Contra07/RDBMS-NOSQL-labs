package ru.ssau.lab1.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.cosinus.swing.boot.SwingApplicationFrame;
import org.cosinus.swing.context.SwingComponent;


import lombok.RequiredArgsConstructor;
import ru.ssau.lab1.pojo.ItemPojo;
import ru.ssau.lab1.pojo.ProcessPojo;
import ru.ssau.lab1.view.custom.CRUDPanel;

@SwingComponent
@RequiredArgsConstructor
public class MainApplicationFrame extends SwingApplicationFrame
{
    private final CRUDPanel<ItemPojo> testPanel1;
    private final CRUDPanel<ProcessPojo> testPanel2;
    
    @Override
    protected void loadContent() 
    {
        setLayout(new BorderLayout(100,100));
        var panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        testPanel1.initComponents();
        testPanel2.initComponents();
        panel.add(testPanel1);
        panel.add(testPanel2);
        setContentPane(panel);
		pack();
		this.setVisible(true);
    }
}
