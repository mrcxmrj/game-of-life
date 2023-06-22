package Classes;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Parameters {
    public static Parameters read(String fileName) {
        ObjectMapper mapper = new ObjectMapper();

        Parameters parameters;
        try {
            parameters = mapper.readValue(new File(fileName), Parameters.class);
        } catch (IOException e) {
            parameters = null;
        }

        return parameters;
    }

    private int width;

    public int getWidth() {
        return width;
    }

    private int height;

    public int getHeight() {
        return height;
    }


    private double startEnergy;

    public double getStartEnergy() {
        return startEnergy;
    }


    private double moveEnergy;

    public double getMoveEnergy() {
        return moveEnergy;
    }


    private int numAnimals;

    public int getNumAnimals() {
        return numAnimals;
    }


    private double plantEnergy;

    public double getPlantEnergy() {
        return plantEnergy;
    }


    private double jungleRatio;

    public double getJungleRatio() {
        return jungleRatio;
    }

}
