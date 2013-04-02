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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

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
		 * @param display RecordListView Object GUI display to be updated
		 */
		public void edit(Record record, RecordListView display);
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
        public Class<?> getColumnClass(int c) {
			switch(c){
			case 0:
				return String.class;
			case 1:
				return Double.class;
			case 2:
				return String.class;
			case 3:
				return Date.class;
			default:
				return null;
			}
        }

		@Override
		public Object getValueAt(int r, int c) {
			Record re = records.get(r);
			switch(c){
			case 0:
				return re.getName();
			case 1:
				if(re instanceof ExpenseRecord){
					return -re.getAmount();
				}else{
					return re.getAmount();
				}
			case 2:
				return re.getCategory().toString();
			case 3:
				return re.getDate();
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
	private Record itemsSelected;
	private int rowSelected;
	private int[] rowsSelected;
	private UpdateNotifyee notifyee;
	
	public RecordListView(RecordEditor ed, RecordHandler rh, UpdateNotifyee notifyee){
		this();	// To call the superclass constructor
		editor = ed;
		rhandler = rh;
		this.notifyee = notifyee;
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setIntercellSpacing(new Dimension(0, 0));
		setShowHorizontalLines(false);
		setShowVerticalLines(false);
		setShowGrid(false);
		setModel(model);
		
		this.setAutoCreateRowSorter(true);
		
		this.setDefaultRenderer(Date.class, new DefaultTableCellRenderer(){
			@Override
			public void setValue(Object value){
				if(value != null) setText(dateFormatter.format(value));
			}
		});
		
		this.setDefaultRenderer(Double.class, new DefaultTableCellRenderer(){
			@Override
			public void setValue(Object value){
				if(value == null)return;
				double dval = (Double)value;
				String val;
				if(dval > 0){
					val = " " + formatter.format(dval);
				}else{
					val = "-" + formatter.format(-dval);
				}
				setText(val);
			}
		});

		
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
				deleteItemsAt(rowsSelected);
			}
			
		});
		menu.add(mntmRemove);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() == 2 && arg0.getButton() == MouseEvent.BUTTON1){
					editItemAt(convertRowIndexToModel(rowAtPoint(arg0.getPoint())));
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
		    	int row = convertRowIndexToModel(rowAtPoint(e.getPoint()));
		    	rowSelected = convertRowIndexToModel(row);
		    	int[] orirows = getSelectedRows();
		    	int[] rows = new int[orirows.length];
		    	for(int i = 0; i< orirows.length; i++){
		    		rows[i] = convertRowIndexToModel(orirows[i]);
		    	}
		    	rowsSelected = rows;
		    	boolean found = false;
		    	for(int i = 0; i < rows.length; i++){
		    		if(rows[i] == row){
		    			found = true;
		    			break;
		    		}
		    	}
		    	if(!found){
		    		setRowSelectionInterval(row, row);
		    		rowsSelected = new int[1];
		    		rowsSelected[0] = row;
		    	}
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
		System.out.println(records.size());
		model.fireTableDataChanged();
	}

	/**
	 * Callback method for edit
	 * <br />Null return means that the user has cancelled the edit
	 * @param row
	 */
	public void itemEdited(Record newItem){
		if(newItem == null)return;
		records.set(rowSelected, newItem);
		model.fireTableRowsUpdated(rowSelected, rowSelected);
		notifyee.updateAll();
		System.out.println("edited");
	}
	
	/**
	 * Edit the selected record 
	 * @param row the row Id of the Record that user has indicated for editing
	 */
	protected void editItemAt(int row){
		rowSelected = row;
		itemSelected = records.get(row);
		editor.edit(itemSelected, this);
	}
	
	/**
	 * Removed the selected record
	 * @param row the row Id of the Record that user has indicated for removal
	 */
	protected void deleteItemsAt(int[] rows){
		String message = "Are you sure you want to remove these records?";
		if(JOptionPane.showConfirmDialog(this, message, "what?!",
				JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
			Vector<Record> originals = new Vector<Record>();
			Vector<Record> old = new Vector<Record>();
			for(int i = 0; i < rows.length; i++){
				Record original = records.get(rows[i]);
				Record copy;
				old.add(original);
				if(original instanceof IncomeRecord){
					copy = ((IncomeRecord)original).copy();
				}else{
					copy = ((ExpenseRecord)original).copy();
				}
				originals.add(copy);
				rhandler.removeRecord(original.getId());
			}
			records.removeAll(old);
			model.fireTableDataChanged();
			
			notifyee.addUndoAction(getUndoDeleteRecord(originals), "Removing Record");
			notifyee.updateAll();
		}
	}
	
	private AbstractAction getUndoDeleteRecord(final Vector<Record> originals){
		return new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for(Record original : originals){
					if(original instanceof IncomeRecord){
							rhandler.createRecord((IncomeRecord)original, false);
					}else{
						rhandler.createRecord((ExpenseRecord)original, false, false);
					}
				}
			}
			
		};
	}
}
