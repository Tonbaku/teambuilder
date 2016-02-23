package model.enums;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public enum SingleType {
	NORMAL("normal", 1), WATER("water", 2), FIRE("fire", 3),
	GRASS("grass", 4), GROUND("ground", 5), ELECTRIC("electric", 6),
	FLYING("flying", 7), BUG("bug", 8), FIGHTING("fighting", 9),
	ICE("ice", 10), PSYCHIC("psychic", 11), DARK("dark", 12),
	DRAGON("dragon", 13), FAIRY("fairy", 14), GHOST("ghost", 15),
	POISON("poison", 16), ROCK("rock", 17), STEEL("steel", 18);
	
	private String name;
	private int id;
	
	SingleType(String name, int id){
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return this.name();
	};
	
	public static SingleType instantiate(String name){
		for(SingleType st : SingleType.values()){
			if(st.getName().equals(name)){
				return st;
			}
		}
		return null;
	}
	
	public static SingleType instantiate(int id){
		for(SingleType st : SingleType.values()){
			if(st.id == id){
				return st;
			}
		}
		return null;
	}
}
