import time
import sys
from Tkinter import *
from threading import Thread

class status_bar( Frame ):
    """
    Status bar to display the score and level
    """
    def __init__(self, parent):
        Frame.__init__( self, parent )
        self.label = Label( self, bd=1, relief=SUNKEN, anchor=W )
        self.label.pack( fill=X )

    def set( self, format, *args):
        self.label.config( text = format % args)
        self.label.update_idletasks()

    def clear( self ):
        self.label.config(test="")
        self.label.update_idletasks()

class BoardCanvas(Frame):
    """
    The BoardCanvas is a graphical representation of a board
    """
    def __init__(self, parent, scale = 20, offset = 3):
        Frame.__init__(self, parent)
        self.parent = parent
        self.scale = scale
        self.offset = offset

    def attach(self, board):
        self.board = board

        self.canvas = Canvas(self.parent,
            height = (self.board.max_y * self.scale) + self.offset,
            width  = (self.board.max_x * self.scale) + self.offset)
        self.canvas.pack()
        self.pack(side=BOTTOM)

    def draw_block_at(self, x, y, color):
        rx = (x * self.scale) + self.offset
        ry = (y * self.scale) + self.offset
        self.canvas.create_rectangle(rx, ry, rx + self.scale, ry + self.scale, fill=color)

    def draw(self, shape):
        self.canvas.delete(ALL) # redraw entire screen

        for x in range(self.board.max_x):
            for y in range(self.board.max_y):
                block = self.board.landed.get((x, y))
                if block:
                    self.draw_block_at(x, y, block) # "block" is actually the block's color

        for (x, y) in shape.coords:
            self.draw_block_at(x, y, shape.color)

    def display_text(self, display_text):
        x = self.board.max_x / 2
        y = self.board.max_y / 2
        rx = (x * self.scale) + self.offset
        ry = (y * self.scale) + self.offset
        self.canvas.create_rectangle(0, ry - self.scale, self.board.max_x * self.scale + self.offset, ry + self.scale, fill = "black")
        self.canvas.create_text(rx, ry, text = display_text, fill = "red", font = ("Helvetica", 16))

class GameWindow(Thread):
    """
    The GUI game window that the user interacts with
    """

    def __init__(self, parent):
        self.parent = parent
        self.parent.protocol("WM_DELETE_WINDOW", self.window_closed_callback)
        self.status_bar = status_bar(parent)
        self.status_bar.pack(side = TOP, fill = X)
        self.show_score(0, 0)

        self.canvas = BoardCanvas(self.parent)

    def attach(self, game_controller):
        self.parent.bind("<Left>", game_controller.left_callback)
        self.parent.bind("<Right>", game_controller.right_callback)
        self.parent.bind("<Up>", game_controller.up_callback)
        self.parent.bind("<Down>", game_controller.down_callback)
        self.parent.bind("a", game_controller.a_callback)
        self.parent.bind("s", game_controller.s_callback)
        self.parent.bind("p", game_controller.p_callback)

        self.game = game_controller
        self.canvas.attach(self.game.board)
        self.game.display_callback = self.canvas.draw

        self.update_display()
        Thread.__init__(self)
        self.start()

    def show_score(self, score, level):
        self.status_bar.set("Score: {0}\t Level: {1} ".format(score, level + 1))

    def update_display(self):
        self.show_score(self.game.score, self.game.level)
        self.canvas.draw(self.game.shape)
        if self.game.paused:
            self.canvas.display_text("PAUSED")
        elif self.game.game_over:
            print "over!!"
            self.canvas.display_text("GAME OVER")

    def window_closed_callback(self):
        self.game.game_over = True
        sys.exit("Close window button clicked")

    def run(self):
        while self.game:
            self.update_display()
            time.sleep(0.1)
            if self.game.game_over:
                self.update_display()
                break