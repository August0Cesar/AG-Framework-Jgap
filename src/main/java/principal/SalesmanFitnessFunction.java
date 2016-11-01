package principal;

import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

public class SalesmanFitnessFunction extends FitnessFunction {
	private final CaixeiroViajante caixeiroViajante;

	public SalesmanFitnessFunction(final CaixeiroViajante caixViajante) {
		caixeiroViajante = caixViajante;
	}

	// Método que avalia o cromossomo calculando valor de fitness ou Avaliação no JGAP.
	protected double evaluate(final IChromosome chromosome) {
		double s = 0;
		Gene[] genes = chromosome.getGenes();
		for (int i = 0; i < genes.length - 1; i++) {
			s += caixeiroViajante.distance(genes[i], genes[i + 1]);
		}
		s += caixeiroViajante.distance(genes[genes.length - 1], genes[0]);
		return Integer.MAX_VALUE / 2 - s;
	}

}
