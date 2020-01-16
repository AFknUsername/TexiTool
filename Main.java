import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    static Point[] points = new Point[2];
    static int numOfClicks = 0;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {


        //Rectangle windowSize = new Rectangle(screenSize);
        JFrame frame = new JFrame("what");

        frame.setSize(screenSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.setUndecorated(true);
        frame.setSize(screenSize);
        frame.setOpacity(0.40f);
        frame.setVisible(true);
        frame.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("pressed");
                if (numOfClicks == 0) {
//					points[0] = MouseInfo.getPointerInfo().getLocation();
                    points[0] = frame.getMousePosition();
                    System.out.println("Point 1: " + points[0].x+", "+points[0].y);
                } else {
//					points[1] = MouseInfo.getPointerInfo().getLocation();
                    points[1] = frame.getMousePosition();
                    System.out.println("Point 2: " + points[1].x+", "+points[1].y);
                }
                numOfClicks++;
                if (numOfClicks == 2) {
                    frame.dispose();
                    try {
                        startCapture(pagePrompt());
                    } catch (InterruptedException | IOException | AWTException ex) {
                        //ignore
                    }
                }
            }
        });
    }

    static int pagePrompt() {
        JOptionPane prompt = new JOptionPane();
        String pageNumberString = JOptionPane.showInputDialog(prompt, "How many pages does the book have?",
                "Page Check", JOptionPane.PLAIN_MESSAGE);
        int pageNumber = Integer.parseInt(pageNumberString);
        prompt.setSize(400, 300);
        prompt.setLocation(screenSize.width / 2, screenSize.height / 2);
        prompt.setVisible(true);
        return pageNumber;
    }

    static void startCapture(int p) throws InterruptedException, IOException, AWTException {
        Robot leeter = new Robot();
        Rectangle captureArea = new Rectangle(points[0].x, points[0].y, points[1].x - points[0].x, points[1].y - points[0].y);
        String OS = System.getProperty("os.name");
        String userName = System.getProperty("user.name");
        String format = "png";
        String directory;
        if (OS.contains("Windows")) {
            directory = "C://Users/"+userName+"/Desktop/scanner/";
        } else if (OS.contains("Mac")) {
            directory = "/Users/"+userName+"/Desktop/scanner/";
        } else {
            directory = "/home/"+userName+"/Desktop/scanner/";
        }

        if (!Files.isDirectory(Paths.get(directory))) {
            while(! new File(directory).mkdirs()) {/*wait*/}
        }

        System.out.println("Capturing...");
        leeter.mousePress(InputEvent.getMaskForButton(1));
        leeter.mouseRelease(InputEvent.getMaskForButton(1));
        Thread.sleep(2000);

        for (int i = 0; i < p; i++) {
            BufferedImage image = leeter.createScreenCapture(captureArea);
            ImageIO.write(image, format, new File(directory+"image"+i+"."+format));
            leeter.keyPress(40);
            leeter.keyPress(39);
            Thread.sleep(200);
        }
    }
}

