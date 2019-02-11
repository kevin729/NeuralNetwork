package ga;

import java.util.Arrays;

public class Population {
	private Chromosome[] chromosomes;
	
	public Population(int length) {
		chromosomes = new Chromosome[length];
		
		for (int i = 0; i < chromosomes.length; i++) {
			chromosomes[i] = new Chromosome(GeneticAlgorithm.TARGET_CHROMOSOME.length);
		}
	}
	
	public void setChromosomes(Chromosome[] children) {
		for (int c = 0; c < chromosomes.length; c++) {
			for (int g = 0; g < chromosomes[c].getGenes().length; g++) {
				chromosomes[c].getGenes()[g] = children[c].getGenes()[g];
			}
		}
	}
	
	public Chromosome[] getChromosomes() {
		return chromosomes;
	}
	
	public void sortChromosomesByFitness() {
		Arrays.sort(chromosomes, (chromosome1, chromosome2) -> {
			if (chromosome1.getFitness() > chromosome2.getFitness()) {
				return -1;
			} else {
				return 1;
			}
		});
	}
}
