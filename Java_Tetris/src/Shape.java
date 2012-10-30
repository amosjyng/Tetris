import java.util.ArrayList;

public class Shape
{
    private ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
    private ArrayList<Coordinate> initialCoordinates = new ArrayList<Coordinate>();
    private String color;

    Shape(ArrayList<Coordinate> coords, String initColor)
    {
        coordinates = coords;
        for(Coordinate coord : coords)
        {
            initialCoordinates.add(coord.clone());
        }
        color = initColor;
    }

    public ArrayList<Coordinate> getCoordinates()
    {
        return coordinates;
    }

    public String getColor()
    {
        return color;
    }

    // todo: Refactor shape colors into Constants
    public static Shape square()
    {
        ArrayList<Coordinate> squareCoords = new ArrayList<Coordinate>();
        squareCoords.add(new Coordinate(4, 0));
        squareCoords.add(new Coordinate(5, 0));
        squareCoords.add(new Coordinate(4, 1));
        squareCoords.add(new Coordinate(5, 1));
        return new Shape(squareCoords, "red");
    }
}
