package me.Christian.pack;



import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	public static Button connect, disconnect, Send;
	public static TextField entry;
	public static ImageView connected,unconnected;
	public static TextField ipf, portf;
	public static String ConnectToIp = "192.168.178.38";
	public static int ConnectToPort = 9977;
	public static Label status;

	public static void main(String[] args){
		System.out.println(OtherStuff.TheNormalTime() + " CLIENT: Loading starts");
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
		primaryStage.setTitle("ConnectorZ");
		primaryStage.setResizable(false);
		Pane root = new Pane();
		primaryStage.getIcons().add(new Image("icon.png"));

		connected = new ImageView(new Image("connected.gif"));
		connected.fitHeightProperty();
		connected.fitWidthProperty();
		connected.setVisible(false);
		root.getChildren().add(connected);

		unconnected = new ImageView(new Image("unconnected.jpg"));
		unconnected.fitHeightProperty();
		unconnected.fitWidthProperty();
		root.getChildren().add(unconnected);

		status = new Label("");
		status.setLayoutX(170);
		status.setLayoutY(45);
		status.setStyle("-fx-text-fill: #FF0000;");
		status.setTooltip(new Tooltip("Try a different IP or check if the Sever is up"));
		root.getChildren().add(status);

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

		ipf = new TextField();
		ipf.setPromptText("Enter the Server IP");
		ipf.setText(ConnectToIp);
		GridPane.setConstraints(ipf, 0, 2);
		grid.getChildren().add(ipf);

		portf = new TextField();
		portf.setPromptText("Enter the Server Port");
		portf.setText(String.valueOf(ConnectToPort));
		GridPane.setConstraints(portf, 0, 3);
		grid.getChildren().add(portf);

		root.getChildren().add(grid);

		connect = new Button("Connect");
		connect.setLayoutX(170);
		connect.setLayoutY(20);
		root.getChildren().add(connect);
		connect.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				disconnect.setVisible(true);
				try{
					if(Integer.valueOf(portf.getText()) > 0 && Integer.valueOf(portf.getText()) < 65565){
						ConnectToPort = Integer.valueOf(portf.getText());
						ConnectToIp = ipf.getText();
					}

				String cmds = cmd.getText();
				String paramss = params.getText();
				ConnectToServer(ConnectToIp, ConnectToPort, cmds, paramss);
				cmd.setText("");
				params.setText("");
				}catch(Exception ex){
					System.out.println(OtherStuff.TheNormalTime() + " CLIENT: Invalid Port or IP");
				}
			}
		});

		disconnect = new Button("Disconnect");
		disconnect.setLayoutX(170);
		disconnect.setLayoutY(20);
		root.getChildren().add(disconnect);
		disconnect.setVisible(false);
		disconnect.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				disconnect.setVisible(false);
				connect.setVisible(true);
				DisconnectFromServer();
			}
		});

		Send = new Button("Send");
		Send.setLayoutX(170);
		Send.setLayoutY(80);
		root.getChildren().add(Send);
		Send.setDisable(true);
		Send.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				String cmds = cmd.getText();
				String paramss = params.getText();
				Client.processMessage("/" + cmds + " " + paramss);
				cmd.setText("");
				params.setText("");
			}
		});


		primaryStage.setScene(new Scene(root, 240, 130));
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});;
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
			}
		}.start();
		
		System.out.println(OtherStuff.TheNormalTime() + " CLIENT: Loading done");
		primaryStage.show();
	}

	protected void update() {
		if(!Client.IsConnectedToServer && connection != null){
			DisconnectFromServer();
			status.setText("Failed");
			System.out.println(OtherStuff.TheNormalTime()+" CLIENT: Lost connection / Host unreachable");
		}
	}

	public static void DisconnectFromServer(){
		connected.setVisible(false);
		unconnected.setVisible(true);
		connect.setVisible(true);
		disconnect.setVisible(false);
		Send.setDisable(true);
		ipf.setDisable(false);
		portf.setDisable(false);
		try {
			connection.din.close();
			connection.dout.close();
			connection.socket.close();
		}catch (Exception e) {}
		try{connection.socket = null;}catch(Exception e){}
		try{connection.din = null;}catch(Exception e){}
		try{connection.dout = null;}catch(Exception e){}
		try{connection.thread = null;}catch(Exception e){}
		try{connection.running = false;}catch(Exception e){}
		try{connection = null;}catch(Exception e){}
	}

	public static boolean ConnectToServer(String ip, int port, String command, String params){
		try{
			connection = new Client(ip, port);
			if(!command.equals("")){
				Client.processMessage("/" + command + " " + params);
			}
			unconnected.setVisible(false);
			connected.setVisible(true);
			Send.setDisable(false);
			ipf.setDisable(true);
			portf.setDisable(true);
			status.setText("");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
}
