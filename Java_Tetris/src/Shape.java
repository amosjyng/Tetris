import java.util.ArrayList;
import java.util.Random;

public class Shape
{
    private ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
    private ArrayList<Coordinate> initialCoordinates = new ArrayList<Coordinate>();
    private String color;
    private String name;

    private static Random random = new Random();

    Shape(String shapeName, ArrayList<Coordinate> coords, String initColor)
    {
        coordinates = coords;
        for(Coordinate coord : coords)
        {
            initialCoordinates.add(coord.clone());
        }
        color = initColor;
        name = shapeName;
    }

    public ArrayList<Coordinate> getCoordinates()
    {
        return coordinates;
    }

    public String getColor()
    {
        return color;
    }

    public String getName()
    {
        return name;
    }

    public ArrayList<Coordinate> getTranslatedCoordinates(Direction direction) throws Exception
    {
        ArrayList<Coordinate> translatedCoordinates = new ArrayList<Coordinate>();
        for(Coordinate coordinate : coordinates)
        {
            translatedCoordinates.add(coordinate.translate(direction));
        }
        return translatedCoordinates;
    }

    public void translate(Direction direction) throws Exception
    {
        for(int i = 0; i < coordinates.size(); i++)
        {
            /*
            note: should I implement an actual translate function in Coordinate that actually modifies the Coordinate
            instead of creating another copy of the coordinate? Could this prevent messy issues later on with cloning
            a Shape?
             */
            coordinates.set(i, coordinates.get(i).translate(direction));
        }
    }

    public static Shape randomShape() throws Exception // note: is a switch statement really the best way to do this?
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
                throw new Exception("Generated random integer outside of range 0 - 7");
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
        return new Shape("Square", squareCoords, "red");
    }

    public static Shape t()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(4, 0));
        squareCoords.add(new Coordinate(3, 0));
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(4, 1));
        return new Shape("T", squareCoords, "yellow");
    }

    public static Shape l()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(4, 0));
        squareCoords.add(new Coordinate(3, 0));
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(3, 1));
        return new Shape("L", squareCoords, "orange");
    }

    public static Shape reverseL()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(4, 0));
        squareCoords.add(new Coordinate(6, 0));
        squareCoords.add(new Coordinate(6, 1));
        return new Shape("Reverse L", squareCoords, "green");
    }

    public static Shape z()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(4, 0));
        squareCoords.add(new Coordinate(5, 1));
        squareCoords.add(new Coordinate(6, 1));
        return new Shape("Z", squareCoords, "purple");
    }

    public static Shape s()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(5, 1));
        squareCoords.add(new Coordinate(4, 1));
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(6, 0));
        return new Shape("S", squareCoords, "magenta");
    }

    public static Shape i()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(4, 0));
        squareCoords.add(new Coordinate(3, 0));
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(6, 0));
        return new Shape("I", squareCoords, "blue");
    }
}
