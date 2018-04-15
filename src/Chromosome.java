
public class Chromosome {
	int[] individual;
	int numgenes;
	int chromosomeLength;
	double probabilty;
	boolean marked;
	
	public Chromosome(int numgenes) {
		setNumgenes(numgenes);
		this.individual = new int[numgenes];
		setChromosomeLength(numgenes);
	}
	public int getNumgenes() {
		return numgenes;
	}
	public void setNumgenes(int numgenes) {
		this.numgenes = numgenes;
	}
	public int getChromosomeLength() {
		return chromosomeLength;
	}
	public void setChromosomeLength(int chromosomeLength) {
		this.chromosomeLength = chromosomeLength;
	}
	public double getProbabilty() {
		return probabilty;
	}
	public void setProbabilty(int probabilty) {
		this.probabilty = probabilty;
	}

}
