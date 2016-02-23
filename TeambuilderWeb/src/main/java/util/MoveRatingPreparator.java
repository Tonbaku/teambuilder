package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import control.DamageResolver;
import control.LiteAccess;
import control.MoveFactory;
import control.PokemonFactory;
import model.Ability;
import model.Move;
import model.Pokemon;
import model.Query;
import model.Rating;

public class MoveRatingPreparator {
	
	public static void main(String args[]){
		long start = System.currentTimeMillis();
		MoveRatingPreparator mrp = new MoveRatingPreparator();
		List<Rating> allRatings = new LinkedList<Rating>();
		LiteAccess lacc = new LiteAccess();
		DamageResolver damRes = new DamageResolver();
		PokemonFactory pf = new PokemonFactory(lacc);
		MoveFactory mf = new MoveFactory(lacc);

		Query q = new Query("name", "pokemon");
		List<String> allPokemonNames = lacc.getValuesForSingleColumn(q.getSQL());
		List<String> megaPokemon = lacc.getValuesForSingleColumn(new Query("name", "altforms").getSQL());
		allPokemonNames.addAll(megaPokemon);
		Pokemon pokemonBuffer;
		long lastTime = 0;
		long lastTimeCreate = 0;
		long lastTimeRate = 0;
		
		int count = 1;
		
		for(String pokemonName : allPokemonNames){
			long startPokemon = System.currentTimeMillis();
			pokemonBuffer = pf.createPokemon(pokemonName);
			List<String> moveList = mf.getAvailableMoves(pokemonBuffer);
			for(Ability ability : pokemonBuffer.getAbilities()){
				long startAbility = System.currentTimeMillis();
				pokemonBuffer.setAbility(ability);
				Move moveBuffer;
				for(String moveName : moveList){
					long startMove = System.currentTimeMillis();
					moveBuffer = mf.createMove(moveName);
					long createTime = System.currentTimeMillis()-startMove;
					if(createTime>lastTimeCreate){
//						System.out.println(MoveRatingPreparator.class + ":CREATE took " + (createTime-lastTimeCreate) + "ms longer than before. Peak: " + createTime);
						lastTimeCreate = createTime;
					}
//					System.out.println(MoveRatingPreparator.class + ":CREATE took " + createTime + "ms. " + count +". Entry");
					long startRate = System.currentTimeMillis();
					Rating r = damRes.rateMovePresence(pokemonBuffer, moveBuffer);
					long rateTime = System.currentTimeMillis()-startRate;
					if(rateTime>lastTimeRate){
//						System.out.println(MoveRatingPreparator.class + ":RATE took " + (rateTime-lastTimeRate) + "ms longer than before. Peak: " + rateTime);
						lastTimeRate = rateTime;
					}
//					System.out.println(MoveRatingPreparator.class + ":RATE took " + rateTime + "ms. " + count +". Entry");
					long startAdd = System.currentTimeMillis();
					allRatings.add(r);
					
					long addTime = System.currentTimeMillis()-startAdd;
					if(addTime>lastTime){
//						System.out.println(MoveRatingPreparator.class + ":ADD took " + (addTime-lastTime) + "ms longer than before. Peak: " + addTime);
						lastTime = addTime;
					}
//					System.out.println(MoveRatingPreparator.class + ":ADD took " + addTime + "ms.");
//					System.out.println(MoveRatingPreparator.class + ": Entry " + count + "done.");
					count++;
					
//					System.out.println(MoveRatingPreparator.class + ":####MOVE#### took " + (System.currentTimeMillis()-startMove) +"ms.");
				}
//				System.out.println(MoveRatingPreparator.class + ":######ABILITY###### "+count+". took " + (System.currentTimeMillis()-startAbility) +"ms. (" + moveList.size() + ")");
			}
//			System.out.println(MoveRatingPreparator.class + ":########Pokemon######## took " + (System.currentTimeMillis()-startPokemon) +"ms.");
		}
		allRatings.sort(mrp.new RatingComparator<Rating>());
		File f = new File("Ratings.txt");
		f.delete();
		try {
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			int i = allRatings.size();
			for(Rating rating : allRatings){
				fw.append(i +". : ");
				fw.append(rating.getRateText());
				fw.append("\n");
				i--;
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Took " + (System.currentTimeMillis()-start) + "ms or ~" + (System.currentTimeMillis()-start)/721 +"ms per Pokemon. (Way to long)");
	}
	
	class RatingComparator<T> implements Comparator<Rating>{

		public int compare(Rating r1, Rating r2) {
			if(r1.getRateValue()>r2.getRateValue()){
				return 1;
			} else if(r1.getRateValue()<r2.getRateValue()){
				return -1;
			} else {
				return 0;
			}
		}
		
	}
}
