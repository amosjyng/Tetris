class Board(object):
    """
    The board represents the tetris playing area. A grid of x by y blocks.
    """
    def __init__(self, max_x = 10, max_y = 20):
        """
        Init and config the tetris board, default configuration:
        Scale (block size in pixels) = 20
        max X (in blocks) = 10
        max Y (in blocks) = 20
        offset (in pixels) = 3
        """

        # blocks are indexed by there corrdinates e.g. (4,5), these are
        self.landed = {}
        self.max_x = max_x
        self.max_y = max_y

    def check_for_complete_rows(self):
        """
        Look for a complete row of blocks, from the bottom up until the top row
        or until an empty row is reached.
        """
        rows_deleted = 0
        empty_row = 0

        # find the first empty row
        for y in range(self.max_y - 1, -1, -1):
            row_is_empty = True
            for x in range(self.max_x):
                if self.landed.get((x,y), None):
                    row_is_empty = False
                    break;
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
                rows_deleted += 1

                #delete the completed row
                for x in range(self.max_x):
                    self.landed.pop((x,y))

                # move all the rows above it down
                for ay in xrange(y - 1, empty_row, -1):
                    for x in xrange(self.max_x):
                        if self.landed.has_key((x, ay)):
                            self.landed[(x, ay + 1)] = self.landed.pop((x, ay))

                # move the empty row down index down too
                empty_row +=1
                # y stays same as row above has moved down.

            else:
                y -= 1

        #self.output() # non-gui diagnostic

        # return the number of rows deleted
        return rows_deleted

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
        Removes all the blocks at the specified coordinates
        """
        for coord in coords:
            self.landed.pop(coord)

    def add_blocks_at(self, coords, color):
        """
        Adds blocks at all the specified coordinates
        """
        for coord in coords:
            self.landed[coord] = color