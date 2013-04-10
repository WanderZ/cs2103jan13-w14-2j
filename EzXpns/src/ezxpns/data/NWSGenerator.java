package ezxpns.data;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


import ezxpns.data.TargetManager.DataProvider;
import ezxpns.data.records.ExpenseType;

/**
 * This class generates target ratios for needs,wants and savings
 * 
 * @author Suzzie
 * 
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
		System.out.println("in constructor");
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

	@Override
	public void afterDeserialize() {
		System.out.println("in deserialise");
		//checks for first time usage: No data in json
		//thisMonthNWS and pastMonth is not set
		if ((!thisMonthNWS.isSet()) && (!pastMonthNWS.isSet())) {
			System.out.println("this and past NOT SET");
			initNWSdata();
		}

		//checks for new month then check for skip a month
		if(isExpired(thisMonthNWS)){
			if(hasSkippedAMonth(thisMonthNWS.getDate())){ 
				initNWSdata();
			}

			else{// normal expiry
				setToPastMonth(thisMonthNWS);
				generateNWS();
			}

		}//end expired
		
		else{//not expired! 
			updateThisMonthProgress();
		}
		markUpdate();
	}

	public void updateNWSdata() {
		System.out.println("hello. there is an update");
		if(pastMonthNWS.isSet()&&hasModifiedPastMonthRecords()){
			updatePastMonthProgress();
			generateNWS();
		}
		
		else{
			updateThisMonthProgress();
		}
		
		markUpdate();
	}
	

	/**
	 * Returns actual amount of not spent this month i.e. the savings
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

	//for test
	public NWSdata getPastNWSdataCopy(){
		if(pastMonthNWS.isSet()){
			return pastMonthNWS.copy();
		}
		else {
			System.out.println("past month not set @getPNWSCopy()");		
			return null;
		}
	}

	private void initNWSdata(){
		System.out.println("init");
		thisMonthNWS.setAll(new GregorianCalendar(), NEEDS, WANTS, SAVINGS,
				data.getMonthlyExpense(ExpenseType.NEED),
				data.getMonthlyExpense(ExpenseType.WANT),
				getMonthlySavings(), data.getMonthlyIncome());
		pastMonthNWS = new NWSdata();
	}

	/**
	 * Sets the ratio for NEEDS, WANTS and SAVINGS
	 * 
	 * @return
	 */
	public void generateNWS() {
		System.out.println("genRatio!");
		//check for valid pastMonthNWS
		if(!pastMonthNWS.isSet()){
			updateThisMonthProgress();
			System.out.println("past month not set, return");
			markUpdate();
			return;
		}
		
		//check for zero income
		if(pastMonthNWS.getIncome()==0){
			updateThisMonthProgress();
			System.out.println("income=0, return");
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
		// diff>0 => did not meet target
		// diff<0 => exceeded target
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

		// if(excess_x<0) => exceed the target by |excess_x|
		// if(excess_x>0) => miss the target by |excess_x|

		switch (i) {

		case 0: // 000 // VERY GOOD but will try and increase savings
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
			System.out.println("state001");
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
			System.out.println("state010");
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
			System.out.println("state011");
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
			System.out.println("state100");
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
			System.out.println("state101");
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
			System.out.println("state110");
			if (!hasExceededTarget(diffFromNT) && hasExceededTarget(diffFromWT)) {
				if (canReduce(MINNEEDS, targetNeeds)
						&& canIncrease(MAXWANTS, targetWants)) {
					targetNeeds -= BUFFER;
					targetWants += BUFFER;
				}
			} else if (hasExceededTarget(diffFromNT) && !hasExceededTarget(diffFromWT)) {
				System.out.println("need exceed, wants under");
				if (canIncrease(MAXNEEDS, targetNeeds)
						&& canReduce(MINWANTS, targetWants)) {
					System.out.println("can increN"+targetNeeds+" decreW"+targetWants);
					targetNeeds += BUFFER;
					targetWants -= BUFFER;
					System.out.println("N"+targetNeeds+"W"+targetWants);
				}
			}
			else{
				System.out.println("wth");
			}
			break;

		case 7: // 111
			System.out.println("state111");
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
				System.out.println("min = Need");
				modifyWants();
				modifySavings();
				allocateRemainingToNeeds();
			}
			else if(min == ExpenseType.WANT){
				System.out.println("min = want");
				modifyNeeds();
				modifySavings();
				allocateRemainingToWants();
			}
			else{
				System.out.println("min = savings");
				modifyNeeds();
				modifyWants();
				allocateRemainingToSavings();
			}
			break;

		default:
			break;
		}

		System.out.println("NWS"+targetNeeds+" "+targetWants+" "+targetSavings);
		thisMonthNWS.setAll(new GregorianCalendar(), targetNeeds, targetWants,
				targetSavings, data.getMonthlyExpense(ExpenseType.NEED),
				data.getMonthlyExpense(ExpenseType.WANT), getMonthlySavings(),
				data.getMonthlyIncome());
		markUpdate();

	}

	/*
	 * 
	 * 
	 * Helper methods
	 */

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
		System.out.println(today.getTime() == (new Date()));
		if (date.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
			if (date.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
				System.out.println("notExpired");
				return false;
			}
		}
		System.out.println("isExpired");
		return true;
	}

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

	private boolean hasExceededTarget(double diff) {
		if (diff < 0)
			return true;
		else
			return false;
	}

	public void printNWS(NWSdata paper){
		System.out.println("Income:$"+paper.getIncome());
		System.out.println("Needs:$"+paper.getTargetNeedsRatio()+"/"+paper.getCurrentNeeds());
		System.out.println("Wants:$"+paper.getTargetWantsRatio()+"/"+paper.getCurrentWants());
		System.out.println("Savings:$"+paper.getTargetSavingsRatio()+"/"+paper.getCurrentSavings());
	}

	private void setToPastMonth(NWSdata thisMonthNWS){
		System.out.println("set to past month");
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

	private double modifySavings(){
		int count = 0;
		while (Math.abs(diffFromST) >= 2 * BUFFER) {
			if (hasExceededTarget(diffFromST)) {
				if (canIncrease(MAXSAVINGS, targetSavings)) {
					targetSavings += BUFFER;
					count -= BUFFER;
					diffFromST = targetSavings - prevMonthSavings;
				} else {
					break;
				}
			} else { // miss target
				if (canReduce(MINSAVINGS, targetSavings)) {
					targetSavings -= BUFFER;
					count += BUFFER;
					diffFromNT = targetSavings - prevMonthSavings;
				} else {
					break;
				}
			}
		}
		return count;
	}

	private double modifyNeeds(){
		int count = 0; 
		while (Math.abs(diffFromNT) >= 2 * BUFFER) {
			if (hasExceededTarget(diffFromNT)) {
				if (canIncrease(MAXNEEDS, targetNeeds)) {
					targetNeeds += BUFFER;
					count  -=BUFFER;
					diffFromNT = targetNeeds - prevMonthNeedsExp;
				} 
				else {
					break;
				}
			} else { // miss target
				if (canReduce(MINNEEDS, targetNeeds)) {
					targetNeeds -= BUFFER;
					count += BUFFER;
					diffFromNT = targetNeeds - prevMonthNeedsExp;
				} else {
					break;
				}
			}
		}
		return count;
	}

	private double modifyWants(){
		int count = 0; 
		while (Math.abs(diffFromWT) >= 2 * BUFFER) {
			if (hasExceededTarget(diffFromWT)) {
				if (canIncrease(MAXWANTS, targetWants)) {
					targetWants += BUFFER;
					count  -=BUFFER;
					diffFromWT = targetWants - prevMonthWantsExp;
				} 
				else {
					break;
				}
			} else { // miss target
				if (canReduce(MINWANTS, targetWants)) {
					targetWants -= BUFFER;
					count += BUFFER;
					diffFromWT = targetWants - prevMonthWantsExp;
				} else {
					break;
				}
			}
		}
		return count;

	}

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

	private boolean hasModifiedPastMonthRecords(){
		if(pastMonthNWS.getIncome() != data.getPrevMonthlyIncome()){
			System.out.println("income is DIFFERENT"+pastMonthNWS.getIncome()+"=="+data.getPrevMonthlyIncome());
			return true;
		}
		else{
			System.out.println("income is the same"+pastMonthNWS.getIncome()+"=="+data.getPrevMonthlyIncome());
		}

		if(pastMonthNWS.getCurrentNeeds() != data.getPrevMonthlyExpense(ExpenseType.NEED)){	
			System.out.println("needs is Different"
		+"pnws"+pastMonthNWS.getCurrentNeeds()
		+"!="+data.getPrevMonthlyExpense(ExpenseType.NEED));
			return true;
		}
		else if(pastMonthNWS.getCurrentWants() != data.getPrevMonthlyExpense(ExpenseType.WANT)){
			System.out.println("wants is Different"
		+"pnws"+pastMonthNWS.getCurrentWants()
		+"!="+data.getPrevMonthlyExpense(ExpenseType.WANT));
			return true;
		}
		else{
			return false;
		}
	}

	private void updateThisMonthProgress(){
		thisMonthNWS.setAll(new GregorianCalendar(), 
				thisMonthNWS.getTargetNeedsRatio(), 
				thisMonthNWS.getTargetWantsRatio(), 
				thisMonthNWS.getTargetSavingsRatio(),
				data.getMonthlyExpense(ExpenseType.NEED),
				data.getMonthlyExpense(ExpenseType.WANT),
				getMonthlySavings(), data.getMonthlyIncome());
	}
	
	private void updatePastMonthProgress(){
		System.out.println("past Month MOD, cont'd");
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

