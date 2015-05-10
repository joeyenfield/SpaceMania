package com.emptypockets.spacemania.command;

public abstract class Command {
	String name;
	String description;
	
	public Command(String name){
		this(name,"");
	}
	public Command(String name, String description){
		this.name= name;
		this.description= description;
	}
	
	public abstract void exec(String args);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
