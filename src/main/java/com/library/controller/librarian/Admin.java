package com.library.controller.librarian;

public class Admin {
	private String name = "123";
	private String pass = "123";
	
	public boolean loginConfirmed(String name,String pass){
		if(this.name.equals(name) && this.pass.equals(pass)){
			return true;
		}else{
			return false;
		}
	}
}
