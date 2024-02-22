package hr.fer.zemris.java.gui.layouts;

public class RCPosition {

    private final int row;
    private final int column;

    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static RCPosition parse(String text) {
        String[] coords = text.split(",");

        if (coords.length != 2) {
            throw new IllegalArgumentException("Invalid position, should have row and column ints separated by comma.");
        }

        int row, column;
        try {
            row = Integer.parseInt(coords[0]);
            column = Integer.parseInt(coords[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid position argument, cannot parse.");
        }

        return new RCPosition(row, column);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RCPosition that = (RCPosition) o;

        if (row != that.row) return false;
        return column == that.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }
}
