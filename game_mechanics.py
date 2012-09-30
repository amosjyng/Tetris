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