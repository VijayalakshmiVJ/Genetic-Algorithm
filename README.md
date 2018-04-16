# Genetic-Algorithm
A genetic algorithm (GA) is an adaptive heuristic search algorithm that is inspired by Charles Darwin’s theory of natural evolution. It is based on evolutionary ideas such as natural selection of the fittest, crossing, mutation and so on. Genetic Algorithms simulate these evolutionary concepts over consecutive generations for solving a given problem.

A real-world problem that we have selected to solve through Genetic Algorithm framework is Equality Expression. Below we will strive to find the best solution to a given Linear Equality Expression.

Our genetic algorithm process involves the following steps to solve the equality expression

**Step 1. PHENOTYPE** <br />
Get the phenotype from user defined configuration file. Phenotype must be an equality expression of the following format <br /> 
```
                                         ax + by – c 
```
where a, b and c are constants and x and y represent the variables to which solutions are to be found such that they satisfy the equation or phenotype <br />
```
                                        ax + by – c = 0 
```

**Step 2. EXPRESSION MAPPING (MAP TO GENOTYPE)** <br />
Map this phenotype to a genotype such that each gene represents one variable starting from the left-hand side of the equality expression. This gene is encoded with a random variable between range 0 – [(c/coefficient of gene) - 1)]. For example, say the number of chromosomes/individuals in population are 6. <br />
-	For a phenotype of x + y – 10, we generate random values for genes x and y between 0 – [(c/coefficient of gene) - 1)] i.e 0 – [(10/1) - 1] i.e. 0 – 9 for all 6 chromosomes/individuals as shown below <br />

```
	Chromosome[1]   =  [x;y]  =  [01;05] 
	Chromosome[2]   =  [x;y]  =  [02;09] 
	Chromosome[3]   =  [x;y]  =  [01;04] 
	Chromosome[4]   =  [x;y]  =  [03;07] 
	Chromosome[5]   =  [x;y]  =  [01;04] 
	Chromosome[6]   =  [x;y]  =  [08;05]
```
-	For phenotype of 2x + y – 22, we generate random values for gene x such that the range is between 0 – [(c/coefficient of x) - 1] =>  0 – [(22/2) - 1]  => 0 – 11. For y however, random values are generated between range 0 – (c/1 - 1) => 0 – 21 since the coefficient of y is 1. <br />

```
	Chromosome[1]   =  [x;y]  =  [12;05] 
	Chromosome[2]   =  [x;y]  =  [02;21] 
	Chromosome[3]   =  [x;y]  =  [10;04] 
	Chromosome[4]   =  [x;y]  =  [20;01] 
	Chromosome[5]   =  [x;y]  =  [01;04] 
	Chromosome[6]   =  [x;y]  =  [18;05] 
```

**Step 3. FITNESS EVALUATION** <br />
Evaluate fitness for each chromosome/individual in the population. Fitness calculation involves finding the probability of each chromosome to satisfy the given equation. It involves the following steps. <br />
1.	Evaluate the expression for each chromosome using the random numbers generated in genotype. For example consider expression x + y – 10, we know from above that the genotype of the population for this phenotype was <br />

```
	Chromosome[1]   =  [x;y]  =  [01;05] 
	Chromosome[2]   =  [x;y]  =  [02;09] 
	Chromosome[3]   =  [x;y]  =  [01;04]
	Chromosome[4]   =  [x;y]  =  [03;07] 
	Chromosome[5]   =  [x;y]  =  [01;04] 
	Chromosome[6]   =  [x;y]  =  [08;05] 
```

Thus evaluations for these chromosome will be as follows: <br />

     	EvaluatedResult[1] = abs( 1 + 5 - 10)  = 4
      	EvaluatedResult[2] = abs( 2 + 9 - 10)  = 1
      	EvaluatedResult[3] = abs( 1 + 4 - 10)  = 6
      	EvaluatedResult[4] = abs( 3 + 7 - 10)  = 0 and so on..


2.	Calculate probability for each of these chromosomes as follows
```
      	Probability[1] = 1 / (1  + EvaluatedResult[1]) = 1/ (1 + 4) = 1/5 = 0.250 
      	Probability[2] = 1 / (1  + EvaluatedResult[2]) = 1/ (1 + 1) = 1/2 = 0.500 
      	Probability[3] = 1 / (1  + EvaluatedResult[3]) = 1/ (1 + 6) = 1/2 = 0.142
      	Probability[4] = 1 / (1  + EvaluatedResult[4]) = 1/ (1 + 0) =  1    = 1  and so on..

      	Total Probability   =  Probability[1] + Probability[2] + Probability[3] + …..
                    	    =  0.250 + 0.500 + 0.142 + 1 + ..
```
3.	Thus fitness /probability of each chromosome will be calculated as follows
```
      	Fitness[1] =  Probability[1]/ Total Probability = 0.250/1.892 = 0.132
      	Fitness[2] =  Probability[2]/ Total Probability = 0.500/1.892 = 0.264
      	Fitness[3] =  Probability[3]/ Total Probability = 0.142/1.892 = 0.075
      	Fitness[4] =  Probability[4]/ Total Probability = 0.250/1.892 = 0.530 and so on…
```
We can see that Chromosome 4 has the highest probability and will one of the chromosomes to be selected by the selection function in the next steps.

**Step 4. SELECTION**<br />
The selection process involves selecting those chromosomes that have the highest probability.
A priority queue is implemented to return the highest probability. Also the number of such chromosomes to be selected is governed by the percentage of members to be retained or eliminated given from configuration file.

If the population to retain is 75% and elimination 25%, which is generally what this GA framework suggests, then for a population of 6 as above, 4 fittest chromosomes will be selected and 2 eliminated.


**Step 5. CROSSOVER** <br />
The fittest chromosomes from the above step are now to be crossed. For crossover we randomly select the crossing points based on the number of the genes. For example if the number of genes are 2 in each chromosome as depicted in above examples, then we choose the middle, i.e 1 as crossing point. If the number of genes are 3 or more, we randomly generate the crossing point to range between 0 - (chromosomeLength or numOfGenes – 1). The crossover will thus happen starting at 0 0 till crossing point (but not including it).
For example, here since number of genes is 2, crossing point = 1. So interchange all genes starting from 0 till 1 (not including 1)

```
		Chromosome[1]   =  [x;y]  =  [01;05] 
				              ||  exchange genes at index 0 only
				              ||
		Chromosome[2]   =  [x;y]  =  [02;09] 
```

Thus, offsprings are 
```
		Chromosome[7]   =  [x;y]  =  [02;05] 
		Chromosome[7]   =  [x;y]  =  [01;09] 
```

If the number of genes are higher say for example 4, randomly select crossing point between 0 - (chromosomeLength – 1) i.e. 0 - 3. Say 2 was randomly generated as crossing point. Then cross all genes starting at 0 till index 1 (not including 2).

```
		Chromosome[1]   =  [x;y]  =  [01;05;02;09]
                              		      || ||
                              		      || || exchange genes starting at index 0 till index 1
		Chromosome[2]   =  [x;y]  =  [08;01;02;09]
```

Thus, offsprings are 
```
		Chromosome[1]   =  [x;y]  =  [08;01;02;09]
		Chromosome[2]   =  [x;y]  =  [01;05;02;09]
```

**Step 7. MUTATION**<br /> 
In our GA framework, we try to model mutation somewhat similar to nature. So, we have induced mutation in every 4 th generation as follows. 1/3 rd of the population is considered for mutation and a random gene is selected to mutate. The random number at that gene index is then replaced by another random number generated by considering coefficients of that gene. 

For example, say Chromosome 2 and Chromosome 5 were selected for mutation. Say gene at index 1 was selected for chromosome 2 and gene at index 0 was selected for Chromosome 5 to mutate. Thus, we may get the following

```
		Chromosome[2]   =  [x;y]  =  [02;<b>09</b>]   ---say mutated to --- > [02;**04**]   
		Chromosome[5]   =  [x;y]  =  [**01**;04]   ---say mutated to --- > [**05**;04]  
```

**Step 8. SOLUTION (BEST CHROMOSOMES)** <br />

Selecting the best chromosome involves checking if we have arrived at solutions x and y that satisfy the phenotype i.e. the equality expression. If we have not arrived at the desired solution through the Steps 1 – 8, then as depicted in the flowchart, another round of population is taken and the entire GA process (Steps 1 - 8) is repeated.

![Alt text](./GA_Flowchart.png?raw=true "Genetic Algorithm Flowchart")



                                         Genetic Algorithm Flowchart

**Few Limitations/Characteristics of our GA framework:**
Owing to time constraints our GA framework is still in beta stage and will try to solve equality expressions with the following assumptions/characteristics only.
1. The equality expression will be linear only. Currently, our GA framework cannot solve quadratic or higher exponents based expressions.
2. The equality expression will be fed in a certain format. Our GA framework relies on the format of the expression where in spaces must be provided after every variable and
operation. For eg:
    - 2x + y – 10 is a valid expression with a space after every operation and variable.
    - 3x+y+z-20 or 3x + y-4 are not valid expressions.
3. The equality expression will not have co-efficients greater than 4 digits. Anything higher will be truncated and only 4 digits will be considered as co-efficients. For eg:
    - 10x + 3y – 10 is a valid expression with right co- efficients.
    - 100000x + y – 10 is not a valid expression to solve for us. The GA will however still try to solve by truncating resulting in expression 1000x + y – 10.
4. Our GA framework will look to find positive solutions only to the equality expression.
5. While updating configuration file for population count, its important to update the same keeping in mind few characteristics of our GA framework. The population count should correspond to the solution we want to arrive at. 
For eg: If the phenotype = x + y – 10 Then we are trying to find those combination of positive integers x and y that lead to a solution of 10. Now if we start off by a population of 1000, its highly likely that we will arrive at the solution in the first/second generation itself.

