package test;

import static org.junit.Assert.*;

import org.junit.Test;

import ezxpns.data.SummaryDetails;
import ezxpns.data.SummaryGenerator;
import ezxpns.data.SummaryGenerator.SummaryType;

/**
 * Test SummaryGenerator
 * @author A0087091B
 *
 */
public class SummaryTest {
	
	SummaryGenerator.DataProvider data;
	SummaryGenerator summaryTest;
	double EPSILON = 0.001;

	public SummaryTest(){
		data = new SummaryGenerator.DataProvider() {
			
			@Override
			public double getYearlyIncome() {
				// TODO Auto-generated method stub
				return 8000;
			}
			
			@Override
			public double getYearlyExpense() {
				// TODO Auto-generated method stub
				return 10000.00;
			}
			
			@Override
			public double getTotalIncome() {
				// TODO Auto-generated method stub
				return 50000.28;
			}
			
			@Override
			public double getTotalExpense() {
				// TODO Auto-generated method stub
				return 40000.99;
			}
			
			@Override
			public double getMonthlyIncome() {
				// TODO Auto-generated method stub
				return 500;
			}
			
			@Override
			public double getMonthlyExpense() {
				// TODO Auto-generated method stub
				return 300;
			}
			
			@Override
			public double getDailyIncome() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public double getDailyExpense() {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		summaryTest = new SummaryGenerator(data);
	}
	
	@Test
	public void testToday() {
		SummaryDetails today = summaryTest.getSummaryDetails(SummaryType.TODAY);
		assertEquals(0, today.getBalance(),EPSILON);
	}
	
	@Test
	public void testMonth() {
		SummaryDetails month = summaryTest.getSummaryDetails(SummaryType.MONTH);
		assertEquals(200, month.getBalance(),EPSILON);
	}
	
	@Test
	public void testYear() {
		SummaryDetails year = summaryTest.getSummaryDetails(SummaryType.YEAR);
		assertEquals(-2000, year.getBalance(),EPSILON);
	}
	
	@Test
	public void testAll() {
		SummaryDetails all = summaryTest.getSummaryDetails(SummaryType.ALLTIME);
		assertEquals(9999.29, all.getBalance(),EPSILON);
	}

}
