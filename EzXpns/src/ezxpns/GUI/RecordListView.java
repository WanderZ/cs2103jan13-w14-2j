/**
 * 
 */
package ezxpns.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import ezxpns.data.records.*;

/**
 * @author yyjhao
 *
 */
public class RecordListView extends JTable {
	public static interface RecordEditor{
		void edit(Record r);
	}
	
	private static String[] headers = {
		"Name",
		"Amount",
		"Category",
		"Date"
	};
	
	private class ListModel extends AbstractTableModel{

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
					return -re.getAmount();
				}else{
					return re.getAmount();
				}
			case 2:
				return re.getCategory();
			case 3:
				return re.getDate();
			default:
				return null;
			}
		}
		
	}
	
	private RecordListView(){}
	
	private RecordEditor editor;
	private RecordHandler rhandler;
	private List<Record> records = new Vector<Record>();
	private ListModel model = new ListModel();
	private JPopupMenu menu;
	private Record itemSelected;
	private int rowSelected;
	
	public RecordListView(RecordEditor ed, RecordHandler rh){
		editor = ed;
		rhandler = rh;
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setIntercellSpacing(new Dimension(0, 0));
		setShowHorizontalLines(false);
		setShowVerticalLines(false);
		setShowGrid(false);
		
		menu = new JPopupMenu();
		
		JMenuItem mntmEdit = new JMenuItem("edit");
		mntmEdit.setAction(new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				editor.edit(itemSelected);
			}
			
		});
		menu.add(mntmEdit);
		
		JMenuItem mntmRemove = new JMenuItem("remove");
		mntmRemove.setAction(new AbstractAction(){

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
	
	public void show(List<Record> records){
		this.records = records;
		model.fireTableDataChanged();
	}
	
	public void itemEdited(Record newItem){
		records.set(rowSelected, newItem);
		model.fireTableRowsUpdated(rowSelected, rowSelected);
		setEnabled(true);
	}
	
	protected void editItemAt(int row){
		setEnabled(false);
		rowSelected = row;
		itemSelected = records.get(row);
		editor.edit(itemSelected);
	}
	
	protected void deleteItemAt(int row){
		rhandler.removeRecord(records.get(row).getId());
		records.remove(row);
		model.fireTableRowsDeleted(row, row);
	}
}