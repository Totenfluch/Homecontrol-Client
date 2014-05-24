package me.Christian.networking;

import java.io.*;
import java.net.*;

import me.Christian.pack.Main;

public class Client implements Runnable
{
	public boolean IsConnectedToServer = false;
	public static String LatestServerReply = "";
	public boolean waitingforreply = false;
	public boolean disconnected = false;
	public boolean running = true;
	public Thread thread = null;

	public String format;
	public Socket socket;
	public DataOutputStream dout;
	public DataInputStream din;

	public Client( String host, int port ) {

		try {
			socket = new Socket( host, port );
			din = new DataInputStream( socket.getInputStream() );
			dout = new DataOutputStream( socket.getOutputStream() );
			IsConnectedToServer = true;
			thread = new Thread( this );
			thread.start();
		}catch( IOException ie ){ 		
			System.out.println("Couldn't connect to the Master Server.");
			System.exit(0);
			disconnected = true;
		}

		running = true;
	}


	public static void processMessage( String message ) {
			try {
				Main.connection.dout.writeUTF( Main.ComputerName + " " + message );
			} catch( Exception ie ){
				ie.printStackTrace();
				System.out.println( ie ); 
			}
		
	}

	public void run() {
		try {
			while (running) {
				if(IsConnectedToServer == true){
					String message = null;
					try{
						message = din.readUTF();
						LatestServerReply = message;
						GetServerMessages.CheckServerMessages(message);
						waitingforreply = false;
					}catch(Exception e){
						e.printStackTrace();
						IsConnectedToServer = false;
						disconnected = true;
					}
				}
			}
		}catch( Exception ie ){
			ie.printStackTrace();
			IsConnectedToServer = false;
			disconnected = true;
		} finally {
			try{
				if(dout != null){
					dout.close();
				}
				if(din != null){
					din.close();
				}
				if(socket != null){
					socket.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}