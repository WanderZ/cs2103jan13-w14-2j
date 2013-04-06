package test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Vector;

import org.junit.Test;

import ezxpns.data.records.*;
import ezxpns.data.records.RecordManager.CategoryUpdateException;
import ezxpns.data.records.RecordManager.RecordUpdateException;

public class DataQueryTest {
	
	private RecordManager<IncomeRecord> m = new RecordManager<IncomeRecord>();
	
	public DataQueryTest(){
		m.afterDeserialize();
	}

	@Test
	public void testIsUpdated() throws RecordUpdateException {
		if(m.isUpdated()){
			fail("Record manager become updated when it it just initialised!");
		}
		Category cat = new Category(10, "isUpdated_category");
		m.addNewCategory(cat);
		if(!m.isUpdated()){
			fail("Record manager not updated after adding a category!");
		}
		m.saved();
		if(m.isUpdated()){
			fail("Record manager remained updated after saving!");
		}
		m.addNewRecord(new IncomeRecord(10,"is_updatedName", "nil", new Date(), cat));
		if(!m.isUpdated()){
			fail("Record manager not updated after adding record!");
		}
	}

	@Test
	public void testUpdateRecords() throws RecordUpdateException {
		Category cat = new Category(11, "update_recordCat");
		cat = m.addNewCategory(cat);
		IncomeRecord r = new IncomeRecord(10,"test_updateName", "nil", new Date(), cat);
		r = m.addNewRecord(r);
		if(m.getRecordsBy(r.getName(), 1).size() == 0){
			fail("Adding record failed!");
		}
		if(m.getRecordBy(r.getId()) == null){
			fail("Adding record failed!");
		}
		IncomeRecord newR = new IncomeRecord(r.getAmount(), r.getName() + "hoho", r.getRemark() + "hoho", r.getDate(), r.getCategory());
		try {
			m.updateRecord(r.getId(), newR);
		} catch (RecordUpdateException e) {
			e.printStackTrace();
			fail("Updating record results in an error!");
		}
		int s = m.getRecordsBy(r.getName() + "hoho", 100).size();
		int ors = m.getRecordsBy(r.getName(), 100).size();
		if(s != 1 || ors != 0){
			fail("Updating record failed! There are " +
					s + " records matching the new name," +
					ors + " records matching the old name!");
		}
		
		try {
			m.removeRecord(newR.getId());
		} catch (RecordUpdateException e) {
			e.printStackTrace();
			fail("Removing record results in an error!");
		}
		if(m.getRecordsBy(newR.getName(), 100).size() > 0){
			fail("Did not manage to remove the record!");
		}
	}

	@Test
	public void testUpdateCategory() throws RecordUpdateException {
		if(m.getCategory((long) 0) != Category.undefined){
			fail("Undefined category cannot be found in record manger");
		}
		Category cat = m.getCategory((long) 1); 
		if(cat != null){
			fail("Trying to get a non-existent category returns " + cat);
		}
		cat = new Category(100, "first");
		m.addNewCategory(cat);
		if(m.getCategory((long) 100) == null){
			fail("Adding category failed");
		}
		Category cat2 = new Category(100, "second");
		m.addNewCategory(cat2);
		if(!m.getCategory((long) 100).getName().equals("first")){
			fail("Adding category with repeated id overrides the original one!");
		}
		
		IncomeRecord r = new IncomeRecord(10, "category_test", "nil", new Date(), m.getCategory((long)100));
		r = m.addNewRecord(r);
		try {
			m.updateCategory((long)100, "third");
		} catch (CategoryUpdateException e) {
			e.printStackTrace();
			fail("Updating category results in an error.");
		}
		
		if(!m.getRecordsBy(r.getName(), 1).get(0).getCategory().getName().equals("third")){
			fail("Updating category failes to have effect on the record.");
		}
	}

	@Test
	public void testGetRecordsByCategoryInt() {
		Category cat = new Category(30, "category");
		Vector<IncomeRecord> result = m.getRecordsBy(cat, 100);
		if(result.size() > 0){
			fail("Getting records with non-existent category returns results: " + result);
		}
		m.addNewCategory(cat);
		cat = m.getCategory(cat.getID());
		try {
			for(int i = 0; i < 10; i++){
				IncomeRecord r = new IncomeRecord(10, "testCatRecord" + i, "nil", new Date((new Date()).getTime() + i), cat);
				m.addNewRecord(r);
			}
		} catch (RecordUpdateException e) {
			fail("Adding record results in an error");
			e.printStackTrace();
		}
		Category cat2 = new Category(10, "eh");
		result = m.getRecordsBy(cat2, 100);
		if(result.size() > 0){
			fail("Getting records with non-existent category returns results: " + result);
		}
		result = m.getRecordsBy(cat, 100);
		if(result.size() != 10){
			fail("Supposed to get 10 records with category, but instead get " + result);
		}
		result = m.getRecordsBy(cat, 2);
		if(result.size() != 2){
			fail("Supposed to get 2 records with category, but instead get " + result);
		}
		for(int i = 0; i < 2; i++){
			if(!result.get(i).getName().equals("testCatRecord" + (9 - i))){
				fail("Output not sorted!");
			}
		}
	}

	@Test
	public void testGetRecordsByStringInt() throws RecordUpdateException {
		Category cat = new Category(40, "category");
		Vector<IncomeRecord> result = m.getRecordsBy("testGettingName", 100);
		if(result.size() > 0){
			fail("Getting records with non-existent name returns results: " + result);
		}
		m.addNewCategory(cat);
		cat = m.getCategory(cat.getID());
		long curtime = (new Date()).getTime();
		for(int i = 0; i < 10; i++){
			IncomeRecord r = new IncomeRecord(10, "testNameRecord" + (i % 2), "nil", new Date(curtime + i), cat);
			m.addNewRecord(r);
		}
		result = m.getRecordsBy("testNameRecord1", 100);
		if(result.size() != 5){
			fail("Supposed to get 5 records with name, but instead get " + result);
		}
		result = m.getRecordsBy("testNameRecord1", 2);
		if(result.size() != 2){
			fail("Supposed to get 2 records with name, but instead get " + result);
		}
		for(int i = 0; i < 2; i++){
			if(!result.get(i).getDate().equals(new Date(curtime + 9 - i * 2))){
				fail("Output not sorted!");
			}
		}
	}

	@Test
	public void testGetRecordsByDateDateIntBoolean() throws RecordUpdateException {
		Category cat = new Category(50, "category");
		Vector<IncomeRecord> result = m.getRecordsBy(new Date(1), new Date(100), 100, false);
		if(result.size() > 0){
			fail("Getting records with non-existent name returns results: " + result);
		}
		m.addNewCategory(cat);
		cat = m.getCategory(cat.getID());
		long curtime = 40;
		for(int i = 0; i < 10; i++){
			IncomeRecord r = new IncomeRecord(10, "testDateRecord" + i, "nil", new Date(curtime + i * 100), cat);
			m.addNewRecord(r);
		}
		result = m.getRecordsBy(new Date(0), new Date(curtime + 900), 100, false);
		if(result.size() != 10){
			fail("Supposed to get 10 records with date, but instead get " + result);
		}
		result = m.getRecordsBy(new Date(40), new Date(1800), 2, false);
		if(result.size() != 2){
			fail("Supposed to get 2 records with date, but instead get " + result);
		}
		for(int i = 0; i < 2; i++){
			if(!result.get(i).getDate().equals(new Date(curtime + i * 100))){
				fail("Output not sorted!");
			}
		}
		result = m.getRecordsBy(new Date(40), new Date(1000), 2, true);
		for(int i = 0; i < 2; i++){
			if(!result.get(i).getDate().equals(new Date(curtime + (9 - i) * 100))){
				fail("Reversed date query failed!");
			}
		}
	}

}
