package de.spinanddrain.supportchat.external.sql.overlay;

public class Table {

	/*
	 * Created by SpinAndDrain on 15.09.2019
	 */

	private String name;
	private Datatype[] datatype;
	
	public Table(String name, Datatype[] datatype) {
		this.name = name;
		this.datatype = datatype;
	}
	
	public String getName() {
		return name;
	}
	
	public Datatype[] getDatatypes() {
		return datatype;
	}
	
}
