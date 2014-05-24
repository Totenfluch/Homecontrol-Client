package me.Christian.pack;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import me.Christian.networking.Client;


public class Main {
	public static Client connection = null;
	public static String ActiveUser = "Root";
	public static InetAddress lComputerIP;
	public static Object ComputerMac;
	public static String ComputerName;
	public static String ComputerIP;

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

		ConnectToServer("192.168.178.44", 9977);
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

	public static boolean ConnectToServer(String ip, int port){
		try{
			connection = new Client(ip, port);
			Client.processMessage("/logconnect " + Main.ActiveUser);
		}catch(Exception e){
			e.printStackTrace();
		}

		return true;
	}
}
