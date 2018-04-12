import java.util.Random;
import java.util.Stack;

public class EvaluatePolynomial {
	
	// Stack for numbers: 'values'
	static Stack<Integer> values = new Stack<Integer>();

	// Stack for Operators: 'operation'
	static Stack<Character> operation = new Stack<Character>();
	static int[] fitness;

	int[][] population;
	static int populationcount;
	static int numgenes;

	public EvaluatePolynomial(int[][] randpopulation, int numgenes, int populationcount) {	
		this.population = randpopulation;
		setPopulationcount(populationcount);
		setNumgenes(numgenes);
	}

	public static int getPopulationcount() {
		return populationcount;
	}

	public static void setPopulationcount(int populationcount) {
		EvaluatePolynomial.populationcount = populationcount;

	}

	public static int getNumgenes() {
		return numgenes;
	}

	public static void setNumgenes(int numgenes) {
		EvaluatePolynomial.numgenes = numgenes;
	}

	public static String substitution (String expression, int[] individual) {
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
			if(Character.isLetter(exp[i]) && count < getNumgenes()) {
				array[i] = Integer.toString(individual[count]).charAt(0);
				count++;
			}
		}
		String string = new String(array);
		System.out.println("Expression  = " + string);
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

	public void calcFitnessProbability() {
		int[] fitnessProb = new int[populationcount];



	}

	public int calcFitness(String phenotype, int[] individual) {
		for(int temp: individual) {
			System.out.println("Individual elements are " + temp);
		}
		String str = substitution(phenotype, individual);
		int result = Math.abs(evaluate(str));
		System.out.println("result = " +  result);
		calcFitnessProbability();
		return result;
	}

	public void calcProbability() {

	}

	public void performSelection(int[] fitness) {

	}

	public void performCrossing() {

	}

	public void performMutation() {

	}


	public static void main(String[] args) {
		// x + y = 10
		// Generate random num between 0 to 9
		Random random = new Random();
		int populationcount = 2;
		int numgenes = 2;
		String phenotype = "x + y - 10";

		int[][] polyarr = new int[populationcount][numgenes];

		for(int i = 0; i < populationcount; i++) {
			for(int j = 0; j < numgenes; j++) {
				polyarr[i][j] = random.nextInt(9) + 0;
			}
		}

		for(int[] temp: polyarr) {
			for(int temp1: temp) {
				System.out.println("elements are " + temp1);
			}
		}

		EvaluatePolynomial poly = new EvaluatePolynomial(polyarr, populationcount, numgenes);

		// Calculate fitness of each individual in the population
		for(int i = 0; i < populationcount; i++) {
			fitness[i] = poly.calcFitness(phenotype, poly.population[i]);
		}







	}



}
