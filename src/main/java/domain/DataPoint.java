package domain;

public class DataPoint {

	private String date;
	private double flowBalance;
	
	public DataPoint(String date, double balance) {
		
		this.date = date;
		this.flowBalance = balance;
		
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getFlowBalance() {
		return flowBalance;
	}

	public void setFlowBalance(double flowBalance) {
		this.flowBalance = flowBalance;
	}

	@Override
	public String toString() {
		return "Päivämäärä: " + this.date + "." + " Päivän balanssi: " + this.flowBalance;
	}
	
	
	
}
