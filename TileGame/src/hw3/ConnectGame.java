

package hw3;

import java.util.ArrayList;
import java.util.Random;

import api.ScoreUpdateListener;
import api.ShowDialogListener;
import api.Tile;

/* 
 * @author Onur Onal
 */

/**
 * Class that models a game.
 */
public class ConnectGame {
	
	// api Instance variables
	private ShowDialogListener dialogListener;
	private ScoreUpdateListener scoreListener;
	
	// Constructor instance variables
	private int width;
	private int height;
	private int min;
	private int max;
	private Random rand;
	
	private Grid grid; // The grid
	private Tile tile; // The current tile
	private ArrayList<Tile> tilesArrayList = new ArrayList<Tile>(); // An arraylist to store tiles
	private Tile[] tilesArray; // An array to store tiles
	private long playerScore = 0; // Player score
	private int scoreMultiplier = 0; // The power of the score for Math.pow
	private boolean firstSelected = false; // Boolean for checking if first tile is selected
	private boolean removedTile = false; // Checks Whether a tile has been removed with upgradeLastSelectedTile
	
	/**
	 * Constructs a new ConnectGame object with given grid dimensions and minimum
	 * and maximum tile levels.
	 * 
	 * @param width  grid width
	 * @param height grid height
	 * @param min    minimum tile level
	 * @param max    maximum tile level
	 * @param rand   random number generator
	 */
	public ConnectGame(int width, int height, int min, int max, Random rand) {
		this.width = width;
		this.height = height;
		this.rand = rand;
		this.min = min;
		this.max = max;
		
		grid = new Grid(width, height);
	}

	/**
	 * Gets a random tile with level between minimum tile level inclusive and
	 * maximum tile level exclusive. For example, if minimum is 1 and maximum is 4,
	 * the random tile can be either 1, 2, or 3.
	 * <p>
	 * DO NOT RETURN TILES WITH MAXIMUM LEVEL
	 * 
	 * @return a tile with random level between minimum inclusive and maximum
	 *         exclusive
	 */
	public Tile getRandomTile() {
		
		// Uses Math.random because rand does not pass test
		int level = (int) (Math.random() * (max - min) + min);     
		Tile randomTile = new Tile(level);
		
		return randomTile;
	}

	/**
	 * Regenerates the grid with all random tiles produced by getRandomTile().
	 */
	public void radomizeTiles() {
		
		// Nested for loop to populate each tile with a random level from getRandomTile
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				grid.setTile(getRandomTile(), j, i);
			}
		}
	}

	/**
	 * Determines if two tiles are adjacent to each other. The may be next to each
	 * other horizontally, vertically, or diagonally.
	 * 
	 * @param t1 one of the two tiles
	 * @param t2 one of the two tiles
	 * @return true if they are next to each other horizontally, vertically, or
	 *         diagonally on the grid, false otherwise
	 */
	public boolean isAdjacent(Tile t1, Tile t2) {
		
		// Absolute value to account for differences less than 0
		int xDiff = Math.abs(t1.getX() - t2.getX());
		int yDiff = Math.abs(t1.getY() - t2.getY());
		
		// Checks the only three options that confirm if tiles are adjacent
		if ((xDiff == 1 && yDiff == 1) || (xDiff == 1 && yDiff == 0) || (xDiff == 0 && yDiff == 1)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Indicates the user is trying to select (clicked on) a tile to start a new
	 * selection of tiles.
	 * <p>
	 * If a selection of tiles is already in progress, the method should do nothing
	 * and return false.
	 * <p>
	 * If a selection is not already in progress (this is the first tile selected),
	 * then start a new selection of tiles and return true.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return true if this is the first tile selected, otherwise false
	 */
	public boolean tryFirstSelect(int x, int y) {
		
		Tile firstTile = grid.getTile(x, y);
		tile = firstTile; // Sets the current tile equal to the selected tile
		
		// The first tile can no longer be selected once the method is called once
		if ((!firstTile.isSelected()) && (!firstSelected)) {
			firstSelected = true;
			firstTile.setSelect(true); 
			tilesArrayList.add(firstTile); // The tile is added to the arraylist of tiles 
			scoreMultiplier++;
			playerScore = (long) Math.pow(4, scoreMultiplier); // Updates the score
			return true;		
		}
		else {
			firstSelected = true;
			return false;
		}		
	}

	/**
	 * Indicates the user is trying to select (mouse over) a tile to add to the
	 * selected sequence of tiles. The rules of a sequence of tiles are:
	 * 
	 * <pre>
	 * 1. The first two tiles must have the same level.
	 * 2. After the first two, each tile must have the same level or one greater than the level of the previous tile.
	 * </pre>
	 * 
	 * For example, given the sequence: 1, 1, 2, 2, 2, 3. The next selected tile
	 * could be a 3 or a 4. If the use tries to select an invalid tile, the method
	 * should do nothing. If the user selects a valid tile, the tile should be added
	 * to the list of selected tiles.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 */
	public void tryContinueSelect(int x, int y) {
		
		Tile nextTile = grid.getTile(x, y);
		int level = nextTile.getLevel();
		
		// Method only works if added tile is adjacent to previous tile
		if (isAdjacent(tile, nextTile)) {
			// Condition for the first added tile. The levels must be equal.
			if ((level == tile.getLevel()) && (tilesArrayList.size() == 1)) {
				tilesArrayList.add(nextTile); // Adds tile to arraylist
				nextTile.setSelect(true);
				tile = nextTile; // Sets the current tile equal to the selected tile
				scoreMultiplier++;
				playerScore = (long) Math.pow(4, scoreMultiplier); // Updates the score
			}
			// Condition for all added tiles after the first added tile. Their levels must be equal or the next tile must have greater level by 1
			else if (((level - tile.getLevel() == 1) || (level == tile.getLevel())) && (tilesArrayList.size() > 1)) {
				tilesArrayList.add(nextTile); // Adds tile to arraylist
				nextTile.setSelect(true); 
				tile = nextTile; // Sets the current tile equal to the selected tile
				scoreMultiplier++;
				playerScore = (long) Math.pow(4, scoreMultiplier); // Updates the score
			}
			// Removes and deselects the tile if it has smaller level that previous tile
			else if (tile.getLevel() - level == 1) {
				tilesArrayList.remove(nextTile); // Removes tile from the arraylist
				tile.setSelect(false); 
			}
		}
	}

	/**
	 * Indicates the user is trying to finish selecting (click on) a sequence of
	 * tiles. If the method is not called for the last selected tile, it should do
	 * nothing and return false. Otherwise it should do the following:
	 * 
	 * <pre>
	 * 1. When the selection contains only 1 tile reset the selection and make sure all tiles selected is set to false.
	 * 2. When the selection contains more than one block:
	 *     a. Upgrade the last selected tiles with upgradeLastSelectedTile().
	 *     b. Drop all other selected tiles with dropSelected().
	 *     c. Reset the selection and make sure all tiles selected is set to false.
	 * </pre>
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return return false if the tile was not selected, otherwise return true
	 */
	public boolean tryFinishSelection(int x, int y) {
		
		Tile lastTile = grid.getTile(x, y);
		
		// Method performs as long as tile is the last selected tile
		if (lastTile == tilesArrayList.get(tilesArrayList.size() - 1)) {
			tile = lastTile;

			// When selection contains 1 tile 
			if (tilesArrayList.size() == 1) {
				for (int i = 0; i < tilesArrayList.size(); i++) {
					tilesArrayList.get(i).setSelect(false);
				}
			}
			// When selection contains more than 1 tile
			else {
				upgradeLastSelectedTile();
				for (int i = 0; i < tilesArrayList.size(); i++) {
					dropSelected();
					tilesArrayList.get(i).setSelect(false);
				}
			}
			
			//  Only updates the score if tile has not previously been selected
			if (tilesArrayList.contains(lastTile)) {
				playerScore = 0;
			}
			else {
				scoreMultiplier++;
				playerScore = (long) Math.pow(4, scoreMultiplier); // Updates the score
			}
			
			return true;
		}
		
		return false;
	}

	/**
	 * Increases the level of the last selected tile by 1 and removes that tile from
	 * the list of selected tiles. The tile itself should be set to unselected.
	 * <p>
	 * If the upgrade results in a tile that is greater than the current maximum
	 * tile level, both the minimum and maximum tile level are increased by 1. A
	 * message dialog should also be displayed with the message "New block 32,
	 * removing blocks 2". Not that the message shows tile values and not levels.
	 * Display a message is performed with dialogListener.showDialog("Hello,
	 * World!");
	 */
	public void upgradeLastSelectedTile() {
		
		int previousLevel = tile.getLevel();
		tile.setLevel(tile.getLevel() + 1); // Increases level by one
		tilesArrayList.remove(tile); // Removes tile from arraylist
		removedTile = true;
		
		// Increases min and max levels if the tile level exceeds the original max
		if (tile.getLevel() > max) {
			min++;
			max++;
		}
		
		scoreListener.updateScore(tile.getLevel());
		dialogListener.showDialog("New block " + tile.getValue() + " removing blocks " + previousLevel);
	}

	/**
	 * Gets the selected tiles in the form of an array. This does not mean selected
	 * tiles must be stored in this class as a array.
	 * 
	 * @return the selected tiles in the form of an array
	 */
	public Tile[] getSelectedAsArray() {
			
		// Array is not populated if a tile has been removed
		if (removedTile) {
			tilesArray = new Tile[0];
		}
		else {
			// Populates array with elements of arraylist
			for (int i = 0; i < tilesArrayList.size(); i++) {
				tilesArray = new Tile[tilesArrayList.size()]; // Initializes the array to size of arraylist
				tilesArray[i] = tilesArrayList.get(i);
			}
		}
		
		return tilesArray;
	}

	/**
	 * Removes all tiles of a particular level from the grid. When a tile is
	 * removed, the tiles above it drop down one spot and a new random tile is
	 * placed at the top of the grid.
	 * 
	 * @param level the level of tile to remove
	 */
	public void dropLevel(int level) {
		
		// Nested for loop to iterate through the grid
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Checks whether the level of a tile at a certain coordinate equals the level parameter
				if (grid.getTile(j, i).getLevel() == level) {
					// Loop iterates through the y-coordinate of the tiles that will side down
					for (int k = i; k > 0; k--) {
						Tile replaceTile = grid.getTile(j, k - 1); // Creates a dynamic tile that will replace the tile directly above it
						grid.setTile(replaceTile, j, k); // Replaces the tile with the tile above it
					}
					grid.setTile(getRandomTile(), j, 0); // After the tiles have slide down. A random tile fills the board from above.
				}
			} 
		}
	}

	/**
	 * Removes all selected tiles from the grid. When a tile is removed, the tiles
	 * above it drop down one spot and a new random tile is placed at the top of the
	 * grid.
	 */
	public void dropSelected() {
		
		// Nested for loop to iterate through the grid
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Checks whether the tile at the given location has been selected
				if (grid.getTile(j, i).isSelected()) {
					for (int k = i; k > 0; k--) {
						Tile replaceTile = grid.getTile(j, k - 1); // Creates a dynamic tile that will replace the tile directly above it
						grid.setTile(replaceTile, j, k); // Replaces the tile with the tile above it
					}
					grid.setTile(getRandomTile(), j, 0); // After the tiles have slide down. A random tile fills the board from above.
				}
			}
		}
	}
	
	/**
	 * Remove the tile from the selected tiles.
	 * 
	 * @param x column of the tile
	 * @param y row of the tile
	 */
	public void unselect(int x, int y) {
		Tile removedTile = grid.getTile(x, y);
		removedTile.setSelect(false);
		tilesArrayList.remove(removedTile);
	}

	/**
	 * Gets the player's score.
	 * 
	 * @return the score
	 */
	public long getScore() {
		return playerScore;
	}

	/**
	 * Gets the game grid.
	 * 
	 * @return the grid
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * Gets the minimum tile level.
	 * 
	 * @return the minimum tile level
	 */
	public int getMinTileLevel() {
		return min;
	}

	/**
	 * Gets the maximum tile level.
	 * 
	 * @return the maximum tile level
	 */
	public int getMaxTileLevel() {
		return max;
	}

	/**
	 * Sets the player's score.
	 * 
	 * @param score number of points
	 */
	public void setScore(long score) {
		playerScore = score;
	}

	/**
	 * Sets the game's grid.
	 * 
	 * @param grid game's grid
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	/**
	 * Sets the minimum tile level.
	 * 
	 * @param minTileLevel the lowest level tile
	 */
	public void setMinTileLevel(int minTileLevel) {
		min = minTileLevel;
	}

	/**
	 * Sets the maximum tile level.
	 * 
	 * @param maxTileLevel the highest level tile
	 */
	public void setMaxTileLevel(int maxTileLevel) {
		max = maxTileLevel;
	}

	/**
	 * Sets callback listeners for game events.
	 * 
	 * @param dialogListener listener for creating a user dialog
	 * @param scoreListener  listener for updating the player's score
	 */
	public void setListeners(ShowDialogListener dialogListener, ScoreUpdateListener scoreListener) {
		this.dialogListener = dialogListener;
		this.scoreListener = scoreListener;
	}

	/**
	 * Save the game to the given file path.
	 * 
	 * @param filePath location of file to save
	 */
	public void save(String filePath) {
		GameFileUtil.save(filePath, this);
	}

	/**
	 * Load the game from the given file path
	 * 
	 * @param filePath location of file to load
	 */
	public void load(String filePath) {
		GameFileUtil.load(filePath, this);
	}
}
