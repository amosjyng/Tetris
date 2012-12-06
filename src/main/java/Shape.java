import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Shape
{
    private ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
    private String color;
    private String name;
    private int orientations;

    public Shape(String shapeName, ArrayList<Coordinate> coords, String initColor, int numberOrientations)
    {
        coordinates = coords;
        color = initColor;
        name = shapeName;
        orientations = numberOrientations;
    }

    @Override
    public Shape clone()
    {
        ArrayList<Coordinate> clonedCoordinates = new ArrayList<Coordinate>();
        for(Coordinate thisCoordinate : this.coordinates)
        {
            clonedCoordinates.add(thisCoordinate.clone());
        }
        return new Shape(this.name, clonedCoordinates, this.color, this.orientations);
    }

    public ArrayList<Coordinate> getCoordinates()
    {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Coordinate> newCoordinates)
    {
        coordinates = newCoordinates;
    }

    public String getColor()
    {
        return color;
    }

    public String getName()
    {
        return name;
    }

    public int getOrientations()
    {
        return orientations;
    }

    private Map<String, Integer> getMostExtremePositions(ArrayList<Coordinate> positions)
    {
        int leftmost = 0, rightmost = Constants.MAXX - 1, topmost = 0;
        for(Coordinate coordinate : positions)
        {
            if(coordinate.y < topmost)
            {
                topmost = coordinate.y;
            }
            if(coordinate.x < leftmost)
            {
                leftmost = coordinate.x;
            }
            else if(coordinate.x > rightmost)
            {
                rightmost = coordinate.x;
            }
        }

        Map<String, Integer> extremePositions = new HashMap<String, Integer>();
        extremePositions.put("leftmost", leftmost);
        extremePositions.put("rightmost", rightmost);
        extremePositions.put("topmost", topmost);

        return extremePositions;
    }

    public ArrayList<Coordinate> getTranslatedCoordinates(Direction direction)
    {
        return getTranslatedCoordinates(coordinates, direction, 1);
    }

    private ArrayList<Coordinate> getTranslatedCoordinates(ArrayList<Coordinate> coordinateArrayList,
                                                           Direction direction, int howMuch)
    {
        ArrayList<Coordinate> translatedCoordinates = new ArrayList<Coordinate>();
        for(Coordinate coordinate : coordinateArrayList)
        {
            translatedCoordinates.add(coordinate.translate(direction, howMuch));
        }
        return translatedCoordinates;
    }

    public ArrayList<Coordinate> getRotatedCoordinates() // rotates clockwise
    { // todo: check middle squares!
        Coordinate middle = coordinates.get(0);
        ArrayList<Coordinate> relativeCoordinates = new ArrayList<Coordinate>();
        for(Coordinate coordinate : coordinates)
        {
            relativeCoordinates.add(new Coordinate(coordinate.x - middle.x, coordinate.y - middle.y));
        }

        ArrayList<Coordinate> rotatedCoordinates = new ArrayList<Coordinate>();
        for(int i = 0; i < coordinates.size(); i++)
        {
            rotatedCoordinates.add(new Coordinate(middle.x + relativeCoordinates.get(i).y,
                                                  middle.y - relativeCoordinates.get(i).x));
        }

        // allow shape to be rotated even when next to edges of board
        Map<String, Integer> extremePositions = getMostExtremePositions(rotatedCoordinates);
        if(extremePositions.get("leftmost") < 0)
        {
            return getTranslatedCoordinates(rotatedCoordinates, Direction.RIGHT, -extremePositions.get("leftmost"));
        }
        else if(extremePositions.get("rightmost") >= Constants.MAXX)
        {
            return getTranslatedCoordinates(rotatedCoordinates, Direction.LEFT,
                                            extremePositions.get("rightmost") - Constants.MAXX + 1);
        }
        if(extremePositions.get("topmost") < 0)
        {
            return getTranslatedCoordinates(rotatedCoordinates, Direction.DOWN, -extremePositions.get("topmost"));
        }

        return rotatedCoordinates; // otherwise we're done!
    }

    // note: is a switch statement really the best way to do this?
    public static Shape randomShape(Random random) throws Exception
    {
        switch (random.nextInt(7))
        {
            case 0:
                return square();
            case 1:
                return t();
            case 2:
                return l();
            case 3:
                return reverseL();
            case 4:
                return z();
            case 5:
                return s();
            case 6:
                return i();
            default:
                throw new Exception("Generated random integer outside of range 0 - 6");
        }
    }

    // todo: Refactor shape colors into Constants
    public static Shape square()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(4, 0));
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(4, 1));
        squareCoords.add(new Coordinate(5, 1));
        return new Shape("Square", squareCoords, "red", 1); // todo: put these colors in constants?
    }

    public static Shape t()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(4, 0));
        squareCoords.add(new Coordinate(3, 0));
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(4, 1));
        return new Shape("T", squareCoords, "yellow", 4);
    }

    public static Shape l()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(4, 0));
        squareCoords.add(new Coordinate(3, 0));
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(3, 1));
        return new Shape("L", squareCoords, "orange", 4);
    }

    public static Shape reverseL()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(4, 0));
        squareCoords.add(new Coordinate(6, 0));
        squareCoords.add(new Coordinate(6, 1));
        return new Shape("Reverse L", squareCoords, "green", 4);
    }

    public static Shape z()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(4, 0));
        squareCoords.add(new Coordinate(5, 1));
        squareCoords.add(new Coordinate(6, 1));
        return new Shape("Z", squareCoords, "purple", 2);
    }

    public static Shape s()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(5, 1));
        squareCoords.add(new Coordinate(4, 1));
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(6, 0));
        return new Shape("S", squareCoords, "magenta", 2);
    }

    public static Shape i()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(4, 0));
        squareCoords.add(new Coordinate(3, 0));
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(6, 0));
        return new Shape("I", squareCoords, "blue", 2);
    }
}
