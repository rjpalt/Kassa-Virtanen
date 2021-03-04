package domain;

public class InvoiceClass {
	
	private int invoiceType;
	private String invoiceClass;
	
	public InvoiceClass(int type, String invoiceClass) {
		
		this.invoiceType = type;
		this.invoiceClass = invoiceClass;
		
	}

	public int getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceClass() {
		return invoiceClass;
	}

	public void setInvoiceClass(String invoiceClass) {
		this.invoiceClass = invoiceClass;
	}

	@Override
	public String toString() {
		return invoiceClass;
	}

}
