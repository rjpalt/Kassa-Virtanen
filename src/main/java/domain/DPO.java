//Datapoint object creator

package domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import logic.ReskontraDAO;
import domain.DataPoint;

public class DPO {

	private ReskontraDAO dao;
	
	public DPO(ReskontraDAO dao) {
		
		this.dao = dao;
		
	}
	
	public ArrayList<String> createDateList(int numberOfDates) {
		
		ArrayList<String> dateList = new ArrayList<String>();
		
		String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());		
		
		String[] pieces = date.split("-");
		
		int year = Integer.valueOf(pieces[2]); //2020
		int month = Integer.valueOf(pieces[1]); //12
		int day = Integer.valueOf(pieces[0]); //17
		
		for (int i = 0; i < numberOfDates; i++) {
			
			day++;
			
			if ((month == 1 || month == 3 || month == 5 || month == 7 || month ==  8 || month == 10 || month == 12) && day == 32) {
				
				month++;
				day = 1;
				
				if (month == 13) {
					
					year++;
					month = 1;
					
				}
				
			} else if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
				
				month++;
				day = 1;
				
			} else if (!isItLeapYear(year) && month == 2 && day == 29) {
				
				month++;
				day = 1;
				
			} else if (isItLeapYear(year) && month == 2 && day == 30){
				
				month++;
				day = 1;
				
			}
			
			String newMonth = Integer.toString(month);
			String newDay = Integer.toString(day);
			
			if (newMonth.length() == 1) {
				newMonth = "0" + newMonth;
			}
			
			if (newDay.length() == 1) {
				newDay = "0" + newDay;
			}
			
			String newDate = year + "-" + newMonth + "-" + newDay;
			
			dateList.add(newDate);
			
		}
						
		return dateList;
		
	}

	public ArrayList<Double> createBalanceByDateList(int numberOfDates) {
		
		ArrayList<Double> balancePoints = new ArrayList<Double>();
		
		ArrayList<String> dateList = createDateList(numberOfDates);
		
		
		for (int i = 0; i < dateList.size(); i++) {
			
			String date = dateList.get(i);
			
			balancePoints.add(this.dao.getDateBalance(date));
			
		}
		
		return balancePoints;
		
	}
	
	public ArrayList<DataPoint> createDataPointList (int numberOfPoints) {
		
		ArrayList<DataPoint> dpList = new ArrayList<DataPoint>();
		
		ArrayList <String> dateList = createDateList(numberOfPoints);
		
		ArrayList<Double> balancePointList = createBalanceByDateList(numberOfPoints);
		
		for (int i = 0; i < dateList.size(); i++) {
			
			dpList.add(new DataPoint(dateList.get(i), balancePointList.get(i)));
			
		}
		
		dpList.forEach((dp) -> {
			System.out.println(dp.toString());
		});
		
		return dpList;
		
	}
	
	public ArrayList<DataPoint> createBalanceFlow(double balance, int flowDuration) {
		
		//Final list for returning (contains the balance flow as represented in datapoints)
		ArrayList<DataPoint> balanceFlow = new ArrayList<DataPoint>();
		
		ArrayList<String> dateList = createDateList(flowDuration);
		ArrayList<Double> balancePointList = createBalanceByDateList(flowDuration);
		
		
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		double initialBalance = balance;
		
		balanceFlow.add(new DataPoint(date, initialBalance));
		
		for (int i = 0; i < dateList.size(); i++) {
			
			double newBalance = balanceFlow.get(i).getFlowBalance() + balancePointList.get(i);
			
	        double roundBalance = Math.round(newBalance * 100.0) / 100.0;
			
			balanceFlow.add(new DataPoint(dateList.get(i), roundBalance));
			
		}
		
		return balanceFlow;
		
	}
	
	public boolean isItLeapYear(int year) {
		
		boolean leapYear = false;
		
		
		if (year % 100 == 0) {
			
			if (year % 400 == 0) {
				
				leapYear = true;
				
			} else {
			    
			    leapYear = false;
			    
			}
			
		} else if (year % 4 == 0) {
		    
		    leapYear = true;
		    
		}
		
		return leapYear;
		
	}
	
}
