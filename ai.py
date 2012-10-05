from threading import Thread
from constants import SHAPES_QUEUE_SIZE

class AI(Thread):
    """
    AI to play Tetris automatically
    """
    def __init__(self):
        Thread.__init__(self)

    def attach(self, game_controller):
        self.game = game_controller

    def find_best_move(self, game, level):
        """
        Simulates playing a game for up to "level" times, where "level" should be the number of items in
        the queue that this game allows for
        """

        game = game.create_copy()
        shape = game.shape # not the original game piece anymore, so no worries altering this
        best_score = 0
        best_moves = [(0,0)]
        for orientation in range(game.shape.orientations):
            shape.rotate() # a rotated piece, not the old one anymore
            rotated_game = game.create_copy()
            rotated_game.move_all_the_way("left")
            for position in range(rotated_game.board.max_x - shape.width() + 1):
                rotated_game.handle_move("right")
                translated_game = game.create_copy()
                translated_game.move_all_the_way("down")
                new_level = level - 1
                this_best_score = 0
                this_best_move = [(orientation, position)]
                if new_level >= 0: # still something left in queue or board
                    results = self.find_best_move(translated_game, new_level)
                    this_best_score = results[0]
                    this_best_move += results[1]
                else:
                    this_best_score = translated_game.score

                if this_best_score > best_score:
                    best_score = this_best_score
                    best_moves = this_best_move

        return (best_score, best_moves)

    def run(self):
        while not self.game.game_over:
            best_move = self.find_best_move(self.game, SHAPES_QUEUE_SIZE)
            for _ in range(best_move[1][0][0]): # the orientation
                self.game.shape.rotate()
            self.game.move_all_the_way("left")
            for _ in range(best_move[1][0][1]): # the position
                self.game.handle_move("right")

            self.game.move_all_the_way("down")