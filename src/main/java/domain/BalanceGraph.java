package domain;

import domain.DataPoint;

import java.util.ArrayList;

import domain.DPO;
import logic.ReskontraDAO;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class BalanceGraph extends Application{
	
	private double saldo;
	private int duration;
	
	public BalanceGraph(double saldo, int duration) {
		
		this.saldo = saldo;
		this.duration = duration;
		
	}
	
	public void start(Stage ikkuna) {
		
        /*Parameters params = getParameters();
        String initBalanceStr = params.getNamed().get("balanssi");
        String flowDurationStr = params.getNamed().get("kesto");
        
        double initBalance = Double.valueOf(initBalanceStr);
        int flowDuration = Integer.valueOf(flowDurationStr);*/

	    CategoryAxis xAkseli = new CategoryAxis();
	    
	    NumberAxis yAkseli = new NumberAxis();
	    
	    LineChart<String, Number> viivakaavio = new LineChart<>(xAkseli, yAkseli);
	    
	    viivakaavio.setTitle("Kassavirran kehitys");
	    viivakaavio.setLegendVisible(false);
	    
	    XYChart.Series dataPisteet = new XYChart.Series();
	    
		String dbPath = "jdbc:sqlite:/home/rjpalt/Projects/kassa-virtanen/src/main/resources/reskontraDB";
	    ReskontraDAO dao = new ReskontraDAO(dbPath);
	    DPO dpo = new DPO(dao);
	    
	    ArrayList<DataPoint> flowPointList = dpo.createBalanceFlow(saldo, duration);
	    
	    flowPointList.forEach((dp) -> {
	    	
	    	dataPisteet.getData().add(new XYChart.Data(dp.getDate(), dp.getFlowBalance()));
	    	
	    });
	    
	    viivakaavio.getData().add(dataPisteet);
	    Scene nakyma = new Scene(viivakaavio, 1024, 768);
	    ikkuna.setScene(nakyma);
        
		ikkuna.show();
	
	}


	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	

}
