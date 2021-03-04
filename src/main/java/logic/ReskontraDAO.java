package logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import domain.Invoice;
import domain.Client;

public class ReskontraDAO {

	private String dbPath;
	
	public ReskontraDAO(String dbPath) {
		
		this.dbPath = dbPath;
		
	}

	//Fixed
	public ArrayList<Invoice> getAllInvoices() {
		
		ArrayList<Invoice> allInvoices = new ArrayList<Invoice>();
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt = 
					conn.prepareStatement("SELECT a.id AS invID, b.name, c.invoice_class, d.id AS typeID, a.date, a.amount FROM invoices AS a, clients AS b, invoice_classes AS c, invoice_types AS d WHERE a.client_id = b.id AND a.class = c.id AND c.invoice_type = d.id;");
			
			ResultSet results = stmt.executeQuery();			
			while (results.next()) {				
				allInvoices.add(new Invoice(results.getInt("invID"), results.getString("name"), results.getString("invoice_class"), results.getInt("typeID"), results.getString("date"), results.getDouble("amount")));			
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return allInvoices;
		
	}
	
	//Fixed
	public ArrayList<Invoice> getAllSales() {
		
		ArrayList<Invoice> salesInvoices = new ArrayList<Invoice>();
		
		try (Connection conn = connectAndSecureDB()) {
			PreparedStatement stmt =
					conn.prepareStatement("SELECT a.id AS invID, b.name, c.invoice_class, d.id AS typeID, a.date, a.amount FROM invoices AS a, clients AS b, invoice_classes AS c, invoice_types AS d WHERE a.client_id = b.id AND a.class = c.id AND c.invoice_type = d.id AND d.id = 2;");
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				salesInvoices.add(new Invoice(results.getInt("invID"), results.getString("name"), results.getString("invoice_class"), results.getInt("typeID"), results.getString("date"), results.getDouble("amount")));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return salesInvoices;
		
	}
	
	//Fixed
	public ArrayList<Invoice> getAllPurchases() {
		
		ArrayList<Invoice> purchasesInvoices = new ArrayList<Invoice>();
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
				conn.prepareStatement("SELECT a.id AS invID, b.name, c.invoice_class, d.id AS typeID, a.date, a.amount FROM invoices AS a, clients AS b, invoice_classes AS c, invoice_types AS d WHERE a.client_id = b.id AND a.class = c.id AND c.invoice_type = d.id AND d.id = 1;");
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				purchasesInvoices.add(new Invoice(results.getInt("invID"), results.getString("name"), results.getString("invoice_class"), results.getInt("typeID"), results.getString("date"), results.getDouble("amount")));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return purchasesInvoices;
		
	}
	
	//OK/Fixed
	public HashMap<Integer, String> getClients() {
		
		HashMap<Integer, String> asiakkaat = new HashMap<Integer, String>();
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
				conn.prepareStatement("SELECT * FROM clients;");
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				asiakkaat.put(results.getInt("id"), results.getString("name"));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return asiakkaat;
		
	}
	
	
	//Fixed
	public void createInvoice(int invClass, int client_id, double sum, String date) {
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
				conn.prepareStatement("INSERT INTO invoices (class, client_id, amount, date) VALUES (?, ?, ?, ?);");
			stmt.setInt(1, invClass);
			stmt.setInt(2, client_id);
			stmt.setDouble(3, sum);
			stmt.setString(4, date);
			stmt.execute();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "!");
		}
		
	}
	
	//OK / Fixed
	public void removeInvoice(int invoiceID) {
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
				conn.prepareStatement("DELETE FROM invoices WHERE id = ?;");
			stmt.setInt(1, invoiceID);
			stmt.execute();			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	//OK/Fixed
	public void updateInvoice (int invoiceID, int invoiceClass, int clientID, double invoiceSum, String dueDate) {
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt = 
				conn.prepareStatement("UPDATE invoices SET class=?, client_id=?, amount=?, date=? WHERE id=?;");
			stmt.setInt(1, invoiceClass);
			stmt.setInt(2, clientID);
			stmt.setDouble(3, invoiceSum);
			stmt.setString(4, dueDate);
			stmt.setInt(5, invoiceID);
			stmt.execute();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	//Fixed
	public Invoice getInvoice (int invoiceID) {
		
		String clientName = "foo";
		String invoiceClass = "bar";
		String dueDate = "bash";
		int typeID = 0;
		double invoiceSum = 0.0;
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
				conn.prepareStatement("SELECT b.name, c.invoice_class, d.id AS typeID, a.date, a.amount FROM invoices AS a, clients AS b, invoice_classes AS c, invoice_types AS d WHERE a.client_id = b.id AND a.class = c.id AND c.invoice_type = d.id AND a.id = ?;");
			stmt.setInt(1, invoiceID);
			
			ResultSet results = stmt.executeQuery();
			
			clientName = results.getString("name");
			invoiceClass = results.getString("invoice_class");
			dueDate = results.getString("date");
			invoiceSum = results.getDouble("amount");
			typeID = results.getInt("typeID");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		Invoice invoice = new Invoice(invoiceID, clientName, invoiceClass, typeID, dueDate, invoiceSum);
		
		return invoice;
		
	}
	
	//OK
	public ArrayList<Integer> getInvoiceTypes() {
		
		ArrayList<Integer> invoiceTypes = new ArrayList<Integer>();
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
				conn.prepareStatement("SELECT id FROM invoice_types;");
			
			ResultSet results = stmt.executeQuery();
			
			while(results.next()) {
				invoiceTypes.add(results.getInt("id"));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return invoiceTypes;
		
	}
	
	//OK
	public ArrayList<Integer> getClientIDs() {
		
		ArrayList<Integer> clientIDs = new ArrayList<Integer>();
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
				conn.prepareStatement("SELECT id FROM clients;");
			
			ResultSet results = stmt.executeQuery();
			
			while (results.next()) {
				clientIDs.add(results.getInt("id"));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return clientIDs;
		
	}
	
	//OK
	public ArrayList<Client> getAllClientInfo() {
		
		ArrayList<Client> allClients = new ArrayList<Client>();
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt = 
				conn.prepareStatement("SELECT * FROM clients;");
			
			ResultSet results = stmt.executeQuery();
			
			while (results.next()) {
				allClients.add(new Client(results.getInt("id"), results.getString("name")));
			}
			
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return allClients;
		
	}
	
	//OK
	public void createClient(String newClient) {
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt = 
				conn.prepareStatement("INSERT INTO clients (name) VALUES (?);");
			
			stmt.setString(1, newClient);
			
			stmt.execute();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	//OK
	public void deleteClient(int clientID) {
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt = 
				conn.prepareStatement("DELETE FROM clients WHERE id=?;");
			
			stmt.setInt(1, clientID);
			
			stmt.execute();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	//OK
	public void updateClientInfo(int clientID, String newClientName) {
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
				conn.prepareStatement("UPDATE clients SET name=? WHERE id=?;");
				
			stmt.setString(1, newClientName);
			stmt.setInt(2, clientID);
			
			stmt.execute();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public Client getClientByName(String clientName) {
		
		Client newClient = new Client(0, "foo");
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
					conn.prepareStatement("SELECT id, name FROM clients WHERE name=?;");
					
			stmt.setString(1, clientName);
			
			ResultSet results = stmt.executeQuery();
			
			newClient.setClientID(results.getInt("id"));
			newClient.setClientName(results.getString("name"));
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return newClient;
		
	}
	
	public Client getClientByID(int id) {
		
		Client queriedClient = new Client(id, "");
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
					conn.prepareStatement("SELECT id, name FROM clients WHERE id=?");
			
			stmt.setInt(1, id);
			
			ResultSet results = stmt.executeQuery();
			
			queriedClient.setClientName(results.getString("name"));
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return queriedClient;
		
	}
	
	public ArrayList<Invoice> getAllInvoicesOnDate(String dueDate) {
		
		ArrayList<Invoice> invoices = new ArrayList<Invoice>();
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt = 
				conn.prepareStatement("SELECT a.id AS invID, b.name, c.invoice_class, d.id AS typeID, a.date, a.amount FROM invoices AS a, clients AS b, invoice_classes AS c, invoice_types AS d WHERE a.client_id = b.id AND a.class = c.id AND c.invoice_type = d.id AND a.date=?;");
			
			stmt.setString(1, dueDate);
			
			ResultSet results = stmt.executeQuery();
			
			
			while (results.next()) {
				invoices.add(new Invoice(results.getInt("invID"), results.getString("name"), results.getString("invoice_class"), results.getInt("typeID"), results.getString("date"), results.getDouble("amount")));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return invoices;
		
	}
	
	public double getDateBalance(String date) {
		
		double dateBalance = 0.0;
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
				conn.prepareStatement("WITH ostot AS (SELECT IFNULL((SUM(amount)), 0) AS summa FROM invoices AS a, invoice_classes AS b WHERE date=? AND a.class = b.id AND b.invoice_type=1), myynnit AS (SELECT IFNULL((SUM(amount)), 0) AS summa FROM invoices AS a, invoice_classes AS b WHERE date=? AND a.class = b.id AND b.invoice_type=2) SELECT myynnit.summa - ostot.summa AS summa FROM ostot, myynnit;");
			
			stmt.setString(1, date);
			stmt.setString(2, date);
			
			ResultSet results = stmt.executeQuery();
			
			dateBalance = results.getDouble("summa");
			DecimalFormat df = new DecimalFormat("#.##");
			dateBalance = Double.valueOf(df.format(dateBalance));
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return dateBalance;
		
	}
	
	public boolean exists(int invoiceID) {
		
		boolean isOrNot = false;
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
				conn.prepareStatement("SELECT COUNT(*) AS is_or_not FROM invoices WHERE id = ?;");
			stmt.setInt(1, invoiceID);
					
			ResultSet results = stmt.executeQuery();
			
			int amount = results.getInt("is_or_not");

			if (amount == 1) {
				isOrNot = true;
			} else {
				isOrNot = false;
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return isOrNot;
		
	}
	
	public ArrayList<String> getInvoiceClasses() {

		ArrayList<String> invoiceClasses = new ArrayList<String>();

		try (Connection conn = connectAndSecureDB()) {

			PreparedStatement stmt =
					conn.prepareStatement("SELECT invoice_class FROM invoice_classes;");

			ResultSet results = stmt.executeQuery();

			while (results.next()) {
				invoiceClasses.add(results.getString("invoice_class"));
			} 

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}


		return invoiceClasses;

	}
	
	public int getType(int classID) {
		
		int invType = 0;
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt =
					conn.prepareStatement("SELECT invoice_type FROM invoice_classes WHERE id = ?;");
			
			stmt.setInt(1, classID);
			
			ResultSet results = stmt.executeQuery();
			
			invType = results.getInt("invoice_type");
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return invType;
		
	}
	
	public ArrayList<Integer> getClassList() {
		
		ArrayList<Integer> classList = new ArrayList<Integer>();
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt = 
					conn.prepareStatement("SELECT id FROM invoice_classes;");
			
			ResultSet results = stmt.executeQuery();
			
			while (results.next()) {
				classList.add(results.getInt("id"));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return classList;
		
	}
	
	public int getClassIDFromString (String className) {
		
		int classID = -1;
		
		try (Connection conn = connectAndSecureDB()) {
			
			PreparedStatement stmt = 
					conn.prepareStatement("SELECT id FROM invoice_classes WHERE invoice_class=?;");
			
			stmt.setString(1, className);
			
			ResultSet results = stmt.executeQuery();
			
			classID = results.getInt("id");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return classID;
		
	}
	

	// Metodi DB-yhteyden luomiselle. Palauttaa yhteyden.
	
	private Connection connectAndSecureDB() throws SQLException {

		Connection conn = null;

		try {
			conn = DriverManager.getConnection(this.dbPath);
		} catch (SQLException t) {
			System.out.println(t.getMessage());
		}

		return conn;

	}
	
}
