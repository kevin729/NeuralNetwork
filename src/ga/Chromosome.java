package ga;

public class Chromosome {
	private int fitness = 0;
	public double fitnessPercent = 0;
	private int[] genes;
	
	public Chromosome(int length) {
		genes = new int[length];
		
		for (int i = 0; i < genes.length; i++) {
			if (Math.random() >= 0.5) {
				genes[i] = 1;
			} else {
				genes[i] = 0;
			}
		}
	}
	
	public void calculateFitness() {
		fitness = 0;
		
		for (int i = 0; i < genes.length; i++) {
			if (genes[i] == GeneticAlgorithm.TARGET_CHROMOSOME[i]) {
				fitness++;
			}
		}
	}
	
	public int getFitness() {
		return fitness;
	}
	
	public int[] getGenes() {
		return genes;
	}
}
