package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class World {
    public static void main(String[] args) {

        if (args[0] == null) {
            System.out.println("Error: enter path to config file");
            return;
        }

        Parameters parameters = Parameters.read(args[0]);
        if (parameters == null) {
            System.out.println("Error: can't read config file");
            return;
        }

        Engine engineA = new Engine(parameters);
        Engine engineB = new Engine(parameters);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Frame FrameA = new Frame(engineA, 10, 10);
                engineA.init();
                FrameA.getPanel().repaint();

                Frame FrameB = new Frame(engineB, 10, 510);
                engineB.init();
                FrameB.getPanel().repaint();

                Timer timer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!engineA.getStopped()) {
                            FrameA.updateLabels();
                            engineA.newDay();
                            FrameA.getPanel().repaint();
                        }
                        if (!engineB.getStopped()) {
                            FrameB.updateLabels();
                            engineB.newDay();
                            FrameB.getPanel().repaint();
                        }
                    }
                });
                timer.start();
            }
        });
    }
}
