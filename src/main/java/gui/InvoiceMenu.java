package gui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import logic.ReskontraDAO;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import domain.Client;
import domain.Invoice;
import logic.IEC;

public class InvoiceMenu extends Application{
	
	private ArrayList<Invoice> invoiceList;
	private String dbPath;
	
	public InvoiceMenu(String db) {
		
		this.dbPath = db;
		
	}
	
	public void start(Stage stage) {
		
		ReskontraDAO dao = new ReskontraDAO(this.dbPath);
		IEC errol = new IEC();
		
		invoiceList = dao.getAllInvoices();
		
		BorderPane mainMontage = new BorderPane();
		TableView itemTable = new TableView<>();
		GridPane controlPane = new GridPane();
		
		//Styling the mainMontage
		
		//itemTable.setStyle("-fx-background-color: #FFFAF0");

		//GridPane buttons
		Button closeButton = buttonFactory("Sulje laskunäkymä");
		Button editButton = buttonFactory("Muokkaa valittua laskua");
		Button addButton = buttonFactory("Lisää lasku");
		Button removeButton = buttonFactory("Poista valittu lasku");
		Button refreshButton = buttonFactory("Päivitä laskut");
		Button sortByDateButton = buttonFactory("Järjestä laskut päivämäärän mukaan");
		

		//GridPane settings and assembly
		controlPane.setPadding(new Insets(20, 20, 20, 20));
		controlPane.setVgap(5);
		controlPane.setHgap(30);
		controlPane.setAlignment(Pos.BOTTOM_CENTER);
		controlPane.add(refreshButton, 0, 0);
		controlPane.add(editButton, 1, 0);
		controlPane.add(addButton, 2, 0);
		controlPane.add(removeButton, 3, 0);
		controlPane.add(closeButton, 0, 1);
		controlPane.add(sortByDateButton, 1, 1);

		//Assembly and setup of TableView
	    TableColumn<String, Invoice> invoiceIDColumn = new TableColumn<>("Laskun ID");
	    invoiceIDColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceID"));

		TableColumn<String, Invoice> clientNameColumn = new TableColumn<>("Asiakkaan nimi");
		clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));

		TableColumn<Double, Invoice> invoiceSumColumn = new TableColumn<>("Laskun summa");
		invoiceSumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
		//invoiceSumColumn.setStyle("-fx-alignment: CENTER;");
		
		TableColumn<String, Invoice> invoiceTypeColumn = new TableColumn<>("Laskun luokka");
		invoiceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceClass"));
		
		TableColumn<String, Invoice> invoiceDateColumn = new TableColumn<>("Laskun eräpäivä");
		invoiceDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

		itemTable.getColumns().addAll(invoiceIDColumn, clientNameColumn, invoiceSumColumn, invoiceTypeColumn, invoiceDateColumn);

		itemTable.setPlaceholder(new Label("Ei laskuja näytettäväksi"));


		//Creating of the selection model for later using selected invoices
		TableViewSelectionModel<Invoice> selectionModel = itemTable.getSelectionModel();
		selectionModel.setSelectionMode(SelectionMode.SINGLE);
		
		invoiceList.stream().forEach((item) -> {
			itemTable.getItems().add(item);
		});
		
		mainMontage.setTop(itemTable);
		mainMontage.setBottom(controlPane);
		
		//Button functionality
		
		//Adding items
		
		addButton.setOnAction((event) -> {

			SceneView newScene = new SceneView();
			Stage addStage = addItemView(dao, errol, itemTable);
			newScene.start(addStage);

		});
		
		//Editing items
		
		editButton.setOnAction((event) -> {
			
			SceneView newScene = new SceneView();
			ObservableList<Invoice> selectedItems = selectionModel.getSelectedItems();
			int id = selectedItems.get(0).getInvoiceID();

			Stage editStage = editItemView(dao, errol, id, itemTable);
			newScene.start(editStage);
			
		});
		
		
		//Close button
		closeButton.setOnAction((event) -> {
			stage.close();
		});
		
		//RefreshButton

		refreshButton.setOnAction((event) -> {
			
			//Get new invoice list from DAO
			invoiceList = dao.getAllInvoices();
			
			//Clear the old itemTable-list
			itemTable.getItems().clear();
			
			//Update the neew list into tableview
			invoiceList.stream().forEach((item) -> {
				itemTable.getItems().add(item);
			});
		});
		
		//sortByDateButton
		
		sortByDateButton.setOnAction((event) -> {
			
			Collections.sort(invoiceList);
			
			itemTable.getItems().clear();
			
			invoiceList.forEach((item) -> {
				itemTable.getItems().add(item);
			});
			
		});
		
		//removeButton 
		
		removeButton.setOnAction((event) -> {
			
			Invoice selectedInvoice = selectionModel.getSelectedItem();
			
			int invToDeleteID = selectedInvoice.getInvoiceID();
			
			dao.removeInvoice(invToDeleteID);
			
			//Get new invoice list from DAO
			invoiceList = dao.getAllInvoices();
			
			//Clear the old itemTable-list
			itemTable.getItems().clear();
			
			//Update the neew list into tableview
			invoiceList.stream().forEach((item) -> {
				itemTable.getItems().add(item);
			});
			
		});
		

		Scene setting = new Scene(mainMontage, 800, 500);
		
		setting.getStylesheets().add("InvoiceMenu.css");

		stage.setTitle("Laskut");
		stage.setScene(setting);
		stage.show();
		
	}
	
	private Button buttonFactory(String buttonName) {
		
		Button newButton = new Button();
		
		Font font = Font.font("Monospaced", 8);
		
		newButton.setAlignment(Pos.CENTER);
		//newButton.setStyle("-fx-border-width: 1px; -fx-border-color:  #800000; -fx-font-size: 1em; -fx-background-color: #F5DEB3; -fx-border-radius: 2;");
		newButton.setFont(font);
		newButton.setText(buttonName);
		
		return newButton;
		
	}
	
	private Stage addItemView(ReskontraDAO dao, IEC errol, TableView itemTable) {

		Stage newStage = new Stage();

		newStage.setAlwaysOnTop(true);

		BorderPane addMontage = new BorderPane();

		//Setup of gridpane for edit
		GridPane addPane = new GridPane();
		addPane.setVgap(5);
		addPane.setHgap(20);
		addPane.setAlignment(Pos.CENTER);
		addPane.setPadding(new Insets(20, 20, 20, 20));

		//Upper label
		Label infoLabel = new Label("Lisää lasku");
		infoLabel.setAlignment(Pos.TOP_CENTER);

		//Creation of components
		Label sumLabel = new Label("Laskun summa (1234.56): ");
		Label dateLabel = new Label("Laskun eräpäivä (VVVV-KK-PP):  ");
		Label typeLabel = new Label("Laskun tyyppi: ");
		Label clientLabel = new Label("Asiakas: ");

		TextField sumField = new TextField("");
		TextField dateField = new TextField("");
		
		//Combobox for invoice classes
		ArrayList<String> invoiceClasses = dao.getInvoiceClasses();
		ComboBox<String> typeBox = new ComboBox<String>();
		
		invoiceClasses.forEach((invoice) -> {
			typeBox.getItems().add(invoice);
		});
		
		//Combobox for clients
		ArrayList<Client> clientList = dao.getAllClientInfo();
		ComboBox<Client> clientBox = new ComboBox<Client>();
		
		clientList.forEach((client) -> {
			clientBox.getItems().add(client);
		});


		//
		HBox buttonBox = new HBox();
		buttonBox.setSpacing(10);
		buttonBox.setPadding(new Insets(20, 20, 100, 20));
		buttonBox.setAlignment(Pos.BOTTOM_CENTER);

		Button saveButtonAdd = new Button("Save");
		Button closeButtonAdd = new Button ("Close");

		saveButtonAdd.setOnAction((event) -> {

			String sum = sumField.getText();
			String date = dateField.getText();
			
			if (errol.checkSum(sum) && errol.checkDate(date)) {
								
				dao.createInvoice(dao.getClassIDFromString(typeBox.getValue()), clientBox.getValue().getClientID(), Double.valueOf(sumField.getText()), dateField.getText());
				infoLabel.setText("Lasku luotu!");
				sumField.clear();
				dateField.clear();
				typeBox.setValue(null); //Kokeile näiden kahden toimimista
				clientBox.setValue(null);
				
				//Get new invoice list from DAO
				invoiceList = dao.getAllInvoices();
				
				//Clear the old itemTable-list
				itemTable.getItems().clear();
				
				//Update the neew list into tableview
				invoiceList.stream().forEach((item) -> {
					itemTable.getItems().add(item);
				});
				
			} else {
				
				ErrorMessageView errorView = new ErrorMessageView();
				Stage errorStage = errorMessageView("Virheellinen syöte! Yritä uudelleen.");
				errorView.start(errorStage);
				
			}
			
		});
		

		closeButtonAdd.setOnAction((event) -> {
			newStage.close();
		});

		buttonBox.getChildren().addAll(saveButtonAdd, closeButtonAdd);

		//Assembling the components
		addPane.add(sumLabel, 0, 0);
		addPane.add(dateLabel, 0, 1);
		addPane.add(typeLabel, 0, 2);
		addPane.add(clientLabel, 0, 3);
		addPane.add(sumField, 1, 0);
		addPane.add(dateField, 1, 1);
		addPane.add(typeBox, 1, 2);
		addPane.add(clientBox, 1, 3);

		addMontage.setTop(infoLabel);
		addMontage.setCenter(addPane);
		addMontage.setBottom(buttonBox);

		Scene setting = new Scene(addMontage, 600, 400);
		newStage.setScene(setting);

		return newStage;


	}
	
	private Stage editItemView(ReskontraDAO dao, IEC errol, int invID, TableView itemTable) {
		
		Invoice editInvoice = dao.getInvoice(invID);
		
		Stage newStage = new Stage();

		newStage.setAlwaysOnTop(true);

		BorderPane addMontage = new BorderPane();

		//Setup of gridpane for edit
		GridPane addPane = new GridPane();
		addPane.setVgap(5);
		addPane.setHgap(20);
		addPane.setAlignment(Pos.CENTER);
		addPane.setPadding(new Insets(20, 20, 20, 20));

		//Upper label
		Label infoLabel = new Label("Muokkaa laskua");
		infoLabel.setAlignment(Pos.TOP_CENTER);

		//Creation of components
		Label sumLabel = new Label("Laskun summa (1234.56): ");
		Label dateLabel = new Label("Laskun eräpäivä (VVVV-KK-PP):  ");
		Label typeLabel = new Label("Laskun tyyppi: ");
		Label clientLabel = new Label("Asiakas: ");

		//Setting the edited invoices info into text fields
		TextField sumField = new TextField(String.valueOf(editInvoice.getSum()));
		TextField dateField = new TextField(editInvoice.getDueDate());
		
		//Combobox for invoice classes
		ArrayList<String> invoiceClasses = dao.getInvoiceClasses();
		ComboBox<String> typeBox = new ComboBox<String>();
		
		invoiceClasses.forEach((invoice) -> {
			typeBox.getItems().add(invoice);
		});
		
		//Get invoice class index for the combobox.
		int iClass = 0;
		while (iClass < invoiceClasses.size()) {
			
			if (editInvoice.getInvoiceClass() == invoiceClasses.get(iClass)) {
				break;
			}
			
			iClass++;
			
		}
		
		//Setting invoice class name on the combobox		
		typeBox.setValue(editInvoice.getInvoiceClass());
		
		//Combobox for clients
		ArrayList<Client> clientList = dao.getAllClientInfo();
		ComboBox<Client> clientBox = new ComboBox<Client>();
		
		clientList.forEach((client) -> {
			clientBox.getItems().add(client);
		});
		
		Client selectedClient = dao.getClientByName(editInvoice.getClientName());
		
		//Setting client name on the combobox
		clientBox.getSelectionModel().select(selectedClient);
		

		//
		HBox buttonBox = new HBox();
		buttonBox.setSpacing(10);
		buttonBox.setPadding(new Insets(20, 20, 100, 20));
		buttonBox.setAlignment(Pos.BOTTOM_CENTER);

		Button saveButtonAdd = new Button("Save");
		Button closeButtonAdd = new Button ("Close");

		saveButtonAdd.setOnAction((event) -> {

			String sum = sumField.getText();
			String date = dateField.getText();
			
			if (errol.checkSum(sum) && errol.checkDate(date)) {
						
				dao.updateInvoice(invID, dao.getClassIDFromString(typeBox.getValue()), clientBox.getValue().getClientID(), Double.valueOf(sumField.getText()), dateField.getText());
				infoLabel.setText("Lasku päivitetty!");
				sumField.clear();
				dateField.clear();
				typeBox.setValue(null);
				clientBox.setValue(null);
				
				//Get new invoice list from DAO
				invoiceList = dao.getAllInvoices();
				
				//Clear the old itemTable-list
				itemTable.getItems().clear();
				
				//Update the neew list into tableview
				invoiceList.stream().forEach((item) -> {
					itemTable.getItems().add(item);
				});
				
				newStage.close();
				
			} else {
				
				ErrorMessageView errorView = new ErrorMessageView();
				Stage errorStage = errorMessageView("Virheellinen syöte! Yritä uudelleen.");
				errorView.start(errorStage);
				
			}
			
		});
		
		closeButtonAdd.setOnAction((event) -> {
			newStage.close();
		});

		buttonBox.getChildren().addAll(saveButtonAdd, closeButtonAdd);

		//Assembling the components
		addPane.add(sumLabel, 0, 0);
		addPane.add(dateLabel, 0, 1);
		addPane.add(typeLabel, 0, 2);
		addPane.add(clientLabel, 0, 3);
		addPane.add(sumField, 1, 0);
		addPane.add(dateField, 1, 1);
		addPane.add(typeBox, 1, 2);
		addPane.add(clientBox, 1, 3);

		addMontage.setTop(infoLabel);
		addMontage.setCenter(addPane);
		addMontage.setBottom(buttonBox);

		Scene setting = new Scene(addMontage, 600, 400);
		newStage.setScene(setting);

		return newStage;
		
	}
	
	private Stage errorMessageView(String message) {

		Stage errorStage = new Stage();

		//
		errorStage.setAlwaysOnTop(true);

		// Setup of main montage for error message view
		BorderPane errorMainMontage = new BorderPane();
		errorMainMontage.setPadding(new Insets(20, 20, 20, 20));

		//VBox layout creation and setup
		VBox errorLayout = new VBox();
		errorLayout.setSpacing(10);
		errorLayout.setAlignment(Pos.CENTER);

		//Creation and setup of components
		Label errorMessage = new Label(message);
		Button okayButton = new Button("OK");

		//Event-handler for the button (Close window)
		okayButton.setOnAction((event) -> {
			errorStage.close();
		});

		//Assembly and mounting of components
		errorLayout.getChildren().addAll(errorMessage, okayButton);
		errorMainMontage.setCenter(errorLayout);

		//Assembly of returning stage
		Scene errorScene = new Scene(errorMainMontage, 200, 200);
		errorStage.setScene(errorScene);

		return errorStage;

	}
	
}
