from constants import MAXX

class shape(object):
    """
    Shape is the  Base class for the game pieces e.g. square, T, S, Z, L,
    reverse L and I. Shapes are constructed of blocks. 
    """

    def __init__(self, coords, color ):
        """
        Initialise the shape base.
        """
        self.coords = coords
        self.color = color

    def translated_positions(self, direction, coords = None):
        if coords is None:
            coords = self.coords
        new_positions = []
        for coord in coords:
            new_positions.append((coord[0] + direction[0], coord[1] + direction[1]))

        return new_positions

    def rotated_positions(self, clockwise = True):
        """
        Rotate the blocks around the 'middle' block, 90-degrees. The
        middle block is always the index 0 block in the list of blocks
        that make up a shape.
        """
        middle = self.coords[0]
        rel = []
        for (x, y) in self.coords: # get position of blocks relative to center block
            rel.append( (x - middle[0], y - middle[1] ) )

        new_positions = []

        # to rotate 90-degrees (x,y) = (-y, x)
        for idx in xrange(len(self.coords)):
            rel_x, rel_y = rel[idx]
            if clockwise:
                x = middle[0] + rel_y
                y = middle[1] - rel_x
                new_positions.append((x, y))
            else:
                x = middle[0] - rel_y
                y = middle[1] + rel_x
                new_positions.append((x, y))

        # allow shape to be rotated even when next to the edges of the board
        top = self.topmost_position(new_positions)
        if top < 0:
            new_positions = self.translated_positions((0, -top), new_positions)
        left = self.leftmost_position(new_positions)
        if left < 0:
            new_positions = self.translated_positions((-left, 0), new_positions)
        right = self.rightmost_position(new_positions)
        if right >= MAXX:
            new_positions = self.translated_positions((MAXX - right - 1, 0), new_positions)

        return new_positions

    def rotate(self, clockwise = True): # clockwise
        self.coords = self.rotated_positions(clockwise)

    def leftmost_position(self, coords = None): # x-position of left-most piece
        if coords is None:
            coords = self.coords
        x_pos = 999999 # todo: make this max-int
        for coord in coords:
            if coord[0] < x_pos:
                x_pos = coord[0]

        return x_pos

    def rightmost_position(self, coords = None):
        if coords is None:
            coords = self.coords
        x_pos = 0
        for coord in coords:
            if coord[0] > x_pos:
                x_pos = coord[0]

        return x_pos

    def topmost_position(self, coords = None):
        if coords is None:
            coords = self.coords
        y_pos = 999999
        for coord in coords:
            if coord[1] < y_pos:
                y_pos = coord[1]

        return y_pos

    def width(self):
        return self.rightmost_position() - self.leftmost_position()

class shape_limited_rotate( shape ):
    """
    This is a base class for the shapes like the S, Z and I that don't fully
    rotate (which would result in the shape moving *up* one block on a 180).
    Instead they toggle between 90 degrees clockwise and then back 90 degrees
    anti-clockwise.
    """
    def __init__( self, coords, colour ):
        self.clockwise = True
        super(shape_limited_rotate, self).__init__(coords, colour)

    def rotate(self, clockwise=True):
        """
        Clockwise, is used to indicate if the shape should rotate clockwise
        or back again anti-clockwise. It is toggled.
        """
        super(shape_limited_rotate, self).rotate(clockwise=self.clockwise)
        if self.clockwise:
            self.clockwise=False
        else:
            self.clockwise=True

class square_shape( shape ):
    def __init__(self):
        coords = [(4,0),(5,0),(4,1),(5,1)]
        self.orientations = 1
        return super(square_shape, self).__init__(coords, "red")

    def rotate(self, clockwise=True):
        """
        Override the rotate method for the square shape to do exactly nothing!
        """
        pass

class t_shape( shape ):
    def __init__(self):
        coords = [(4,0),(3,0),(5,0),(4,1)]
        self.orientations = 4
        return super(t_shape, self).__init__(coords, "yellow")

class l_shape( shape ):
    def __init__(self):
        coords = [(4,0),(3,0),(5,0),(3,1)]
        self.orientations = 4
        return super(l_shape, self).__init__(coords, "orange")

class reverse_l_shape( shape ):
    def __init__(self):
        coords = [(5,0),(4,0),(6,0),(6,1)]
        self.orientations = 4
        return super(reverse_l_shape, self).__init__(coords, "green")

class z_shape( shape_limited_rotate ):
    def __init__(self):
        coords = [(5,0),(4,0),(5,1),(6,1)]
        self.orientations = 2
        return super(z_shape, self).__init__(coords, "purple")

class s_shape( shape_limited_rotate ):
    def __init__(self):
        coords = [(5,1),(4,1),(5,0),(6,0)]
        self.orientations = 2
        return super(s_shape, self).__init__(coords, "magenta")

class i_shape( shape_limited_rotate ):
    def __init__(self):
        coords = [(4,0),(3,0),(5,0),(6,0)]
        self.orientations = 2
        return super(i_shape, self).__init__(coords, "blue")
