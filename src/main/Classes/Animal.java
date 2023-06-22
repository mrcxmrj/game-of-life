package Classes;

import Enums.MapDirection;

import static Classes.Engine.MATING_COST;

public class Animal {
    private double energy; //number of days
    public static final double STARTING_ENERGY = 0.5;
    private int age;

    public int getAge() {
        return age;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public void incrementEnergy(double addedEnergy) {
        this.energy += addedEnergy;
    }

    private MapDirection orientation;

    public MapDirection getOrientation() {
        return orientation;
    }

    public void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
    }

    private Vector2d position;

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public Genome getGenome() {
        return genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    private Genome genome;

    public Animal(int energy, MapDirection orientation, Vector2d position, int[] genome) {
        this.energy = energy;
        this.orientation = orientation;
        this.position = position;
        this.genome = new Genome();
        age = 0;
    }

    public Animal(int x, int y) {
        position = new Vector2d(x, y);
        genome = new Genome();
        orientation = MapDirection.N;
        energy = 0.0;
        age = 0;
    }

    public Animal(int x, int y, double startEnergy) {
        position = new Vector2d(x, y);
        genome = new Genome();
        orientation = MapDirection.N;
        energy = startEnergy;
        age = 0;
    }

    //constructor for children
    public Animal(Animal firstParent, Animal secondParent) {
        position = firstParent.getPosition();
        genome = new Genome();
        genome.generateChildGenome(firstParent.getGenome(), secondParent.getGenome());
        this.orientation = MapDirection.N;
        energy = firstParent.getEnergy() * MATING_COST + secondParent.getEnergy() * MATING_COST;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "energy=" + energy +
                ", orientation=" + orientation +
                ", position=" + position.getX() + ", " + position.getY() +
                ", genome=" + genome +
                '}';
    }

    public void turnAndMove() {
        orientation = genome.getRandomRotation();
        position = position.add(orientation.toUnitVector());
        age++;
    }

}
