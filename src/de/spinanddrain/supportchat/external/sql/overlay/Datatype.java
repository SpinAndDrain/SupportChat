package de.spinanddrain.supportchat.external.sql.overlay;

public class Datatype {

	/*
	 * Created by SpinAndDrain on 15.09.2019
	 */

	/**
	 *  *** STOREROOM ***
	 */
	
	public enum Type {
		TINYINT(),
		SMALLINT(),
		MEDIUMINT(),
		INT(),
		BIGINT(),
		DECIMAL(),
		FLOAT(),
		DOUBLE(),
		BIT(),
		CHAR(),
		VARCHAR(),
		BINARY(),
		VARBINARY(),
		TINYBLOB(),
		BLOB(),
		MEDIUMBLOB(),
		LONGBLOB(),
		TINYTEXT(),
		TEXT(),
		MEDIUMTEXT(),
		LONGTEXT(),
		ENUM(),
		SET(),
		DATE(),
		TIME(),
		DATETIME(),
		TIMESTAMP(),
		YEAR(),
		GEOMETRY(),
		POINT(),
		LINESTRING(),
		POLYGON(),
		GEOMETRYCOLLECTION(),
		MULTILINESTRING(),
		MULTIPOINT(),
		MULTIPOLYGON(),
		BOOLEAN();
	}
	
	public static final int NONE = -1;
	
	private String name;
	private Type type;
	private int max;
	
	public Datatype(Type type, String name, int max) {
		this.name = name;
		this.type = type;
		this.max = max;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}

	public int getMax() {
		return max;
	}
	
}
