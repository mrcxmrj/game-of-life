package Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Engine {
    public static final double MATING_COST = 0.25;

    private final List<Animal> animals;
    private final List<Plant> plants;
    private final double startEnergy;
    private final double plantEnergy;
    private final double moveEnergy;
    private final int jungleSide;
    private final int startingAnimalNumber;
    private final Vector2d lowerLeft;
    public Stats stats;

    private boolean stopped;


    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean getStopped() {
        return stopped;
    }

    public double getMoveEnergy() {
        return moveEnergy;
    }

    public int getJungleSide() {
        return jungleSide;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getDay() {
        return day;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    private int day;
    private final int mapWidth;
    private final int mapHeight;
    private final Random random;

    private final List<Vector2d> jungleField;

    private final List<Vector2d> plainsField;
    //public static final double JUNGLE_SIDE = 10;


    public Engine(Parameters parameters) {
        this.random = new Random();

        this.animals = new ArrayList<>();
        this.plants = new ArrayList<>();

        this.mapWidth = parameters.getWidth();
        this.mapHeight = parameters.getHeight();
        this.startEnergy = parameters.getStartEnergy();
        this.plantEnergy = parameters.getPlantEnergy();
        this.moveEnergy = parameters.getMoveEnergy();
        startingAnimalNumber = parameters.getNumAnimals();

        jungleField = new ArrayList<>();
        double jungleArea = parameters.getJungleRatio() * mapWidth * mapHeight / (1.0 + parameters.getJungleRatio());
        jungleSide = (int) Math.round(Math.sqrt(jungleArea));
        lowerLeft = new Vector2d((mapWidth - jungleSide) / 2, (mapHeight - jungleSide) / 2);

        for (int y = lowerLeft.getY(); y < lowerLeft.getY() + jungleSide; y++) {
            for (int x = lowerLeft.getX(); x < lowerLeft.getX() + jungleSide; x++) {
                Vector2d position = new Vector2d(x, y);
                jungleField.add(position);
            }
        }

        plainsField = new ArrayList<>();
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                Vector2d position = new Vector2d(x, y);
                if (jungleField.contains(position)) continue;
                plainsField.add(position);
            }
        }
    }

    public void init() {
        day = 0;

        for (int i = 0; i < startingAnimalNumber; i++) {
            animals.add(new Animal(random.nextInt(mapWidth), random.nextInt(mapHeight), startEnergy));
        }

        stats = new Stats(animals);
    }

    public void newDay() {
        day++;

        removeDeadAnimals();
        moveAnimals();
        consumption();
        mating();
        addNewPlants();

        updateStats();
        System.out.println(stats.getAverageEnergy());
    }

    public void updateStats() {
        stats.setAnimals(animals);
        stats.setPlants(plants);
        stats.setDay(day);
    }

/*    public void showAnimals() {

        for (int i = 0; i < animals.size(); i++) {
            System.out.println(animals.get(i).toString());
        }
        for (int i = 0; i < plants.size(); i++) {
            System.out.println(plants.get(i).toString());
        }
    }*/

    public void consumption() {
        List<Integer> plantsEatenIndices = new ArrayList<>();
        for (int i = 0; i < plants.size(); i++) {
            //animalIndicesAtPosition stores indices of animals at ith position with highest energy
            List<Integer> animalIndicesAtPosition = new ArrayList<>();
            double highestEnergy = 0;
            for (int j = 0; j < animals.size(); j++) {
                //System.out.println("Pozycja zwierzecia: " + animals.get(j).getPosition().toString());

                if (plants.get(i).getPosition().equals(animals.get(j).getPosition()) && animals.get(j).getEnergy() > highestEnergy) {
                    animalIndicesAtPosition.clear();
                    animalIndicesAtPosition.add(j);
                }
                if (plants.get(i).getPosition().equals(animals.get(j).getPosition()) && animals.get(j).getEnergy() == highestEnergy) {
                    animalIndicesAtPosition.add(j);
                }
            }

            //if there is more than one highest energy animal at position, the plant is shared
            for (int animalIndex : animalIndicesAtPosition
            ) {
                Animal placeholder = animals.get(animalIndex);
                double addedEnergy = plantEnergy / animalIndicesAtPosition.size();
                placeholder.incrementEnergy(addedEnergy);
                animals.set(animalIndex, placeholder);
            }

            if (animalIndicesAtPosition.size() > 0) {
                plantsEatenIndices.add(i);
            }
        }

        for (int i = plantsEatenIndices.size() - 1; i >= 0; i--) {
            Vector2d freedSpacePosition = plants.get(i).getPosition();
            if (freedSpacePosition.getX() >= lowerLeft.getX() && freedSpacePosition.getY() >= lowerLeft.getY()
                    && freedSpacePosition.getX() < lowerLeft.getX() + jungleSide && freedSpacePosition.getY() < lowerLeft.getY() + jungleSide) {
                jungleField.add(freedSpacePosition);
            } else {
                plainsField.add(freedSpacePosition);
            }
            plants.remove(i);
        }
    }

    public void mating() {
        List<Vector2d> checkedPositions = new ArrayList<>();
        for (int i = 0; i < animals.size(); i++) {
            if (checkedPositions.contains(animals.get(i).getPosition())) continue;

            //resolving mating at ith position
            List<Animal> potentialParentsAtPosition = new ArrayList<>();
            potentialParentsAtPosition.add(animals.get(i));

            for (int j = i + 1; j < animals.size(); j++) {
                if (animals.get(i).getPosition().equals(animals.get(j).getPosition()) && animals.get(j).getEnergy() > 0) {
                    potentialParentsAtPosition.add(animals.get(j));
                }
            }

            if (potentialParentsAtPosition.size() < 2) continue;

            Animal firstParent = new Animal(-1, -1);
            Animal secondParent = new Animal(-1, -1);

            //get highest and second highest energy from potential parents
            double highestEnergy = 0;
            double secondHighestEnergy = 0;

            for (int j = 1; j < potentialParentsAtPosition.size(); j++) {
                if (potentialParentsAtPosition.get(j).getEnergy() > highestEnergy) {
                    secondHighestEnergy = highestEnergy;
                    highestEnergy = potentialParentsAtPosition.get(j).getEnergy();
                }
            }

            //count how many potential parents have the same energy then select parents accordingly
            int counter = 0;
            for (Animal parent : potentialParentsAtPosition) {
                if (parent.getEnergy() == highestEnergy) counter++;
                if (counter > 2) break;
            }
            switch (counter) {//not tested for animals with different energies
                case 1:
                    for (Animal parent : potentialParentsAtPosition) {
                        if (parent.getEnergy() == highestEnergy) firstParent = parent;
                        if (parent.getEnergy() == secondHighestEnergy) secondParent = parent;
                    }
                    break;
                case 2:
                    boolean firstChosen = false;
                    for (Animal parent : potentialParentsAtPosition) {
                        if (parent.getEnergy() == highestEnergy && firstChosen) {
                            secondParent = parent;
                            break;
                        }
                        if (parent.getEnergy() == highestEnergy && !firstChosen) {
                            firstParent = parent;
                            firstChosen = true;
                        }
                    }
                    break;
                case 3:
                    for (int j = potentialParentsAtPosition.size() - 1; j >= 0; j--) {
                        if (potentialParentsAtPosition.get(j).getEnergy() != highestEnergy) {
                            potentialParentsAtPosition.remove(j);
                        }
                    }
                    int randomIndex1 = random.nextInt(potentialParentsAtPosition.size());
                    int randomIndex2 = random.nextInt(potentialParentsAtPosition.size());
                    while (randomIndex1 == randomIndex2) {
                        randomIndex2 = random.nextInt(potentialParentsAtPosition.size());
                    }
                    firstParent = potentialParentsAtPosition.get(randomIndex1);
                    secondParent = potentialParentsAtPosition.get(randomIndex2);
                    break;
            }
            Animal child = new Animal(firstParent, secondParent);

            //System.out.println(firstParent.toString() + " + " + secondParent.toString());

            firstParent.setEnergy(firstParent.getEnergy() * (1 - MATING_COST));
            secondParent.setEnergy(secondParent.getEnergy() * (1 - MATING_COST));

            /*Vector2d newPosition = new Vector2d(-1, -1);
            while (!isOccupied(newPosition)) {
                newPosition = child.getPosition().add(child.getGenome().getRandomRotation().toUnitVector());
            }
            System.out.println(newPosition.toString());
            child.setPosition(newPosition);
            child.setOrientation(child.getGenome().getRandomRotation());*/

            boolean s = true;
            while (s) {
                Vector2d unitVector = child.getGenome().getRandomRotation().toUnitVector();
                if (!isOccupied(child.getPosition().add(unitVector))) {
                    child.setPosition(child.getPosition().add(unitVector));
                    s = false;
                }
            }
            child.setOrientation(child.getGenome().getRandomRotation());

            //System.out.println(child.toString());
            animals.add(child);

            checkedPositions.add(animals.get(i).getPosition());
        }
    }

    //removes all Animals with energy == 0
    public void removeDeadAnimals() {
        for (int i = animals.size() - 1; i >= 0; i--) {
            if (animals.get(i).getEnergy() <= 0) {
                stats.addDeadAnimalAge(animals.get(i).getAge());
                animals.remove(i);
            }
        }
    }

    public void moveAnimals() {
        for (Animal animal : animals) {
            animal.turnAndMove();
            animal.setEnergy(animal.getEnergy() - moveEnergy);
            animal.setPosition(new Vector2d(animal.getPosition().getX() % mapWidth, animal.getPosition().getY() % mapHeight));
        }
    }

    public boolean isOccupied(Vector2d position) {
        for (Animal animal : animals
        ) {
            if (animal.getPosition().equals(position)) return true;
        }
/*        for (Plant plant : plants
        ) {
            if (plant.getPosition().equals(position)) return true;
        }*/
        return false;
    }

    public void addNewPlants() {
        if (plainsField.size() > 0) {
            int plainsIndex = random.nextInt(plainsField.size());
            plants.add(new Plant(plainsField.get(plainsIndex)));
            plainsField.remove(plainsIndex);
        }

        if (jungleField.size() > 0) {
            int jungleIndex = random.nextInt(jungleField.size());
/*            while (isOccupied(jungleField.get(jungleIndex))) {
                System.out.println(jungleIndex);
                jungleIndex = random.nextInt(jungleField.size());
            }*/
            plants.add(new Plant(jungleField.get(jungleIndex)));
            jungleField.remove(jungleIndex);
        }
    }

}

