import java.util.ArrayList;

public class Board
{
    private ArrayList<ArrayList<String>> landed = new ArrayList<ArrayList<String>>();

    public Board()
    {
        for(int x = 0; x < Constants.MAXX; x++)
        {
            landed.add(new ArrayList<String>());
            for(int y = 0; y < Constants.MAXY; y++)
            {
                landed.get(x).add(new String());
            }
        }
    }

    @Override
    public Board clone()
    {
        // todo: rename newBoard to clonedBoard like other classes' clone functions
        Board newBoard = new Board();
        for(int x = 0; x < Constants.MAXX; x++)
        {
            for(int y = 0; y < Constants.MAXY; y++)
            {
                newBoard.landed.get(x).set(y, this.landed.get(x).get(y));
            }
        }
        return newBoard;
    }

    public void output()
    {
        for(int y = 0; y < Constants.MAXY; y++)
        {
            for(int x = 0; x < Constants.MAXX; x++)
            {
                String square = landed.get(x).get(y);
                if(square.isEmpty())
                {
                    System.out.print(". ");
                }
                else
                {
                    System.out.print(square.charAt(0) + " ");
                }
            }
            System.out.println();
        }
    }

    public String getSquare(int x, int y)
    {
        return landed.get(x).get(y);
    }

    public double getAverageHeight()
    {
        int cumulativeHeight = 0, totalBlocks = 0;
        for(int x = 0; x < Constants.MAXX; x++)
        {
            for(int y = 0; y < Constants.MAXY; y++)
            {
                if(!landed.get(x).get(y).isEmpty())
                {
                    cumulativeHeight += Constants.MAXY - y; // positive y points downwards
                    totalBlocks++;
                }
            }
        }

        return cumulativeHeight * 1.0 / totalBlocks;
    }

    public int getOverhangCount()
    {
        int overhangs = 0;
        for(int x = 0; x < Constants.MAXX; x++)
        {
            boolean encounteredSquare = false;
            for(int y = 0; y < Constants.MAXY; y++)
            {
                if(landed.get(x).get(y).isEmpty())
                {
                    if(encounteredSquare)
                    {
                        overhangs++;
                    }
                }
                else
                {
                    encounteredSquare = true;
                }
            }
        }

        return overhangs;
    }

    private boolean isValid(Coordinate coordinate)
    {
        // first check if it's not out of bounds
        if(coordinate.x < 0 || coordinate.x >= Constants.MAXX || coordinate.y < 0 || coordinate.y >= Constants.MAXY)
        {
            return false;
        } // then check if it's already occupied by a spot on the board
        else if(!landed.get(coordinate.x).get(coordinate.y).isEmpty())
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    public boolean areValid(ArrayList<Coordinate> coordinates)
    {
        for(Coordinate coordinate : coordinates)
        {
            if(!isValid(coordinate))
            {
                return false;
            }
        }
        return true;
    }

    public boolean areEmpty(ArrayList<Coordinate> coordinates) // weaker check than areValid
    {
        for(Coordinate coordinate : coordinates)
        {
            if(!landed.get(coordinate.x).get(coordinate.y).isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    public void addBlocksAt(ArrayList<Coordinate> coordinates, String color)
    {
        for(Coordinate coordinate : coordinates)
        {
            landed.get(coordinate.x).set(coordinate.y, color);
        }
    }

    private void removeBlocksAt(ArrayList<Coordinate> coordinates)
    {
        for(Coordinate coordinate : coordinates)
        {
            landed.get(coordinate.x).set(coordinate.y, "");
        }
    }

    private void removeRow(int row)
    {
        for(int y = row; y > 0; y--)
        {
            for(int x = 0; x < Constants.MAXX; x++)
            {
                landed.get(x).set(y, landed.get(x).get(y - 1));
            }
        }
        for(int x = 0; x < Constants.MAXX; x++) // clear top row
        {
            landed.get(0).set(x, new String());
        }
    }

    private boolean isRowFull(int row)
    {
        for(int x = 0; x < Constants.MAXX; x++)
        {
            if(landed.get(x).get(row).isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Integer> clearCompletedRows()
    {
        ArrayList<Integer> removedRows = new ArrayList<Integer>();
        for(int y = 0; y < Constants.MAXY; y++)
        {
            if(isRowFull(y))
            {
                removedRows.add(y);
            }
        }
        for(int y : removedRows)
        {
            removeRow(y);
        }
        return removedRows;
    }

    public void restore(ArrayList<Integer> removedRows, Shape landedShape)
    {
        for(int i = removedRows.size() - 1; i >= 0; i--)
        {
            for(int x = 0; x < Constants.MAXX; x++)
            {
                for(int y = 0; y < removedRows.get(i); y++)
                {
                    landed.get(x).set(y, landed.get(x).get(y + 1));
                }
                landed.get(x).set(removedRows.get(i), Constants.REMOVED_SQUARE_COLOR);
            }
        }
        removeBlocksAt(landedShape.getCoordinates());
    }
}
