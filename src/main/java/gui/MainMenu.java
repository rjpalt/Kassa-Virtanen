package gui;

import javafx.scene.control.Button;
import domain.BalanceGraph;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class MainMenu extends Application{
	
	private	String dbPath = "jdbc:sqlite:/home/rjpalt/Projects/kassa-virtanen/src/main/resources/reskontraDB";
	
	public void start(Stage window) {
		
		
		GridPane montage = new GridPane();
		//montage.getStyleClass().add("grid");
		montage.setStyle("-fx-background-color: ivory;");
		
		
		Button invoices = buttonFactory("Laskut");
		
		invoices.setOnMouseClicked((event) -> {
			Stage stage = new Stage();
			InvoiceMenu invoiceMenu = new InvoiceMenu(dbPath);
			invoiceMenu.start(stage);
		});

		
		Button clients = buttonFactory("Asiakasrekisteri");
		
		clients.setOnAction((event) -> {
			
			Stage stage = new Stage();
			ClientMenu clientMenu = new ClientMenu(dbPath);
			clientMenu.start(stage);
			
		});
		
		
		Button cashFlow = buttonFactory("Kassavirta");
		
		cashFlow.setOnAction((event) -> {
			
			Stage stage = new Stage();

			BalanceGraphInit BGI = new BalanceGraphInit();
			BGI.start(stage);
			
		}); 
		
		
		Button settings = buttonFactory("Asetukset");
		
		
		
		montage.setPrefSize(640, 480);
		montage.setAlignment(Pos.CENTER);
		montage.setVgap(50);
		montage.setHgap(50);
		montage.setPadding(new Insets(20, 20, 20, 20));

		montage.add(invoices, 0, 0);
		montage.add(clients, 1, 0);
		montage.add(cashFlow, 0, 1);
		montage.add(settings, 1, 1);
		
		Scene setting = new Scene(montage, 1024, 768);
		
		//setting.getStylesheets().add("MainMenu.css");
			
		window.setScene(setting);
		
		window.setTitle("Kassa-Virtanen");
		
		window.show();
		
	}
	
	private Button buttonFactory(String buttonName) {
		
		Button newButton = new Button();
		
		Font font = Font.font("Monospaced", 24);
		
		newButton.setMinSize(180, 180);
		newButton.setAlignment(Pos.CENTER);
		newButton.setStyle("-fx-border-width: 4px; -fx-border-color:  #800000; -fx-font-size: 1.5em; -fx-background-color: #F5DEB3; -fx-border-radius: 7;");
		newButton.setFont(font);
		newButton.setText(buttonName);
		newButton.getStyleClass().add("button");
		
		return newButton;
		
	}
	
	
	

}
