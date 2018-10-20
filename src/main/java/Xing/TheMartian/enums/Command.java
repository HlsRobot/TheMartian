package Xing.TheMartian.enums;

public enum Command {
    MOVE('M'), RIGHT('R'), LEFT('L'), UNKNOWN('U');

    private char commandCode;

    Command(char commandCode) {
        this.commandCode = commandCode;
    }

    public char getCommandCode() {
        return this.commandCode;
    }

    public static Command getByCode(char code) {
        for(Command command : values()) {
            if(command.commandCode == code) {
                return command;
            }
        }
        return UNKNOWN;
    }

}
