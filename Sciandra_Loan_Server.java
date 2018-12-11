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
	This code is a mash up of Loan, LoanCalculator, and Server
	Java API:
		https://docs.oracle.com/javase/7/docs/api/java/io/ObjectInputStream.html
		https://docs.oracle.com/javase/8/docs/api/java/io/ObjectOutputStream.html
	Eclipse IDE:
		auto complete
		method header auto generation
*/

//Cadet Sciandra contributed 100% of this class
import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Sciandra_Loan_Server extends Application {
	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		// Text area for displaying contents
		TextArea ta = new TextArea();
		// Create a scene and place it in the stage
		Scene scene = new Scene(new ScrollPane(ta), 450, 200);
		primaryStage.setTitle("Server"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		// Make a thread to run everything in
		new Thread(() -> {
			try {
				// Create a server socket
				ServerSocket serverSocket = new ServerSocket(8000);
				Platform.runLater(() -> ta.appendText("Server started at " + new Date() + '\n'));
				// Listen for a connection request
				Socket socket = serverSocket.accept();
				// Create data input and output streams
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
				while (true) {
					double AnnualInterestRate = inputFromClient.readDouble();
					double Years = inputFromClient.readDouble();
					double Loan = inputFromClient.readDouble();

					// Compute Monthly and total values (from original loan class
					double Monthly = Loan * (AnnualInterestRate / 12)
							/ (1 - (Math.pow(1 / (1 + AnnualInterestRate / 12), Years * 12)));
					double Total = Monthly * Years * 12;
					// Send data back
					outputToClient.writeDouble(Monthly);
					outputToClient.writeDouble(Total);
					// Append the data back to the server log
					Platform.runLater(() -> {
						ta.appendText("Input received at " + System.currentTimeMillis() + "\n");
						ta.appendText("Annual Interest Rate received from client: " + AnnualInterestRate + '\n');
						ta.appendText("Years received from client: " + Years + '\n');
						ta.appendText("Annual Interest Rate received from client: " + Loan + '\n');

						ta.appendText("Monthly Rate is: " + Monthly + '\n');
						ta.appendText("Total is: " + Total + '\n');
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
