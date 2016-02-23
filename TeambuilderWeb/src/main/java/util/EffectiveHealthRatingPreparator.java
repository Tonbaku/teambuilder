package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import control.DamageResolver;
import control.LiteAccess;
import control.MoveFactory;
import control.PokemonFactory;
import model.Ability;
import model.EffectiveHealthRating;
import model.Move;
import model.Pokemon;
import model.Query;
import model.Rating;
import model.enums.Spectrum;

public class EffectiveHealthRatingPreparator {
	
	public static void main(String args[]){
		long start = System.currentTimeMillis();
		EffectiveHealthRatingPreparator ehrp = new EffectiveHealthRatingPreparator();
		List<EffectiveHealthRating> allRatings = new ArrayList<EffectiveHealthRating>();
		LiteAccess lacc = new LiteAccess();
		DamageResolver damRes = new DamageResolver();
		PokemonFactory pf = new PokemonFactory(lacc);

		Query q = new Query("name", "pokemon");
		List<String> allPokemonNames = lacc.getValuesForSingleColumn(q.getSQL());
		List<String> megaPokemon = lacc.getValuesForSingleColumn(new Query("name", "altforms").getSQL());
		allPokemonNames.addAll(megaPokemon);
		Pokemon pokemonBuffer;
		
		for(String pokemonName : allPokemonNames){
			pokemonBuffer = pf.createPokemon(pokemonName);
			allRatings.add(damRes.getEffectiveHealth(pokemonBuffer));
		}
		allRatings.sort(ehrp.new EffectiveHealthRatingComparator<EffectiveHealthRating>());
		File f = new File("HealthRatings.txt");
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
	
	class EffectiveHealthRatingComparator<T> implements Comparator<EffectiveHealthRating>{
		public int compare(EffectiveHealthRating r1, EffectiveHealthRating r2) {
			double r1Combined = r1.getEffectiveHealthValues().get(Spectrum.PHYSICAL) + r1.getEffectiveHealthValues().get(Spectrum.SPECIAL);
			double r2Combined = r2.getEffectiveHealthValues().get(Spectrum.PHYSICAL) + r2.getEffectiveHealthValues().get(Spectrum.SPECIAL);
			if(r1Combined>r2Combined){
				return 1;
			} else if(r1Combined<r2Combined){
				return -1;
			} else {
				return 0;
			}
		}
		
	}
}
