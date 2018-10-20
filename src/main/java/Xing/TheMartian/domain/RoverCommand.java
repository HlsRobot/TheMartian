package Xing.TheMartian.domain;

public class RoverCommand {
    private Rover rover;
    private String commandLine;

    private RoverCommand(Rover rover, String commandLine) {
        this.rover = rover;
        this.commandLine = commandLine;
    }

    public RoverCommand() {
    }

    public Rover getRover() {
        return rover;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public static class Builder {
        private Rover rover;
        private String commandLine;

        public Builder rover(final Rover rover) {
            this.rover = rover;
            return this;
        }

        public Builder commandLine(final String commandLine) {
            this.commandLine = commandLine;
            return this;
        }

        public RoverCommand build() {
            return new RoverCommand(this.rover, this.commandLine);
        }
    }

}
