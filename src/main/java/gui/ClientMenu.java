package gui;

import java.util.ArrayList;
import java.util.Collections;

import domain.Client;
import domain.Invoice;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import logic.IEC;
import logic.ReskontraDAO;

public class ClientMenu extends Application{
	
	private String dbPath;
	private ArrayList<Client> clientList;
	
	public ClientMenu(String dbPath) {
		
		this.dbPath = dbPath;
		
	}
	
	public void start(Stage stage) {
		
		ReskontraDAO dao = new ReskontraDAO(dbPath);
		
		this.clientList = dao.getAllClientInfo();
		
		BorderPane mainMontage = new BorderPane();
		TableView itemTable = new TableView<>();
		GridPane controlPane = new GridPane();
		
		//Styling the mainMontage
		
		//itemTable.setStyle("-fx-background-color: #FFFAF0");

		//GridPane buttons
		Button closeButton = buttonFactory("Sulje asiakasrekisteri");
		Button editButton = buttonFactory("Muokkaa valittua asiakasta");
		Button addButton = buttonFactory("Lisää uusi asiakas");
		Button removeButton = buttonFactory("Poista valittu asiakas");
		Button refreshButton = buttonFactory("Päivitä asiakasrekisteri");
		
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
		
		//Assembly and setup of TableView
	    TableColumn<String, Invoice> clientIDColumn = new TableColumn<>("Asiakkaan ID");
	    clientIDColumn.setCellValueFactory(new PropertyValueFactory<>("clientID"));

		TableColumn<String, Invoice> clientNameColumn = new TableColumn<>("Asiakkaan nimi");
		clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));

		itemTable.getColumns().addAll(clientIDColumn, clientNameColumn);

		itemTable.setPlaceholder(new Label("Ei asiakastietoja nätettäväksi"));


		//Creating of the selection model for later using selected invoices
		TableViewSelectionModel<Client> selectionModel = itemTable.getSelectionModel();
		selectionModel.setSelectionMode(SelectionMode.SINGLE);
		
		clientList.stream().forEach((item) -> {
			itemTable.getItems().add(item);
		});
		
		mainMontage.setTop(itemTable);
		mainMontage.setBottom(controlPane);
		
		//Button functionality
		
		//Adding items
		
		addButton.setOnAction((event) -> {

			SceneView newScene = new SceneView();
			Stage addStage = addClientView(dao, itemTable);
			newScene.start(addStage);

		});
		
		//Editing items
		
		editButton.setOnAction((event) -> {
			
			SceneView newScene = new SceneView();
			ObservableList<Client> selectedItems = selectionModel.getSelectedItems();
			int id = selectedItems.get(0).getClientID();

			Stage editStage = editClientView(dao, id, itemTable);
			newScene.start(editStage);
			
		});
		
		
		//Close button
		closeButton.setOnAction((event) -> {
			stage.close();
		});
		
		//RefreshButton

		refreshButton.setOnAction((event) -> {
			
			//Get new invoice list from DAO
			clientList = dao.getAllClientInfo();
			
			//Clear the old itemTable-list
			itemTable.getItems().clear();
			
			//Update the neew list into tableview
			clientList.stream().forEach((item) -> {
				itemTable.getItems().add(item);
			});
		});
		
		//removeButton 
		
		removeButton.setOnAction((event) -> {
			
			//Get selected client from the tableview
			Client selectedClient = selectionModel.getSelectedItem();
			
			//Delete client from database
			dao.deleteClient(selectedClient.getClientID());
			
			//Clear and refresh the tableview
			clientList = dao.getAllClientInfo();
			
			itemTable.getItems().clear();
			
			clientList.stream().forEach((item) -> {
				itemTable.getItems().add(item);
			});
			
		});
		

		Scene setting = new Scene(mainMontage, 800, 500);
		
		setting.getStylesheets().add("InvoiceMenu.css");

		stage.setTitle("Asiakasrekisteri");
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
	
	private Stage addClientView(ReskontraDAO dao, TableView itemTable) {
		
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
		Label infoLabel = new Label("Lisää uusi asiakas");
		infoLabel.setAlignment(Pos.TOP_CENTER);
		
		//Creation of components
		Label clientName = new Label("Uuden asiakkaan nimi:  ");

		TextField clientNameField = new TextField("Uusi asiakas");
		
		HBox buttonBox = new HBox();
		buttonBox.setSpacing(10);
		buttonBox.setPadding(new Insets(20, 20, 100, 20));
		buttonBox.setAlignment(Pos.BOTTOM_CENTER);

		Button saveButtonAdd = new Button("Tallenna");
		Button closeButtonAdd = new Button ("Sulje valikko");
		
		saveButtonAdd.setOnAction((event) -> {
									
				dao.createClient(clientNameField.getText());
				infoLabel.setText("Uusi asiakas luotu!");
				clientNameField.clear();
				
				//Refreshing the tableview item
				clientList = dao.getAllClientInfo();
				
				itemTable.getItems().clear();
				
				clientList.forEach((item) -> {
					itemTable.getItems().add(item);
				});
			
		});
		
		closeButtonAdd.setOnAction((event) -> {
			newStage.close();
		});

		buttonBox.getChildren().addAll(saveButtonAdd, closeButtonAdd);

		//Assembling the components
		addPane.add(clientName, 0, 0);
		addPane.add(clientNameField, 0, 1);

		addMontage.setTop(infoLabel);
		addMontage.setCenter(addPane);
		addMontage.setBottom(buttonBox);

		Scene setting = new Scene(addMontage, 600, 400);
		newStage.setScene(setting);

		return newStage;
		
	}
	
	private Stage editClientView(ReskontraDAO dao, int id, TableView itemTable) {
		
		Client editClient = dao.getClientByID(id);
		
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
		Label infoLabel = new Label("Muokkaa asiakkaan tietoja");
		infoLabel.setAlignment(Pos.TOP_CENTER);
		
		//Creation of components
		Label clientName = new Label("Asiakkaan nimi:  ");

		TextField clientNameField = new TextField(editClient.getClientName());
		
		HBox buttonBox = new HBox();
		buttonBox.setSpacing(10);
		buttonBox.setPadding(new Insets(20, 20, 100, 20));
		buttonBox.setAlignment(Pos.BOTTOM_CENTER);

		Button saveButtonAdd = new Button("Tallenna");
		Button closeButtonAdd = new Button ("Sulje valikko");
		
		saveButtonAdd.setOnAction((event) -> {
									
				dao.updateClientInfo(editClient.getClientID(), clientNameField.getText());
				infoLabel.setText("Asiakkaan tiedot päivitetty!");
				clientNameField.clear();
				
				//Refreshing the tableview item
				clientList = dao.getAllClientInfo();
				
				itemTable.getItems().clear();
				
				clientList.forEach((item) -> {
					itemTable.getItems().add(item);
				});

				newStage.close();
			
		});
		
		closeButtonAdd.setOnAction((event) -> {
			newStage.close();
		});

		buttonBox.getChildren().addAll(saveButtonAdd, closeButtonAdd);

		//Assembling the components
		addPane.add(clientName, 0, 0);
		addPane.add(clientNameField, 0, 1);

		addMontage.setTop(infoLabel);
		addMontage.setCenter(addPane);
		addMontage.setBottom(buttonBox);

		Scene setting = new Scene(addMontage, 600, 400);
		newStage.setScene(setting);

		return newStage;
		
	}
	
}
