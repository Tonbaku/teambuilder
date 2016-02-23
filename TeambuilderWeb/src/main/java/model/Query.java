package model;

import java.util.HashSet;
import java.util.Set;

public class Query {
	private final String SELECT = "select ";
	private final String FROM = " from ";
	private final String WHERE = " where ";
	private final String GROUP = " group by ";
	private final String ORDER = " order by ";
	private final String JOIN = " inner join ";
	private final String ON = " on ";
	private final String AS = " as ";
	
	
	
	//Comparand values
	public static final String EQUALS = "=";
	public static final String OR = " OR ";
	
	//Tables
	public static final String POKEMON = "pokemon";
	public static final String ABILITIES = "abilities";
	public static final String ABILITYSET = "abilityset";
	public static final String MOVES = "moves";
	public static final String LEARNSET = "learnset";
	
	private String queryString = "";
	private String table = "";
	private String column = "";
	private String condition = "";
	private Set<String> joins = new HashSet<String>();
	
	public Query(String col, String table){
		this.table = table;
		this.column = col;
	}
	
	public Query(String col, String table, String conditionCol, String comparand ,String conditionValue){
		this.table = table;
		this.column = col;
		this.condition = conditionCol + comparand + "\""+ conditionValue + "\"";
	}
	
	public void addJoin(String table, String on){
		this.joins.add(this.JOIN + table + this.ON + on);
		
	}
	
	public void addWhereCondition(String where){
		this.condition = where;
	}
	
	public String getSQL(){
		buildQueryString();
		return queryString;
	}
	
	private void buildQueryString(){
		StringBuilder query = new StringBuilder();
		query.append(this.SELECT);
		query.append(this.column);
		query.append(this.FROM);
		query.append(this.table);
		for(String join : joins){
			query.append(join);
		}
		if(!this.condition.equals("")){
			query.append(this.WHERE);
			query.append(this.condition);
		}
		queryString = query.toString();
	}
	
	public void setTable(String table){
		this.table = table;
	}
}
