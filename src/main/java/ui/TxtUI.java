package ui;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import logic.ReskontraDAO;
import domain.Invoice;
import javafx.application.Application;
import domain.BalanceGraph;
import domain.Client;
import domain.DPO;

public class TxtUI {
	
	private Scanner lukija;
	private ReskontraDAO dao;
	
	public TxtUI(Scanner lukija, ReskontraDAO dao) {
		
		this.lukija = lukija;
		this.dao = dao;
		
	}
	
	public void start() {
		
		System.out.println("Tekstipohjainen käyttöliittymä kytketty päälle.");
		
		while (true) {
			
			System.out.println("");
			System.out.println("Valitse suoritettava toiminto: ");
			System.out.println("");
			System.out.println("[1] Listaa kaikki laskut (myynti ja osto).");
			System.out.println("[2] Listaa myyntilaskut.");
			System.out.println("[3] Listaa ostolaskut.");
			System.out.println("[4] Lisää lasku.");
			System.out.println("[5] Poista lasku.");
			System.out.println("[6] Muokkaa laskua.");
			System.out.println("[7] Hae kaikki laskut valitulta päivältä.");
			System.out.println("[8] Asiakasrekisteri");
			System.out.println("[9] Kehittyneemmät toiminnot");
			System.out.println("[0] Lopeta ohjelma");
			System.out.println("");
			
			System.out.print("> ");
			
			String input = lukija.nextLine();
			
			if (input == "0") {
				break;
			}
			
			int cmd = -1;
			
			if (input.matches("[0-9]{1}")) {
				cmd = Integer.valueOf(input);
			} else {
				System.out.println("Virheellinen syöte. Yritä uudestaan.");
				return;
			}
			
			if (cmd == 0) {
				System.out.println("Ohjelma päätetty.");
				break;
			} else if (cmd == 1) {
				
				// Haetaan DAO:n kautta tietokannasta kaikki laskut, ja laitetaan ne arraylistille, jolta ne luetaan.
				
				System.out.println("Haetaan laskuja tietokannasta.");
				ArrayList<Invoice> invoices = dao.getAllInvoices();
				tulostaTiedot(invoices);
				
			} else if (cmd == 2) {
				
				//Haetaan DAO:n kautta tietokannasta kaikki myyntilaskut arraylistille, jolta ne luetaan
				
				System.out.println("Haetaan myyntilaskuja tietokannasta.");
				ArrayList<Invoice> invoices = dao.getAllSales();
				tulostaTiedot(invoices);		
				
			} else if (cmd == 3) {
				
				//Haetaan DAO:n kautta tietokannasta kaikki ostolaskut arraylistille, jolta ne luetaan
				
				System.out.println("Haetaan ostolaskuja tietokannasta");
				ArrayList<Invoice> invoices = dao.getAllPurchases();
				tulostaTiedot(invoices);
				
			} else if (cmd == 4) {
				
				luoLasku();
				
			} else if (cmd == 5) {
				
				poistaLasku();							
				
			} else if (cmd == 6) {
				
				muokkaaLaskua();
				
			} else if (cmd == 7) {
				
				haeLaskut();
				
			} else if (cmd == 8) {
				
				asiakasRekisteri();
				
			} else if (cmd == 9) {
				
				advancedStuff();
				
			}
			
			
		}
		
	}
	
	public void luoLasku() {
		
		int laskunLuokka = 0;
		int asiakasNumero = 0;
		double summa = 0.0;
		String erapaiva = "";
		HashMap<Integer, String> asiakkaat = dao.getClients();
		
		while (true) {
			
			ArrayList<String> classList = dao.getInvoiceClasses();
			
			System.out.println("");
			System.out.println("Valitse laskun luokka: ");
			System.out.print("[0] Palaa edelliseen valikkoon");

			for (int i = 0; i < classList.size(); i++) {
				System.out.print("\n[" + (i + 1) + "] " + classList.get(i));
			}
			
			System.out.println("");
			System.out.print("> " );
			
			String inputClass = lukija.nextLine();
			
			if (inputClass.equals("0")) {
				break;
			}
			
			if (!inputClass.matches("[1-" + classList.size() +"]{1}")) {
				System.out.println("Virheellinen syöte!");
				continue;
			} else {
				laskunLuokka = Integer.valueOf(inputClass);
			}
			
			System.out.println("");
			System.out.println("Anna asiakkaan tunnusnumero.");
			System.out.print("> ");
			
			String inputClient = lukija.nextLine();
			
			if (!inputClient.matches("^\\d+$")) {
				System.out.println("Virheellinen syöte! Syötä asiakkaan tunnusnumero kokonaislukuna.");
				continue;
			} else {
				if (!asiakkaat.containsKey(Integer.valueOf(inputClient))) {
					System.out.println("Antamallasi tunnusnumerolla ei löydy asiakasta.");
					continue;
				} else {
					asiakasNumero = Integer.valueOf(inputClient);
				}
			}
			
			System.out.println("");
			System.out.println("Anna laskun summa (Muoto: KKKK.DD).");
			System.out.print("> ");
			
			String inputSum = lukija.nextLine();
			
			if (!inputSum.matches("^\\d+\\.\\d{2}$")) {
				System.out.println("Virheellinen syöte! Syötä laskun summa muodossa 1234.56.");
				continue;
			} else {
				summa = Double.valueOf(inputSum);
			}
		
			System.out.println("");
			System.out.println("Anna laskun eräpäivä (VVVV-KK-PP).");
			System.out.print("> ");
			
			String inputDate = lukija.nextLine();
			
			if(!inputDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|(1|2)[0-9]|3[0-1])")) {
				System.out.println("Virheellinen syöte! Anna laskun eräpäivä muodossa VVVV-KK-PP.");
			} else {
				erapaiva = inputDate;
				break;
			}
			
		}
		
		dao.createInvoice(laskunLuokka, asiakasNumero, summa, erapaiva);
		
	}
	
	public void poistaLasku() {
		
		ArrayList<Invoice> invoices = dao.getAllInvoices();
		
		int laskuID = -1;
		
		while (true) {
			System.out.println("");
			System.out.println("Anna poistettavan laskun tunnusnumero. ([0] Poistu edeltävään valikkoon.)");
			System.out.print("> ");
			
			String input = lukija.nextLine();
			
			if (input.equals("0")) {
				break;
			}

			
			if (!input.matches("^\\d+$")) {
				System.out.println("Virheellinen syöte. Laskun tunnusnumero on positiivinen kokonaisluku.");
				continue;
			} else {
				laskuID = Integer.valueOf(input);
			}
			
			if (sisaltaaLaskun(invoices, laskuID)) {
				System.out.println("Poistetaan lasku numero " + laskuID + ".");
				
				int i = 0;
				
				while (i < invoices.size()) {
					
					if (laskuID == invoices.get(i).getInvoiceID()) {
						dao.removeInvoice(laskuID);
						break;
					}
					
					i++;
				}

			} else {
				System.out.println("Laskua ei löydy tietokannasta.");
				continue;
			}
			
		}
		
		
	}
	
	public void muokkaaLaskua() {
		
		ArrayList<Invoice> invoices = dao.getAllInvoices();
		int laskuID = 0;
		
		while (true) {
			
			System.out.println("");
			System.out.println("Anna muokattavan laskun tunnusnumero. ([0] Poistu edeltävään valikkoon.)");
			System.out.print("> ");
			
			String input = lukija.nextLine();
			
			if (input.equals("0")) {
				break;
			}
			
			if (!input.matches("^\\d+$")) {
				System.out.println("Virheellinen syöte! Laskun tunnusnumero on positiivinen kokonaisluku.");
				continue;
			} else {		
				laskuID = Integer.valueOf(input);				
			}
			
			if (sisaltaaLaskun(invoices, laskuID)) {
				
				int uusiLuokkaNro = -1;
				int uusiAsNro = -1;
				double uusiSumma = 0.0;
				String uusiPvm = "";
				
				while (true) {
					
					System.out.print("Anna laskun uusi tyyppinumero: ");
					
					String inputLuokka = lukija.nextLine();
					ArrayList<String> classList = dao.getInvoiceClasses();
					
					if (!inputLuokka.matches("^\\d$")) {
						System.out.println("Virheellinen syöte. Anna laskun tyyppinumero (1 - "+ classList.size() +").");
						continue;
					}
					
					uusiLuokkaNro = Integer.valueOf(inputLuokka);
					ArrayList<Integer> luokkaNrot = dao.getClassList(); //Lista class numeroista
					
					if (!luokkaNrot.contains(uusiLuokkaNro)) {
						System.out.println("Antamaasi tyyppinumeroa ei löydy. Koeta uudestaan.");
						continue;
					} else {
						break;
					}
					
				}
				
				while (true) {
					
					System.out.print("Anna laskun uusi asiakasnumero: ");
					
					String inputAsiakas = lukija.nextLine();
					
					if (!inputAsiakas.matches("^\\d{1,3}$")) {
						System.out.println("Virheellinen syöte. Anna asiakkaan asiakasnumero (1- 999).");
						continue;
					}
					
					uusiAsNro = Integer.valueOf(inputAsiakas);
					ArrayList<Integer> asiakasIDt = dao.getClientIDs();
					
					if (!asiakasIDt.contains(uusiAsNro)) {
						System.out.println("Asiakasta ei löydy antamallasi asiakasnumerolla. Yritä uudestaan.");
						continue;
					} else {
						break;
					}
					
				}
				
				while (true) {
					
					System.out.print("Anna laskun uusi summa: ");
					
					String inputSumma = lukija.nextLine();
					
					if (!inputSumma.matches("^\\d+\\.\\d{2}$")) {
						System.out.println("Virheellinen syöte. Anna laskun summa muodossa LLLL.DD.");
						continue;
					} else {
						uusiSumma = Double.valueOf(inputSumma);
						break;
					}
					
				}
				
				while (true) {
					
					System.out.print("Anna laskun uusi eräpäivä (VVVV-KK-PP): ");
					
					String inputPvm = lukija.nextLine();
					
					if (!inputPvm.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|(1|2)[0-9]|3[0-1])")) {
						System.out.println("Virheellinen syöte. Anna eräpäivä muodossa VVVV-KK-PP.");
						continue;
					} else {
						uusiPvm = inputPvm;
						break;
					}
					
				}
				
				System.out.println("");
				System.out.println("Päivitetään laskun tiedot.");
				dao.updateInvoice(laskuID, uusiLuokkaNro, uusiAsNro, uusiSumma, uusiPvm);
				break;
				
			} else {
				System.out.println("Laskua ei löydy tietokannasta. Yritä uudelleen.");
				continue;
			}
			
		}
		
	}
		
	public void tulostaTiedot(ArrayList<Invoice> invoices) {
		
		invoices.forEach((invoice) -> {
			System.out.println(invoice.toString());
		});
		
	}
	
	public boolean sisaltaaLaskun (ArrayList<Invoice> invoices, int hakuID) {
		
		boolean loytyykoLasku = false;
		
		int i = 0;
		
		while (i < invoices.size()) {
			
			int laskuID = invoices.get(i).getInvoiceID();
			
			if (laskuID == hakuID) {
				loytyykoLasku = true;
				break;
			}
			
			i++;
			
		}
		
		return loytyykoLasku;
		
	}
	
	public void asiakasRekisteri() {
		
		while(true) {
			
			System.out.println("Asiakasrekisteri - valitse suoritettava toiminto:");
			System.out.println("[1] Listaa asiakkaat.");
			System.out.println("[2] Lisää asiakas.");
			System.out.println("[3] Poista asiakas.");
			System.out.println("[4] Muokkaa asiakastietoja.");
			System.out.println("[0] Palaa päävalikkoon.");
			System.out.print("> ");
			
			String input = lukija.nextLine();
			
			if (!input.matches("^\\d$")) {
				System.out.println("Virheellinen syöte! Anna käsky kokonaislukuna.");
				continue;
			}
			
			int cmd = Integer.valueOf(input);
			
			if (cmd == 0) {
				break;
			} else if (cmd == 1) {
				
				ArrayList<Client> asiakkaat = dao.getAllClientInfo();
				
				System.out.println("");
				
				asiakkaat.forEach((client) -> {
					System.out.println(client.toString());
				});
				
			} else if (cmd == 2) {
				
				luoAsiakas();
				
			} else if (cmd == 3) {
				
				poistaAsiakas();
				
			} else if (cmd == 4) {
				
				muokkaaAsiakasta();
				
			}
			
			
		}
		
	}
	
	public void luoAsiakas() {
		
		while(true) {
			
			System.out.println("");
			System.out.print("Anna luotavan asiakkaan nimi: ");
			
			String uusiAsiakas = lukija.nextLine();
			
			if (uusiAsiakas.isEmpty()) {
				System.out.println("Asiakkaan nimi ei voi olla tyhjä kenttä. Yritä uudestaan.");
				continue;
			} else {
				dao.createClient(uusiAsiakas);
				break;
			}
			
		}
		
	}
	
	public void poistaAsiakas() {
		
		while (true) {
			
			System.out.println("");
			System.out.print("Anna poistettavan asiakkaan tunnusnumero ([0] Palaa edelliseen valikkoon): ");
			
			String input = lukija.nextLine();
			
			if (input == "0") {
				break;
			}
			
			if (!input.matches("^\\d{1,3}$")) {
				System.out.println("Virheellinen syöte! Asiakasnumero on kokonaisluku väliltä 1 - 999.");
				continue;
			}
			
			int asiakasID = Integer.valueOf(input);
			
			ArrayList<Integer> asiakasIDt= dao.getClientIDs();
			
			if (!asiakasIDt.contains(asiakasID)) {
				System.out.println("Kyseisellä asiakastunnuksella ei löydy asiakasta rekisteristä. Yritä uudestaan.");
				continue;
			} else {
				dao.deleteClient(asiakasID);
				break;
			}
			
		}
		
	}

	public void muokkaaAsiakasta() {
		
		while (true) {
			
			System.out.println("");
			System.out.print("Anna muokattavan asiakkaan tunnusnumero ([0] Palaa edelliseen valikkoon): ");
			
			String input = lukija.nextLine();
			
			if (input.equals("0")) {
				break;
			} else if (!input.matches("^\\d{1,3}$")) {
				System.out.println("Virheellinen syöte! Asiakasluku on kokonaisluku väliltä 1 - 999. Yritä uudestaan.");
				continue;
			}
			
			int asiakasID = Integer.valueOf(input);
			
			ArrayList<Integer> asiakasIDt = dao.getClientIDs();
			
			if (!asiakasIDt.contains(asiakasID)) {
				System.out.println("Antamallasi tunnusnumerolla ei löydy asiakastietoja. Yritä uudestaan.");
				continue;
			} else {
				
				System.out.print("Anna asiakkaan uusi nimi: ");
				
				String uusiNimi = lukija.nextLine();
				
				dao.updateClientInfo(asiakasID, uusiNimi);
				
				break;
				
			}
			
		}
		
	}
	
	public void haeLaskut() {
		
		while (true) {
			
			System.out.println("");
			System.out.print("Anna päivämäärä, jolta laskut haetaan (VVVV-KK-PP): ");
			
			String input = lukija.nextLine();
			
			if (!input.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|(1|2)[0-9]|3[0-1])")) {
				System.out.println("");
				System.out.println("Virheellinen syöte! Päivämäärän tulee olla muodossa VVVV-KK-PP.");
				System.out.println("");
				continue;
			} else {
				
				ArrayList<Invoice> laskut = dao.getAllInvoicesOnDate(input);
				
				laskut.forEach((invoice) -> {
					System.out.println(invoice.toString());
				});
				
				break;
				
			}
			
		}
		
	}
	
	public void advancedStuff() {
		
		DPO dpo = new DPO(dao);
		
		while (true) {
			
			System.out.println("");
			System.out.println("Valitse toiminto:");
			System.out.println("[1] Luo seuraavat X päivämäärää.");
			System.out.println("[2] Luo balanssipisteet seuraavalle X päivälle.");
			System.out.println("[3] Luo listaus päivämääristä ja balanssipisteistä.");
			System.out.println("[4] Luo kassavirta tästä päivästä eteenpäin halutulle X päivälle.");
			System.out.println("[5] Luo kaavio kassavirrasta.");
			System.out.println("[6] Järjestä laskut päivämäärän mukaan.");
			System.out.print("> ");
			
			String input = lukija.nextLine();
			
			if (input.equals("0")) {
				break;
			}
			
			if (!input.matches("^[1-6]$")) {
				System.out.println("Anna komento, joka vastaa valikon vaihtoehtoja.");
				continue;
			}
			
			int cmd = Integer.valueOf(input);
			
			if (cmd == 1) {
				
				luoPvmt(dpo);
				
			} else if (cmd == 2) {
				
				luoBalanssiPisteet(dpo);
				
			} else if (cmd == 3) {
				
				luoPvmJaBalanssi(dpo);
				
			} else if (cmd == 4) {
				
				luoKassavirta(dpo);
				
			} else if (cmd == 5) {
				
				luoKaavio();
				
			} else if (cmd == 6) {
				
				pvmJarjestys();
				
			}
			
		}
		
	}
	
	public void luoPvmt(DPO dpo) {
		
		while (true) {
			
			System.out.println("");
			System.out.print("Kuinka monelle päivälle luodaan päivämäärät ([0] Palaa takaisin): ");
			
			String input = lukija.nextLine();
			
			if (input.equals("0")) {
				break;
			}
			
			if (!input.matches("^\\d{1,3}$")) {
				System.out.println("Virheellinen syöte. Anna kokonaisluku väliltä 1 - 999.");
			} else {
				
				int pvmlkm = Integer.valueOf(input);
				
				dpo.createDateList(pvmlkm);
				
				break;
				
			}
			
		}
		
	}

	public void luoBalanssiPisteet(DPO dpo) {
		
		while (true) {

			System.out.println("");
			System.out.print("Kuinka monelle päivälle luodaan balanssipisteet? ([0] Palaa takaisin edeltävään valikkoon.): ");

			String input = lukija.nextLine();

			if (input.equals("0")) {
				break;
			}

			if (!input.matches("^\\d{1,3}$")) {
				System.out.println("Virheellinen syöte! Anna kokonaisluku väliltä 1 - 999!");
			} else {

				int pvmlkm = Integer.valueOf(input);

				dpo.createBalanceByDateList(pvmlkm);

			}

		} 
	}
	
	public void luoPvmJaBalanssi(DPO dpo) {
		
		while (true) {
			
			System.out.println("");
			System.out.print("Kuinka monelle päivälle luodaan balanssipisteet? ([0] Palaa takaisin edeltävään valikkoon.): ");
			
			String input = lukija.nextLine();
			
			if (input.equals("0")) {
				break;
			}
			
			if (!input.matches("^\\d{1,4}$")) {
				System.out.println("Virheellinen syöte! Anna kokonaisluku väliltä 1 - 9999!");
			} else {
				
				int pvmlkm = Integer.valueOf(input);
				
				dpo.createDataPointList(pvmlkm);
			}
			
		}
		
	}
	
	public void luoKassavirta(DPO dpo) {
		
		while (true ) {
			
			System.out.println("");
			System.out.print("Anna kassan balanssi tälle päivälle: ");
			
			String balanssiInput = lukija.nextLine();
			
			if (balanssiInput.equals("0")) {
				break;
			}
			
			if (!balanssiInput.matches("^\\d+\\.\\d{2}$")) {
				System.out.println("Virheellinen syöte! Anna balanssi muodossa LLLL.DD.");
				continue;
			}
			
			System.out.print("Kuinka monelle päivälle kassavirta lasketaan: ");
			
			String virtaInput = lukija.nextLine();
			
			if (!virtaInput.matches("^\\d{1,3}$")) {
				System.out.println("Virheellinen syöte! Syötä kokonaisluku väliltä 1 - 999.");
				continue;
			}
			
			double alkuBalanssi = Double.valueOf(balanssiInput);
			int pvmLkm = Integer.valueOf(virtaInput);
			
			dpo.createBalanceFlow(alkuBalanssi, pvmLkm);
			
			break;
			
		}
		
	}
	
	public void luoKaavio() {
		
		String param1 = "--balanssi=";
		String param2 = "--kesto=";
		
		while (true) {
			
			System.out.println("");
			System.out.print("Anna kassan balanssi tälle päivälle ([0] Palaa edelliseen valikkoon): ");
			
			String balanssiInput = lukija.nextLine();
			
			if (balanssiInput.equals("0")) {
				break;
			}
			
			if (!balanssiInput.matches("^\\d+\\.\\d{2}$")) {
				System.out.println("Virheellinen syöte! Anna balanssi muodossa LLLL.DD.");
				continue;
			}
			
			System.out.print("Kuinka monelle päivälle kassavirta lasketaan: ");
			
			String virtaInput = lukija.nextLine();
			
			if (!virtaInput.matches("^\\d{1,3}$")) {
				System.out.println("Virheellinen syöte! Syötä kokonaisluku väliltä 1 - 999.");
				continue;
			}
			
			param1 = param1 + balanssiInput;
			param2 = param2 + virtaInput;
			break;
			
		}
		
        Application.launch(BalanceGraph.class,
                param1,
                param2);
		
	}
	
	public void pvmJarjestys() {
		
		ArrayList<Invoice> laskulista = dao.getAllInvoices();
		
		Collections.sort(laskulista);
		
		laskulista.forEach((lasku) -> {
			System.out.println(lasku);
		});
		
	}
	
}

