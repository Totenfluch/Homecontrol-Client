package me.Christian.networking;

import me.Christian.pack.OtherStuff;


public class GetServerMessages{
	public static String newestreply = null;


	public static void CheckServerMessages(String message){
		if(message.startsWith("/example")){

		}
		System.out.println(OtherStuff.TheNormalTime() + " SERVER: " + message);
	}
}
