package ezxpns.data;

import java.util.Calendar;
import java.util.GregorianCalendar;
import ezxpns.data.records.ExpenseType;

/**
 * This class generates target ratios for needs,wants and savings
 * 
 * @author A0085413J
 */

public class NWSGenerator extends Storable {


	public static interface DataProvider {
		double getMonthlyExpense(ExpenseType type);

		double getMonthlyIncome();

		double getPrevMonthlyExpense(ExpenseType type); // get the previous
		// month's data

		double getPrevMonthlyIncome(); // get previous month's income
	}



	private transient DataProvider data;
	private transient boolean dataUpdated = true;

	// Ideal or default ratios
	private final double NEEDS = 0.5;
	private final double WANTS = 0.3;
	private final double SAVINGS = 0.2;

	private final double BUFFER = 0.05; // +- 5% buffer

	// 0.1<Needs<0.8
	private final double MAXNEEDS = 0.7;
	private final double MINNEEDS = 0.1;
	// 0.1<Wants<0.5
	private final double MAXWANTS = 0.5;
	private final double MINWANTS = 0.1;
	// 0.2<Savings<0.7
	private final double MAXSAVINGS = 0.7;
	private final double MINSAVINGS = 0.12;

	private NWSdata pastMonthNWS = new NWSdata();
	private NWSdata thisMonthNWS = new NWSdata();

	private transient double targetNeeds;
	private transient double targetWants;
	private transient double targetSavings;

	private transient double prevMonthNeedsExp;
	private transient double prevMonthWantsExp;
	private transient double prevMonthSavings; 

	private transient double diffFromNT;
	private transient double diffFromWT;
	private transient double diffFromST;

	public NWSGenerator(DataProvider data) {

		this.data = data;
		dataUpdated = true;
	}


	public void setDataProvider(DataProvider data) {

		this.data = data;
		dataUpdated = true;
	}


	public void markDataUpdated() {

		dataUpdated = true;
	}

	/**
	 * Updates data after deserialization
	 */
	@Override
	public void afterDeserialize() {

		if ((!thisMonthNWS.isValid()) && (!pastMonthNWS.isValid())) {
			initNWSdata();
		}

		if(isExpired(thisMonthNWS)){

			if(hasSkippedAMonth(thisMonthNWS.getDate())){ 
				initNWSdata();
			}
			else{
				setToPastMonth(thisMonthNWS);
				generateNWS();
			}
		}

		else{
			updateThisMonthProgress();
		}
		markUpdate();
	}

	/**
	 * Updates NWSdata and saves it in json
	 */
	public void updateNWSdata() {
		if(pastMonthNWS.isValid()&&hasModifiedPastMonthRecords()){
			updatePastMonthProgress();
			generateNWS();
		}
		else{
			updateThisMonthProgress();
		}
		markUpdate();
	}

	/**
	 * Returns a copy of NWSdata for this month
	 * 
	 * @return
	 */
	public NWSdata getNWSdataCopy() {
		if (dataUpdated) {
			updateNWSdata();
			dataUpdated = false; 
		}

		return thisMonthNWS.copy();
	}

	/**
	 * Generates and sets the ratio for NEEDS, WANTS and SAVINGS
	 * @return
	 */
	public void generateNWS() {
		//check for valid pastMonthNWS
		if(!pastMonthNWS.isValid()){
			updateThisMonthProgress();
			markUpdate();
			return;
		}

		//check for zero income
		if(pastMonthNWS.getIncome()==0){
			updateThisMonthProgress();
			markUpdate();
			return;
		}


		//generates ratio
		targetNeeds = pastMonthNWS.getTargetNeedsRatio();
		targetWants = pastMonthNWS.getTargetWantsRatio();
		targetSavings = pastMonthNWS.getTargetSavingsRatio();

		prevMonthNeedsExp = pastMonthNWS.getCurrNeedsRatio(); // ratio
		prevMonthWantsExp = pastMonthNWS.getCurrWantsRatio(); // ratio
		prevMonthSavings = pastMonthNWS.getCurrSavingsRatio(); // ratio

		// difference from ____ Targets = Target ratio - Actual ratio
		diffFromNT = targetNeeds - prevMonthNeedsExp;
		diffFromWT = targetWants - prevMonthWantsExp;
		diffFromST = targetSavings - prevMonthSavings;

		int i = 0;

		/*
		 * if the difference from the target is more than 5%, we will count it
		 * as missing the target Now we will compute i, which represent the
		 * state. the value of i should be read at base 2 eg. i = 6 (in base 10)
		 * = 110 (in base 2) Needs|Wants|Savings 1 | 1 | 0 this implies that
		 * Needs and Wants did not meet the target, while Savings has met the
		 * target (<5% diff)
		 */
		if (Math.abs(diffFromNT) > BUFFER) {
			i += 4;
		}

		if (Math.abs(diffFromWT) > BUFFER) {
			i += 2;
		}

		if (Math.abs(diffFromST) > BUFFER) {
			i += 1;
		}

		switch (i) {

		case 0: // 000 
			if (hasExceededTarget(diffFromST)) {
				if (!hasExceededTarget(diffFromNT)) {
					if (canIncrease(MAXSAVINGS, targetSavings)
							&& canReduce(MINNEEDS, targetNeeds))
						targetNeeds -= BUFFER;
					targetSavings += BUFFER;
				}

				if (!hasExceededTarget(diffFromWT)) {
					if (canIncrease(MAXSAVINGS, targetSavings)
							&& canReduce(MINWANTS, targetWants)) {
						targetWants -= BUFFER;
						targetSavings += BUFFER;
					}
				}
			}
			break;

		case 1:// 001
			if (!hasExceededTarget(diffFromST)) {
				if (canReduce(MINSAVINGS, targetSavings)) {
					if (hasExceededTarget(diffFromNT)
							&& canIncrease(MAXNEEDS, targetNeeds)) {
						targetSavings -= BUFFER;
						targetNeeds += BUFFER;
					}

					else if (hasExceededTarget(diffFromWT)
							&& canIncrease(MAXWANTS, targetWants)) {
						targetSavings -= BUFFER;
						targetWants += BUFFER;
					}

				}
			}

			else { // target more than 0
				if (canIncrease(MAXSAVINGS, targetSavings)) {
					if (!hasExceededTarget(diffFromNT)
							&& canReduce(MINNEEDS, targetNeeds)) {
						targetSavings += BUFFER;
						targetNeeds -= BUFFER;
					} else if (!hasExceededTarget(diffFromWT)
							&& canReduce(MINWANTS, targetWants)) {
						targetSavings += BUFFER;
						targetWants -= BUFFER;
					}
				}
			}

			break;
		case 2:// 010
			if (!hasExceededTarget(diffFromWT)) {
				if (canReduce(MINWANTS, targetWants)) {
					if (hasExceededTarget(diffFromNT)
							&& canIncrease(MAXNEEDS, targetNeeds)) {
						targetWants -= BUFFER;
						targetNeeds += BUFFER;
					}

					else if (hasExceededTarget(diffFromST)
							&& canIncrease(MAXSAVINGS, targetSavings)) {
						targetWants -= BUFFER;
						targetSavings += BUFFER;
					}
				}
			}

			else {
				if (canIncrease(MAXWANTS, targetWants)) {
					if (!hasExceededTarget(diffFromNT)
							&& canReduce(MINNEEDS, targetNeeds)) {
						targetWants += BUFFER;
						targetNeeds -= BUFFER;
					} else if (!hasExceededTarget(diffFromST)
							&& canReduce(MINSAVINGS, targetSavings)) {
						targetWants += BUFFER;
						targetSavings -= BUFFER;
					}
				}
			}

			break;

		case 3:// 011
			if (!hasExceededTarget(diffFromWT) 
					&& hasExceededTarget(diffFromST)) {
				if (canIncrease(MAXSAVINGS, targetSavings)
						&& canReduce(MINWANTS, targetWants)) {
					targetSavings += BUFFER;
					targetWants -= BUFFER;
				}
			}

			else if (hasExceededTarget(diffFromWT)
					&& !hasExceededTarget(diffFromST)) {
				if (canReduce(MINSAVINGS, targetSavings)
						&& canIncrease(MAXWANTS, targetWants)) {
					targetSavings -= BUFFER;
					targetWants += BUFFER;
				}
			}
			break;

		case 4:// 100
			if (!hasExceededTarget(diffFromNT)) {
				if (canReduce(MINNEEDS, targetNeeds)) {
					if (hasExceededTarget(diffFromST)
							&& canIncrease(MAXSAVINGS, targetSavings)) {
						targetSavings += BUFFER;
						targetNeeds -= BUFFER;
					}

					else if (hasExceededTarget(diffFromWT)
							&& canIncrease(MAXWANTS, targetWants)) {
						targetWants += BUFFER;
						targetNeeds -= BUFFER;
					}
				}
			} else {
				if (canIncrease(MAXNEEDS, targetNeeds)) {
					if (!hasExceededTarget(diffFromST)
							&& canReduce(MINSAVINGS, targetSavings)) {
						targetSavings -= BUFFER;
						targetNeeds += BUFFER;
					} else if (!hasExceededTarget(diffFromWT)
							&& canReduce(MINWANTS, targetWants)) {
						targetWants -= BUFFER;
						targetNeeds += BUFFER;
					}
				}
			}
			break;

		case 5:// 101
			if (!hasExceededTarget(diffFromNT) && hasExceededTarget(diffFromST)) {
				if (canReduce(MINNEEDS, targetNeeds)
						&& canIncrease(MAXSAVINGS, targetSavings)) {
					targetNeeds -= BUFFER;
					targetSavings += BUFFER;
				}
			} else if (hasExceededTarget(diffFromNT)
					&& !hasExceededTarget(diffFromST)) {
				if (canIncrease(MAXNEEDS, targetNeeds)
						&& canReduce(MINSAVINGS, targetSavings)) {
					targetNeeds += BUFFER;
					targetSavings -= BUFFER;
				}
			}
			break;

		case 6:// 110
			if (!hasExceededTarget(diffFromNT) && hasExceededTarget(diffFromWT)) {
				if (canReduce(MINNEEDS, targetNeeds)
						&& canIncrease(MAXWANTS, targetWants)) {
					targetNeeds -= BUFFER;
					targetWants += BUFFER;
				}
			} else if (hasExceededTarget(diffFromNT) && !hasExceededTarget(diffFromWT)) {
				if (canIncrease(MAXNEEDS, targetNeeds)
						&& canReduce(MINWANTS, targetWants)) {
					targetNeeds += BUFFER;
					targetWants -= BUFFER;
				}
			}
			break;

		case 7: // 111
			ExpenseType min = ExpenseType.NEED;
			double minAmt = Math.abs(diffFromNT);

			if(Math.abs(diffFromST)<minAmt){
				min = ExpenseType.SAVE;
				minAmt = Math.abs(diffFromST);
			}

			if(Math.abs(diffFromWT)<minAmt){
				min = ExpenseType.WANT;
				minAmt = Math.abs(diffFromWT);
			}

			if(min == ExpenseType.NEED){	
				modifyWants();
				modifySavings();
				allocateRemainingToNeeds();
			}
			else if(min == ExpenseType.WANT){
				modifyNeeds();
				modifySavings();
				allocateRemainingToWants();
			}
			else{
				modifyNeeds();
				modifyWants();
				allocateRemainingToSavings();
			}
			break;

		default:
			break;
		}

		thisMonthNWS.setAll(new GregorianCalendar(), targetNeeds, targetWants,
				targetSavings, data.getMonthlyExpense(ExpenseType.NEED),
				data.getMonthlyExpense(ExpenseType.WANT), getMonthlySavings(),
				data.getMonthlyIncome());
		markUpdate();

	}

	/******Helper Methods******/

	/**
	 * Returns the actual amount of money not spent this month i.e. this month's savings
	 * 
	 * @return
	 */
	private double getMonthlySavings() {
		double savings = data.getMonthlyIncome()
				- data.getMonthlyExpense(ExpenseType.NEED)
				- data.getMonthlyExpense(ExpenseType.WANT);
		if (savings > 0) // makes sure there is no negative savings
			return savings;
		else
			return 0;
	}

	/**
	 * Returns the actual amount of money not spent in the previous month i.e. previous month's savings
	 * @return
	 */
	private double getPrevMonthlySavings() {
		double savings = data.getPrevMonthlyIncome()
				- data.getPrevMonthlyExpense(ExpenseType.NEED)
				- data.getPrevMonthlyExpense(ExpenseType.WANT);
		if (savings > 0) // makes sure there is no negative savings
			return savings;
		else
			return 0;
	}


	/**
	 * Set NWS values to default and start from clean slate.
	 */
	private void initNWSdata(){
		thisMonthNWS.setAll(new GregorianCalendar(), NEEDS, WANTS, SAVINGS,
				data.getMonthlyExpense(ExpenseType.NEED),
				data.getMonthlyExpense(ExpenseType.WANT),
				getMonthlySavings(), data.getMonthlyIncome());
		pastMonthNWS = new NWSdata();
	}

	/**
	 * This returns true if the needs, wants or savings target can be increased
	 * by 5% without exceeding MAX
	 * 
	 * @param MAX
	 * @param oldTarget
	 * @return
	 */
	private boolean canIncrease(double MAX, double oldTarget) {
		if ((oldTarget + 0.05) <= MAX) {
			return true;
		}
		return false;
	}

	/**
	 * This returns true if the needs, wants or savings target can be reduced by
	 * 5% without going below MIN
	 * 
	 * @param MIN
	 * @param oldTarget
	 * @return
	 */
	private boolean canReduce(double MIN, double oldTarget) {
		if ((oldTarget - 0.05) >= MIN) {
			return true;
		}
		return false;
	}

	/**
	 * This returns true if it is a new month.
	 * 
	 * @param date
	 * @return true if date's Month and Year is different from the date in
	 *         thisMonthNWS
	 */
	private boolean isExpired(NWSdata nwsData) {
		Calendar date = nwsData.getDate();
		Calendar today = new GregorianCalendar();
		if (date.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
			if (date.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if today's date is more than two months from last recorded date
	 * @param date
	 * @return
	 */
	private boolean hasSkippedAMonth(Calendar date){
		Calendar today = new GregorianCalendar();	
		if(today.get(Calendar.YEAR)==date.get(Calendar.YEAR)){ //is same year
			if(today.get(Calendar.MONTH)-date.get(Calendar.MONTH)==1){//has a difference of 1 month
				return false;
			}
		}
		else if(today.get(Calendar.YEAR)-date.get(Calendar.YEAR)==1){//one year difference
			if(today.get(Calendar.MONTH)==Calendar.JANUARY && date.get(Calendar.MONTH)==Calendar.DECEMBER){//december to january
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if diff is less than 0
	 * @param diff
	 * @return
	 */
	private boolean hasExceededTarget(double diff) {
		if (diff < 0)
			return true;
		else
			return false;
	}

	/**
	 * Sets pastMonthNWS using thisMonthNWS's data
	 * @param thisMonthNWS
	 */
	private void setToPastMonth(NWSdata thisMonthNWS){
		pastMonthNWS.setAll(
				thisMonthNWS.getDate(),
				thisMonthNWS.getTargetNeedsRatio(),
				thisMonthNWS.getTargetWantsRatio(),
				thisMonthNWS.getTargetSavingsRatio(),
				thisMonthNWS.getCurrentNeeds(),
				thisMonthNWS.getCurrentWants(),
				thisMonthNWS.getCurrentSavings(),
				thisMonthNWS.getIncome());
	}

	/**
	 * Modifies savings target until the difference from savings target is more or equal to two times the buffer amount
	 * @return
	 */
	private void modifySavings(){
		while (Math.abs(diffFromST) >= 2 * BUFFER) {
			if (hasExceededTarget(diffFromST)) {
				if (canIncrease(MAXSAVINGS, targetSavings)) {
					targetSavings += BUFFER;
					diffFromST = targetSavings - prevMonthSavings;
				} else {
					break;
				}
			} else { // miss target
				if (canReduce(MINSAVINGS, targetSavings)) {
					targetSavings -= BUFFER;
					diffFromNT = targetSavings - prevMonthSavings;
				} else {
					break;
				}
			}
		}

	}

	/**
	 * Modifies needs target until the difference from needs target is more or equal to two times the buffer amount
	 * @return
	 */
	private void modifyNeeds(){
		while (Math.abs(diffFromNT) >= 2 * BUFFER) {
			if (hasExceededTarget(diffFromNT)) {
				if (canIncrease(MAXNEEDS, targetNeeds)) {
					targetNeeds += BUFFER;
					diffFromNT = targetNeeds - prevMonthNeedsExp;
				} 
				else {
					break;
				}
			} else { // miss target
				if (canReduce(MINNEEDS, targetNeeds)) {
					targetNeeds -= BUFFER;
					diffFromNT = targetNeeds - prevMonthNeedsExp;
				} else {
					break;
				}
			}
		}
	}

	/**
	 * Modifies wants target until the difference from wants target is more or equal to two times the buffer amount
	 */
	private void modifyWants(){
		while (Math.abs(diffFromWT) >= 2 * BUFFER) {
			if (hasExceededTarget(diffFromWT)) {
				if (canIncrease(MAXWANTS, targetWants)) {
					targetWants += BUFFER;
					diffFromWT = targetWants - prevMonthWantsExp;
				} 
				else {
					break;
				}
			} else { // miss target
				if (canReduce(MINWANTS, targetWants)) {
					targetWants -= BUFFER;
					diffFromWT = targetWants - prevMonthWantsExp;
				} else {
					break;
				}
			}
		}
	}

	/**
	 * Sets wants target using remaining percentages
	 */
	private void allocateRemainingToWants(){
		targetWants = 1 - targetNeeds - targetSavings;
		while(targetWants>MAXWANTS || targetWants<MINWANTS){
			if (targetWants > MAXWANTS) {
				targetWants -= BUFFER;
				if (targetNeeds + BUFFER <= MAXNEEDS) {
					targetNeeds += BUFFER;
				} else if (targetSavings + BUFFER <= MAXSAVINGS) {
					targetSavings += BUFFER;
				}
			} else if (targetWants < MINWANTS) {
				targetWants += BUFFER;
				if (targetNeeds - BUFFER >= MINNEEDS) {
					targetNeeds -= BUFFER;
				} else if (targetSavings - BUFFER >= MINSAVINGS) {
					targetSavings -= BUFFER;
				}
			}
			else break;
		}
	}

	/**
	 * Sets savings target using remaining percentages
	 */
	private void allocateRemainingToSavings(){
		targetSavings = 1 - targetNeeds - targetWants;
		while(targetSavings>MAXSAVINGS || targetSavings<MINSAVINGS){
			if (targetSavings > MAXSAVINGS) {
				targetSavings -= BUFFER;
				if (targetNeeds + BUFFER <= MAXNEEDS) {
					targetNeeds += BUFFER;
				} else if (targetWants + BUFFER <= MAXWANTS) {
					targetWants += BUFFER;
				}
			} else if (targetSavings < MINSAVINGS) {
				targetSavings += BUFFER;
				if (targetNeeds - BUFFER >= MINNEEDS) {
					targetNeeds -= BUFFER;
				} else if (targetWants - BUFFER >= MINWANTS) {
					targetWants -= BUFFER;
				}
			}
			else break;
		}
	}

	/**
	 * Sets needs target using remaining percentages
	 */
	private void allocateRemainingToNeeds(){
		targetNeeds = 1 - targetSavings - targetWants;
		while(targetNeeds>MAXNEEDS||targetNeeds<MINNEEDS){
			if(targetNeeds>MAXNEEDS){
				targetNeeds-= BUFFER;
				if(targetSavings+BUFFER<=MAXSAVINGS){
					targetSavings+=BUFFER;
				}
				else if(targetWants+BUFFER<=MAXWANTS){
					targetWants+=BUFFER;
				}
			}
			else if(targetNeeds<MINNEEDS){
				targetNeeds += BUFFER;
				if(targetSavings-BUFFER>=MINSAVINGS){
					targetSavings-=BUFFER;
				}
				else if(targetWants-BUFFER>=MINWANTS){
					targetWants-=BUFFER;
				}
			}
			else break;
		}
	}

	/**
	 * Returns true if past month's records has been modified
	 * @return
	 */
	private boolean hasModifiedPastMonthRecords(){
		if(pastMonthNWS.getIncome() != data.getPrevMonthlyIncome()){
			return true;
		}
		if(pastMonthNWS.getCurrentNeeds() != data.getPrevMonthlyExpense(ExpenseType.NEED)){	
			return true;
		}
		else if(pastMonthNWS.getCurrentWants() != data.getPrevMonthlyExpense(ExpenseType.WANT)){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Updates the values of date, currentNeeds, currentWants and currentSavings of thisMonthNWS. Other values are unchanged
	 */

	private void updateThisMonthProgress(){
		thisMonthNWS.setAll(new GregorianCalendar(), 
				thisMonthNWS.getTargetNeedsRatio(), 
				thisMonthNWS.getTargetWantsRatio(), 
				thisMonthNWS.getTargetSavingsRatio(),
				data.getMonthlyExpense(ExpenseType.NEED),
				data.getMonthlyExpense(ExpenseType.WANT),
				getMonthlySavings(), data.getMonthlyIncome());
	}


	/**
	 * Updates the values of currentNeeds, currentWants and currentSavings of pastMonthNWS. Other values are unchanged
	 */

	private void updatePastMonthProgress(){
		pastMonthNWS.setAll(pastMonthNWS.getDate(), 
				pastMonthNWS.getTargetNeedsRatio(), 
				pastMonthNWS.getTargetWantsRatio(), 
				pastMonthNWS.getTargetWantsRatio(), 
				data.getPrevMonthlyExpense(ExpenseType.NEED), 
				data.getPrevMonthlyExpense(ExpenseType.WANT), 
				getPrevMonthlySavings(), 
				data.getPrevMonthlyIncome());
	}

}

