
public class Population {
	Chromosome[] population;
	int populationcount;
	int survivorcount;
	Chromosome[] fittest;
	
	
	public Population(Chromosome[] chromosomes, int populationcount, int survivorcount) {
		this.population = new Chromosome[populationcount];
		for(int i = 0; i < populationcount; i++) {
			population[i] = chromosomes[i];
		}
		this.populationcount = populationcount;
		this.survivorcount = survivorcount;
	}
	
	public Chromosome[] getPopulation() {
		return population;
	}
	public void setPopulation(Chromosome[] population) {
		this.population = population;
	}
	public int getPopulationcount() {
		return populationcount;
	}
	public void setPopulationcount(int populationcount) {
		this.populationcount = populationcount;
	}

	


}
