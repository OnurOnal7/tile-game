

package hw3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import api.Tile;

/* 
 * @author Onur Onal
 */

/**
 * Utility class with static methods for saving and loading game files.
 */
public class GameFileUtil {
	/**
	 * Saves the current game state to a file at the given file path.
	 * <p>
	 * The format of the file is one line of game data followed by multiple lines of
	 * game grid. The first line contains the: width, height, minimum tile level,
	 * maximum tile level, and score. The grid is represented by tile levels. The
	 * conversion to tile values is 2^level, for example, 1 is 2, 2 is 4, 3 is 8, 4
	 * is 16, etc. The following is an example:
	 * 
	 * <pre>
	 * 5 8 1 4 100
	 * 1 1 2 3 1
	 * 2 3 3 1 3
	 * 3 3 1 2 2
	 * 3 1 1 3 1
	 * 2 1 3 1 2
	 * 2 1 1 3 1
	 * 4 1 3 1 1
	 * 1 3 3 3 3
	 * </pre>
	 * 
	 * @param filePath the path of the file to save
	 * @param game     the game to save
	 */
	public static void save(String filePath, ConnectGame game) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			Grid grid = game.getGrid();
			
			String firstLine = (grid.getWidth() + " " + grid.getHeight() + " " + game.getMinTileLevel() + " " + game.getMaxTileLevel() + " " + game.getScore());		
					
			// Writes the first line and moves to next line
			writer.write(firstLine);
			writer.write("\n");
			
			// Iteratively writes levels for each tile in repsective grid position 
			for (int i = 0; i < grid.getHeight(); i++) {
				for (int j = 0; j < grid.getWidth(); j++) {	
					// Adds space after each entry as long as it is not the last entry
					if (j != grid.getWidth() - 1) {
						writer.write(grid.getTile(j, i).getLevel() + " ");
					}
					// Does not add a space and moves to the next line if on last entry
					else {
						if(i != grid.getHeight() - 1) {
							writer.write(grid.getTile(j, i).getLevel() + "\n");
						}
						else {
							writer.write(grid.getTile(j, i).getLevel() + "");
						}
					}
				}
			}
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the file at the given file path into the given game object. When the
	 * method returns the game object has been modified to represent the loaded
	 * game.
	 * <p>
	 * See the save() method for the specification of the file format.
	 * 
	 * @param filePath the path of the file to load
	 * @param game     the game to modify
	 */
	public static void load(String filePath, ConnectGame game) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String[] line = reader.readLine().split(" "); // Creates a string array of values for the first line
	        
			// Gets grid dimensions and parses and sets remaining constructor parameters for the game
	        int width = Integer.parseInt(line[0]);
	        int height = Integer.parseInt(line[1]);
	        game.setMinTileLevel(Integer.parseInt(line[2]));
	        game.setMaxTileLevel(Integer.parseInt(line[3]));
	        game.setScore(Long.parseLong(line[4])); // Parsese as long for the score
			Grid grid = new Grid(width, height); // Creates a grid of parsed width and height dimensions
			
			// Repeats above process for all rows in grid
			for (int i = 0; i < height; i++) {
	            String[] nextLine = reader.readLine().split(" "); // Creates a string array of values for each row
	            // Parses and sets the tile for each spot in that row
	            for (int j = 0; j < width; j++) {
	                Tile tile = new Tile(Integer.parseInt(nextLine[j]));
	                grid.setTile(tile, j, i);
	            }
	        }
	        game.setGrid(grid); // Sets the grid with all added tiles
	        reader.close();
	
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
	}
}
