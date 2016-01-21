package forms.textlayout.philips.topquery;

import java.util.logging.Logger;

public class MarsRover {
    private static int x = 0; //Position on x-axis
    private static int y = 0; //Position on y-axis
    private static String dir = ""; //Direction the rover is facing

    //Logger to log information
    private Logger logger = Logger.getLogger("MarsRoboRover") ;

    public static void main(String[] args) {
        //Other Test Inputs
//		String currentPosition = "1 2 N";
//		String commands = "LMLMLMLMM"; 

        String currentPosition = "3 3 E";
        String commands = "MMRMMRMRRM";

        String[] positions = currentPosition.split(" ");
        x = Integer.valueOf(positions[0]);
        y = Integer.valueOf(positions[1]);
        dir = positions[2];

        for (char command : commands.toCharArray()) {
            rove(command);
        }

        //Output Status and Result
        System.out.println("currentPosition..." + currentPosition);
        System.out.println("commands..." + commands);
        System.out.println("newPosition..." + x + " " + y + " " + dir);
    }

    /**
     * All logic for movement is in this method
     * @param command char
     */
    private static void rove(char command) {
//		System.out.println("Start....." + x + " " + y + " " + direction + " " + command);
        if (dir.equalsIgnoreCase("N")) {
            switch (command) {
                case 'L':
                    dir = "W";
                    break;
                case 'R':
                    dir = "E";
                    break;
                case 'M':
                    y++;
                    break;
            }
        } else if (dir.equalsIgnoreCase("E")) {
            switch (command) {
                case 'L':
                    dir = "N";
                    break;
                case 'R':
                    dir = "S";
                    break;
                case 'M':
                    x++;
                    break;
            }
        } else if (dir.equalsIgnoreCase("S")) {
            switch (command) {
                case 'L':
                    dir = "E";
                    break;
                case 'R':
                    dir = "W";
                    break;
                case 'M':
                    y--;
                    break;
            }
        } else if (dir.equalsIgnoreCase("W")) {
            switch (command) {
                case 'L':
                    dir = "S";
                    break;
                case 'R':
                    dir = "N";
                    break;
                case 'M':
                    x--;
                    break;
            }
        }

//		System.out.println("End....." + x + " " + y + " " + direction + " " + command);
    }

    public static String location() {
        String loc = "[" + x + ", " + "y" + ", " + dir + "]";
        return loc;
    }

    public static int getX() {
        return x;
    }

    public static void setX(int x) {
        MarsRover.x = x;
    }

    public static int getY() {
        return y;
    }

    public static void setY(int y) {
        MarsRover.y = y;
    }

    public static String getDir() {
        return dir;
    }

    public static void setDir(String dir) {
        MarsRover.dir = dir;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}