/**
 *  This class was originally written by Rob Camick and taken from:
 *  http://tips4java.wordpress.com/2009/07/12/table-button-column/
 *  
 *  I removed parts from it and added support for highlighting the delete button
 *  when the mouse is hovering over it
 *  
 *  The ButtonColumn class provides a renderer and an editor that looks like a
 *  JButton. The renderer and editor will then be used for a specified column
 *  in the table. The TableModel will contain the String to be displayed on
 *  the button.
 *
 *  The button can be invoked by a mouse click or by pressing the space bar
 *  when the cell has focus. Optionally a mnemonic can be set to invoke the
 *  button. When the button is invoked the provided Action is invoked. The
 *  source of the Action will be the table. The action command will contain
 *  the model row number of the button that was clicked.
 *
 */
package info.msxlaunchers.openmsx.launcher.ui.view.swing.component;

import info.msxlaunchers.openmsx.launcher.ui.view.swing.MainWindow;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class JTableButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener, MouseMotionListener
{
	private final JTable table;
	private final Action action;

	private final JButton renderButton;
	private final JButton actionButton;
	private Object editorValue;
	private boolean isButtonColumnEditor;
	private int currentRow;
	private int currentColumn;

	/**
	 *  Create the JTableButtonColumn to be used as a renderer and editor. The
	 *  renderer and editor will automatically be installed on the TableColumn
	 *  of the specified column.
	 *
	 *  @param action the Action to be invoked when the button is invoked
	 *  @param column the column to which the button renderer/editor is added
	 */
	public JTableButtonColumn(JTable table, Action action, int column, Icon icon, String tooltipText)
	{
		this.table = table;
		this.action = action;

		renderButton =  new JButton();
		renderButton.setContentAreaFilled(false);
		renderButton.addMouseListener(this);
		renderButton.setIcon(icon);
		renderButton.setToolTipText(tooltipText);

		actionButton = new JButton();
		actionButton.setContentAreaFilled(false);
    	actionButton.setBorder(BorderFactory.createEtchedBorder());
		actionButton.setIcon(icon);
		actionButton.addActionListener(this);

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(column).setCellRenderer(this);
		columnModel.getColumn(column).setCellEditor(this);
		table.addMouseListener(this);
		table.addMouseMotionListener(this);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		this.editorValue = value;

		return actionButton;
	}

	@Override
	public Object getCellEditorValue()
	{
		return editorValue;
	}

	//
	//  Implement TableCellRenderer interface
	//
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		if(row == currentRow && column == currentColumn)
		{
	 		renderButton.setBackground(MainWindow.POPUP_MENU_ITEM_BUTTON_HOVER_BG_COLOR);
        	renderButton.setBorder(BorderFactory.createEtchedBorder());
		}
		else
		{
			renderButton.setBackground(Color.white);
        	renderButton.setBorder(null);
		}

		return renderButton;
	}

	//
	//  Implement ActionListener interface
	//
	/*
	 *	The button has been pressed. Stop editing and invoke the custom Action
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		int row = table.convertRowIndexToModel(table.getEditingRow());
		fireEditingStopped();

		//  Invoke the Action
		action.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, "" + row));
	}

	//
	//  Implement MouseListener interface
	//
	/*
	 *  When the mouse is pressed the editor is invoked. If you then then drag
	 *  the mouse to another cell before releasing it, the editor is still
	 *  active. Make sure editing is stopped when the mouse is released.
	 */
	@Override
    public void mousePressed(MouseEvent e)
    {
    	if(table.isEditing() && table.getCellEditor() == this)
    	{
			isButtonColumnEditor = true;
    	}
    }

	@Override
    public void mouseReleased(MouseEvent e)
    {
    	if(isButtonColumnEditor &&  table.isEditing())
    	{
    		table.getCellEditor().stopCellEditing();
    	}

		isButtonColumnEditor = false;
    }

	@Override
    public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e)
	{
		currentRow = currentColumn = 0;
		table.repaint();
	}

	//
	//  Implement MouseMotionListener interface
	//
	@Override
	public void mouseDragged(MouseEvent e) {}

	/*
	 * The following overridden method is used to highlight the delete button
	 * when the mouse pointer is over it
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		Point p = e.getPoint();
	    if(p != null)
	    {
	        currentRow = table.rowAtPoint(p);
	        currentColumn = table.columnAtPoint(p);
	        table.repaint();
	    }
	}
}