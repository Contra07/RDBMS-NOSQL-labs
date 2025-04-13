package ru.ssau.lab1.view.custom;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumn;

import org.cosinus.swing.form.Table;

import ru.ssau.lab1.service.CRUDService;
import ru.ssau.lab1.service.ServiceException;


public class CRUDTable<E> extends Table
{
    private final CRUDService<E, UUID> crudService;
    private int sRow = -1;
    private boolean selected = false;
    private GenericTableModel<E> model = null;

    public CRUDTable(CRUDService<E, UUID> crudService, E entity) 
    {
        super();
        this.crudService = crudService;
        model = new GenericTableModel<E>(entity);
        setModel(model);
    }

    @Override
    public void initComponents() 
    {
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setCellSelectionEnabled(true);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if(getSelectionModel() != null)
        {
            // getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            getSelectionModel().addListSelectionListener(e -> cellSelected(e));
        }
        reload();
    }

    @Override
    public void translate() {}

    public E getSelectedItem()
    {
        return getSelectedIndex() >= 0 && getSelectedIndex() < getRowCount() 
            ? (model).getDataVector().get(getSelectedIndex())
            : null;
    }
    
    public E getNewItem()
    {
        E result = null;
        try 
        {
            result = (E)model.getEntity().getClass().getConstructor(new Class[0]).newInstance();
        } 
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | NoSuchMethodException e) 
        {
            ErrorPane.showErrorDialog(this, e);
        }
        return result;
    }

    public int getSelectedIndex()
    {
        return sRow;
    }

    public void add(E item) 
    {
        try 
        {
            crudService.Create(item);
        } 
        catch (ServiceException e) 
        {
            ErrorPane.showErrorDialog(this, e);
        }
        selected = false;
        sRow = -1;
        reload();
    }

    public void edit(E item, int rowIndex) 
    {
        if(rowIndex >= 0 && rowIndex < getRowCount() && item != null)
        {
            try 
            {
                crudService.Update(item);
            } 
            catch (ServiceException e) 
            {
                ErrorPane.showErrorDialog(this, e);
            }
            selected = false;
            sRow = -1;
            reload();
        }
    }

    public void reload() 
    {   
        try 
        {
            var items = crudService.ReadAll();
            Vector<E> vector = new Vector<E>();
            for (E item : items) 
            {
                vector.add(item);
            }
            model.setDataVector(vector);
        } 
        catch (ServiceException e) 
        {
            ErrorPane.showErrorDialog(this, e);
        }
    }

    public void delete(int rowIndex)
    {
        if(rowIndex >= 0 && rowIndex < getRowCount())
        {
            UUID id = (UUID)getValueAt(rowIndex, 0);
            try 
            {
                crudService.Delete(id);
            } 
            catch (ServiceException e) 
            {

                ErrorPane.showErrorDialog(this, e);
            }
            selected = false;
            sRow = -1;
            reload();
        }
    }

    private void cellSelected(ListSelectionEvent event) 
    {
        if (!event.getValueIsAdjusting()) 
        {
            var selectedRow = getSelectedRow();
            var selectedAnchorRow = getSelectionModel().getAnchorSelectionIndex();
            var selectedLeadRow = getSelectionModel().getLeadSelectionIndex();
            var selectedColumn = getColumnModel().getSelectionModel().getAnchorSelectionIndex();
            var selectedAnchorColumn = getColumnModel().getSelectionModel().getAnchorSelectionIndex();
            var selectedLeadColumn = getColumnModel().getSelectionModel().getLeadSelectionIndex();
            var selectedIndices = getSelectionModel().getSelectedIndices();
            if (selectedRow != -1 && sRow != selectedRow) 
            {
                if(sRow != selectedRow)
                {       
                    sRow = selectedRow;
                    selected = false;
                }
                if(!selected)
                {
                    getSelectionModel().removeSelectionInterval
                    (
                        getSelectionModel().getAnchorSelectionIndex(), 
                        getSelectionModel().getLeadSelectionIndex()
                    ); 
                    getColumnModel().getSelectionModel().removeSelectionInterval
                    (
                        getColumnModel().getSelectionModel().getAnchorSelectionIndex(), 
                        getColumnModel().getSelectionModel().getLeadSelectionIndex()
                    );
                    getSelectionModel().setSelectionInterval(selectedAnchorRow, selectedAnchorRow);
                    getColumnModel().getSelectionModel().setAnchorSelectionIndex(selectedAnchorColumn);
                    getColumnModel().getSelectionModel().setLeadSelectionIndex(selectedAnchorColumn);
                    selected = true;
                }
            }
        }
    }
}
