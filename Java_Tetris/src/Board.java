import java.util.ArrayList;

public class Board
{
    private int max_x = Constants.MAXX();
    private int max_y = Constants.MAXY();
    private ArrayList<ArrayList<String>> landed = new ArrayList<ArrayList<String>>();

    Board()
    {
        for(int x = 0; x < max_x; x++)
        {
            landed.add(new ArrayList<String>());
            for(int y = 0; y < max_y; y++)
            {
                landed.get(x).add(new String());
            }
        }
    }

    public void output()
    {
        for(int y = 0; y < max_y; y++)
        {
            for(int x = 0; x < max_x; x++)
            {
                if(landed.get(x).get(y).isEmpty())
                {
                    System.out.print(". ");
                }
                else
                {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }
}
