package ezxpns.GUI;

import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import ezxpns.data.Report;
import ezxpns.data.ReportCategory;

public class tzReportData {
	
	
	public Report createTestReport(){
		Report myReport = new Report(new Date(), new Date());
		myReport.setHeading(1000, 800, 200, 5);
		Vector<ReportCategory> myExpense = new Vector<ReportCategory>();
		ReportCategory one = new ReportCategory("Food");
		one.incrementAmount(500);
		for (int i = 0; i < 16; i++){
			one.incrementFreq();
		}
		one.setPercentage(80);
		one.calAmtPerFreq();
		myExpense.add(one);
		
		ReportCategory two = new ReportCategory("Transport");
		two.incrementAmount(300);
		for (int i = 0; i < 40; i++){
			two.incrementFreq();
		}
		two.setPercentage(20);
		two.calAmtPerFreq();
		myExpense.add(two);
		Collections.sort(myExpense);
		
		myReport.setSectionExpense(myExpense);
		
		//System.out.println(myExpense.get(0).getCategory());
		
		return myReport;
		
	}

}
