//Input Error Checking

package logic;

public class IEC {
	
	public IEC() {
		
	}
	
	public boolean checkDate (String date) {
		
		if (!date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|(1|2)[0-9]|3[0-1])")) {
			return false;
		} else {
			return true;
		}
		
	}
	
	public boolean checkSum(String sum) {
		
		if (!sum.matches("^\\d+\\.\\d{2}$")) {
			return false;
		} else {
			return true;
		}
		
	}
	
	public boolean checkDuration(String duration) {
		
		if (!duration.matches("^\\d{1,3}$")) {
			return false;
		} else {
			return true;
		}
		
	}

}
