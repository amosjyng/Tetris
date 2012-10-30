import random
from time import sleep
from copy import deepcopy
from threading import Thread

from shapes import *
from board import Board
from constants import *

shapes = [square_shape, t_shape, l_shape, reverse_l_shape, z_shape, s_shape, i_shape ]

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
            sleep(DOWNWARDS_INTERVAL - self.game.level * 0.1)
            self.game.handle_move('down')

class game_controller():
    """
    Main game loop and receives GUI callback events for keypresses etc...
    """

    def __init__(self):
        """
        Initialize the game...
        """

        self.shapes_queue = []
        for _ in range(SHAPES_QUEUE_SIZE): # initialize the queue
            self.shapes_queue.append(random.choice(shapes)())

        self.score = 0
        self.level = 0
        self.delay = 1000    #ms
        self.thresholds = level_thresholds(500, NO_OF_LEVELS)
        self.board = Board()
        self.under_simulation = False
        self.currentShape = self.get_next_shape()
        self.paused = False
        self.game_over = False
        self.backup_info = []

    def create_copy(self):
        """
        Creates a clone of this game (for AI purposes)
        """
        clone = game_controller()
        clone.shapes_queue = deepcopy(self.shapes_queue)
        clone.score = deepcopy(self.score)
        clone.board = deepcopy(self.board)
        clone.currentShape = deepcopy(self.currentShape)
        clone.under_simulation = True # pretty much the only reason why a copy is needed

        return clone

    def get_next_shape_in_queue(self, add_new_shape = True):
        """
        Returns the next shape in the queue, and puts another shape into the queue
        """
        if add_new_shape or len(self.shapes_queue) is 0:
            self.shapes_queue.append(random.choice(shapes)())
        return self.shapes_queue.pop(0)

    def check_move(self, direction):
        """
        See if the current shape can be moved in that direction
        """
        return self.board.are_empty(self.currentShape.translated_positions(direction_d[direction]))

    def check_rotate(self, direction):
        """
        See if the current shape can be rotated in that direction
        """
        return self.board.are_empty(self.currentShape.rotated_positions(direction))

    def update_score(self, rows_cleared):
        self.score += 100 * rows_cleared * rows_cleared

    def backup(self):
        new_backup_info = {} # todo: make this a dictionary literal
        new_backup_info['score'] = self.score
        new_backup_info['shape'] = self.currentShape
        new_backup_info['deleted_rows'] = []
        new_backup_info['drop_distance'] = 0
        self.backup_info.append(new_backup_info)

    def undo(self):
        last_move = self.backup_info.pop()
        self.shapes_queue.insert(0, self.currentShape)
        self.score = last_move['score']
        self.currentShape = last_move['shape']
        self.board.restore(last_move['deleted_rows'], self.currentShape.coords)
        self.currentShape.coords = self.currentShape.translated_positions((0, -last_move['drop_distance']))
        self.game_over = False

    def land_shape(self):
        self.backup()
        self.board.add_blocks_at(self.currentShape.coords, self.currentShape.color)
        deleted_rows = self.board.check_for_complete_rows()
        self.update_score(len(deleted_rows))
        self.currentShape = self.get_next_shape()

        if not self.board.are_empty(self.currentShape.coords): # can't place any more shapes,
            # player has lost!
            if not self.under_simulation:
                self.board.add_blocks_at(self.currentShape.coords, self.currentShape.color)
            self.game_over = True

        # do we go up a level?
        if self.level < NO_OF_LEVELS and self.score >= self.thresholds[self.level]:
            self.level += 1
            self.delay -= 100

        return deleted_rows

    def handle_move(self, direction):
        if self.paused or self.game_over:
            # note: this is probably going to cause a problem. the AI needs to handle game over's
            # correctly
            #return False # note: I believe this momentarily solved some issues with this
            # function not returning anything...
            pass
        elif self.check_move(direction):
            self.currentShape.coords = self.currentShape.translated_positions(direction_d[direction])
        elif direction is 'down': # if your heading down then the shape has 'landed'
            return self.land_shape()

    def handle_rotate(self, clockwise):
        if not self.paused and self.check_rotate(clockwise):
            self.currentShape.coords = self.currentShape.rotated_positions(clockwise)

    def move_all_the_way(self, direction):
        drop_distance = 0
        while self.check_move(direction):
            self.handle_move(direction)
            drop_distance += 1

        result = self.handle_move(direction)

        if direction is 'down':
            if result is None:
                print 'uh oh, game over, something bad has happened. losing piece was at {0}' \
                        .format(self.currentShape.coords)
                self.backup_info.pop() # we don't need the latest move, since it never happened
            else:
                self.backup_info[-1]['drop_distance'] = drop_distance
                self.backup_info[-1]['deleted_rows']  = result

    def left_callback( self, event ):
        if self.currentShape:
            self.handle_move('left')

    def right_callback( self, event ):
        if self.currentShape:
            self.handle_move('right')

    def up_callback( self, event ):
        if self.currentShape:
            # drop the tetrominoe to the bottom
            self.move_all_the_way('down')

    def down_callback( self, event ):
        if self.currentShape:
            self.handle_move('down')

    def a_callback( self, event):
        if self.currentShape:
            self.handle_rotate(clockwise=True)

    def s_callback( self, event):
        if self.currentShape:
            self.handle_rotate(clockwise=False)

    def p_callback(self, event):
        self.paused = not self.paused

    def get_next_shape( self ): # todo: call get_next_shape_in_queue directly
        """
        Returns next tetromino from queue
        """
        return self.get_next_shape_in_queue(not self.under_simulation)
        #return square_shape()