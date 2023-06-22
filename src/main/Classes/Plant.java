package Classes;

public class Plant {
    private Vector2d position;

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public Plant(Vector2d position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "position=" + position.getX() + ", " + position.getY() +
                '}';
    }
}
