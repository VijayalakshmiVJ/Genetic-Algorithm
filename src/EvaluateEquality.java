
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class EvaluateEquality {
	
	// Stack for numbers: 'values'
	static Stack<Integer> values = new Stack<Integer>();
	// Stack for Operators: 'operation'
	static Stack<Character> operation = new Stack<Character>();
	
	DecimalFormat df = new DecimalFormat("0.###");
	Chromosome[] fittestChromosomes;
	static int[] evaluatedResult;
	static double[] fitnessProb;
	static double[] fittest;
	static int survivorcount;
	
	public EvaluateEquality(int populationcount, int survivorcount) {
		evaluatedResult = new int[populationcount];
		fitnessProb = new double[populationcount];
		fittest = new double[survivorcount];
		EvaluateEquality.survivorcount = survivorcount;
	}
	
	// Substitutes the random numbers/genes for the variables within the equality expression 
	// and returns the substituted string
	// EG : If expression = x + y - 10, and Chromosome = [1, 5]
	// then return string = 1 + 5 - 10
	public static String substitution (String expression, int[] individual, int numgenes) {
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
					newstring = newstring.substring(0, (i + 1)) + " * " + 
				                newstring.substring((i + 1), newstring.length());
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
		return string;
	}

	public static boolean checkPrecedence(char operation1, char operation2){
		if ((operation1 == '*' || operation1 == '/') && (operation2 == '+' || operation2 == '-')) {
			return false;
		}
		else
			return true;
	}

	public static int applyOperation(char operation, int b, int a){
		switch (operation)
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
				UnsupportedOperationException("Divide by zero encountered");
			return a / b;
		}
		return 0;
	}

	// Evaluates the expression from the substitution method using stack operation 
	// and returns the resultant value
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
				while ( !operation.empty() && checkPrecedence(exp[i], operation.peek()) )
					values.push(applyOperation(operation.pop(), values.pop(), values.pop()));
				// Push current character to 'operation'.	
				operation.push(exp[i]);
			}
		}
		// Entire expression has been parsed at this point, apply remaining
		// operations to remaining values
		while (!operation.empty()) {
			values.push(applyOperation(operation.pop(), values.pop(), values.pop()));
		}
		// Top of stack 'values' contains the result
		return values.pop();
	}

	public double calcFitnessProbability(int ind, int populationcount) {
		Double.parseDouble(df.format(fitnessProb[ind]));
		fitnessProb[ind] = (double)(1/(1 + (double) evaluatedResult[ind]));
		fitnessProb[ind] = Double.parseDouble(df.format(fitnessProb[ind]));
		return fitnessProb[ind];
	}

	// Calculate probability of each chromosome to satisfy the given equality equation
	public void calcFitness(String phenotype, Population population) {
		Chromosome chromosome;
		String str;
		double cumulativeProbability = 0;

		for(int i = 0; i < population.populationcount; i++) {
			chromosome = population.population[i];
			str = substitution(phenotype, chromosome.individual, chromosome.numgenes);
			evaluatedResult[i] = Math.abs(evaluate(str));
			cumulativeProbability += calcFitnessProbability(i, population.populationcount);
			
		}
		cumulativeProbability = Double.parseDouble(df.format(cumulativeProbability));
		
		for(int i = 0; i < population.populationcount; i++) {
			if(cumulativeProbability != 0) {
				population.population[i].probabilty = fitnessProb[i]/cumulativeProbability;
			}
			else {
				population.population[i].probabilty = 0;
			}
			population.population[i].probabilty = Double.parseDouble(df.format(population.population[i].probabilty));
		}
	}

	public Chromosome matchProbIndividual(double prob, Population population) {
		Chromosome probIndividual = null;
		for(int i = 0; i < population.populationcount; i++) {
			if(prob == population.population[i].probabilty) {
				if(population.population[i].marked != true) {
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
	
	public boolean performSelection(Population population) {		
		Chromosome[] chromosomes = population.population;
		fittestChromosomes = new Chromosome[population.survivorcount];
		Chromosome fittestChromosome = null;
		PriorityQueue queue = new PriorityQueue(population.populationcount);
		
		// Use priority queue to store all individuals/chromosomes probability
		for(int i = 0; i < population.populationcount; i++) {
			queue.insert(population.population[i].probabilty);
		}
		// Update the fittest list by populating it with those chromosomes
		// that have a higher probability to satisfy the equality equation
		for(int i = 0; i < population.survivorcount; i++) {
			fittest[i] = queue.delmax();
		}

		for(int i = 0; i < population.survivorcount; i++) {
			fittestChromosome =  matchProbIndividual(fittest[i], population);
			if(fittestChromosome != null) {
				population.population[i] = fittestChromosome;
			}
		}
		population.populationcount = population.survivorcount;
		if(population.survivorcount == 2) {
			survivorcount = population.survivorcount;
			return true;
		}
		return false;
	}
	
	
	public void swapGenes(Chromosome parent1, Chromosome parent2, int idx) {
		int temp = 0;
		temp = parent1.individual[idx];
		parent1.individual[idx] = parent2.individual[idx];
		parent2.individual[idx] = temp;
	}
	
	public void cross(int crossingPoint, Chromosome parent1, Chromosome parent2) {
		for(int i = 0; i < crossingPoint; i++) {
			swapGenes(parent1, parent2, i);
		}
	}

	public void performCrossing(Population population) {
		int crossingPoint = 0;
		Random random = new Random();
		
		for(int i = 0; i < population.survivorcount; i++) {
			// Cross first with last, second with second last and so on
			// Randomly generate crossing point for each such crossing/mating
			crossingPoint = random.nextInt(population.population[i].chromosomeLength) + 1;
			if (population.population[i].numgenes == 2) {
				crossingPoint = 1;
			}	
			cross(crossingPoint, population.population[i], population.population[fittest.length - i]);
		}
	}

	public static int[] getCoEfficients(String[] str) {
		// Support a maximum of 4 co-efficients in phenotype
		int[] coEfficients = new int[4];
		int i = 0;
		int count = 0;
		String result = null;
		StringBuilder sb = new StringBuilder();
		
		while(count != (str.length - 1)) {
			if(Character.isLetter(str[count].charAt(0))) {
				coEfficients[i]  = '1' - '0';
			}
			else {
				for(int j = 0; j < 4; j++) {
					sb.append(str[count].charAt(j));
					if(Character.isLetter(str[count].charAt(j + 1))) {
						break;
					}
				}
				result = sb.toString();
				coEfficients[i]  = Integer.parseInt(result);
			    sb.setLength(0);
			}
			// The coefficients are found at every 2nd index.
			count +=2;
			i++;
		}
		return coEfficients;
	}
	
	public void performMutation(Population population, int numgenes, int range) {
		Chromosome chromosome;
		Random random = new Random();
		int randChromosome;
		int randGene;
		// Let us mutate 1/3rd of the population
		int numChromosomesToMutate = population.populationcount/3;

		for(int i = 0; i < numChromosomesToMutate; i++) {
			// Randomly select the Chromosome to mutate
			randChromosome = random.nextInt(population.populationcount);
			// Randomly select the gene to mutate within that Chromosome
			randGene = random.nextInt(numgenes);
			chromosome = population.population[randChromosome];
			chromosome.individual[randGene] = random.nextInt(range - 1) + 0;
		}
	}
	
	public boolean checkIfSolutionArrived(Population population, int range, int numgenes, int[] coefficients) {
		int result = 0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < population.survivorcount; i++) {
			for(int j = 0; j < numgenes; j++) {
				result += (coefficients[j] * population.population[i].individual[j]);
				list.add(population.population[i].individual[j]);
			}
			if(result ==  range) {
				System.out.println("Found the below best solution");
				System.out.println(list);
				return true;
			}
			list.clear();
			result = 0;
		}
		return false;
	}

	public static void main(String[] args) {
		// Generate solutions to satisfy a given linear equality equation
		int populationcount = 0;
		int numgenes = 0;
		int survivorcount = 0;
		String phenotype = null;
		int range = 0;
		int mutationCount = 0;
		boolean bestSolutionFound = false;
		int countNumGeneration = 1;
		int countPopulationGeneration = 0;
		int numPopulationGenerations = 0;
		Chromosome[] chromosomes;
		String[] str;
		Random random = new Random();
		int[] coEfficients = new int[4];
		
		// Lets us read 5 inputs from Configuration file
		int numInputs = 5;
		String inputs[] = new String[numInputs];
		int inputcount = 0;
		URL path = EvaluateEquality.class.getResource("Configuration.txt");
		File f = new File(path.getFile());

		try (BufferedReader bufin = new BufferedReader(new FileReader(f))){
			String Currline;
			String[] tempstring;
			
			while((Currline = bufin.readLine()) != null) {
				tempstring = Currline.split("=");
				inputs[inputcount++] = tempstring[1];
			}
			
		}catch (IOException e) {
			e.printStackTrace();
		}

		populationcount = Integer.parseInt(inputs[0]);
		numgenes = Integer.parseInt(inputs[1]);
		survivorcount = (int)(Double.parseDouble(inputs[2]) * populationcount);
		phenotype = inputs[3];
		numPopulationGenerations = Integer.parseInt(inputs[4]);
		
		chromosomes = new Chromosome[populationcount];
		str = phenotype.split(" ");
		range = Integer.parseInt(str[str.length - 1]);
		
		coEfficients = getCoEfficients(str);
		EvaluateEquality poly = new EvaluateEquality(populationcount, survivorcount);

		while(bestSolutionFound == false && countPopulationGeneration < numPopulationGenerations) {
			// MAPPING PHENOTYPE --> GENOTYPE
			// For EG : To map the phenotype x + y - 10 to genotype, generate random number
			// between 0 - 9 to represent x and y respectively
			for(int i = 0; i < populationcount; i++) {
				chromosomes[i] = new Chromosome(numgenes);
				for(int j = 0; j < numgenes; j++) {
					chromosomes[i].individual[j] = random.nextInt((range/coEfficients[j]) - 1);
				}
			}
			Population population = new Population(chromosomes, populationcount, survivorcount);

			// Continue to perform fitness calculation, crossing and mutation until there are only 2 children left. If no
			// solution is found at this point, let's regenerate the population again and repeat the below process.
			while(population.survivorcount >= 2) {
				poly.calcFitness(phenotype, population);
				poly.performSelection(population);
				poly.performCrossing(population);

				// At this point check if we have already arrived at the solution that 
				// satisfies the equality expression.
				if(poly.checkIfSolutionArrived(population, range, numgenes, coEfficients)){
					bestSolutionFound = true;
					break;
				}
				// Lets perform mutation on every 4th generation. Here the idea is not to
				// induce mutation in every generation but after every couple of generations
				// to increase diversity similar to nature.
				if(mutationCount % 4 == 0) {
					poly.performMutation(population, numgenes, range);
				}
				
				// At this point after mutation, check if we have arrived at the solution that 
				// satisfies the equality expression.
				if(poly.checkIfSolutionArrived(population, range, numgenes, coEfficients)){
					bestSolutionFound = true;
					break;
				}
				population.survivorcount = (int) ( 0.75 * population.survivorcount);
				mutationCount++;
				countNumGeneration++;
			}
			countPopulationGeneration++;
			if(bestSolutionFound == true) {
				System.out.println("Num generations within a population to arrive to solution = " + countNumGeneration);
				break;
			}
		}
		if(bestSolutionFound == false) {
			System.out.println("Couldnt find exact solution, try again by increasing the countPopulationGeneration in config file ");
			for(int i = 0; i < EvaluateEquality.survivorcount; i++) {
				System.out.println("[" + chromosomes[i].individual[0] + ", " + chromosomes[i].individual[0 + 1] + "]");
			}
		}
		System.out.println("Populations generated to arrive to solution = " + countPopulationGeneration);
		
	}
}

