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