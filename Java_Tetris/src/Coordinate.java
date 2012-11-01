public class Coordinate
{
    public int x;
    public int y;

    public Coordinate(int xPos, int yPos)
    {
        x = xPos;
        y = yPos;
    }

    @Override
    public Coordinate clone()
    {
        return new Coordinate(x, y);
    }

    public Coordinate translate(Direction direction)
    {
        return translate(direction, 1);
    }

    public Coordinate translate(Direction direction, int howMuch)
    {
        switch (direction)
        { // note: assuming positive y axis points downwards
            case UP:
                return new Coordinate(x, y - howMuch);
            case DOWN:
                return new Coordinate(x, y + howMuch);
            case LEFT:
                return new Coordinate(x - howMuch, y);
            case RIGHT:
                return new Coordinate(x + howMuch, y);
            default:
                throw new RuntimeException("Unrecognized direction " + direction);
        }
    }
}
