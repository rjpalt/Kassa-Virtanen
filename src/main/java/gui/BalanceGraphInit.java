package gui;


import domain.BalanceGraph;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.IEC;
import gui.ErrorMessageView;

public class BalanceGraphInit extends Application{
	
	private IEC errol;
	
	public void start(Stage stage) {
		
		this.errol = new IEC();
		
		stage.setAlwaysOnTop(false);

		BorderPane addMontage = new BorderPane();

		//Setup of gridpane for edit
		GridPane addPane = new GridPane();
		addPane.setVgap(5);
		addPane.setHgap(20);
		addPane.setAlignment(Pos.CENTER);
		addPane.setPadding(new Insets(20, 20, 20, 20));

		//Upper label
		Label infoLabel = new Label("Anna vaaditut parametrit kuvaajan laatimiselle.");
		infoLabel.setAlignment(Pos.TOP_CENTER);

		//Creation of components
		Label saldoLabel = new Label("Tilin saldo: ");
		Label durationLabel = new Label("Kuinka pitkälle aikajaksolle kuvaaja luodaan:  ");


		//Setting the edited invoices info into text fields
		TextField saldoField = new TextField("0.00");
		TextField durationField = new TextField("30");
		
		//
		HBox buttonBox = new HBox();
		buttonBox.setSpacing(10);
		buttonBox.setPadding(new Insets(20, 20, 100, 20));
		buttonBox.setAlignment(Pos.BOTTOM_CENTER);

		Button createGraphButton = new Button("Luo kuvaaja");
		Button closeButton = new Button ("Sulje valikko");

		createGraphButton.setOnAction((event) -> {

			String saldo = saldoField.getText();
			String duration = durationField.getText();
			
			
			if (errol.checkSum(saldo) && errol.checkDuration(duration)) {
				
				Stage BGStage = new Stage();
				BalanceGraph BG = new BalanceGraph(Double.valueOf(saldo), Integer.valueOf(duration));

				BG.start(BGStage);
				
				stage.close();
				
			} else {
				
				ErrorMessageView errorView = new ErrorMessageView();
				Stage errorStage = errorMessageView("Virheellinen syöte! Yritä uudelleen.");
				errorView.start(errorStage);
				
			}
			
			});
			
		
		closeButton.setOnAction((event) -> {
			stage.close();
		});

		buttonBox.getChildren().addAll(createGraphButton, closeButton);

		//Assembling the components
		addPane.add(saldoLabel, 0, 0);
		addPane.add(durationLabel, 0, 1);
		addPane.add(saldoField, 1, 0);
		addPane.add(durationField, 1, 1);

		addMontage.setTop(infoLabel);
		addMontage.setCenter(addPane);
		addMontage.setBottom(buttonBox);

		Scene setting = new Scene(addMontage, 600, 400);
		stage.setScene(setting);
		
		stage.show();
		
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
		Scene errorScene = new Scene(errorMainMontage, 400, 200);
		errorStage.setScene(errorScene);

		return errorStage;

	}

}
