package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Query;

public class LiteAccess {
	
	private Connection c;
	private Statement stmt;

	public LiteAccess(){
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:E:\\Program Files (x86)\\SQLite\\Pokemon.db");
			System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("You should probably fix the pom by adding the missing class.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Check the Database, maybe it's locked by another process?");
		}
		
	}
	
	public String getValueForSelect(String sql) {
		ResultSet rs;
		try {
			stmt.close();
			rs = stmt.executeQuery(sql);
			String ret = rs.getString(1);
			rs.next();
			stmt.close();
			stmt = c.createStatement();
			return ret;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public List<String> getValuesForSingleColumn(String sql) {
		List<String> values = new ArrayList<String>();
		ResultSet rs;
		try {
			stmt.close();
			stmt = c.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				values.add(rs.getString(1));
			}
			rs.close();
			stmt.close();
			stmt = c.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		return values;

	}
	
	public List<Integer> getIntValuesForSingleColumn(String sql) {
		List<Integer> values = new ArrayList<Integer>();
		ResultSet rs;
		try {
			stmt.close();
			stmt = c.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				values.add(rs.getInt(1));
			}
			rs.close();
			stmt.close();
			stmt = c.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return values;

	}
	
	public boolean doesPokemonExist(String name){
		ResultSet rs;
		try {
			Query q = new Query('"' + name + '"', Query.POKEMON, "name", Query.EQUALS, name);
			stmt.close();
			stmt = c.createStatement();
			rs = stmt.executeQuery(q.getSQL());
			boolean exists = rs.next();
			rs.close();
			stmt.close();
			stmt = c.createStatement();
			if(exists){
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public ResultSet getValuesForMultiColumn(String sql) {
		long start = System.currentTimeMillis();
		ResultSet rs = null;
		try {
			stmt.close();
			stmt = c.createStatement();
//			System.out.println(this.getClass() + ": CREATE-STATEMENT took " + (System.currentTimeMillis()-start) +"ms.");
			rs = stmt.executeQuery(sql);
//			System.out.println(this.getClass() + ": QUERY-EXECUTE took " + (System.currentTimeMillis()-start) +"ms. Executed: " + sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}

	public Connection getC() {
		return c;
	}

	public void setC(Connection c) {
		this.c = c;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

}
