package principal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.event.EventManager;
import org.jgap.impl.GreedyCrossover;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.StockRandomGenerator;
import org.jgap.impl.SwappingMutationOperator;
import org.jgap.impl.ThresholdSelector;

public class CaixeiroViajante {
	/*
	 * 1 – inicialização da população, 2 - avaliação, 3 – seleção, 4 –
	 * cruzamento, 5 – mutação.
	 */
	double teste=0;
	int cont = 0;
	Configuration config;
	int maxEvolucao = 128;
	int quantPopulacao = 512;
	static final int quantCidades = 7;
	// cria a Matris com 7 cidades
	static final int[][] arrayCidades = new int[][] { { 2, 4 }, { 7, 5 }, { 7, 11 }, { 8, 1 }, { 1, 6 }, { 5, 9 },
			{ 0, 11 } };

	public IChromosome criarCromossomo(final Object a_initial_data) {
		System.out.println("" + "criarCromossomo");
		try {
			Gene[] genes = new Gene[quantCidades];
			for (int i = 0; i < genes.length; i++) {
				genes[i] = new IntegerGene(config, 0, quantCidades - 1);
				genes[i].setAllele(new Integer(i));
			}
			IChromosome exemplo = new Chromosome(config, genes);
			return exemplo;
		}

		catch (InvalidConfigurationException iex) {
			throw new IllegalStateException(iex.getMessage());
		}
	}

	// funcao de avaliacao
	public FitnessFunction criarFuncaoFitness(final Object a_initial_data) {
		System.out.println("" + "criarFuncaoFitness");
		return new SalesmanFitnessFunction(this);
	}

	public Configuration criarConfiguracao(final Object a_initial_data) throws InvalidConfigurationException {
		System.out.println("" + "criarConfiguracao");
		Configuration configuracao = new Configuration();
		configuracao.removeNaturalSelectors(true);
		configuracao.addNaturalSelector(new ThresholdSelector(configuracao, 0.1), false);
		configuracao.setRandomGenerator(new StockRandomGenerator());
		configuracao.addGeneticOperator(new GreedyCrossover(configuracao));
		configuracao.setMinimumPopSizePercent(0);
		configuracao.setEventManager(new EventManager());
		configuracao.setFitnessEvaluator(new DefaultFitnessEvaluator());
		configuracao.addGeneticOperator(new SwappingMutationOperator(configuracao, 20));
		return configuracao;
	}

	// IChromosome é um individuo, uma possivel solucao

	public IChromosome procurarMelhorSolucao(final Object a_initial_data) throws InvalidConfigurationException {
		System.out.println("" + "procurarMelhorSolucao");

		config = criarConfiguracao(a_initial_data);

		// cria a funcao de avaliacao
		FitnessFunction funcaoFitness = criarFuncaoFitness(a_initial_data);
		config.setFitnessFunction(funcaoFitness);

		// cria um individuo
		IChromosome exemploCromossomo = criarCromossomo(a_initial_data);
		config.setSampleChromosome((exemploCromossomo));
		config.setPopulationSize(quantPopulacao);
		IChromosome[] cromossomos = new IChromosome[config.getPopulationSize()];
		Gene[] exemploGenes = exemploCromossomo.getGenes();
		for (int i = 0; i < cromossomos.length; i++) {
			Gene[] genes = new Gene[exemploGenes.length];
			for (int j = 0; j < genes.length; j++) {
				genes[j] = exemploGenes[j].newGene();
				genes[j].setAllele(exemploGenes[j].getAllele());
			}
			cromossomos[i] = new Chromosome(config, genes);
		}

		// cria uma estrutura
		Genotype populacao = new Genotype(config, new Population(config, cromossomos));
		IChromosome melhorCromossomo = null;
		for (int i = 0; i < maxEvolucao; i++) {
			
			populacao.evolve();
			melhorCromossomo = populacao.getFittestChromosome();
		}
		 
		System.out.println(teste);
		
		return melhorCromossomo;
	}

	// Método que calcula distância entre duas cidades no JGAP.
	public double distance(Gene a_from, Gene a_to) {
		System.out.println(cont + "distance");
		cont++;

		IntegerGene geneA = (IntegerGene) a_from;
		IntegerGene geneB = (IntegerGene) a_to;

		int a = geneA.intValue();
		int b = geneB.intValue();

		int x1 = arrayCidades[a][0];
		int y1 = arrayCidades[a][1];
		int x2 = arrayCidades[b][0];
		int y2 = arrayCidades[b][1];
		double val = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
		return Math.sqrt(val);

	}

	public static void main(String[] args) {
		try {
			long tempoInicial = System.currentTimeMillis();

			CaixeiroViajante c = new CaixeiroViajante();

			// cria um cromossomo que recebe o objeto c que chama a função
			// procurar melhor solucao
			IChromosome cromossomoExemplo = c.procurarMelhorSolucao(null);
			System.out.println("Solucao: ");

			// cria um objeto gene que recebe os genes do cromossomo criando
			// anteriormente
			Gene[] genes = cromossomoExemplo.getGenes();

			for (int i = 0; i < cromossomoExemplo.size(); i++) {
				System.out.print(genes[i].getAllele().toString() + "");
			}

			System.out.println("");
			long tempoFinal = System.currentTimeMillis();
			Date data = new Date();
			BufferedWriter saida = new BufferedWriter(
					new FileWriter("C:/Users/Augusto/Desktop/TempoExecucaoJGAP.txt", true));
			saida.newLine();
			saida.write(data.toGMTString()); // tempoFinal
			saida.write(" -- Tempo de Execução -> " + (tempoInicial) + " Milissegundos");

			saida.newLine();
			saida.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

}
