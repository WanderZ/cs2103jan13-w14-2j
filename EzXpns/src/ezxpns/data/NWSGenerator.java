package ezxpns.data;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ezxpns.data.TargetManager.DataProvider;
import ezxpns.data.records.ExpenseType;

/**
 * This class generates target ratios for needs,wants and savings 
 * @author Suzzie
 *
 */


public class NWSGenerator extends Storable{

	public static interface DataProvider{
		double getMonthlyExpense(ExpenseType type);
		double getMonthlyIncome();

		double getPrevMonthlyExpense(ExpenseType type); //get the previous month's data
		double getPrevMonthlyIncome(); 					// get previous month's income
	}

	private transient DataProvider data;
	private transient boolean dataUpdated = true;

	//Ideal or default ratios
	private final double NEEDS = 0.5;
	private final double WANTS = 0.3;
	private final double SAVINGS = 0.2;

	private final double BUFFER = 0.05; //+- 5% buffer

	// 0.1<Needs<0.8
	private final double MAXNEEDS = 0.7;
	private final double MINNEEDS = 0.1;
	//0.1<Wants<0.5
	private final double MAXWANTS = 0.5;
	private final double MINWANTS = 0.1;
	//0.2<Savings<0.7
	private final double MAXSAVINGS = 0.7;
	private final double MINSAVINGS = 0.2;



	private NWSdata pastMonthNWS = new NWSdata(); 
	private NWSdata thisMonthNWS = new NWSdata();


	public NWSGenerator(DataProvider data){
		this.data = data;
		dataUpdated = true;


		//if there is no past month data or this month data, set the ratio as default.
		if((!thisMonthNWS.isSet())&&(!pastMonthNWS.isSet())){
			thisMonthNWS.setAll(new GregorianCalendar(), NEEDS, WANTS, SAVINGS,
					data.getMonthlyExpense(ExpenseType.NEED),
					data.getMonthlyExpense(ExpenseType.WANT),
					getMonthlySavings(),
					data.getMonthlyIncome());
		}


		if(thisMonthNWS.isSet()){
			if(isNewMonth(thisMonthNWS.getDate())){
				pastMonthNWS.setAll(
						thisMonthNWS.getDate(),
						thisMonthNWS.getTargetNeeds(),
						thisMonthNWS.getTargetWants(),
						thisMonthNWS.getTargetSavings(), 
						thisMonthNWS.getCurrentNeeds(),
						thisMonthNWS.getCurrentWants(),
						thisMonthNWS.getCurrentSavings(),
						thisMonthNWS.getIncome());
				generateRatios(); //this updates thisMonthNWS
			}
		}
	}


	public void setDataProvider(DataProvider data){
		this.data = data;
		dataUpdated = true;
	}

	public void markDataUpdated(){
		dataUpdated = true;
	}

	@Override
	public void afterDeserialize(){

	}
	
	public void updateNWSdata(){
		if(pastMonthNWS.isSet()){
		pastMonthNWS.setCurrentNeeds(data.getPrevMonthlyExpense(ExpenseType.NEED));
		pastMonthNWS.setCurrentWants(data.getPrevMonthlyExpense(ExpenseType.WANT));
		pastMonthNWS.setCurrentSavings(getPrevMonthlySavings());
		pastMonthNWS.setIncome(data.getPrevMonthlyIncome());
		}
		generateRatios();
	}




	/*
	 * Getter Methods for GUI 
	 */


	/**
	 * Returns actual amount of not spent this month i.e. the savings
	 * @return 
	 */
	private double getMonthlySavings(){
		double savings = data.getMonthlyIncome()-data.getMonthlyExpense(ExpenseType.NEED)-data.getMonthlyExpense(ExpenseType.WANT);
		if(savings>0) //makes sure there is no negative savings 
			return savings;
		else 
			return 0;
	}
	
	private double getPrevMonthlySavings(){
		double savings = data.getPrevMonthlyIncome()-data.getPrevMonthlyExpense(ExpenseType.NEED)-data.getPrevMonthlyExpense(ExpenseType.WANT);
		if(savings>0) //makes sure there is no negative savings 
			return savings;
		else 
			return 0;
		
	}

	/**
	 * Returns a copy of NWSdata for this month
	 * @return
	 */
	public NWSdata getNWSdataCopy(){
		if(dataUpdated){
			generateRatios();
			dataUpdated = false; // i just add this cos it is similar to targetManager. 
			//the point is I need to call generateRatio when 
			//1. someone changes last month's records. Recalculate ratio for this month since it is based on last month's data
			//2. Transit to new month so i need the calculate the new ratios for this month
		}

		return thisMonthNWS.copy();
	}

	/**
	 * Sets the ratio for NEEDS, WANTS and SAVINGS
	 * @return
	 */
	public void generateRatios(){
		
		if(!pastMonthNWS.isSet()){
			thisMonthNWS.setAll(new GregorianCalendar(), NEEDS, WANTS, SAVINGS,
					data.getMonthlyExpense(ExpenseType.NEED),
					data.getMonthlyExpense(ExpenseType.WANT),
					getMonthlySavings(),
					data.getMonthlyIncome());
			markUpdate();
			return;
		}


		double targetNeeds =  pastMonthNWS.getTargetNeeds();
		double targetWants = pastMonthNWS.getTargetWants();
		double targetSavings = pastMonthNWS.getTargetSavings();

		double prevMonthNeedsExp = pastMonthNWS.getNeedsProgress(); //ratio
		double prevMonthWantsExp = pastMonthNWS.getWantsProgress(); //ratio
		double prevMonthSavings = pastMonthNWS.getSavingsProgress(); //ratio


		//difference from  ____ Targets = Target ratio - Actual ratio
		// diff>0 => did not meet target
		// diff<0 => exceeded target
		double diffFromNT = targetNeeds - prevMonthNeedsExp;
		double diffFromWT = targetWants - prevMonthWantsExp;
		double diffFromST = targetSavings - prevMonthSavings;	

		int i=0;

		/*
		 * if the difference from the target is more than 5%, 
		 * we will count it as missing the target
		 * Now we will compute i, which represent the state. 
		 * the value of i should be read at base 2
		 * eg. i = 6 (in base 10) = 110 (in base 2)
		 * Needs|Wants|Savings
		 *   1  |  1  |  0   
		 * this implies that Needs and Wants did not meet the 
		 * target, 
		 * while Savings has met the target (<5% diff)
		 */
		if(Math.abs(diffFromNT)>=BUFFER){
			i+=4;
		}

		if(Math.abs(diffFromWT)>=BUFFER){
			i+=2;
		}

		if(Math.abs(diffFromST)>=BUFFER){
			i+= 1;	
		}


		//if(excess_x<0) => exceed the target by |excess_x|
		//if(excess_x>0) => miss the target by |excess_x|

		switch(i){

		case 0: // 000 // VERY GOOD but will try and increase savings
			if(diffFromST<0){
				if(diffFromNT>0){
					if(canIncrease(MAXSAVINGS, targetSavings)
							&& canReduce(MINNEEDS, targetNeeds))
						targetNeeds-=BUFFER;
					targetSavings+=BUFFER;
				}

				if(diffFromWT>0){
					if(canIncrease(MAXSAVINGS, targetSavings)
							&& canReduce(MINWANTS, targetWants)){
						targetWants-=BUFFER;
						targetSavings+=BUFFER;
					}
				}
			}
			break;

		case 1://001	
			if(diffFromST>0){
				if(canReduce(MINSAVINGS, targetSavings)){
					if(diffFromNT<0&&canIncrease(MAXNEEDS, targetNeeds)){
						targetSavings -= BUFFER;
						targetNeeds += BUFFER;
					}

					else if(diffFromWT<0 && canIncrease(MAXWANTS, targetWants)){
						targetSavings -= BUFFER;
						targetWants += BUFFER;
					}

				}
			}

			else{ //target more than 0
				if(canIncrease(MAXSAVINGS, targetSavings)){
					if(diffFromNT>0&&canReduce(MINNEEDS, targetNeeds)){
						targetSavings += BUFFER;
						targetNeeds -= BUFFER;
					}
					else if(diffFromWT>0&&canReduce(MINWANTS, targetWants)){
						targetSavings += BUFFER;
						targetWants -= BUFFER;
					}
				}
			}


			break;
		case 2://010 
			if(diffFromWT>0){
				if(canReduce(MINWANTS, targetWants)){
					if(diffFromNT<0&&canIncrease(MAXNEEDS, targetNeeds)){
						targetWants -= BUFFER;
						targetNeeds += BUFFER;
					}

					else if(diffFromST<0 && canIncrease(MAXSAVINGS, targetSavings)){
						targetWants -= BUFFER;
						targetSavings += BUFFER;
					}
				}
			}

			else{
				if(canIncrease(MAXWANTS, targetWants)){
					if(diffFromNT>0&&canReduce(MINNEEDS, targetNeeds)){
						targetWants += BUFFER;
						targetNeeds -= BUFFER;
					}
					else if(diffFromST>0&&canReduce(MINSAVINGS, targetSavings)){
						targetWants += BUFFER;
						targetSavings-= BUFFER;
					}
				}
			}


			break;

		case 3://011 
			if(diffFromWT>0 && diffFromST<0){
				if(canIncrease(MAXSAVINGS,targetSavings)&&canReduce(MINWANTS, targetWants)){
					targetSavings+=BUFFER;
					targetWants-=BUFFER;
				}		
			}

			else if( diffFromWT<0 && diffFromST>0){
				if(canReduce(MINSAVINGS,targetSavings) && canIncrease(MAXWANTS, targetWants)){
					targetSavings -= BUFFER;
					targetWants += BUFFER;
				}
			}
			break;

		case 4://100 
			if(diffFromNT>0){
				if(canReduce(MINNEEDS, targetNeeds)){
					if(diffFromST<0 && canIncrease(MAXSAVINGS, targetSavings)){
						targetSavings += BUFFER;
						targetNeeds -= BUFFER;
					}

					else if(diffFromWT<0 && canIncrease(MAXWANTS, targetWants)){
						targetWants += BUFFER;
						targetNeeds -= BUFFER;
					}
				}
			}
			else{
				if(canIncrease(MAXNEEDS, targetNeeds)){
					if(diffFromST>0 && canReduce(MINSAVINGS, targetSavings)){
						targetSavings -= BUFFER;
						targetNeeds += BUFFER;
					}
					else if(diffFromWT>0 && canReduce(MINWANTS, targetWants)){
						targetWants -= BUFFER;
						targetNeeds += BUFFER;
					}
				}
			}
			break;

		case 5://101
			if(diffFromNT>0 && diffFromST<0){
				if(canReduce(MINNEEDS, targetNeeds) && canIncrease(MAXSAVINGS, targetSavings)){
					targetNeeds -= BUFFER;
					targetSavings += BUFFER;
				}
			}
			else if(diffFromNT<0 && diffFromST>0){
				if(canIncrease(MAXNEEDS, targetNeeds) && canReduce(MINSAVINGS, targetSavings)){
					targetNeeds += BUFFER;
					targetSavings -= BUFFER;
				}
			}
			break;

		case 6://110
			if(diffFromNT>0 && diffFromWT<0){
				if(canReduce(MINNEEDS, targetNeeds) && canIncrease(MAXWANTS, targetWants)){
					targetNeeds -= BUFFER;
					targetWants += BUFFER;
				}
			}
			else if(diffFromNT<0 && diffFromWT>0){
				if(canIncrease(MAXNEEDS, targetNeeds) && canReduce(MAXWANTS, targetWants)){
					targetNeeds += BUFFER;
					targetWants -= BUFFER;
				}
			}
			break;

		case 7: //111
			targetNeeds = NEEDS;
			targetWants = WANTS;
			targetSavings = SAVINGS;
			break;

		default:
			break;	
		}


		thisMonthNWS.setAll(new GregorianCalendar(), targetNeeds, targetWants, targetSavings,
				data.getMonthlyExpense(ExpenseType.NEED),
				data.getMonthlyExpense(ExpenseType.WANT),
				getMonthlySavings(), 
				data.getMonthlyIncome());																																					
		markUpdate();

	}


	/*
	 * 
	 * 
	 * Helper methods
	 * 
	 * 
	 * 
	 * */

	/**
	 * This returns true if the needs, wants or savings target can be increased by 5% without exceeding MAX
	 * @param MAX
	 * @param oldTarget
	 * @return 
	 */
	private boolean canIncrease(double MAX, double oldTarget){
		if((oldTarget+0.05)<=MAX){
			return true;
		}
		return false;
	}

	/**
	 * This returns true if the needs, wants or savings target can be reduced by 5% without going below MIN
	 * @param MIN
	 * @param oldTarget
	 * @return
	 */
	private boolean canReduce(double MIN, double oldTarget){
		if((oldTarget-0.05)>=MIN){
			return true;
		}
		return false;
	}

	/**
	 * This returns true if it is a new month. 
	 * @param date
	 * @return true if date's Month and Year is different from the date in thisMonthNWS
	 */
	private boolean isNewMonth(Calendar date){
		Calendar today = new GregorianCalendar();
		if(date.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
			if(date.get(Calendar.YEAR)==today.get(Calendar.YEAR)){
				return false;
			}
		}
		return true;
	}
	
	

}