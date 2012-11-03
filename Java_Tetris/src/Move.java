public class Move
{
    public int position;
    public int orientation;
    public int heuristicScore = Integer.MIN_VALUE;

    public Move(int movePosition, int moveOrientation)
    {
        position = movePosition;
        orientation = moveOrientation;
    }

    @Override
    public Move clone()
    {
        Move clonedMove = new Move(position, orientation);
        clonedMove.heuristicScore = this.heuristicScore;
        return clonedMove;
    }
}
