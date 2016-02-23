package control;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import control.abstracts.AAccessUser;
import model.Ability;
import model.Move;
import model.Pokemon;
import model.Query;
import model.ShedinjaStats;
import model.Stat;
import model.Type;
import model.enums.SingleType;

public class PokemonFactory extends AAccessUser {
	
	public PokemonFactory(){
		
	}
	
	public PokemonFactory(LiteAccess lacc){
		super(lacc);
	}
	
	public Pokemon createPokemon(String name){
		Pokemon ret = new Pokemon();
		
		ret.setName(name);
		fetchData(name, ret);
		ret.setMoves(new ArrayList<Move>());
		
		return ret;
	}

	private void fetchData(String name, Pokemon p) {
		Query q;
		if(name.startsWith("Mega ")){
			System.out.println(name);
			q = new Query("form_id, type1, type2, kp, atk, def, satk, sdef, spd, base_id", "altforms", "name", Query.EQUALS, name);
		} else {
			q = new Query("nummer, type1, type2, kp, atk, def, satk, sdef, spd", Query.POKEMON, "name", Query.EQUALS, name);
		}
		
		ResultSet rs = lacc.getValuesForMultiColumn(q.getSQL());
		try {
			int type1 = rs.getInt("type1");
			int type2 = rs.getInt("type2");
			int nummer;
			if(!name.startsWith("Mega ")){
				nummer = rs.getInt("nummer");
			} else {
				nummer = (rs.getInt("base_id"));
			}
			int kp = rs.getInt("kp");
			int atk = rs.getInt("atk");
			int def = rs.getInt("def");
			int satk = rs.getInt("satk");
			int sdef = rs.getInt("sdef");
			int spd = rs.getInt("spd");
			
			p.setType(getType(type1, type2));
			p.setNumber(nummer);
			
			if(name.equals("Shedinja")){
				p.setStats(getStats(kp, atk, def, satk, sdef, spd, ShedinjaStats.class));
			} else {
				p.setStats(getStats(kp, atk, def, satk, sdef, spd, Stat.class));
			}
			p.setAbilities(getAbilities(name));
			//preset ability to first entry
			p.setAbility(p.getAbilities().iterator().next().getName());
			
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
	}

	private List<Stat> getStats(int kp, int atk, int def, int satk, int sdef, int spd, Class<? extends Stat> statclass) throws SQLException {
		ArrayList<Stat> ret = new ArrayList<Stat>();
		try {
			ret.add(statclass.getDeclaredConstructor(String.class, Integer.TYPE).newInstance("kp", kp));
			ret.add(statclass.getDeclaredConstructor(String.class, Integer.TYPE).newInstance("atk", atk));
			ret.add(statclass.getDeclaredConstructor(String.class, Integer.TYPE).newInstance("def", def));
			ret.add(statclass.getDeclaredConstructor(String.class, Integer.TYPE).newInstance("satk", satk));
			ret.add(statclass.getDeclaredConstructor(String.class, Integer.TYPE).newInstance("sdef", sdef));
			ret.add(statclass.getDeclaredConstructor(String.class, Integer.TYPE).newInstance("spd", spd));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private Type getType(int type1, int type2) throws SQLException {
		SingleType t1 = SingleType.instantiate(type1);
		SingleType t2 = SingleType.instantiate(type2);
		Type ret = new Type(t1, t2);
		this.getResistances(ret);
		this.getWeaknesses(ret);
		ret.matchRelations();
		return ret;
	}

	private Set<Ability> getAbilities(String name) throws SQLException {
		Set<Ability> ret = new HashSet<Ability>();
		Query q;
		if(name.startsWith("Mega ")){
			q = new Query("name", Query.ABILITIES);
			q.addWhereCondition(Query.ABILITIES+".ability_id=(select ability_id from altforms where name=\""+name+"\")");
		} else {
			q = new Query(Query.ABILITIES+".name", Query.ABILITIES, Query.POKEMON+".name", Query.EQUALS, name);
			q.addJoin(Query.ABILITYSET, Query.ABILITYSET+".ability_id="+Query.ABILITIES+".ability_id");
			q.addJoin(Query.POKEMON, Query.POKEMON+".nummer="+Query.ABILITYSET+".nummer");
		}
		
		for(String ability : lacc.getValuesForSingleColumn(q.getSQL())){
			Ability a = new Ability();
			a.setName(ability);
			ret.add(a);
		}
		return ret;
	}
	
	private void getWeaknesses(Type t){
		for(SingleType st : t){
			if(st==null){
				continue;
			}
			for(int weak : lacc.getIntValuesForSingleColumn(
					  "select tc.attacking "
					+ "from type as t inner join typechart as tc "
						+ "on t.type_id=tc.defending "
					+ "where tc.efficiency=2 "
						+ "and t.name=\"" + st.getName().toLowerCase()
					+ "\";"
			)){
				List weaks;
				if((weaks = t.getWeaknesses()).contains(weak)){
					weaks.remove((Integer)weak);
					t.getDoubleWeaknesses().add(weak);
				} else {
					weaks.add(weak);
				}
			};
		}
	}
	
	private void getResistances(Type t){
		for(SingleType st : t){
			if(st==null){
				continue;
			}
			for(int resist : lacc.getIntValuesForSingleColumn(
					  "select tc.attacking "
					+ "from type as t inner join typechart as tc "
						+ "on t.type_id=tc.defending "
					+ "where tc.efficiency=0.5 "
						+ "and t.name=\"" + st.getName().toLowerCase()
					+ "\";"
			)){
				List resists;
				if((resists = t.getResistances()).contains(resist)){
					resists.remove((Integer)resist);
					t.getDoubleResistances().add(resist);
				} else {
					resists.add(resist);
				}
			};
		}
	}
}
