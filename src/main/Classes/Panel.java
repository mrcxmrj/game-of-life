package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Panel extends JPanel {
    static final int CELL_SIZE = 10;

    Engine engine;
    Graphics2D g2d;

    public Panel(Engine engine) {
        this.engine = engine;
        setPreferredSize(new Dimension(engine.getMapWidth() * CELL_SIZE + 1, engine.getMapHeight() * CELL_SIZE + 1));
    }

    private double changeUnits(int distance) {
        return distance * CELL_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;

        paintScene();
        paintAnimals();
        paintPlants();
    }

    protected void paintScene() {
        Rectangle2D mapBorder = new Rectangle2D.Double(changeUnits(0), changeUnits(0), changeUnits(engine.getMapWidth()), changeUnits(engine.getMapHeight()));
        Rectangle2D jungleBorder = new Rectangle2D.Double(
                changeUnits((engine.getMapWidth() - engine.getJungleSide()) / 2),
                changeUnits((engine.getMapHeight() - engine.getJungleSide()) / 2),
                changeUnits(engine.getJungleSide()),
                changeUnits(engine.getJungleSide()));

        g2d.draw(mapBorder);
        g2d.draw(jungleBorder);
    }

    protected void paintAnimals() {
        for (Animal animal : engine.getAnimals()) {
            drawAnimal(animal);
        }
    }

    protected void paintPlants() {
        for (Plant plant : engine.getPlants()) {
            drawPlant(plant);
        }
    }

    protected void drawAnimal(Animal animal) {
        Ellipse2D ellipse = new Ellipse2D.Double(changeUnits(animal.getPosition().getX()), changeUnits(animal.getPosition().getY()), CELL_SIZE, CELL_SIZE);
        g2d.setColor(energyToColor(animal.getEnergy()));
        g2d.fill(ellipse);
    }

    protected void drawPlant(Plant plant) {
        Rectangle2D rectangle = new Rectangle2D.Double(changeUnits(plant.getPosition().getX()) + 1.0, changeUnits(plant.getPosition().getY()) + 1.0, CELL_SIZE - 1.0, CELL_SIZE - 1.0);
        g2d.setColor(new Color(0, 170, 0));
        g2d.fill(rectangle);
    }

    private Color energyToColor(double energy) {
        if (energy >= 7 * engine.getMoveEnergy()) return new Color(0x0088DD);
        if (energy >= 5 * engine.getMoveEnergy()) return new Color(0xFFD301);
        if (energy >= 3 * engine.getMoveEnergy()) return new Color(0xFF8B01);
        if (energy >= 1 * engine.getMoveEnergy()) return new Color(0xC23B21);
        return new Color(0xA40001);

    }
}
