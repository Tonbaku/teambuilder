package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import control.abstracts.AAccessUser;
import model.Pokemon;
import model.Move;
import model.Query;
import model.enums.MoveProperty;
import model.enums.SingleType;

public class MoveFactory extends AAccessUser {
	//TODO REMOVE! ONLY FOR DEBUG
	private int count = 1;
	public MoveFactory(){
		
	}
	
	public MoveFactory(LiteAccess lacc){
		super(lacc);
	}
	
	public Move createMove(String name){
		long start = System.currentTimeMillis();
		Move ret = new Move();
//		System.out.println(this.getClass() + ": MOVECREATION took " + (System.currentTimeMillis()-start) +"ms. " + count +". Move");
		fetchData(name, ret);
		count++;
		return ret;
	}
	
	private void fetchData(String name, Move target) {
		Query q = new Query("name, bp, type, category, sideeffect, contact, punch, bitemove, recoil, launch, multihit", Query.MOVES);
		q.addWhereCondition("name=\""+name+"\"");
		long start = System.currentTimeMillis();
		String sql = q.getSQL();
//		System.out.println(this.getClass() + ": SQL-CONVERSION took " + (System.currentTimeMillis()-start) +"ms. " + count +". Move");
		ResultSet rs = lacc.getValuesForMultiColumn(sql);
//		System.out.println(this.getClass() + ": SQL-QUERY took " + (System.currentTimeMillis()-start) +"ms. " + count +". Move");
		try {
			target.setName(rs.getString("name"));
			target.setBasePower(rs.getInt("bp"));
			target.setType(SingleType.instantiate(rs.getInt("type")));
			target.setSpectrum(rs.getString("category"));
			target.setProperties(MoveProperty.resolveMoveProperty(
					Boolean.parseBoolean(rs.getString("sideeffect")),
					Boolean.parseBoolean(rs.getString("contact")),
					Boolean.parseBoolean(rs.getString("punch")),
					Boolean.parseBoolean(rs.getString("bitemove")),
					Boolean.parseBoolean(rs.getString("recoil")),
					Boolean.parseBoolean(rs.getString("launch")),
					Boolean.parseBoolean(rs.getString("multihit"))
			));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		System.out.println(this.getClass() + ": DATAFETCH took " + (System.currentTimeMillis()-start) +"ms. " + count +". Move");
		
	}
	
	public List<String> getAvailableMoves(Pokemon p){
		List<String> ret;
		Query q = new Query(Query.MOVES + ".name", Query.MOVES);
		q.addJoin(Query.LEARNSET, Query.MOVES+".move_id="+Query.LEARNSET+".move_id");
		q.addJoin(Query.POKEMON, Query.POKEMON+".nummer="+Query.LEARNSET+".nummer");
		q.addWhereCondition(Query.POKEMON+".nummer" + Query.EQUALS + p.getNumber());
		ret = lacc.getValuesForSingleColumn(q.getSQL());
		return ret;
	}
	
	public List<String> getMetaMoves(Pokemon p){
		List<String> ret = new ArrayList<String>();
		
		return ret;
	}
	
	@Deprecated
	public List<Move> createDefaultMoveSet(){
		List<Move> ret = new ArrayList<Move>();
		
		Query q = new Query("name, bp, type, category, sideeffect, contact, punch, bitemove, recoil, launch, multihit", Query.MOVES);
		q.addWhereCondition("name=\"Scald\"" + Query.OR + "name=\"Icy Wind\"" + Query.OR + "name=\"Secret Sword\"" + Query.OR + "name=\"Hydro Pump\"");
		ResultSet rs = lacc.getValuesForMultiColumn(q.getSQL());
		try {
			while(rs.next()){
				Move m = new Move();
				m.setName(rs.getString("name"));
				m.setBasePower(rs.getInt("bp"));
				m.setType(SingleType.instantiate(rs.getInt("type")));
				m.setSpectrum(rs.getString("category"));
				m.setProperties(MoveProperty.resolveMoveProperty(
						Boolean.parseBoolean(rs.getString("sideeffect")),
						Boolean.parseBoolean(rs.getString("contact")),
						Boolean.parseBoolean(rs.getString("punch")),
						Boolean.parseBoolean(rs.getString("bitemove")),
						Boolean.parseBoolean(rs.getString("recoil")),
						Boolean.parseBoolean(rs.getString("launch")),
						Boolean.parseBoolean(rs.getString("multihit"))
				));
				ret.add(m);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	private void prepareMetdata(){
		
	}
}
