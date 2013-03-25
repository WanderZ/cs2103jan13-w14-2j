package ezxpns.data;

import java.util.Calendar;

public class NWSdata {
Calendar date;
private double targetNeeds;
private double targetWants;
private double targetSavings;
private boolean isSet;

public NWSdata(Calendar date, double n, double w, double s){
	this.date = date;
	targetNeeds = n;
	targetWants = w;
	targetSavings = s;
	isSet = true;
}

public NWSdata(){
date = null;
targetNeeds = 0;
targetWants = 0;
targetSavings = 0;
	isSet = false;
}

public void setAll(Calendar date, double n, double w, double s){
	this.date = date;
	targetNeeds = n;
	targetWants = w;
	targetSavings = s;
	isSet = true;
}

public double getNeeds(){
	return targetNeeds;
}

public double getWants(){
	return targetWants;
}

public double getSavings(){
	return targetSavings;
}

public Calendar getDate(){
	return date;
}

public boolean isSet(){
	return isSet;
}
}
