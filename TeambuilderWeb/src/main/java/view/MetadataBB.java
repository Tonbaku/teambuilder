package view;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import model.CounterInformation;
import model.MetaPokemon;
import model.Metadata;
import util.MetadataProvider;

public class MetadataBB {
	private Metadata<MetaPokemon> data;
	private MetadataProvider provider;
	
	@PostConstruct
	public void init(){
		this.data = this.provider.provideData();
	}
	
	public ArrayList<String> getCountersFor(String pokemon){
		ArrayList<String> counter = new ArrayList<String>();
		HashMap<String, CounterInformation> counterMap = this.data.getCountersFor(pokemon);
		for(String counterName : counterMap.keySet()){
			counter.add(counterName);
		}
		return counter;
	}

	public MetadataProvider getProvider() {
		return provider;
	}

	public void setProvider(MetadataProvider provider) {
		this.provider = provider;
	}
}
