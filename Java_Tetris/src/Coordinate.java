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
}
