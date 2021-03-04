
//import logic.ReskontraDAO;
//import ui.TxtUI;
import gui.MainMenu;

//import java.util.Scanner;
import javafx.application.Application;



public class kassavirtanen {

	public static void main(String[] args) {
		//Scanner lukija = new Scanner(System.in);

		//Syötä tähän tietokannan sijainti
		//String dbPath = "jdbc:sqlite:/home/rjpalt/Projects/kassa-virtanen/src/main/resources/reskontraDB";

		//Luo DAO
		//ReskontraDAO dao = new ReskontraDAO(dbPath);
		
		
		//Uuden tekstipohjaisen kälin luominen
		//TxtUI tui = new TxtUI(lukija, dao);

		//Kälin käynnistys
		//tui.start();
		
		Application.launch(MainMenu.class);

	}

}
