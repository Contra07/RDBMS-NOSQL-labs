package ru.ssau.lab1.view.custom;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ErrorPane 
{
    public static void showErrorDialog(Component parentComponent, Throwable ex)
    {
        ex.printStackTrace();
        Throwable thr = ex;
        while (thr.getCause() != null)
            thr = thr.getCause();
        var text = thr.equals(ex) ? ex.getMessage() : ex.getMessage() + "\n" + thr.getMessage();
        JOptionPane.showMessageDialog(parentComponent, text, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
