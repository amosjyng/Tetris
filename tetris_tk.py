#!/usr/bin/env python
"""
Tetris Tk - A tetris clone written in Python using the Tkinter GUI library.

Controls:
    Left Arrow      Move left
    Right Arrow     Move right
    Down Arrow      Move down
    Up Arrow        Drop Tetronimoe to the bottom
    'a'             Rotate anti-clockwise (to the left)
    's'             Rotate clockwise (to the right)
    'p'             Pause the game.
"""

from game_mechanics import game_controller
from Tkinter import Tk

if __name__ == "__main__":
    root = Tk()
    root.title("Tetris Tk")
    theGame = game_controller( root )
    
    root.mainloop()
