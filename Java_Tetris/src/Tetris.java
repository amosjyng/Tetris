class Tetris
{
    public static void main(String[] args)
    {
        Board board = new Board();
        Shape square = Shape.square();
        board.addBlocksAt(square.getCoordinates(), square.getColor());
        board.output();
    }
}