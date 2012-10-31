public class Coordinate
{
    public int x;
    public int y;

    Coordinate(int xPos, int yPos)
    {
        x = xPos;
        y = yPos;
    }

    @Override
    public Coordinate clone()
    {
        return new Coordinate(x, y);
    }

    public Coordinate translate(Direction direction) throws Exception
    {
        switch (direction)
        { // note: assuming positive y axis points downwards
            case UP:
                return new Coordinate(x, y - 1);
            case DOWN:
                return new Coordinate(x, y + 1);
            case LEFT:
                return new Coordinate(x - 1, y);
            case RIGHT:
                return new Coordinate(x + 1, y);
            default:
                throw new Exception("Unrecognized direction " + direction);
        }
    }
}
