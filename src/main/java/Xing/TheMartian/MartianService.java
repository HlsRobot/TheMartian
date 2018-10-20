package Xing.TheMartian;

import Xing.TheMartian.enums.Command;
import Xing.TheMartian.enums.Orientation;
import Xing.TheMartian.domain.Coordinates;
import Xing.TheMartian.domain.Rover;

import java.util.logging.Logger;

public class MartianService {

    private static final Logger LOGGER = Logger.getLogger( MartianService.class.getName() );

//    /**
//     * Public method called from the main to start the game
//     * Calls all the required methods for each work
//     *
//     * @throws IOException       because it calls getPlaysOfRound and getExtraPlays
//     */
//    void startMartianApp() throws IOException {
//        System.out.println("Welcome to Mars.");
//        System.out.println("****************************");
//        System.out.println("Please input x and y max coordinates of the plateau:");
//        List<Frame> frameList = this.initiateBowlingRounds();
//
//        for (int i = 0; i < frameList.size(); i++) {
//            if (i < frameList.size() - 1) {
//                System.out.println("Round: " + (i + 1));
//                this.getPlaysOfRound(this.bufferedReader, frameList, i);
//            } else {
//                if (frameList.get(i - 1).isStrike() || frameList.get(i - 1).isSpare()) {
//                    System.out.println("Extra round!");
//                    this.getExtraPlays(this.bufferedReader, frameList, i - 1);
//                }
//            }
//            this.printScores(frameList);
//        }
//        System.out.println();
//        // rounds.size() -1 for the additional play and -1 to avoid IndexOutOfBoundsException
//        System.out.println("Final score: " + this.calculateResult(frameList, frameList.size() - 2));
//        System.out.println("*** Game over ***");
//    }
//
//    /**
//     * Method that handles the user input from the console
//     *
//     * @param br      BufferedReader
//     * @return integer
//     * @throws IOException       in case of IO error
//     */
//    private int handleInput(final BufferedReader br) throws IOException {
//        boolean correctValue = false;
//        while (!correctValue) {
//
//            final String input = br.readLine();
//            try {
//                pinsHit = Integer.parseInt(input);
//                if (pinsHit < 0 || pinsHit > maxPins) {
//                    System.err.println("Please provide a value larger than 0");
//                } else {
//                    correctValue = true;
//                }
//            } catch (NumberFormatException nfe) {
//                System.err.println("Invalid Format!");
//            }
//
//        }
//
//        return pinsHit;
//    }

    public Rover startMartianApp(final Coordinates limitCoordinates, final Rover rover, final String commandLine) {
        this.executeCommands(limitCoordinates, rover, commandLine);
        return rover;
    }

    public void executeCommands(final Coordinates limitCoordinates, final Rover rover, final String commandLine) {
        final char[] commands = commandLine.toCharArray();

        for (int i = 0; i < commands.length; i++) {
            if (this.isValidCommand(commands[i])) {
                if (commands[i] == Command.MOVE.getCommandCode()) {
                    this.moveRover(rover, limitCoordinates);
                } else {
                    rover.changeOrientation(Command.getByCode(commands[i]));
                }
            } else {
                LOGGER.warning(String.format("Command %c was not recognized and was ignored", commands[i]));
            }
        }
    }

    private boolean isValidCommand(final char command) {
        return command == 'R' || command == 'M' || command == 'L';
    }

    private void moveRover(final Rover rover, final Coordinates limitCoordinates) {
        switch (Orientation.getByCode(rover.getOrientation())) {
            case NORTH:
                rover.moveNorth(limitCoordinates.getY());
                break;
            case WEST:
                rover.moveWest();
                break;
            case SOUTH:
                rover.moveSouth();
                break;
            case EAST:
                rover.moveEast(limitCoordinates.getX());
                break;
            default:
                break;
        }
    }

}
