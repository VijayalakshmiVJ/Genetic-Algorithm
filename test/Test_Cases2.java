import static org.junit.Assert.*;
import org.junit.Test;

public class Test_Cases2 {
	
	EvaluateEquality poly;
	Population population;
	Chromosome[] chromosomes;
	int populationcount = 3;
	int numgenes = 2;
	int survivorcount = (int) (0.75 * populationcount);
	
	@Test
	public void testCalcFitness() throws Exception {
		String phenotype = "2x + 5y - 100";	
		chromosomes = new Chromosome[populationcount];
		
		for (int i = 0; i < populationcount; i++) {
			chromosomes[i] = new Chromosome(numgenes);
		}		
		chromosomes[0].individual[0] = 30;
		chromosomes[0].individual[1] = 24;

		chromosomes[1].individual[0] = 0;
		chromosomes[1].individual[1] = 10;
		
		chromosomes[2].individual[0] = 41;
		chromosomes[2].individual[1] = 18;
		
		poly = new EvaluateEquality(populationcount, survivorcount);			
		population = new Population(chromosomes, populationcount, survivorcount);
		
		poly.calcFitness(phenotype, population);
		Chromosome[] c = population.getPopulation();

		assertEquals(0.364, c[0].getProbabilty(), 0);	
		assertEquals(0.303, c[1].getProbabilty(), 0);
		assertEquals(0.333, c[2].getProbabilty(), 0);
	}

	@Test
	public void testperformSelection() throws Exception {
		String phenotype = "2x + 5y - 100";
		chromosomes = new Chromosome[populationcount];
		
		for (int i = 0; i < populationcount; i++) {
			chromosomes[i] = new Chromosome(numgenes);
		}		
		chromosomes[0].individual[0] = 30;
		chromosomes[0].individual[1] = 24;
		chromosomes[1].individual[0] = 41;
		chromosomes[1].individual[1] = 18;
		chromosomes[2].individual[0] = 0;
		chromosomes[2].individual[1] = 10;
		
		poly = new EvaluateEquality(populationcount, survivorcount);			
		population = new Population(chromosomes, populationcount, survivorcount);
		
		poly.calcFitness(phenotype, population);
		
		poly.performSelection(population);
		
		assertEquals(population.population[0].getProbabilty(), 0.364, 0);
		assertEquals(population.population[1].getProbabilty(), 0.333, 0);
	}
	
	@Test
	public void testCrossMethod() throws Exception {
		String phenotype = "2x + 5y - 100";
		chromosomes = new Chromosome[populationcount];
		
		for (int i = 0; i < populationcount; i++) {
			chromosomes[i] = new Chromosome(numgenes);
		}		
		chromosomes[0].individual[0] = 30;
		chromosomes[0].individual[1] = 24;
		chromosomes[1].individual[0] = 41;
		chromosomes[1].individual[1] = 18;
		chromosomes[2].individual[0] = 0;
		chromosomes[2].individual[1] = 10;
		
		poly = new EvaluateEquality(populationcount, survivorcount);			
		population = new Population(chromosomes, populationcount, survivorcount);
		
		poly.performCrossing(population);
		
		assertEquals(0,population.population[0].individual[0]);
		assertEquals(24,population.population[0].individual[1]);
		
		assertEquals(41,population.population[1].individual[0]);
		assertEquals(18,population.population[1].individual[1]);
		
		assertEquals(30,population.population[2].individual[0]);
		assertEquals(10,population.population[2].individual[1]);
				
	}
}