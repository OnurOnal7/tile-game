# Tile Game

## Overview
This project implements a tile-based game where the player connects sequences of identical tiles or tiles that increase in powers of two. When a valid sequence is made, the tiles are removed from the grid, and new tiles fall in from the top. Points are awarded for the removed tiles, and the last tile in the sequence is upgraded.

The backend logic of the game is implemented through three classes:
- **Grid**: Manages the 2D array of tiles.
- **ConnectGame**: Models the game mechanics, including tile selection and scoring.
- **GameFileUtil**: Handles saving and loading game states to and from files.

## Features
- **Grid Management**:
  - The game operates on a 2D grid of tiles.
  - Tiles are randomly generated between a minimum and maximum value (powers of two).
  
- **Tile Selection and Connection**:
  - Players can select sequences of tiles that start with two identical tiles and can continue with tiles in increasing powers of two (e.g., 2 → 4 → 8 → 16).
  - Valid sequences result in tile removal, and new tiles fall into place.
  
- **Scoring**:
  - Points are awarded based on the tiles removed during a valid sequence.
  - The last tile in the sequence is upgraded to the next power of two.

- **Game Saving and Loading**:
  - The game state can be saved to and loaded from a file using the `GameFileUtil` class.

## Public Methods
### Grid Class
- `setTile(Tile tile, int x, int y)`: Sets a tile at the specified position.
- `getTile(int x, int y)`: Retrieves the tile at the specified position.
- `getWidth()`: Returns the width of the grid.
- `getHeight()`: Returns the height of the grid.

### ConnectGame Class
- `tryFirstSelect(int x, int y)`: Begins selecting a tile sequence.
- `tryContinueSelect(int x, int y)`: Continues the tile selection.
- `tryFinishSelection(int x, int y)`: Finalizes the tile selection.
- `randomizeTiles()`: Randomizes the tiles on the grid.
- `getSelectedAsArray()`: Retrieves the currently selected tiles.
- `getScore()`: Returns the current score of the game.
- `getRandomTile()`: Generates a random tile based on the current minimum and maximum values.

### GameFileUtil Class
- `save(String filename, ConnectGame game)`: Saves the current game state to a file.
- `load(String filename, ConnectGame game)`: Loads a saved game state from a file.
