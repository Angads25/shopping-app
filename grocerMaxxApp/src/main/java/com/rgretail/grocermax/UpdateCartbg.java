package com.rgretail.grocermax;

import java.util.ArrayList;

public class UpdateCartbg {
	public ArrayList<String> alDeleteId;
	public boolean bLocally = false;            //true when move from cart screen to any screen till the result 
	                                            //not to come from server and set false in ConnectionServiceParser.
	private static UpdateCartbg instance = null;
	public static UpdateCartbg getInstance(){
		if(instance == null){
			instance = new UpdateCartbg();
		}
		return instance;
	}
	public UpdateCartbg(){
		instance = this;
	}
	
	
}
