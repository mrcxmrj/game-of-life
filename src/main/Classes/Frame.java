package Classes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class Frame extends JFrame {
    private final Panel Panel;

    public JPanel getPanel() {
        return Panel;
    }

    private JButton buttonStopStart;

    private JLabel labelDay;
    private JLabel labelAnimalNumber;
    private JLabel labelPlantNumber;
    private JLabel labelAverageEnergy;
    private JLabel labelAverageLifeExpectancy;


    private final Engine engine;

    public void setStopped(boolean stopped) {
        engine.setStopped(stopped);
        buttonStopStart.setText(engine.getStopped() ? "Start" : "Stop");
    }

    public Frame(Engine engine, int x, int y) {
        super("EvolutionSimulator");

        this.engine = engine;

        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        Panel = createPanel();
        add(Panel, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(x, y);
        setVisible(true);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        buttonStopStart = new JButton();
        Dimension width = new Dimension(100, 100);
        buttonStopStart.setMinimumSize(width);
        buttonStopStart.setMaximumSize(width);
        setStopped(false);
        buttonStopStart.addActionListener(e -> setStopped(!engine.getStopped()));
        panel.add(buttonStopStart);

        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton buttonWriteToFile = new JButton();
        buttonWriteToFile.setMinimumSize(width);
        buttonWriteToFile.setMaximumSize(width);
        buttonWriteToFile.setText("Write to file");
        buttonWriteToFile.addActionListener(e -> {
            try {
                engine.stats.writeToFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        panel.add(buttonWriteToFile);

        labelDay = this.newLabel(panel, "day: ");
        panel.add(labelDay);

        labelAnimalNumber = this.newLabel(panel, "number of animals: ");
        panel.add(labelAnimalNumber);

        labelPlantNumber = this.newLabel(panel, "number of plants: ");
        panel.add(labelPlantNumber);

        labelAverageEnergy = this.newLabel(panel, "average energy of living animals: ");
        panel.add(labelAverageEnergy);

        labelAverageLifeExpectancy = this.newLabel(panel, "average life expectancy of dead animals: ");
        panel.add(labelAverageLifeExpectancy);


        return panel;
    }

    private Panel createPanel() {
        return new Panel(engine);
    }

    private JLabel newLabel(JPanel panel, String text) {
        JLabel labelDayCaption = new JLabel(text);
        panel.add(labelDayCaption);

        Dimension width = new Dimension(40, 100);
        JLabel label = new JLabel();
        label.setMinimumSize(width);
        label.setMaximumSize(width);
        return label;
    }

    public void updateLabels() {
        labelDay.setText(Integer.toString(engine.getDay()));
        labelAnimalNumber.setText(Integer.toString(engine.getAnimals().size()));
        labelPlantNumber.setText(Integer.toString(engine.getPlants().size()));
        labelAverageEnergy.setText(Double.toString(engine.stats.getAverageEnergy()));
        labelAverageLifeExpectancy.setText(Double.toString(engine.stats.getAverageLifeExpectancy()));

    }
}
