package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBInserter {
	
	public static Connection c;
	public static Statement stmt;
	public static String currentNumber;
	public static HashMap<String, List<String>> megaAbilities;
	private static String currentName;
	
	
	public static void main(String args[]) throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:E:\\Program Files (x86)\\SQLite\\Pokemon.db");
			System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
			String sql = "drop table if exists Moves; CREATE TABLE Moves "
					+ "(move_id INT PRIMARY KEY			NOT NULL,"
					+ " name				char(20)	NOT NULL, " 
					+ " type				int			NOT NULL, "
					+ " pp            		int     	NOT NULL, "
					+ " bp        			int 		NOT NULL, "
					+ " acc         		int			NOT NULL, "
					+ " category			char(20)	NOT NULL, "
					+ " sideeffect			boolean		NOT NULL, "
					+ " priority			int			NOT NULL, "
					+ " contact				boolean		NOT NULL, "
					+ " sound				boolean		NOT NULL, "
					+ " punch				boolean		NOT NULL, "
					+ " bitemove			boolean		NOT NULL, "
					+ " recoil				boolean 	NOT NULL, "
					+ " launch				boolean 	NOT NULL,"
					+ " multihit			boolean		NOT NULL);";
			//this results to the pattern "name|type|spectrum|pp|basepower|accuracy|sideefect|priority|contact|sound|punch\n"
			stmt.executeUpdate(sql);
			for(String insert : insertMoveValuesFromFile()){
				sql = insert;
				stmt.executeUpdate(sql);
			}
			System.out.println("MOVE INSERTION DONE!");
			stmt.close();
			insertAbilities();
			
			sql = "drop table if exists AbilitySet;"
					+ " create table AbilitySet "
					+ "(nummer			int not null,"
					+ "ability_id		int not null,"
					+ "primary key(nummer, ability_id));";
			
			stmt.close();
			stmt = c.createStatement();
			
			stmt.executeUpdate(sql);
			
			sql = "drop table if exists Pokemon; CREATE TABLE Pokemon "
					+ "(nummer INT PRIMARY KEY	NOT NULL,"
					+ " NAME			char(20)NOT NULL, " 
					+ " KP				int		NOT NULL, "
					+ " ATK            	INT     NOT NULL, "
					+ " DEF        		int 	NOT NULL, "
					+ " SATK         	int		NOT NULL, "
					+ " SDEF			int 	NOT NULL, " 
					+ " SPD				int 	NOT NULL,"
					+ " Type1			int 	NOT NULL,"
					+ " Type2			int		);";
			stmt.close();
			stmt = c.createStatement();
			
			stmt.executeUpdate(sql);
			sql = "drop table if exists Learnset;"
					+ " create table Learnset "
					+ "(nummer			int not null,"
					+ "move_id			int not null,"
					+ "primary key(nummer, move_id));";
			System.out.println(sql);
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			for(String insert : insertPokemonValuesFromFile()){
				System.out.println(insert);
				String moves = insert.split("INSERT INTO Learnset \\(nummer, move_id\\) VALUES")[1];
				Pattern p = Pattern.compile("\\(([0-9]{3}), ([0-9]{1,3})\\),");
				Matcher mat = p.matcher(moves);
				List<String> l = new ArrayList<String>();
				while(mat.find()){
					if(l.contains(mat.group(2)))
						System.err.println("ERROR AT " + mat.group(2) + " DUPLICATE");
					else
						l.add(mat.group(2));
				}
				sql = insert;
				stmt = c.createStatement();
				stmt.executeUpdate(sql);
			}
			
			insertMegas();
			
			
		    c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		} 
		
	}
	
	private static Set<String> insertMoveValuesFromFile() throws IOException, SQLException {
		Set<String> sqlInserts = new HashSet<String>();
		StringBuilder sqlInsert = new StringBuilder();
		File f = new File("movestest.txt");
		if(!f.exists()){
			return new HashSet<String>();
		}
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String lineContent = null;
		while ((lineContent = br.readLine()) != null) {
			int index = 0;
			String[] lineparts = lineContent.split("\\|");
			sqlInsert.append("INSERT INTO Moves (move_id, name, type, category, pp, bp, acc, sideeffect, priority, contact, sound, punch, bitemove,"
					+ "recoil, launch, multihit) VALUES (");
			boolean defaultMove = false;
			for (String linepart : lineparts) {
				//TODO flag for multihit-moves
				//TODO flag for recoil moves
				//TODO flag for punch moves
				//TODO flag for bite moves
				//TODO flag for contact moves
				//621 | Stored Power | psychic | special | 10 | 20 | 100
				switch (index) {
					// move_id
					case (0):
						sqlInsert.append(linepart.trim() + ", ");
						break;
					// name
					case (1):
						sqlInsert.append("\"" +linepart.trim() + "\", ");
						break;
					// type
					case (2):
						Statement stmt = c.createStatement();
				      	ResultSet rs = stmt.executeQuery( "SELECT type_id FROM type WHERE name=\""+ linepart.trim() +"\";" );
						sqlInsert.append(rs.getString(1) + ", ");
						rs.close();
						stmt.close();
						break;
					// category
					case (3):
						sqlInsert.append("\"" +linepart.trim() + "\", ");
						break;
					// pp
					case (4):
						sqlInsert.append(linepart.trim() + ", ");
						break;
					// bp
					case (5):
						sqlInsert.append(linepart.trim() + ", ");
						break;
					// acc
					case (6):
						sqlInsert.append(linepart.trim() + ", ");
						break;
					//sideeffect
					case (7):
						
						sqlInsert.append("\""+linepart.trim() + "\", ");
						break;
					//priority
					case (8):
						try {
							int priorityLevel = Integer.parseInt(linepart.trim());
							sqlInsert.append(priorityLevel + ", ");
						} catch (NumberFormatException ex){
							defaultMove = true;
						}
						break;
					//contact
					case (9):
						sqlInsert.append("\""+linepart.trim() + "\", ");
						break;
					//sound
					case (10):
						sqlInsert.append("\""+linepart.trim() + "\", ");
						break;
					//punch
					case (11):
						sqlInsert.append("\""+linepart.trim() + "\", ");
						break;
					//bite
					case (12):
						sqlInsert.append("\""+linepart.trim() + "\", ");
						break;
					//recoil
					case (13):
						sqlInsert.append("\""+linepart.trim() + "\", ");
						break;
					//launch
					case (14):
						sqlInsert.append("\""+linepart.trim() + "\", ");
						break;
					//multihit
					case (15):
						sqlInsert.append("\""+linepart.trim() + "\"");
						break;
				}
				if(defaultMove){
					defaultMove = false;
					sqlInsert.append("0, \"false\", \"false\", \"false\", \"false\", \"false\", \"false\", \"false\"");
					index = 0;
					break;
				}
				if (index == lineparts.length) {
					index = 0;
				} else {
					index++;
				}
			}
			
			sqlInsert.append(");");

			sqlInserts.add(sqlInsert.toString());
			sqlInsert = new StringBuilder();
		}
		return sqlInserts;
	}

	private static Set<String> insertPokemonValuesFromFile() throws IOException, SQLException{
		Set<String> sqlInserts = new HashSet<String>();
		StringBuilder sqlInsert = new StringBuilder();
		fillMegaAbilities();
		File f = new File("stats.txt");
		if(!f.exists()){
			return new HashSet<String>();
		}
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String lineContent = null;
		while ((lineContent = br.readLine()) != null) {
			int index = 0;
			String[] lineparts = lineContent.split("\\|");
			sqlInsert.append("INSERT INTO Pokemon (NAME, nummer, KP,ATK,DEF,SATK,SDEF,SPD,Type1,Type2) VALUES (");
			handleMisc(lineparts[0].trim(), sqlInsert);
			handleStats(lineparts[1].trim(), sqlInsert);
			handleType(lineparts[2].trim(), sqlInsert);
			sqlInsert.append(");");
			handleMoves(lineparts[3].trim(), sqlInsert);
			handleAbilities(lineparts[4].trim(), sqlInsert);

			sqlInserts.add(sqlInsert.toString());
			sqlInsert = new StringBuilder();
		}
		return sqlInserts;
	}

	private static void fillMegaAbilities() throws SQLException, IOException {
		megaAbilities = new HashMap<String, List<String>>();
		File f = new File("megas.txt");
		if(!f.exists()){
			return;
		}
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String lineContent = null;
		while ((lineContent = br.readLine()) != null) {
			int index = 0;
			String[] lineparts = lineContent.split("\\|");
			//Beedrill  | Adaptability | 65 | 150 | 40 | 15 | 80 | 145
			String name = lineparts[0].split(" ")[0];
			if(megaAbilities.containsKey(name)){
				List<String> abilities = megaAbilities.get(name);
				abilities.add(lineparts[1].trim());
				megaAbilities.put(name.trim(), abilities);
			} else {
				ArrayList<String> ability = new ArrayList<String>();
				ability.add(lineparts[1].trim());
				megaAbilities.put(name.trim(), ability);
			}
			
		}
	}
		
	private static void insertMegas() throws SQLException, IOException{
		stmt = c.createStatement();
		String sql = "drop table if exists AltForms;"
				 	+ "CREATE TABLE AltForms "
					+ "(form_id 	INTEGER PRIMARY KEY	AUTOINCREMENT	NOT NULL,"
					+ " NAME			char(20)NOT NULL, " 
					+ " KP				int		NOT NULL, "
					+ " ATK            	INT     NOT NULL, "
					+ " DEF        		int 	NOT NULL, "
					+ " SATK         	int		NOT NULL, "
					+ " SDEF			int 	NOT NULL, " 
					+ " SPD				int 	NOT NULL,"
					+ " Type1			int 	NOT NULL,"
					+ " Type2			int		,"
					+ " ability_id		int		NOT NULL,"
					+ " base_id			int		NOT NULL);";
		stmt.executeUpdate(sql);
		
		File f = new File("megas.txt");
		FileReader fr;
		try {
			fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String readBuffer;
			String insert;
			ResultSet rs;
			while((readBuffer = br.readLine())!=null){
				//readBuffer -> Beedrill  | Adaptability | bug # poison #  | 65 | 150 | 40 | 15 | 80 | 145
				String[] parts = readBuffer.split("\\|");
				insert = "INSERT INTO AltForms (name, kp, atk, def, satk, sdef, spd, type1, type2, ability_id, base_id) VALUES (\""
						+ "Mega " + parts[0].trim() + "\", \"";
				
				
				//kp
				insert += parts[3].trim() + "\", \"";
				//atk
				insert += parts[4].trim() + "\", \"";
				//def
				insert += parts[5].trim() + "\", \"";
				//spa
				insert += parts[6].trim() + "\", \"";
				//spdef
				insert += parts[7].trim() + "\", \"";
				//speed
				insert += parts[8].trim() + "\", \"";
				
				//types
				for(String type : parts[2].split(" # ")){
					if(!type.trim().equals("")){
						stmt = c.createStatement();
						rs = stmt.executeQuery( "SELECT type_id FROM type WHERE name=\""+ type.toLowerCase().trim() +"\";" );
				      	insert+=rs.getString("type_id") + "\", \"";
				      	rs.close();
				      	stmt.close();
					}
				}
				if(parts[2].split(" # ").length==2){
					insert+="null\", \"";
				}
				
				stmt = c.createStatement();
				rs = stmt.executeQuery( "SELECT ability_id FROM abilities WHERE name=\""+ parts[1].trim() +"\";" );
				System.out.println("SELECT ability_id FROM abilities WHERE name=\""+ parts[1].trim() +"\";");
		      	String abilityId = rs.getString("ability_id");
		      	rs.close();
		      	stmt.close();
		      	
		      	insert += abilityId;
		      	insert += "\"";
		      	
		      	stmt = c.createStatement();
		      	rs = stmt.executeQuery( "SELECT nummer FROM pokemon WHERE name=\""+ parts[0].trim().split(" ")[0] +"\";" );
		      	System.out.println("SELECT nummer FROM pokemon WHERE name=\""+ parts[0].trim().split(" ")[0] +"\";" );
		      	String base_id = rs.getString("nummer");
		      	rs.close();
		      	stmt.close();
		      	
		      	insert += ",\"";
		      	insert += base_id;
		      	insert += "\");";
				stmt = c.createStatement();
				System.err.println(insert);
				stmt.executeUpdate(insert);
				stmt.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void handleType(String data, StringBuilder sqlInsert) throws SQLException {
		for(String linepart : data.trim().split(" / ")){
			sqlInsert.append(", ");
	      	ResultSet rs = stmt.executeQuery( "SELECT type_id FROM type WHERE name=\""+ linepart.toLowerCase().trim() +"\";" );
			sqlInsert.append(rs.getString(1));
			rs.close();
			
		}
		if(data.trim().split(" / ").length == 1){
			sqlInsert.append(", null");
		}
	}

	private static void handleStats(String data, StringBuilder sqlInsert) {
		int index = 0;
		for (String linepart : data.trim().split(" ")) {
			// Bulbasaur - #001 - Serebii.net Pokédex | KP 45 Angriff 49 Vert 49
						// SpA 65 SpV 65 Init 45 | Types grass / poison
			switch (index) {
				// KP
				case (1):
					sqlInsert.append(linepart + ", ");
					break;
				// Angriff
				case (3):
					sqlInsert.append(linepart + ", ");
					break;
				// Vert
				case (5):
					sqlInsert.append(linepart + ", ");
					break;
				// SpA
				case (7):
					sqlInsert.append(linepart + ", ");
					break;
				// SpV
				case (9):
					sqlInsert.append(linepart + ", ");
					break;
				// SpD
				case (11):
					sqlInsert.append(linepart);
					break;
				//reset or increment index
				
			}
			if (index == data.trim().split(" ").length) {
				index = 0;
			} else {
				index++;
			}
		}
		
	}

	private static void handleMisc(String data, StringBuilder sqlInsert) {
		int index = 0;
		for (String linepart : data.trim().split(" - ")) {
			switch(index){
				//name
				case(0): currentName=linepart.trim();sqlInsert.append("\"" + linepart + "\", ");break;
				//number
				case(1): currentNumber=linepart.substring(1, linepart.length());sqlInsert.append(currentNumber+ ", ");break;
			}
			if (index == data.trim().split(" ").length) {
				index = 0;
			} else {
				index++;
			}
		}
	}
	
	private static void handleAbilities(String data, StringBuilder sqlInsert) throws SQLException {
		sqlInsert.append("INSERT INTO AbilitySet (nummer, ability_id) VALUES ");
		boolean first = true;
		for (String linepart : data.trim().split(" # ")) {
			if(megaAbilities.containsKey(currentName)){
				if(megaAbilities.get(currentName).contains(linepart.trim())){
					megaAbilities.get(currentName).remove(linepart.trim());
					continue;
				}
			}
			
			if(!first){
				sqlInsert.append(", "); 
			} else {
				first = false;
			}
			
	      	ResultSet rs = stmt.executeQuery( "SELECT ability_id FROM abilities WHERE name=\""+ linepart.trim() +"\";" );
	      	System.out.println( "HandleAbilities: SELECT ability_id FROM abilities WHERE name=\""+ linepart.trim() +"\";" );
			sqlInsert.append("(" +currentNumber + ", " + rs.getString(1) +")");
			rs.close();
		}
		sqlInsert.append(";");
		System.out.println("End of HandleAbilities(single pokemon):" + sqlInsert.toString());
	}
	
	private static void handleMoves(String data, StringBuilder sqlInsert) throws SQLException {
		sqlInsert.append("INSERT INTO Learnset (nummer, move_id) VALUES ");
		boolean first = true;
		for (String linepart : data.trim().split(" - ")) {
			if(!first){
				sqlInsert.append(", "); 
			} else {
				first = false;
			}
	      	ResultSet rs = stmt.executeQuery( "SELECT move_id FROM moves WHERE name=\""+ linepart.trim() +"\";" );
	      	System.out.println( "HandleMoves: SELECT move_id FROM moves WHERE name=\""+ linepart.trim() +"\";" );
			sqlInsert.append("(" +currentNumber + ", " + rs.getString(1) +")");
			rs.close();
		}
		sqlInsert.append(";");
		System.out.println("End of HandleMoves(single pokemon):" + sqlInsert.toString());
	}
	
	private static void insertAbilities() throws SQLException, IOException{
		stmt = c.createStatement();
		String sql = "drop table if exists Abilities; CREATE TABLE Abilities "
				+ "(ability_id 	INTEGER PRIMARY KEY	AUTOINCREMENT	NOT NULL,"
				+ " name		char(20)							NOT NULL);";
		stmt.executeUpdate(sql);
		
		File f = new File("abilities.txt");
		FileReader fr;
		try {
			fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			StringBuilder moveBuffer = new StringBuilder();
			int readBuffer;
			String insert;
			while((readBuffer = br.read()) != -1){
				if(readBuffer != '|'){
					moveBuffer.append((char)readBuffer);
				} else {
					
					insert = "INSERT INTO Abilities (name) VALUES (\"" + moveBuffer.toString().trim() + "\")";
					moveBuffer = new StringBuilder();
					stmt = c.createStatement();
					stmt.executeUpdate(insert);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
