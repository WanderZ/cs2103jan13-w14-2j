package ezxpns.GUI;

import java.util.List;

import ezxpns.data.records.Category;
import ezxpns.data.records.Record;

/**
 * Temporal Pseudo Master Handler class to test all the functionality in the UI
 *
 */
public class PseudoMasterHandler implements CategoryHandlerInterface, RecordHandlerInterface, SearchHandlerInterface {

	private java.util.Stack undoStack;
	
	@Override
	public List<Record> search(int searchCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> getAllRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Record getRecord(int identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createRecord(Record newRecord) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRecord(int identifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRecord(Record selectedRecord) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyRecord(Record selectedRecord) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Record> searchRecord(SearchRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createCategory(Category newCat) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeCategory(int identifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeCategory(Category selectedCat) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyCategory(Category selectedCat) {
		// TODO Auto-generated method stub
		return false;
	}

}
