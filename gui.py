from Tkinter import *
import tkMessageBox

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
    def __init__(self, parent, board, scale = 20, offset = 3):
        Frame.__init__(self, parent)
        self.parent = parent
        self.board = board
        self.scale = scale
        self.offset = offset

        self.canvas = Canvas(parent,
            height = (self.board.max_y * scale) + offset,
            width = (self.board.max_x * scale) + offset)
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

class GameWindow(object):
    """
    The GUI game window that the user interacts with
    """

    def __init__(self, parent, game_controller):
        self.parent = parent
        self.status_bar = status_bar(parent)
        self.status_bar.pack(side = TOP, fill = X)
        self.show_score(0, 0)

        self.parent.bind("<Left>", game_controller.left_callback)
        self.parent.bind("<Right>", game_controller.right_callback)
        self.parent.bind("<Up>", game_controller.up_callback)
        self.parent.bind("<Down>", game_controller.down_callback)
        self.parent.bind("a", game_controller.a_callback)
        self.parent.bind("s", game_controller.s_callback)
        self.parent.bind("p", game_controller.p_callback)

    def show_score(self, score, level):
        self.status_bar.set("Score: {0}\t Level: {1} ".format(score, level + 1))

    def show_pause(self):
        tkMessageBox.askquestion(title="Paused!", message="Continue?", type=tkMessageBox.OK)

    def show_game_over(self, score, level):
        self.status_bar.set("Score: {0}\t Level: {1} ".format(score, level + 1))
        tkMessageBox.showwarning(title = "GAME OVER", message = "Score: {0}\t Level: {1} ".format(score, level + 1),
            parent = self.parent )

        Toplevel().destroy()
        self.parent.destroy()