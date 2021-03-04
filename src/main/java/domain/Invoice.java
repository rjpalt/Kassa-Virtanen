package domain;

public class Invoice implements Comparable<Invoice>{
	
	private int invoiceID;
	private String invoiceClass;
	private int invoiceType;
	private String clientName;
	private String dueDate;
	private double sum;
	private String otherInfo;
	
	public Invoice (int id, String client, String invClass, int type, String date, double sum, String other) {
		
		this.invoiceID = id;
		this.clientName = client;
		this.invoiceClass = invClass;
		this.invoiceType = type;
		this.dueDate = date;
		this.sum = sum;
		this.otherInfo = other;
		
	}
	
	public Invoice (int id, String client, String invClass, int type, String date, double sum) {

		this.invoiceID = id;
		this.clientName = client;
		this.invoiceClass = invClass;
		this.invoiceType = type;
		this.dueDate = date;
		this.sum = sum;
		this.otherInfo = "";
		
	}
	
	

	public int getInvoiceID() {
		return invoiceID;
	}

	public void setInvoiceID(int invoiceID) {
		this.invoiceID = invoiceID;
	}

	public int getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}	

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}	
	

	public String getInvoiceClass() {
		return invoiceClass;
	}

	public void setInvoiceClass(String invoiceClass) {
		this.invoiceClass = invoiceClass;
	}
	
	public int getYear() {
		
		String date[] = dueDate.split("-");
		
		int invYear = Integer.valueOf(date[0]);
		
		return invYear;
		
	}
	
	public int getMonth() {
		
		String date[] = dueDate.split("-");
		
		int invMonth = Integer.valueOf(date[1]);
		
		return invMonth;
		
	}
	
	public int getDate() {
		
		String date[] = dueDate.split("-");
		
		int invDay = Integer.valueOf(date[2]);
		
		return invDay;
		
	}

	@Override
	public String toString() {
		return "Laskun numero: " + this.invoiceID + ", tyyppi: " + this.invoiceClass + ", asiakas: " + this.clientName + ", eräpäivä: " + this.dueDate
				+ ", summa " + this.sum;
	}
	
    public int compareTo(Invoice invoice) {
    	   	
    	
        if (this.getYear() == invoice.getYear() && this.getMonth() == invoice.getMonth() && this.getDate() == invoice.getDate()) {
            return 0;
        } else if (this.getYear() > invoice.getYear()) {
            return 1;
        } else if (this.getYear() == invoice.getYear() && this.getMonth() > invoice.getMonth()) {
        	return 1;
        } else if (this.getYear() == invoice.getYear() && this.getMonth() == invoice.getMonth() && this.getDate() > invoice.getDate()) {
        	return 1;
        } else {
        	return -1;
        }
    }
	
}
