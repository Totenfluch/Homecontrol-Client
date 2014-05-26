package me.Christian.pack;



import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


















import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import me.Christian.networking.Client;


public class Main extends Application{
	public static Client connection = null;
	public static String ActiveUser = "Root";
	public static InetAddress lComputerIP;
	public static Object ComputerMac;
	public static String ComputerName;
	public static String ComputerIP;
	public static Button s, sd;
	public static TextField entry;
	
	public static void main(String[] args){
		try {
			lComputerIP = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		ComputerMac = OtherStuff.getMacAdress();
		ComputerName = lComputerIP.getHostName();

		ActiveUser = ComputerName;

		ComputerIP = lComputerIP.getHostAddress();

		launch(args);
	}
	
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Homecontrol");
		primaryStage.setResizable(false);
		System.out.println("Loading: Done");
		Pane root = new Pane();
		

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);

		final TextField cmd = new TextField();
		cmd.setPromptText("Enter the CMD");
		cmd.setPrefColumnCount(10);
		cmd.getText();
		GridPane.setConstraints(cmd, 0, 0);
		grid.getChildren().add(cmd);

		final TextField params = new TextField();
		params.setPromptText("Enter the Params");
		GridPane.setConstraints(params, 0, 1);
		grid.getChildren().add(params);
		root.getChildren().add(grid);
		
		s = new Button("Send");
		s.setLayoutX(170);
		s.setLayoutY(20);
		root.getChildren().add(s);
		s.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        sd.setVisible(true);
		        s.setVisible(false);
		        if(cmd.getText() != "" && params.getText() != ""){
		        	ConnectToServer("192.168.178.38", 9977, cmd.getText(), params.getText());
		        	cmd.setText("");
		        	params.setText("");
		        }else{
		        	System.out.println("Error - one line is empty!");
		        }
		    }
		});
		
		sd = new Button("reset");
		sd.setLayoutX(170);
		sd.setLayoutY(20);
		root.getChildren().add(sd);
		sd.setVisible(false);
		sd.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        sd.setVisible(false);
		        s.setVisible(true);
		        DisconnectFromServer();
		    }
		});
		
		
		
		primaryStage.setScene(new Scene(root, 230, 70));
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});;
	}

	public static void DisconnectFromServer(){
		try {
			connection.din.close();
			connection.dout.close();
			connection.socket.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		connection.socket = null;
		connection.din = null;
		connection.dout = null;
		connection.thread = null;
		connection.running = false;
		connection = null;
	}

	public static boolean ConnectToServer(String ip, int port, String command, String params){
		try{
			connection = new Client(ip, port);
			Client.processMessage("/" + command + " " + Main.ActiveUser + " " + params);
		}catch(Exception e){
			e.printStackTrace();
		}

		return true;
	}
}
