/**
 * 
 */
package ezxpns.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import ezxpns.data.records.*;

/**
 * @author yyjhao
 *
 */
@SuppressWarnings("serial")
public class RecordListView extends JTable {
	
	/**
	 * The GUI Editor that manages the user side inputs for editing of a Record
	 */
	public static interface RecordEditor{
		/**
		 * Edit the user indicated Record
		 * @param record to be Edited
		 */
		public void edit(Record record);
	}
	
	private static NumberFormat formatter = NumberFormat.getCurrencyInstance();
	private static DateFormat dateFormatter = DateFormat.getDateInstance();
	private static String[] headers = {
		"Name",
		"Amount",
		"Category",
		"Date"
	};
	
	private class ListModel extends AbstractTableModel {

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public int getRowCount() {
			return records.size();
		}

		@Override
		public Object getValueAt(int r, int c) {
			Record re = records.get(r);
			switch(c){
			case 0:
				return re.getName();
			case 1:
				if(re instanceof ExpenseRecord){
					return "-" + formatter.format(re.getAmount());
				}else{
					return " " + formatter.format(re.getAmount());
				}
			case 2:
				return re.getCategory();
			case 3:
				return dateFormatter.format(re.getDate());
			default:
				return null;
			}
		}
		
		@Override
		public String getColumnName(int col){
			return headers[col];
		}
	}
	
	/**
	 * Private Empty Constructor to disallow users from creating it without parameters 
	 */
	private RecordListView() {
		super();
	}
	
	private RecordEditor editor;
	private RecordHandler rhandler;
	private List<Record> records = new Vector<Record>();
	private ListModel model = new ListModel();
	private JPopupMenu menu;
	private Record itemSelected;
	private int rowSelected;
	
	public RecordListView(RecordEditor ed, RecordHandler rh){
		this();	// To call the superclass constructor
		editor = ed;
		rhandler = rh;
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Single selection
		setIntercellSpacing(new Dimension(0, 0));
		setShowHorizontalLines(false);
		setShowVerticalLines(false);
		setShowGrid(false);
		setModel(model);
		
		setFont(new java.awt.Font("Segoe UI", 0, 14)); // #Font

		menu = new JPopupMenu();
		
		JMenuItem mntmEdit = new JMenuItem("edit");
		mntmEdit.setAction(new AbstractAction("edit"){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				editItemAt(rowSelected);
			}
			
		});
		menu.add(mntmEdit);
		
		JMenuItem mntmRemove = new JMenuItem("remove");
		mntmRemove.setAction(new AbstractAction("remove"){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteItemAt(rowSelected);
			}
			
		});
		menu.add(mntmRemove);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() == 2){
					editItemAt(rowAtPoint(arg0.getPoint()));
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e){
		        if (e.isPopupTrigger())
		            doPop(e);
		    }

			@Override
		    public void mouseReleased(MouseEvent e){
		        if (e.isPopupTrigger())
		            doPop(e);
		    }

		    private void doPop(MouseEvent e){
		    	int row = rowAtPoint(e.getPoint());
		    	rowSelected = row;
		    	setRowSelectionInterval(row, row);
		    	itemSelected = records.get(row);
				menu.show(e.getComponent(), e.getX(), e.getY());
		    }
		});
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
		Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
		if(isCellSelected(rowIndex, vColIndex)) { 	// Default highlighting for user selection
			return c;
		}
		
		if (rowIndex % 2 == 1 && !isCellSelected(rowIndex, vColIndex)) {
			c.setBackground(new Color(230, 230, 230));
		} else {
			c.setBackground(Color.WHITE); 			// Even rows background color
		}
		
		return c;
	}
	
	/**
	 * Displays the records
	 * @param records
	 */
	public void show(List<Record> records){
		this.records = records;
		model.fireTableDataChanged();
	}

	/**
	 * Callback method for edit
	 * <br />Null return means that the user has cancelled the edit
	 * @param row
	 */
	public void itemEdited(Record newItem){
		records.set(rowSelected, newItem);
		model.fireTableRowsUpdated(rowSelected, rowSelected);
		setEnabled(true);
	}
	
	/**
	 * Edit the selected record 
	 * @param row the row Id of the Record that user has indicated for editing
	 */
	protected void editItemAt(int row){
		setEnabled(false);
		rowSelected = row;
		itemSelected = records.get(row);
		editor.edit(itemSelected);
	}
	
	/**
	 * Removed the selected record
	 * @param row the row Id of the Record that user has indicated for removal
	 */
	protected void deleteItemAt(int row){
		String message = "Are you sure you want to remove this record?";
		if(JOptionPane.showConfirmDialog(this, message, "what?!",
				JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
			rhandler.removeRecord(records.get(row).getId());
			records.remove(row);
			model.fireTableRowsDeleted(row, row);
		}
	}
}
