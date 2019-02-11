package ga;

import java.util.Arrays;

import utils.Utils;

public class GeneticAlgorithm {
	
	public static final int[] TARGET_CHROMOSOME = {1,1,0,1,0,0,1,1,1,0};
	
	private Population population;
	private Population nextPopulation;
	private Chromosome[] children;
	
	private int midPoint = 5;
	private double mutation = 0;
	
	public GeneticAlgorithm(int populationSize) {
		children = new Chromosome[populationSize];
		population = new Population(populationSize);
		nextPopulation = new Population(populationSize);
	}
	
	public void start() {
		int generation = 0;
		do {
			generation++;
			System.out.println("========================= Generation: " + generation + "===============================");
			
			//breed
			if (generation != 1) {
				for (int i = 0; i < population.getChromosomes().length; i++) {
					evolve(i);
				}
				
				population.setChromosomes(children);
			}
			
			//outputs
			for (int i = 0; i < population.getChromosomes().length; i++) {
				population.getChromosomes()[i].calculateFitness();
				population.sortChromosomesByFitness();
				System.out.println("Chromosome " + i + ": " + Arrays.toString(population.getChromosomes()[i].getGenes()) + " Fitness: " + population.getChromosomes()[i].getFitness());
			}
		} while (generation != 100);
	}
	
	private void evolve(int index) {
		Chromosome[] parents = select(index);
		Chromosome child = crossOver(parents[0], parents[1]);
		mutate(child);
		
		children[index] = child;
	}
	
	private Chromosome[] select(int index) {
		int selected;
		Chromosome parent1 = population.getChromosomes()[index];
		Chromosome parent2;
		do {
			selected = Utils.randomInt(0, population.getChromosomes().length-1);
			parent2 = population.getChromosomes()[selected];
		} while (selected == index);
		
		return new Chromosome[] {parent1, parent2};
	}
	
	private Chromosome crossOver(Chromosome parent1, Chromosome parent2) {
		Chromosome child = new Chromosome(parent1.getGenes().length);
		
		
		for (int gene = 0; gene < child.getGenes().length; gene++) {
			if (gene < midPoint) {
				child.getGenes()[gene] = parent1.getGenes()[gene];
			} else {
				child.getGenes()[gene] = parent2.getGenes()[gene];
			}
		}
		
		return child;
	}
	
	private Chromosome mutate(Chromosome child) {
		for (int gene = 0; gene < child.getGenes().length; gene++) {
			if (Math.random() < mutation) {
				child.getGenes()[gene] = child.getGenes()[gene] == 0 ? 1 : 0;
			}
		}
		return child;
	}
}
