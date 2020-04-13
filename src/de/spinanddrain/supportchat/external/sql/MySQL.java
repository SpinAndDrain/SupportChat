package de.spinanddrain.supportchat.external.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.spinanddrain.supportchat.external.sql.exception.ConnectionFailedException;
import de.spinanddrain.supportchat.external.sql.exception.QueryException;
import de.spinanddrain.supportchat.external.sql.exception.WrongDatatypeException;
import de.spinanddrain.supportchat.external.sql.overlay.DataValue;
import de.spinanddrain.supportchat.external.sql.overlay.Datatype;
import de.spinanddrain.supportchat.external.sql.overlay.Table;

public class MySQL {

	/*
	 * Created by SpinAndDrain on 15.09.2019
	 */

	private String host;
	private String port;
	private String database;
	private String user;
	private String password;
	
	private Connection connection;
	
	public MySQL(String host, String port, String database, String user, String password) {
		super();
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
	}
	
	public void connect() throws ConnectionFailedException {
		if(!isConnected()) {
			try {
				connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+"?autoReconnect=true", user, password);
			} catch (SQLException e) {
				throw new ConnectionFailedException("MySQL Connection failed [UP]");
			}
		}
	}
	
	public void disconnect() throws ConnectionFailedException {
		if(isConnected()) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new ConnectionFailedException("MySQL Connection failed [DOWN]");
			}
		}
	}

	public boolean isConnected() {
		return (connection == null ? false : true);
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void createTable(Table table) throws QueryException {
		String query = "CREATE TABLE IF NOT EXISTS " + table.getName() + " (";
		Datatype[] types = table.getDatatypes();
		for(int i = 0; i < types.length; i++) {
			String add = null;
			if(types[i].getMax() == Datatype.NONE) {
				add = "";
			} else {
				add = "(" + types[i].getMax() + ")";
			}
			query += types[i].getName() + " " + types[i].getType() + add;
			if((i + 1) == types.length) {
				query += ")";
			} else
				query += ", ";
		}
		if(isConnected()) {
			try {
				connection.prepareStatement(query).executeUpdate();
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'createTable' -> Query: '" + query +"'");
			}
		}
	}
	
	public void createTableRaw(String query) throws QueryException {
		if(isConnected()) {
			try {
				connection.prepareStatement(query).executeUpdate();
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'createTable' -> Query: '" + query +"'");
			}
		}
	}
	
	public byte getByte(Table table, DataValue key, String value) throws WrongDatatypeException, QueryException {
		if(!(key.getValue() instanceof Byte)) {
			throw new WrongDatatypeException("'" + key.getValue() +"' is not a 'byte'");
		}
		if(isConnected()) {
			try {
				PreparedStatement ps = connection.prepareStatement("SELECT " + value + " FROM " + table.getName() + " WHERE " + key.getKey() + " = ?");
				ps.setByte(1, (byte) key.getValue());
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					return rs.getByte(value);
				}
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'getByte' -> No response");
			}
		}
		return 0;
	}
	
	public short getShort(Table table, DataValue key, String value) throws WrongDatatypeException, QueryException {
		if(!(key.getValue() instanceof Short)) {
			throw new WrongDatatypeException("'" + key.getValue() +"' is not a 'short'");
		}
		if(isConnected()) {
			try {
				PreparedStatement ps = connection.prepareStatement("SELECT " + value + " FROM " + table.getName() + " WHERE " + key.getKey() + " = ?");
				ps.setShort(1, (short) key.getValue());
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					return rs.getShort(value);
				}
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'getShort' -> No response");
			}
		}
		return 0;
	}
	
	public int getInt(Table table, DataValue key, String value) throws WrongDatatypeException, QueryException {
		if(!(key.getValue() instanceof Integer)) {
			throw new WrongDatatypeException("'" + key.getValue() +"' is not a 'int'");
		}
		if(isConnected()) {
			try {
				PreparedStatement ps = connection.prepareStatement("SELECT " + value + " FROM " + table.getName() + " WHERE " + key.getKey() + " = ?");
				ps.setInt(1, (int) key.getValue());
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					return rs.getInt(value);
				}
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'getInt' -> No response");
			}
		}
		return 0;
	}
	
	public long getLong(Table table, DataValue key, String value) throws WrongDatatypeException, QueryException {
		if(!(key.getValue() instanceof Long)) {
			throw new WrongDatatypeException("'" + key.getValue() +"' is not a 'long'");
		}
		if(isConnected()) {
			try {
				PreparedStatement ps = connection.prepareStatement("SELECT " + value + " FROM " + table.getName() + " WHERE " + key.getKey() + " = ?");
				ps.setLong(1, (long) key.getValue());
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					return rs.getLong(value);
				}
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'getLong' -> No response");
			}
		}
		return 0;
	}

	public String getString(Table table, DataValue key, String value) throws WrongDatatypeException, QueryException {
		if(!(key.getValue() instanceof String)) {
			throw new WrongDatatypeException("'" + key.getValue() +"' is not a 'String'");
		}
		if(isConnected()) {
			try {
				PreparedStatement ps = connection.prepareStatement("SELECT " + value + " FROM " + table.getName() + " WHERE " + key.getKey() + " = ?");
				ps.setString(1, (String) key.getValue());
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					return rs.getString(value);
				}
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'getString' -> No response");
			}
		}
		return null;
	}
	
	public boolean getBoolean(Table table, DataValue key, String value) throws WrongDatatypeException, QueryException {
		if(!(key.getValue() instanceof Boolean)) {
			throw new WrongDatatypeException("'" + key.getValue() +"' is not a 'boolean'");
		}
		if(isConnected()) {
			try {
				PreparedStatement ps = connection.prepareStatement("SELECT " + value + " FROM " + table.getName() + " WHERE " + key.getKey() + " = ?");
				ps.setBoolean(1, (boolean) key.getValue());
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					return rs.getBoolean(value);
				}
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'getBoolean' -> No response");
			}
		}
		return false;
	}
	
	public double getDouble(Table table, DataValue key, String value) throws WrongDatatypeException, QueryException {
		if(!(key.getValue() instanceof Double)) {
			throw new WrongDatatypeException("'" + key.getValue() +"' is not a 'double'");
		}
		if(isConnected()) {
			try {
				PreparedStatement ps = connection.prepareStatement("SELECT " + value + " FROM " + table.getName() + " WHERE " + key.getKey() + " = ?");
				ps.setDouble(1, (double) key.getValue());
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					return rs.getDouble(value);
				}
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'getDouble' -> No response");
			}
		}
		return 0;
	}
	
	public float getFloat(Table table, DataValue key, String value) throws WrongDatatypeException, QueryException {
		if(!(key.getValue() instanceof Float)) {
			throw new WrongDatatypeException("'" + key.getValue() +"' is not a 'float'");
		}
		if(isConnected()) {
			try {
				PreparedStatement ps = connection.prepareStatement("SELECT " + value + " FROM " + table.getName() + " WHERE " + key.getKey() + " = ?");
				ps.setFloat(1, (float) key.getValue());
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					return rs.getFloat(value);
				}
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'getFloat' -> No response");
			}
		}
		return 0;
	}
	
	public void update(Table table, DataValue key, Map<String, DataValue> values) throws WrongDatatypeException, QueryException {
		if(!(key.getKey() instanceof String)) {
			throw new WrongDatatypeException("The key at 'update' has to be a String!");
		}
		if(isConnected()) {
			try {
				for(String keys : values.keySet()) {
					PreparedStatement ps = connection.prepareStatement("UPDATE " + table.getName() + " SET " + keys + " = ? WHERE " + key.getValue() + " = ?");
					ps.setObject(1, values.get(keys));
					ps.setString(2, (String) key.getValue());
					ps.executeUpdate();
				}
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'update' -> No response");
			}
		}
	}
	
	public void insert(Table table, DataValue[] values) throws QueryException {
		if(isConnected()) {
			String query = "INSERT INTO " + table.getName() + " (";
			try {
				String q = "";
				for(int i = 0; i < values.length; i++) {
					query += values[i].getKey();
					q += "?";
					if((i + 1) == values.length) {
						query += ") VALUES (" + q + ")";
					} else {
						q += ",";
						query += ",";
					}
				}
				PreparedStatement ps = getConnection().prepareStatement(query);
				for(int i = 0; i < values.length; i++) {
					ps.setObject(i+1, values[i].getValue());
				}
				ps.executeUpdate();
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'insert' -> Query: '" + query + "'");
			}
		}
	}
	
	public boolean isRegistered(Table table, DataValue key, String anyColumn) throws WrongDatatypeException, QueryException {
		if(!(key.getKey() instanceof String)) {
			throw new WrongDatatypeException("The key at 'isRegistered' has to be a String!");
		}
		if(isConnected()) {
			try {
				PreparedStatement ps = connection.prepareStatement("SELECT " + anyColumn + " FROM " + table.getName() + " WHERE " + key.getKey() + " = ?");
				ps.setString(1, (String) key.getValue());
				ResultSet rs = ps.executeQuery();
				return rs.next();
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'isRegistered' -> No response");
			}
		}
		return false;
	}
	
	public void deleteAll(Table table, DataValue key) throws WrongDatatypeException, QueryException {
		if(!(key.getKey() instanceof String)) {
			throw new WrongDatatypeException("The key at 'deleteAll' has to be a String!");
		}
		if(isConnected()) {
			try {
				PreparedStatement ps = connection.prepareStatement("DELETE FROM " + table.getName() + " WHERE " + key.getKey() + " = ?");
				ps.setString(1, (String) key.getValue());
				ps.executeUpdate();
			} catch(SQLException e) {
				throw new QueryException("MySQL failed at 'deleteAll' -> No response");
			}
		}
	}
	
	public List<String> getStringifiedKeys(Table table, String key) {
		if(isConnected()) {
			try {
				PreparedStatement ps = connection.prepareStatement("SELECT " + key + " FROM " + table.getName());
				List<String> tmp = new ArrayList<>();
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					tmp.add(rs.getString(key));
				}
				return tmp;
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
