
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Stack;
public class EvaluatePolynomial {
	
	// Stack for numbers: 'values'
	static Stack<Integer> values = new Stack<Integer>();
	DecimalFormat df = new DecimalFormat("0.###");

	// Stack for Operators: 'operation'
	static Stack<Character> operation = new Stack<Character>();
	int[] evaluatedResult;
	static int[] probOfChromosome;
	static double[] fitnessProb;
	static double[] fittest;

	int[][] population;
	static int survivorcount;
	static int numgenes;
	static int chromosomeLength;
	Chromosome[] fittestChromosomes;
	
	public EvaluatePolynomial(int populationcount, int survivorcount) {
		evaluatedResult = new int[populationcount];
		fitnessProb = new double[populationcount];
		fittest = new double[survivorcount];
		
	}
	
	public static int getChromosomeLength() {
		return chromosomeLength;
	}

	public static void setChromosomeLength(int chromosomeLength) {
		EvaluatePolynomial.chromosomeLength = chromosomeLength;
	}

	public static int getSurvivorcount() {
		return survivorcount;
	}

	public static void setSurvivorcount(int survivorcount) {
		EvaluatePolynomial.survivorcount = survivorcount;
	}

	public static int getNumgenes() {
		return numgenes;
	}

	public static void setNumgenes(int numgenes) {
		EvaluatePolynomial.numgenes = numgenes;
	}

	
	public static String substitution (String expression, int[] individual, int numgenes) {
		//System.out.println("Inside substitution");
		
		//System.out.println("received expression " + expression);
		//for(int temp: individual) {
		//	System.out.println("individual elements = " + temp);
		//}
		
		int count = 0;
		char[] array;
		String newstring = expression;
		char[] exp = expression.toCharArray();
		
		for(int i = 0; i < exp.length; i++) {
			if(exp[i] == ' ') {
				continue;
			}
			if((i + 1) < exp.length) {
				if(Character.isDigit(exp[i]) && Character.isLetter(exp[i + 1])) {
					newstring = newstring.substring(0, (i + 1)) + " * " + newstring.substring((i + 1), newstring.length());
					exp = newstring.toCharArray();
				}
			}
		}
		array = newstring.toCharArray();

		for(int i = 0; i < newstring.toCharArray().length; i++) {
			if(exp[i] == ' ') {
				continue;
			}
			if(Character.isLetter(exp[i]) && count < numgenes) {
				array[i] = Integer.toString(individual[count]).charAt(0);
				count++;
			}
		}
		String string = new String(array);
		//System.out.println("Final Expression  = " + string);
		return string;
	}

	public static boolean hasPrecedence(char op1, char op2){
		if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
			return false;
		}
		else
			return true;
	}

	public static int applyOp(char op, int b, int a){
		switch (op)
		{
		case '+':
			return a + b;
		case '-':
			return a - b;
		case '*':
			return a * b;
		case '/':
			if (b == 0)
				throw new
				UnsupportedOperationException("Cannot divide by zero");
			return a / b;
		}
		return 0;
	}

	public static int evaluate(String expression){
		char[] exp = expression.toCharArray();

		for(int i = 0; i < exp.length; i++) {
			if(exp[i] == ' ') {

				continue;
			}
			if (exp[i] >= '0' && exp[i] <= '9'){
				StringBuffer sbuf = new StringBuffer();
				// There may be more than one digits in number
				while (i < exp.length && exp[i] >= '0' && exp[i] <= '9')
					sbuf.append(exp[i++]);
				values.push(Integer.parseInt(sbuf.toString()));
			}
			else if (exp[i] == '+' || exp[i] == '-' ||
					exp[i] == '*' || exp[i] == '/'){
				while ( !operation.empty() && hasPrecedence(exp[i], operation.peek()) )
					values.push(applyOp(operation.pop(), values.pop(), values.pop()));
				// Push current character to 'operation'.	
				operation.push(exp[i]);
			}
		}
		// Entire expression has been parsed at this point, apply remaining
		// ops to remaining values
		while (!operation.empty()) {
			values.push(applyOp(operation.pop(), values.pop(), values.pop()));
		}
		// Top of 'values' contains the result
		return values.pop();
	}

	public double calcFitnessProbability(int ind, int populationcount) {
		Double.parseDouble(df.format(fitnessProb[ind]));
		fitnessProb[ind] = (double)(1/(1 + (double) evaluatedResult[ind]));
		fitnessProb[ind] = Double.parseDouble(df.format(fitnessProb[ind]));
		//System.out.println("fitnessProb[ind] = " + fitnessProb[ind]);
		return fitnessProb[ind];
	}

	public void calcFitness(String phenotype, Population population) {
		System.out.println("Entering calcFitness ");
		Chromosome chromosome;
		String str;
		double cumulativeProbability = 0;

		for(int i = 0; i < population.populationcount; i++) {
			chromosome = population.population[i];
			str = substitution(phenotype, chromosome.individual, chromosome.numgenes);
			evaluatedResult[i] = Math.abs(evaluate(str));
			//System.out.println("evaluatedResult[i] = " + evaluatedResult[i]);
			cumulativeProbability += calcFitnessProbability(i, population.populationcount);
			
		}
		cumulativeProbability = Double.parseDouble(df.format(cumulativeProbability));
		System.out.println("cumulativeProbability " + cumulativeProbability);
		
		for(int i = 0; i < population.populationcount; i++) {
			population.population[i].probabilty = fitnessProb[i]/cumulativeProbability;
			population.population[i].probabilty = Double.parseDouble(df.format(population.population[i].probabilty));
			System.out.println("probabilty = " + population.population[i].probabilty);
			
		}
	}

	public Chromosome matchProbIndividual(double prob, Population population) {

		Chromosome probIndividual = null;

		for(int i = 0; i < population.populationcount; i++) {
			if(prob == population.population[i].probabilty) {
				System.out.println("prob matched for " + prob + " at i = " + i);
				if(population.population[i].marked != true) {
					System.out.println("Marked true for i " + i);
					population.population[i].marked = true;
					probIndividual =  population.population[i];
					break;
				}
				else {
					continue;
				}
			}
		}
		return probIndividual;

	}
	
	public void performSelection(Population population) {
		
		fittestChromosomes = new Chromosome[population.survivorcount];
		
		Chromosome[] chromosomes;
		System.out.println("Entering performSelection ");
		Chromosome fittestChromosome = null;
		PriorityQueue queue = new PriorityQueue(population.populationcount);
		// Use priority queue to store all individuals/chromosomes probability
		
		System.out.println("populationcount " + population.populationcount);
		for(int i = 0; i < population.populationcount; i++) {
			queue.insert(population.population[i].probabilty);
		}
		// Update the fittest list by populating it with those chromosomes
		// that have a higher probability to satisfy the equality equation
		for(int i = 0; i < population.survivorcount; i++) {
			fittest[i] = queue.delmax();
			System.out.println("fittest probabilities are " + fittest[i]);
		}
		for(int i = 0; i < population.survivorcount; i++) {
			fittestChromosome =  matchProbIndividual(fittest[i], population);
			
			if(fittestChromosome != null) {
				population.population[i] = fittestChromosome;
			}
		}
		
		// copy them to population
		//for(int i = 0; i < population.survivorcount; i++) {
		//	population.population[i] = fittestChromosomes[i];
		//}
		
		population.populationcount = population.survivorcount;
		
		chromosomes = population.population;
		System.out.println("After selection fittest are");
		for(int i = 0; i < population.survivorcount; i++) {
			System.out.println("[" + chromosomes[i].individual[0] + ", " + chromosomes[i].individual[0 + 1] + "]");
		}

	}
	
	
	public void swap(Chromosome parent1, Chromosome parent2, int idx) {
		int temp = 0;
		
		temp = parent1.individual[idx];
		parent1.individual[idx] = parent2.individual[idx];
		parent2.individual[idx] = temp;
	}
	
	public void cross(int crossingPoint, Chromosome parent1, Chromosome parent2) {
		for(int i = 0; i < crossingPoint; i++) {
			swap(parent1, parent2, i);
		}
	}

	public void performCrossing(Population population) {
		System.out.println("Entering performCrossing");
		int crossingPoint = 0;
		Random random = new Random();
		
		for(int i = 0; i < survivorcount; i++) {
			// Cross first with last, second with second last and so on
			// Randomly generate crossing point for each such crossing/mating
			
			crossingPoint = random.nextInt(chromosomeLength) + 1;
			
			if (population.population[i].numgenes == 2) {
				crossingPoint = 1;
			}	
			cross(crossingPoint, population.population[i], population.population[fittest.length - i]);
		}
	}

	public static void mappingPhenotypeGenotype(Chromosome[] chromosomes, int populationcount, int range) {
		// Generate random num between 0 to 9
		Random random = new Random();
		
		for(int i = 0; i < populationcount; i++) {
			chromosomes[i] = new Chromosome(numgenes);
			for(int j = 0; j < numgenes; j++) {
				chromosomes[i].individual[j] = random.nextInt(range - 1) + 0;
				//System.out.println("Individal " + i + " = " + chromosomes[i].individual[j]);
			}
		}
	}
	
	public void performMutation(Population population, int numgenes, int range) {
		
		System.out.println("Entered performMutation");
		Chromosome chromosome;
		Random random = new Random();
		
		int numChromosomesToMutate = population.populationcount/3;
		//System.out.println("numChromosomesToMutate = " + numChromosomesToMutate);
		
		int randChromosome = random.nextInt(population.populationcount) + 0;
		//System.out.println("randChromosome chosen = " + randChromosome);
		
		int randGene = random.nextInt(numgenes) + 0;
		//System.out.println("randGene chosen = " + randGene);
		
		for(int i = 0; i < numChromosomesToMutate; i++) {
			chromosome = population.population[randChromosome];
			chromosome.individual[randGene] = random.nextInt(range - 1) + 0;
		}
	}

	public static void main(String[] args) {
		// x + y = 10

		int populationcount = 8;
		int numgenes = 2;
		int survivorcount = (int) (0.75 * populationcount);
		String phenotype = "x + y - 10";
		Chromosome[] chromosomes = new Chromosome[populationcount];
		Chromosome chromosome;
		String[] str = phenotype.split(" ");
		int range = Integer.parseInt(str[str.length - 1]);
		Random random = new Random();
		int count = 0;

		
		System.out.println("range = " + range);
		
		// Map the phenotype x + y - 10 to genotype i.e.
		// x and y will be a random integer generated between 0 - 9
		//mappingPhenotypeGenotype(chromosomes, populationcount, range);
		for(int i = 0; i < populationcount; i++) {
			chromosomes[i] = new Chromosome(numgenes);
			for(int j = 0; j < numgenes; j++) {
				chromosomes[i].individual[j] = random.nextInt(range - 1) + 0;
				//System.out.println("Individal " + i + " = " + chromosomes[i].individual[j]);
				
			}
		}

		System.out.println("Initial population");
		for(int i = 0; i < populationcount; i++) {
			System.out.println("[" + chromosomes[i].individual[0] + ", " + chromosomes[i].individual[0 + 1] + "]");
		}
	        
		Population population = new Population(chromosomes, populationcount, survivorcount);


		EvaluatePolynomial poly = new EvaluatePolynomial(populationcount, survivorcount);

		// Calculate fitness of each individual in the population
		while(population.survivorcount >= 2) {
			System.out.println("Entered while");
			
			poly.calcFitness(phenotype, population);
			
			poly.performSelection(population);
			
			poly.performCrossing(population);
			
			// Lets perform mutation on every second generation
			// Here the idea is not to induce mutation in every generation but say in every second generation
			// to increase diversity similar to nature.
			if(count % 2 == 0) {
				poly.performMutation(population, numgenes, range);
			}
			
			//for(int i = 0; i < population.survivorcount; i++) {
			//	chromosome = population.population[i];
			//	for(int j = 0; j < numgenes; j++) {
			//		System.out.println("After Individal " + i + " = " + chromosome.individual[j]);
			//	}
			//}
			
			System.out.println("While round");
			for(int i = 0; i < population.survivorcount; i++) {
				System.out.println("[" + chromosomes[i].individual[0] + ", " + chromosomes[i].individual[0 + 1] + "]");
			}
			
			population.survivorcount = (int) ( 0.75 * population.survivorcount);
			count++;
		}
		
		System.out.println("After all rounds");
		//for(int i = 0; i < population.survivorcount; i++) {
		//	chromosome = population.population[i];
		//	for(int j = 0; j < numgenes; j++) {
		//		System.out.println("After everything Individal " + i + " = " + chromosome.individual[j]);
		//	}
		//}
		
		for(int i = 0; i < population.survivorcount; i++) {
			System.out.println("[" + chromosomes[i].individual[0] + ", " + chromosomes[i].individual[0 + 1] + "]");
		}

		
		

	}



}
