//Programmer's Name: Willem Sciandra
//Program purpose: Act as a client for server classes
//Final Edit: 12/8/18
//Additional Notes: None
//HR:
/*
	Previous code:
		Loan.java
		LoanCalculator.java
		Server.java
		Client.java
	This code is a mash up of Loan, LoanCalculator, and Client
	Eclipse IDE:
		auto complete
		method header auto generation
*/

//Cadet Sciandra contributed 100% of this class
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Sciandra_Loan_Client extends Application {
	private TextField tfAnnualInterestRate = new TextField();
	private TextField tfNumberOfYears = new TextField();
	private TextField tfLoanAmount = new TextField();
	private TextField tfMonthlyPayment = new TextField();
	private TextField tfTotalPayment = new TextField();
	private Button btCalculate = new Button("Calculate");
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		// Panel p to hold the label and text field
		GridPane gridPane = new GridPane();
		gridPane.add(new Label("Annual Interest Rate:"), 0, 0);
		gridPane.add(tfAnnualInterestRate, 1, 0);
		gridPane.add(new Label("Number of Years:"), 0, 1);
		gridPane.add(tfNumberOfYears, 1, 1);
		gridPane.add(new Label("Loan Amount:"), 0, 2);
		gridPane.add(tfLoanAmount, 1, 2);
		gridPane.add(new Label("Monthly Payment:"), 0, 3);
		gridPane.add(tfMonthlyPayment, 1, 3);
		gridPane.add(new Label("Total Payment:"), 0, 4);
		gridPane.add(tfTotalPayment, 1, 4);
		gridPane.add(btCalculate, 1, 5);

		gridPane.setAlignment(Pos.CENTER);
		tfAnnualInterestRate.setAlignment(Pos.BOTTOM_RIGHT);
		tfNumberOfYears.setAlignment(Pos.BOTTOM_RIGHT);
		tfLoanAmount.setAlignment(Pos.BOTTOM_RIGHT);
		tfMonthlyPayment.setAlignment(Pos.BOTTOM_RIGHT);
		tfTotalPayment.setAlignment(Pos.BOTTOM_RIGHT);
		tfMonthlyPayment.setEditable(false);
		tfTotalPayment.setEditable(false);
		GridPane.setHalignment(btCalculate, HPos.RIGHT);

		TextArea ta = new TextArea();
		gridPane.setHalignment(ta, HPos.RIGHT);
		Scene scene = new Scene(gridPane, 400, 250);
		primaryStage.setTitle("LoanCalculator"); // Set title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		btCalculate.setOnAction(a -> {
			try {
				// Get the values from the text field
				double AnnualInterestRate = Double.parseDouble(tfAnnualInterestRate.getText().trim());
				double Years = Double.parseDouble(tfNumberOfYears.getText().trim());
				double Loan = Double.parseDouble(tfLoanAmount.getText().trim());

				toServer.writeDouble(AnnualInterestRate);
				toServer.writeDouble(Years);
				toServer.writeDouble(Loan);
				toServer.flush();
				// Get area from the server
				double Monthly = fromServer.readDouble();
				double Total = fromServer.readDouble();
				// tfMonthlyPayment=Monthly;
				System.out.println(Monthly);
				tfMonthlyPayment.setText("" + Monthly);
				tfTotalPayment.setText("" + Total);

			} catch (Exception e) {
				System.err.println(e);
			}
		});
		try {
			// Create a socket to connect to the server
			Socket socket = new Socket("localhost", 8000);
			// Socket socket = new Socket("130.254.204.36", 8000);
			// Socket socket = new Socket("drake.Armstrong.edu", 8000);
			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());
			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			ta.appendText(e.toString() + '\n');
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void tfMonthlyPayment(double Monthly) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}
}
