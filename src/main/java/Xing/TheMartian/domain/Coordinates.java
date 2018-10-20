package Xing.TheMartian.domain;

public class Coordinates {

    private int x;
    private int y;

    private Coordinates(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates() {}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static class Builder {
        private int x;
        private int y;

        public Builder x(final int x) {
            this.x = x;
            return this;
        }

        public Builder y(final int y) {
            this.y = y;
            return this;
        }

        public Coordinates build() {
            return new Coordinates(this.x, this.y);
        }

    }
}
