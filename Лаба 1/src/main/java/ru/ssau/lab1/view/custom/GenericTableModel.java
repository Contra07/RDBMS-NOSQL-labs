package ru.ssau.lab1.view.custom;

import java.lang.reflect.*;
import java.util.*;
import javax.swing.table.*;

/**
 * This is an implementation of <code>TableModel</code> that could be passed to
 * the constructor of {@code JTable} to create a table with any domain object
 * (POJO) with the use of annotations( {@link ColumnName} and
 * {@link TableTransient}) on the domain objects.
 * 
 * @author Sewwandi
 * 
 */
public class GenericTableModel<E> extends AbstractTableModel 
{

	private static final long serialVersionUID = 1L;

	/**
	 * Holds domain objects of type {@code E}
	 */
	private Vector<E> tableDataVector = new Vector<E>();
	private E e;
	private List<Field> fields = new ArrayList<Field>();

	/**
	 * @param e an object of type E
	 */
	public GenericTableModel(E e) 
	{
		super();
		this.e = e;
		setAllColumns();
	}

	public GenericTableModel(E e, Vector<E> data) 
	{
		this(e);
		setDataVector(data);
	}

	public E getEntity()
	{
		return e;
	}

	public Vector<E> getDataVector() 
	{
		return tableDataVector;
	}

	public void setDataVector(Vector<E> vector) 
	{
		this.tableDataVector = vector;
		fireTableDataChanged();
	}

	/**
	* Iterates the list of fields in the domain object to find the field which
	* should be set as columns of the table.
	*/
	public void setAllColumns() 
	{
		Field[] allFields = e.getClass().getDeclaredFields();
		for (int i = 0; i < allFields.length; i++) 
		{
			if (allFields[i].getAnnotation(TableTransient.class) == null) 
			{
				fields.add(allFields[i]);
			}
		}
	}

	@Override
	public int getColumnCount() 
	{
		return fields.size();
	}

	@Override
	public String getColumnName(int column) 
	{
		String columnName = "";
		// Uses the @ColumnName annotation to get the preferred column name.
		ColumnName colName = fields.get(column).getAnnotation(ColumnName.class);
		if (colName != null) 
		{
			columnName = colName.name();
		} 
		else 
		{
			columnName = fields.get(column).getName();
		}
		return columnName;

	}

	@Override
	public int getRowCount() 
	{
		return tableDataVector.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int c) 
	{
		return fields.get(c).getType();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) 
	{
		Object value = null;
		try 
		{
			E temp = tableDataVector.get(rowIndex);
				Field f = temp.getClass().getDeclaredField(
						fields.get(columnIndex).getName());
				f.setAccessible(true);
				value = f.get(temp);
				f.setAccessible(false);
		} 
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) 
		{
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<Field> fields) 
	{
		this.fields = fields;
	}

	/**
	 * @return the fields
	 */
	public List<Field> getFields() 
	{
		return fields;
	}

}

