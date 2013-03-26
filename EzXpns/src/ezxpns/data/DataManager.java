package ezxpns.data;

import java.util.*;

import ezxpns.data.records.*;
import ezxpns.util.Pair;

/**
 * A wrapper containing all data <br />
 * with some helper functions to query both types <br />
 * Everything here is handled by StorageManager to save to file <br />
 * Remember to add transient if the data is not meant to be persistent
 * @author yyjhao
 */
public class DataManager extends Storable
	implements
		ReportGenerator.DataProvider,
		TargetManager.DataProvider,
		SummaryGenerator.DataProvider,
		NWSGenerator.DataProvider{
	/**
	 * @author yyjhao
	 * A helper class that handles query that ask for both
	 * data types
	 */
	public static class CombinedRecordsQueryHandler
		implements RecordQueryHandler<Record>{
		private RecordManager<IncomeRecord> incomes;
		private ExpenseRecordManager expenses;
		/**
		 * @param _incomes A record manager that handles income records
		 * @param _expenses A record manager that handles expense records
		 */
		public CombinedRecordsQueryHandler(
				RecordManager<IncomeRecord> _incomes,
				RecordManager<ExpenseRecord> _expenses) {
			incomes = _incomes;
			expenses = (ExpenseRecordManager) _expenses;
		}

		@Override
		public Vector<Record> getRecordsBy(String name, int max) {
			Vector<Record> rs = new Vector<Record>();
			rs.addAll(incomes.getRecordsBy(name, max));
			rs.addAll(expenses.getRecordsBy(name, max));
			Collections.sort(rs);
			if(max != -1){
				for(int i = rs.size() - 1; i >= max; i--){
					rs.remove(i);
				}
			}
			return rs;
		}

		@Override
		public Vector<Record> getRecordsBy(Category category, int max) {
			Vector<Record> rs = new Vector<Record>();
			rs.addAll(incomes.getRecordsBy(category, max));
			rs.addAll(expenses.getRecordsBy(category, max));
			Collections.sort(rs);
			if(max != -1){
				for(int i = rs.size() - 1; i >= max; i--){
					rs.remove(i);
				}
			}
			return rs;
		}

		@Override
		public Vector<Record> getRecordsBy(Date start, Date end, int max,
				boolean reverse) {
			Vector<Record> rs = new Vector<Record>();
			rs.addAll(incomes.getRecordsBy(start, end, max, reverse));
			rs.addAll(expenses.getRecordsBy(start, end, max, reverse));
			Collections.sort(rs);
			if(max != -1){
				for(int i = rs.size() - 1; i >= max; i--){
					rs.remove(i);
				}
			}
			return rs;
		}

		@Override
		public Vector<Record> getRecordsByCategory(String name) {
			Vector<Record> rs = new Vector<Record>();
			rs.addAll(incomes.getRecordsByCategory(name));
			rs.addAll(expenses.getRecordsByCategory(name));
			Collections.sort(rs);
			return rs;
		}

		@Override
		public Vector<Record> getRecordsWithNamePrefix(String prefix) {
			Vector<Record> rs = new Vector<Record>();
			rs.addAll(incomes.getRecordsWithNamePrefix(prefix));
			rs.addAll(expenses.getRecordsWithNamePrefix(prefix));
			Collections.sort(rs);
			return rs;
		}
		
	}
	
	private ExpenseRecordManager _expenses = new ExpenseRecordManager();
	private RecordManager<IncomeRecord> _incomes = new RecordManager<IncomeRecord>();
	private NWSGenerator nwsGen = new NWSGenerator(this);
	/**
	 * Note that this is a combination of both income and expense record manager, <br />
	 * so it need not be persistent, since all its data is from the two manager
	 */
	private transient CombinedRecordsQueryHandler _combined;
	
	private TargetManager _targetManager = new TargetManager(this);
	
	@Override
	public Pair<Vector<ExpenseRecord>, Vector<IncomeRecord>> getDataInDateRange(
			Date start, Date end) {
		return new Pair<Vector<ExpenseRecord>, Vector<IncomeRecord>>(
				_expenses.getRecordsBy(start, end, -1, false),
				_incomes.getRecordsBy(start, end, -1, false));
	}
	
	/**
	 * @return The target manager
	 */
	public TargetManager targetManager(){
		return _targetManager;
	}
	
	/**
	 * @return Expense record manager
	 */
	public ExpenseRecordManager expenses(){
		return _expenses;
	}
	
	/**
	 * @return Income record manager
	 */
	public RecordManager<IncomeRecord> incomes(){
		return _incomes;
	}
	
	/**
	 * @return A query handler that returns both income and expense records
	 */
	public CombinedRecordsQueryHandler combined(){
		return _combined;
	}
	
	
	@Override
	public boolean isUpdated(){
		return _expenses.isUpdated() || _incomes.isUpdated()
				|| _targetManager.isUpdated()
				|| nwsGen.isUpdated();
	}
	
	@Override
	public void afterDeserialize(){
		_incomes.afterDeserialize();
		_expenses.afterDeserialize();
		_combined = new CombinedRecordsQueryHandler(_incomes, _expenses);
		_targetManager.setDataProvider(this);
		_targetManager.afterDeserialize();
		nwsGen.afterDeserialize();
	}
	
	@Override
	public void saved(){
		_expenses.saved();
		_incomes.saved();
		_targetManager.saved();
		nwsGen.saved();
	}

	@Override
	public double getMonthlyExpense(Category cat) {
		return _expenses.getMonthlySum(cat);
	}

	@Override
	public double getTotalExpense() {
		return _expenses.getAllTimeSum();
	}

	@Override
	public double getTotalIncome() {
		return _incomes.getAllTimeSum();
	}

	@Override
	public double getDailyExpense() {
		return _expenses.getDailySum();
	}

	@Override
	public double getDailyIncome() {
		return _incomes.getDailySum();
	}

	@Override
	public double getMonthlyExpense() {
		return _expenses.getMonthlySum();
	}

	@Override
	public double getMonthlyIncome() {
		return _incomes.getMonthlySum();
	}

	@Override
	public double getYearlyExpense() {
		return _expenses.getYearlySum();
	}

	@Override
	public double getYearlyIncome() {
		return _expenses.getYearlySum();
	}

	@Override
	public Category getCategory(long id) {
		return _expenses.getCategory(id);
	}

	@Override
	public double getMonthlyExpense(ExpenseType type) {
		return _expenses.getMonthlySum();
	}

	@Override
	public double getPrevMonthlyExpense(ExpenseType type) {
		return 0;
	}

	@Override
	public double getPrevMonthIncome() {
		return 0;
	}
}
