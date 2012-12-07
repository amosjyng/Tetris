import com.beust.jcommander.JCommander;
import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;

class Tetris
{
    public static void main(String[] args)
    {
        JCommander commander = new JCommander(new Constants(), args);
        commander.setProgramName("Tetris");

        if(Constants.help)
        {
            commander.usage();
        }
        else
        {
            GameWindow gameWindow = null;
            if(!Constants.NO_GUI)
            {
                gameWindow = new GameWindow();
            }
            if(Constants.RUN_AI)
            {
                System.out.println(Runtime.getRuntime().availableProcessors() + " cores detected.");

                if(Constants.USE_GA)
                {
                    Configuration configuration = new DefaultConfiguration();
                    try
                    {
                        configuration.setFitnessFunction(new ScoreFitnessFunction());

                        Gene[] genes = new Gene[3];
                        genes[0] = new DoubleGene(configuration, 0, 100);
                        genes[1] = new DoubleGene(configuration, -100, 0);
                        genes[2] = new DoubleGene(configuration, -100, 0);

                        Chromosome chromosome = new Chromosome(configuration, genes);
                        configuration.setSampleChromosome(chromosome);

                        configuration.setPopulationSize(Constants.POPULATION);
                        Genotype population = Genotype.randomInitialGenotype(configuration);

                        for(int i = 0; i < Constants.MAX_EVOLUTIONS; i++)
                        {
                            population.evolve();
                            System.out.println("Optimal chromosome for generation " + (i + 1) + ":");
                            IChromosome mostFit = population.getFittestChromosome();
                            new Options(mostFit).print();
                            System.out.println("Fitness of most fit chromosome: " + mostFit.getFitnessValue());
                        }
                    }
                    catch (InvalidConfigurationException ice)
                    {
                        System.err.println("There was an error with the fitness function configuration!");
                        ice.printStackTrace();
                    }
                }
                else
                {
                    Experimentalist experimentalist = new Experimentalist();
                    experimentalist.attach(gameWindow);
                    experimentalist.run();
                }
            }
            else
            {
                GameController humanGame = new GameController(new Options());
                gameWindow.attach(humanGame);
            }
        }
    }
}