from threading import Thread
from constants import SHAPES_QUEUE_SIZE
from copy import deepcopy
import yappi

class AI(Thread):
    """
    AI to play Tetris automatically
    """
    def __init__(self):
        Thread.__init__(self)

    def attach(self, game_controller):
        self.game = game_controller

    def heuristic(self, game):
        return game.score * 10 - game.board.highest_piece()

    def find_best_move(self, game, level):
        """
        Simulates playing a game for up to "level" times, where "level" should be the number of items in
        the queue that this game allows for
        """

        shape = game.shape # not the original game piece anymore, so no worries altering this
        best_score = -999999
        best_moves = (0,0)
        for orientation in range(game.shape.orientations):
            rotated_game = game
            rotated_game.move_all_the_way("left")
            if orientation is not 0: # don't "rotate" for first orientation
                shape.rotate()
            for position in range(rotated_game.board.max_x - shape.width() + 2):
                if position is not 0: # don't "translate" for first translation
                    rotated_game.handle_move("right")
                translated_game = rotated_game
                translated_game.move_all_the_way("down")
                if translated_game.game_over:
                    translated_game.undo()
                    print 'skipping game over move'
                    continue # not worth exploring this branch anymore
                new_level = level - 1
                this_best_score = 0
                this_best_move = (orientation, position)
                if new_level >= 0: # still something left in queue or board
                    this_best_score, _ = self.find_best_move(translated_game, new_level)
                    this_best_score += translated_game.score * 1.1
                else:
                    this_best_score = self.heuristic(translated_game)

                if this_best_score > best_score:
                    best_score = this_best_score
                    best_moves = this_best_move

                translated_game.undo() # now it's back at the top

        return best_score, best_moves

    def run(self):
        yappi.start()
        while not self.game.game_over:
            best_score, best_move = self.find_best_move(self.game.create_copy(), SHAPES_QUEUE_SIZE)
            self.game.move_all_the_way("left")
            for _ in range(best_move[0]): # the orientation
                self.game.move_all_the_way("left")
                self.game.shape.rotate()
            for _ in range(best_move[1]): # the position
                self.game.handle_move("right")

            self.game.move_all_the_way("down")
        yappi.print_stats()