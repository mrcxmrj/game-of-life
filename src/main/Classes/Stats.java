package Classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Stats {
    private double averageEnergy;
    private double averageLifeExpectancy;
    private int day;
    private List<Animal> animals;
    private List<Plant> plants;
    private final List<Integer> lifeExpectancies;


    public double getAverageEnergy() {
        return averageEnergy;
    }

    double getAverageLifeExpectancy() {
        return averageLifeExpectancy;
    }

    public Stats(List<Animal> animals) {
        this.animals = animals;
        lifeExpectancies = new ArrayList<>();
        calculateAverageEnergy();
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
        calculateAverageEnergy();
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }

    public void addDeadAnimalAge(int age) {
        lifeExpectancies.add(age);
        calculateAverageLifeExpectancy();
    }

    public void calculateAverageEnergy() {
        double energySum = 0;
        for (Animal animal : animals) {
            energySum += animal.getEnergy();
        }
        energySum /= animals.size();
        averageEnergy = energySum;
    }

    public void calculateAverageLifeExpectancy() {
        double ageSum = 0;
        for (int age : lifeExpectancies
        ) {
            ageSum += age;
        }
        ageSum /= lifeExpectancies.size();
        averageLifeExpectancy = ageSum;
    }

    public void writeToFile() throws IOException {
        String fullFilePath = "stats.txt";

        File file = new File(fullFilePath);
        FileWriter writer = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(writer);

        printWriter.println("Days: " + day);
        printWriter.println("Number of animals: " + animals.size());
        printWriter.println("Number of plants: " + plants.size());
        printWriter.println("Average animal energy: " + averageEnergy);
        printWriter.println("Average animal life expectancy: " + averageLifeExpectancy);
        printWriter.close();
    }
}
