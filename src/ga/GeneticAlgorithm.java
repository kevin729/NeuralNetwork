package ga;

import java.util.ArrayList;
import java.util.Arrays;

import utils.Utils;

public class GeneticAlgorithm {
	
	public static final int[] TARGET_CHROMOSOME = {0, 1, 0, 0};
	
	private Population population;
	private Chromosome[] children;
	
	private double mutation = 0;
	
	public GeneticAlgorithm(int populationSize) {
		children = new Chromosome[populationSize];
		population = new Population(populationSize);
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
			
			population.setFitnessPercent();
			population.sortChromosomesByFitness();
			
			//outputs
			for (int i = 0; i < population.getChromosomes().length; i++) {
				System.out.println("Chromosome " + i + ": " + Arrays.toString(population.getChromosomes()[i].getGenes()) + " Fitness: " + population.getChromosomes()[i].getFitness() + " Fitness Percent: " + population.getChromosomes()[i].fitnessPercent);
			}
			System.out.println(population.totalFitness);
		} while (generation != 100);
		//population.getChromosomes()[0].getFitness() != TARGET_CHROMOSOME.length
	}
	
	private void evolve(int index) {
		Chromosome[] parents = select(index);
		Chromosome child = crossOver(parents[0], parents[1]);
		mutate(child);
		
		children[index] = child;
	}
	
	private Chromosome[] select(int index) {
		Chromosome parent1 = population.getChromosomes()[index];
		Chromosome parent2;
		ArrayList<Chromosome> matingPool = new ArrayList<Chromosome>();
		
		for (int c = 0; c < population.getChromosomes().length; c++) {
			int copy = (int)Math.floor(population.getChromosomes()[c].fitnessPercent*100);
			if (c != index) {
				for (int m = 0; m < copy; m++) {
					matingPool.add(population.getChromosomes()[c]);
				}
			}
		}

		int selected = Utils.randomInt(0, matingPool.size()-1);
		parent2 = matingPool.get(selected);
		
		return new Chromosome[] {parent1, parent2};
	}
	
	private Chromosome crossOver(Chromosome parent1, Chromosome parent2) {
		Chromosome child = new Chromosome(parent1.getGenes().length);
		
		int midPoint = Utils.randomInt(1, child.getGenes().length - 1);
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
			if (Utils.randomInt(0, 1000) < mutation) {
				child.getGenes()[gene] = child.getGenes()[gene] == 0 ? 1 : 0;
			}
		}
		return child;
	}
}
