/**
 *	To handle the records between the Graphical User Interface and the data storage
 */
public interface RecordHandler {
	
	public List<Record> getRecords();
	
	public boolean nwRecord(Record newRecord);
	
	public boolean rmRecord(int identifier);
	
	public boolean rmRecord(Record selectedRecord);
	
	public boolean mdRecord(Record selectedRecord);
	
	public List<Record> searchRecord(Search request);
	
}

