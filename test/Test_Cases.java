import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.Test;

public class Test_Cases {
	
	int populationcount=3;
	int numgenes = 2;
	int survivorcount = (int) (0.75 * populationcount);
	
	EvaluateEquality poly;
	Population population;
	Chromosome[] chromosomes;
	
	public Test_Cases() {

		chromosomes = new Chromosome[populationcount];
		
		for (int i=0; i<populationcount; i++) {
			chromosomes[i] = new Chromosome(numgenes);
		}
				
		chromosomes[0].individual[0] = 6;
		chromosomes[0].individual[1] = 4;
		
		chromosomes[1].individual[0] = 7;
		chromosomes[1].individual[1] = 5;
		
		chromosomes[2].individual[0] = 5;
		chromosomes[2].individual[1] = 5;
		
//		String[] str = phenotype.split(" ");
//		int range = Integer.parseInt(str[str.length - 1]);
//		Random random = new Random();
//		int mutationCount = 0;
//		boolean bestSolutionFound = false;
//		int countNumGeneration = 1;
//		int countPopulationGeneration = 0;
//		int numPopulationGenerations = 10;
//		int[] coEfficients = new int[4];
//		coEfficients = poly.getCoEfficients(str);
		
		poly = new EvaluateEquality(populationcount, survivorcount);			
		population = new Population(chromosomes, populationcount, survivorcount);
	}
	
	//Testing the Calculate Fitness Function
	@Test
	public void Test1() {
		String phenotype = "x + y - 10";
				
		poly.calcFitness(phenotype, population);
		
		Chromosome[] c = population.getPopulation();
		assertEquals(0.429, c[0].getProbabilty());
		assertEquals(0.143, c[1].getProbabilty());
		assertEquals(0.429, c[2].getProbabilty());
	}
	
	//Testing the Selection Fitness Function
	@Test
	public void Test2() {
		assertTrue(poly.performSelection(population));
		
		//We check here that the selected individuals had higher probability than the culled individual
		assertFalse(population.population[0].getProbabilty() < population.population[1].getProbabilty());
		assertFalse(population.population[2].getProbabilty() < population.population[1].getProbabilty());
	}
	
	//Testing the Cross Function
	@Test
	public void Test3() {
		poly.performCrossing(population);
		
		
		assertEquals(5, population.population[0].individual[0]);
		assertEquals(4, population.population[0].individual[1]);
		
		assertEquals(7, population.population[1].individual[0]);
		assertEquals(5, population.population[1].individual[1]);
		
		assertEquals(6, population.population[2].individual[0]);
		assertEquals(5, population.population[2].individual[1]);
	}

	
}