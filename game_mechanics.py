import random

SCALE = 20
OFFSET = 3
MAXX = 10
MAXY = 22

NO_OF_LEVELS = 10

LEFT = "left"
RIGHT = "right"
DOWN = "down"

SHAPES_QUEUE_SIZE = 1

direction_d = { "left": (-1, 0), "right": (1, 0), "down": (0, 1) }

from shapes import *
from gui import GameWindow
from board import Board

#lookup table
shapes = [square_shape, t_shape, l_shape, reverse_l_shape, z_shape, s_shape, i_shape ]

shapes_queue = []

for _ in range(SHAPES_QUEUE_SIZE): # initialize the queue
    shapes_queue.append(random.choice(shapes))

def get_next_shape_in_queue():
    """
    Returns the next shape in the queue, and puts another shape into the queue
    """
    shapes_queue.append(random.choice(shapes))
    print "Queued shape is {0}".format(shapes_queue[-1])
    return shapes_queue.pop(0)

def level_thresholds( first_level, no_of_levels ):
    """
    Calculates the score at which the level will change, for n levels.
    """
    thresholds =[]
    for x in xrange( no_of_levels ):
        multiplier = 2**x
        thresholds.append( first_level * multiplier )

    return thresholds


class game_controller(object):
    """
    Main game loop and receives GUI callback events for keypresses etc...
    """

    def __init__(self, tk_root):
        """
        Intialise the game...
        """
        self.score = 0
        self.level = 0
        self.delay = 1000    #ms

        self.thresholds = level_thresholds(500, NO_OF_LEVELS)

        self.game_window = GameWindow(tk_root, self)

        self.board = Board(
            tk_root,
            scale=SCALE,
            max_x=MAXX,
            max_y=MAXY,
            offset=OFFSET
        )

        self.game_over = False
        self.shape = self.get_next_shape()
        self.after_id = self.game_window.parent.after(self.delay, self.move_my_shape)

    def handle_move(self, direction):
        #if you can't move then you've hit something
        if not self.shape.move(direction):
            # if your heading down then the shape has 'landed'
            if direction == DOWN:
                self.score += self.board.check_for_complete_row(
                    self.shape.blocks
                )
                del self.shape
                self.shape = self.get_next_shape()

                # If the shape returned is None, then this indicates that
                # that the check before creating it failed and the
                # game is over!
                if self.shape is None:
                    self.game_window.show_game_over(self.score, self.level)
                    self.game_over = True
                    return False

                # do we go up a level?
                if (self.level < NO_OF_LEVELS and
                    self.score >= self.thresholds[self.level]):
                    self.level += 1
                    self.delay -= 100

                self.game_window.show_score(self.score, self.level)

                # Signal that the shape has 'landed'
                return False
        return True

    def left_callback( self, event ):
        if self.shape:
            self.handle_move(LEFT)

    def right_callback( self, event ):
        if self.shape:
            self.handle_move(RIGHT)

    def up_callback( self, event ):
        if self.shape:
            # drop the tetrominoe to the bottom
            while self.handle_move(DOWN):
                pass

    def down_callback( self, event ):
        if self.shape:
            self.handle_move(DOWN)

    def a_callback( self, event):
        if self.shape:
            self.shape.rotate(clockwise=True)

    def s_callback( self, event):
        if self.shape:
            self.shape.rotate(clockwise=False)

    def p_callback(self, event):
        self.game_window.parent.after_cancel(self.after_id)
        self.game_window.show_pause()
        self.after_id = self.game_window.parent.after(self.delay, self.move_my_shape)

    def move_my_shape( self ):
        if self.shape and not self.game_over:
            self.handle_move(DOWN)
            self.after_id = self.game_window.parent.after(self.delay, self.move_my_shape)

    def get_next_shape( self ):
        """
        Get next tetromino from queue and put it in the board
        """
        return get_next_shape_in_queue().check_and_create(self.board)