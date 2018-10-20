package Xing.TheMartian.domain;

import java.util.Collections;
import java.util.List;

public class InputDto {
    private Coordinates limitCoordinates;
    private List<RoverCommand> roverCommandList;

    private InputDto(final Coordinates limitCoordinates, final List<RoverCommand> roverCommandList) {
        this.limitCoordinates = limitCoordinates;
        this.roverCommandList = roverCommandList;
    }

    public InputDto() {
    }

    public Coordinates getLimitCoordinates() {
        return limitCoordinates;
    }

    public List<RoverCommand> getRoverCommandList() {
        return roverCommandList;
    }

    public static class Builder {
        private Coordinates limitCoordinates;
        private List<RoverCommand> roverCommandList;

        public Builder limitCoordinates(final Coordinates limitCoordinates) {
            this.limitCoordinates = limitCoordinates;
            return this;
        }

        public Builder roverCommandList(final List<RoverCommand> roverCommandList) {
            this.roverCommandList = roverCommandList == null ? Collections.emptyList() : roverCommandList ;
            return this;
        }

        public InputDto build() {
            return new InputDto(this.limitCoordinates, this.roverCommandList);
        }
    }
}
