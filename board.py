from constants import MAXX, MAXY

class Board(object):
    """
    The board represents the tetris playing area. A grid of x by y blocks.
    """
    def __init__(self, max_x = MAXX, max_y = MAXY):
        # blocks are indexed by their corrdinates e.g. (4,5), these are
        self.landed = {}
        self.max_x = max_x
        self.max_y = max_y

    def falling_distance(self, cleared_position):
        x, y = cleared_position
        for y_pos in range(y + 1, self.max_y):
            if self.landed.has_key((x, y_pos)):
                return y_pos - y

        return self.max_y - y

    def highest_piece(self):
        """
        Returns how far off the bottom of the board the top piece in the board is
        """
        highest_y = self.max_y
        for key in self.landed.iterkeys():
            if key[1] < highest_y:
                highest_y = key[1]

        return self.max_y - highest_y

    def average_height(self):
        """
        Returns average height of all pieces on the board
        """
        ys = [coord[1] for coord in self.landed.keys()]
        return self.max_y - (sum(ys) * 1.0 / len(ys))

    def overhangs(self):
        """
        Returns the number of overhangs in the game board
        """
        overhang = 0

        for column in range(self.max_x):
            encountered_block = False
            for row in range(self.max_y):
                if self.landed.has_key((column, row)):
                    encountered_block = True
                elif encountered_block:
                    overhang += 1

        return overhang

    def check_for_complete_rows(self):
        """
        Look for a complete row of blocks, from the bottom up until the top row
        or until an empty row is reached.
        """
        deleted_rows = []
        empty_row = 0

        # find the first empty row
        for y in range(self.max_y - 1, -1, -1):
            row_is_empty = True
            for x in range(self.max_x):
                if self.landed.get((x,y), None):
                    row_is_empty = False
                    break
            if row_is_empty:
                empty_row = y
                break

        # Now scan up and until a complete row is found.
        y = self.max_y - 1
        while y > empty_row:
            complete_row = True
            for x in xrange(self.max_x):
                if not self.landed.has_key((x,y)):
                    complete_row = False
                    break

            if complete_row:
                deleted_rows.append(y)

                #delete the completed row
                for x in range(self.max_x):
                    self.landed.pop((x,y))

            y -= 1

        # todo: refactor so that we're not mixing x/y and rows/columns
        drop_distance = len(deleted_rows)
        if drop_distance > 0:
            for column in range(self.max_x): # go through each column
                # and then shift each block in that column down by 1
                for row in range(max(deleted_rows), 0, -1):
                    if self.landed.has_key((column, row)):
                        self.landed[(column, row + drop_distance)] = self.landed.pop((column, row))

        return deleted_rows

    def output( self ):
        for y in xrange(self.max_y):
            line = []
            for x in xrange(self.max_x):
                if self.landed.get((x,y), None):
                    line.append("X")
                else:
                    line.append(".")
            print "".join(line)

    def check_block( self, (x, y) ):
        """
        Check if the x, y coordinate can have a block placed there.
        That is; if there is a 'landed' block there or it is outside the
        board boundary, then return False, otherwise return true.
        """
        if x < 0 or x >= self.max_x or y < 0 or y >= self.max_y:
            return False
        elif self.landed.has_key( (x, y) ):
            return False
        else:
            return True

    def are_empty(self, coords):
        """
        Checks to see if the board is empty at all the specified coordinates
        """
        for coord in coords:
            if not self.check_block(coord):
                return False

        return True

    def remove_blocks_at(self, coords):
        """
        Removes blocks at all the specified coordinates
        """
        for coord in coords:
            self.landed.pop(coord)

    def add_blocks_at(self, coords, color):
        """
        Adds blocks at all the specified coordinates
        """
        for coord in coords:
            self.landed[coord] = color

    def restore(self, deleted_rows, previous_shape_positions):
        for deleted_row in deleted_rows: # go through each already-deleted row
            for column in range(self.max_x): # and go through each column
                # and then shift each block in that column up by 1
                for row in range(0, deleted_row, +1):
                    if self.landed.has_key((column, row + 1)):
                        self.landed[(column, row)] = self.landed.pop((column, row + 1))

                self.landed[(column, deleted_row)] = 'black'
        self.remove_blocks_at(previous_shape_positions)