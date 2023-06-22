package Classes;

import Enums.MapDirection;

import java.util.Arrays;
import java.util.Random;

public class Genome {
    public static final int NUMBER_OF_GENES = 8;
    public static final int GENOME_SIZE = 32;
    private final Random random;

    private final int[] genomeArray;
    private final float[] preferences;

    public Genome() {
        this.random = new Random();
        this.genomeArray = new int[GENOME_SIZE];
        this.preferences = new float[NUMBER_OF_GENES];
        for (int i = 0; i < genomeArray.length; i++) {
            genomeArray[i] = random.nextInt(NUMBER_OF_GENES);
        }
        calculatePreferences();
        fixGenome();
    }

    @Override
    public String toString() {
        return "{" +
                "genome=" + Arrays.toString(genomeArray) +
//                ", preferences=" + Arrays.toString(preferences) +
                '}';
    }

    public void calculatePreferences() {
        for (int gene : genomeArray) {
            preferences[gene]++;
        }
        for (int i = 0; i < preferences.length; i++) {
            preferences[i] /= genomeArray.length;
        }
    }

    public void generateChildGenome(Genome firstParentGenome, Genome secondParentGenome) {
        //to split parent genomes into 3 parts, we pick lowerBound from [1, GENOME_SIZE-2] == [1, GENOME_SIZE-1)
        //and upperBound from [lowerBound+1, GENOME_SIZE-1] == [lowerBound+1, GENOME_SIZE)
        //bounds are generated using the following formula: Math.random() * (max - min + 1) + min
        //with the min value inclusive and the max exclusive
        //example split: [1, 27) => splits genome into [0,1);[1,27);[27,GENOME_SIZE)
        //example split: [31, 31) => splits genome into [0,31);[31,31);[31,GENOME_SIZE)
        int lowerBound = (int) (Math.random() * (GENOME_SIZE) + 1);
        int upperBound = (int) (Math.random() * (GENOME_SIZE - lowerBound) + lowerBound);

        //firstParentMajority determines whether the first parent provides 2/3 of genome
        boolean firstParentMajority = random.nextBoolean();

        if (firstParentMajority) {
            //System.out.println("FIRST | " + lowerBound + ", " + upperBound);

            for (int currentIndex = 0; currentIndex < lowerBound; currentIndex++) {
                this.genomeArray[currentIndex] = firstParentGenome.genomeArray[currentIndex];
            }
            for (int currentIndex = lowerBound; currentIndex < upperBound; currentIndex++) {
                this.genomeArray[currentIndex] = secondParentGenome.genomeArray[currentIndex];
            }
            for (int currentIndex = upperBound; currentIndex < GENOME_SIZE; currentIndex++) {
                this.genomeArray[currentIndex] = firstParentGenome.genomeArray[currentIndex];
            }
        } else {
            //System.out.println("SECOND | " + lowerBound + ", " + upperBound);

            for (int currentIndex = 0; currentIndex < lowerBound; currentIndex++) {
                this.genomeArray[currentIndex] = secondParentGenome.genomeArray[currentIndex];
            }
            for (int currentIndex = lowerBound; currentIndex < upperBound; currentIndex++) {
                this.genomeArray[currentIndex] = firstParentGenome.genomeArray[currentIndex];
            }
            for (int currentIndex = upperBound; currentIndex < GENOME_SIZE; currentIndex++) {
                this.genomeArray[currentIndex] = secondParentGenome.genomeArray[currentIndex];
            }
        }
        calculatePreferences();
        fixGenome();
    }

    //fixGenome() fixes the genome if any gene was eliminated (its preference == 0)
    //to call this method preferences have to be calculated
    public void fixGenome() {
        for (int i = 0; i < preferences.length; i++) {
            if (preferences[i] == 0) {
                int randomIndex = random.nextInt(GENOME_SIZE);
                this.genomeArray[randomIndex] = i;
            }
        }
        calculatePreferences();
    }

    public MapDirection getRandomRotation() {
        int index = random.nextInt(GENOME_SIZE);
        MapDirection direction = MapDirection.N;
        return direction.intToMapDirection(genomeArray[index]);
    }
}
