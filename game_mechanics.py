import random
import time
from threading import Thread

from shapes import *
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

class DownKeyThread(Thread):
    """
    Thread that presses the down key every now and then
    """
    def __init__(self, game_controller):
        Thread.__init__(self)
        self.game = game_controller

    def run(self):
        while not self.game.game_over:
            time.sleep(DOWNWARDS_INTERVAL)
            self.game.handle_move(DOWN)

class game_controller(Thread):
    """
    Main game loop and receives GUI callback events for keypresses etc...
    """

    def __init__(self):
        """
        Initialize the game...
        """
        Thread.__init__(self)
        self.score = 0
        self.level = 0
        self.delay = 1000    #ms
        self.thresholds = level_thresholds(500, NO_OF_LEVELS)
        self.board = Board(max_x=MAXX, max_y=MAXY)
        self.shape = self.get_next_shape()
        self.paused = False
        self.game_over = False

    def run(self):
        DownKeyThread(self).run()

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

    def update_score(self, rows_cleared):
        self.score += 100 * rows_cleared * rows_cleared

    def handle_move(self, direction):
        print direction
        if self.paused or self.game_over:
            pass
        elif self.check_move(direction):
            self.shape.coords = self.new_positions(direction)
        elif direction is DOWN: # if your heading down then the shape has 'landed'
            self.board.add_blocks_at(self.shape.coords, self.shape.color)
            self.update_score(self.board.check_for_complete_rows())
            self.shape = self.get_next_shape()

            if not self.board.are_empty(self.shape.coords): # can't place any more shapes, player has lost!
                self.board.add_blocks_at(self.shape.coords, self.shape.color)
                self.game_over = True

            # do we go up a level?
            if self.level < NO_OF_LEVELS and self.score >= self.thresholds[self.level]:
                self.level += 1
                self.delay -= 100

    def handle_rotate(self, clockwise):
        if not self.paused and self.check_rotate(clockwise):
            self.shape.coords = self.shape.rotated_positions(clockwise)

    def left_callback( self, event ):
        if self.shape:
            self.handle_move(LEFT)

    def right_callback( self, event ):
        if self.shape:
            self.handle_move(RIGHT)

    def up_callback( self, event ):
        if self.shape:
            # drop the tetrominoe to the bottom
            while self.check_move(DOWN):
                self.handle_move(DOWN)

            self.handle_move(DOWN)

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
        self.paused = not self.paused

    def get_next_shape( self ):
        """
        Returns next tetromino from queue
        """
        return get_next_shape_in_queue()