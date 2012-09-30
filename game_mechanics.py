import random

from shapes import *
from gui import GameWindow, BoardCanvas
from board import Board
from constants import *

#lookup table
shapes = [square_shape, t_shape, l_shape, reverse_l_shape, z_shape, s_shape, i_shape ]

shapes_queue = []

for _ in range(SHAPES_QUEUE_SIZE): # initialize the queue
    shapes_queue.append(random.choice(shapes)())

def get_next_shape_in_queue():
    """
    Returns the next shape in the queue, and puts another shape into the queue
    """
    shapes_queue.append(random.choice(shapes)())
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
        self.board = Board(max_x=MAXX, max_y=MAXY)
        self.game_over = False
        self.shape = self.get_next_shape()

        self.game_window = GameWindow(tk_root, self)
        self.board_canvas = BoardCanvas(tk_root, self.board, SCALE, OFFSET)
        self.update_display()

        self.after_id = self.game_window.parent.after(self.delay, self.move_my_shape)

    def new_positions(self, direction):
        """
        Get new positions of shape after it has moved in a direction
        """
        dx, dy = direction_d[direction]
        block_positions = []

        for (x, y) in self.shape.coords:
            block_positions.append((x + dx, y + dy))

        return block_positions

    def check_move(self, direction):
        """
        See if the current shape can be moved in that direction
        """
        return self.board.are_empty(self.new_positions(direction))

    def check_rotate(self, direction):
        """
        See if the current shape can be rotated in that direction
        """
        return self.board.are_empty(self.shape.rotated_positions(direction))

    def update_display(self):
        self.game_window.show_score(self.score, self.level)
        self.board_canvas.draw(self.shape)

    def update_score(self, rows_cleared):
        self.score += 100 * rows_cleared * rows_cleared

    def handle_move(self, direction):
        if self.check_move(direction):
            self.shape.coords = self.new_positions(direction)
        elif direction is DOWN: # if your heading down then the shape has 'landed'
            self.board.add_blocks_at(self.shape.coords, self.shape.color)
            self.update_score(self.board.check_for_complete_rows())
            self.shape = self.get_next_shape()

            if not self.board.are_empty(self.shape.coords): # can't place any more shapes, player has lost!
                self.board.add_blocks_at(self.shape.coords, self.shape.color)
                self.update_display()
                self.game_window.show_game_over(self.score, self.level)
                self.game_over = True
                return False

            # do we go up a level?
            if (self.level < NO_OF_LEVELS and
                self.score >= self.thresholds[self.level]):
                self.level += 1
                self.delay -= 100

            self.update_display()

            # Signal that the shape has 'landed'
            return False
        self.update_display()
        return True

    def handle_rotate(self, clockwise):
        if self.check_rotate(clockwise):
            self.shape.coords = self.shape.rotated_positions(clockwise)
        self.update_display()

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
            self.handle_rotate(clockwise=True)

    def s_callback( self, event):
        if self.shape:
            self.handle_rotate(clockwise=False)

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
        Returns next tetromino from queue
        """
        return get_next_shape_in_queue()