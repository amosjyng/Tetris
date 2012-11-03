public class Move
{
    public int position;
    public int orientation;

    public Move(int movePosition, int moveOrientation)
    {
        position = movePosition;
        orientation = moveOrientation;
    }

    @Override
    public Move clone()
    {
        return new Move(position, orientation);
    }
}
