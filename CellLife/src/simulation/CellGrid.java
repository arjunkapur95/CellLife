package simulation;

import core.Cell;

public class CellGrid
{
	// Store the cells of the game in this 2D array
	private Cell[][] cells;
	private double mutChance;
	private int setsize;

	/**
	 * Contructor for a CellGrid. Populates the grid with cells that will be
	 * living and normal (with probability given by lifeChance) or dead. Cells
	 * will NOT start mutated.
	 * 
	 * @param size
	 *            the size of the grid will be size x size
	 * @param lifeChance
	 *            the probability of each cell starting out alive
	 * @param mutationChance
	 *            the probability that (when required) each cell will mutate
	 */
	public CellGrid(int size, double lifeChance, double mutationChance)
	{
		// init
		cells = new Cell[size][size];
		mutChance = mutationChance;
		setsize = size;
		
		int x, y;
		for( x = 0; x < size ; x++ ) {
			for( y = 0; y < size ; y++ ) {
				if( Math.random() < lifeChance )
					cells[x][y] = new NormalCell();
				else
					cells[x][y] = new DeadCell();
			}
		}
	}

	/**
	 * Run one step in the simulation. This has 2 stages in the following order:
	 * 
	 * 1. Update all cells in the grid according to the rules given in the
	 * assignment specification sheet (higher rules happen with more importance
	 * than lower rules).
	 * 
	 * 2. Attempt to mutate every living cell.
	 */
	public void simulateStep()
	{
		// make new grid to save changes
		Cell[][] tempCells = new Cell[setsize][setsize];
		int x, y;
		for( x = 0; x < setsize; x++ ) {
			for( y = 0; y < setsize; y++ ) {
				tempCells[x][y] = getCell(x, y).getNextStage(countNormalNeighbours(x, y), countMutatedNeighbours(x, y));
			}
		}
		
		// need to set cells to their tempcells
		for( x = 0; x < setsize; x++ ) {
			for( y = 0; y < setsize; y++ ) {
				setCell(x, y, tempCells[x][y]);
			}
		}
		
		// attempt to mutate
		for( x = 0; x < setsize; x++ ) {
			for( y = 0; y < setsize; y++ ) {
				if( isNormalCell(x, y) || isMutatedCell(x, y))
					mutateCell(x, y);
			}
		}
	}
	
	/**
	 * Mutate the cell at the given coordinates. The mutation will be successful
	 * with probability as given in the constructor, and only if the cell is
	 * alive (i.e. not dead).
	 * 
	 * NOTE: This method can actually result in a cell changing from NormalCell to
	 * MutatedCell; not only returning the result.
	 * 
	 * @param x
	 *            the x coordinate (column) of the cell
	 * @param y
	 *            the y coordinate (row) of the cell
	 * @return true if the mutation was successfully performed, false otherwise
	 *         (including if the coordinates are invalid)
	 */
	public boolean mutateCell(int x, int y)
	{
		if( isValidCoordinate(x, y) ) {
			if( isNormalCell(x, y) ) {
				if( Math.random() < mutChance ) {
					cells[x][y] = new MutatedCell();
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Check if the given coordinates are inside the grid of cells.
	 * 
	 * @param x
	 *            the x coordinate (column) of the cell
	 * @param y
	 *            the y coordinate (row) of the cell
	 * @return true if the given coordinates are inside the grid of cells, false
	 *         otherwise.
	 */
	public boolean isValidCoordinate(int x, int y)
	{
		if( 0 <= x && x < setsize )
			if( 0 <= y && y < setsize )
				return true;
		return false;
	}
	
	/**
	 * Count the number of normal neighbours in the 8 cells surrounding the
	 * given coordinates.
	 * 
	 * @param x
	 *            the x coordinate (column) of the cell
	 * @param y
	 *            the y coordinate (row) of the cell
	 * @return the number of normal neighbours of the cell at the given
	 *         coordinates or 0 if the coordinates are invalid.
	 */
	public int countNormalNeighbours(int x, int y)
	{
		int count = 0;
		// check each of around ones
		for( int a = -1 ; a < 2 ; a++ ) {
			for( int b = -1 ; b < 2 ; b++ ) {
				if( a == 0 && b == 0 )
					continue; // skip itself;
				if( isValidCoordinate(x+a, y+b) )
					if( isNormalCell(x+a, y+b) )
						count++;
			}
		}
		return count;
	}
	
	/**
	 * Count the number of mutated neighbours in the 8 cells surrounding the
	 * given coordinates.
	 * 
	 * @param x
	 *            the x coordinate (column) of the cell
	 * @param y
	 *            the y coordinate (row) of the cell
	 * @return the number of mutated neighbours of the cell at the given
	 *         coordinates or 0 if the coordinates are invalid.
	 */
	public int countMutatedNeighbours(int x, int y)
	{
		int count = 0;
		// check each of around ones
		for( int a = -1 ; a < 2 ; a ++ ) {
			for( int b = -1 ; b < 2 ; b++ ) {
				if( a == 0 && b == 0 )
					continue; // skip itself;
				if( isValidCoordinate(x+a, y+b) ){
					if( isMutatedCell(x+a, y+b) ) {
						count++;
					}
				}
			}
		}
		return count;
	}
	
	/**
	 * Get the cell at the given coordinates.
	 * 
	 * @param x
	 *            the x coordinate (column) of the cell
	 * @param y
	 *            the y coordinate (row) of the cell
	 * @return the cell at the given coordinates or null if the coordinates are
	 *         invalid
	 */
	public Cell getCell(int x, int y)
	{
		if( isValidCoordinate(x, y) )
			return cells[x][y];
		return null;
	}

	/**
	 * Set the cell at the given coordinates to the cell provided, if the
	 * coordinates are valid.
	 * 
	 * @param x
	 *            the x coordinate (column) of the cell
	 * @param y
	 *            the y coordinate (row) of the cell
	 * @param cell
	 *            the new cell to put at the coordinates given.
	 */
	public void setCell(int x, int y, Cell cell)
	{
		if( isValidCoordinate(x, y) ) {
			cells[x][y] = cell;
		}
	}

	/**
	 * Checks if an outbreak of mutated cells has occurred. An outbreak has
	 * occurred if the number of living mutated cells is AT LEAST 10% of the
	 * total number of living cells.
	 * 
	 * Note: "living" cells include both NormalCells and MutatedCells.
	 * 
	 * @return true if at least 10% of currently living cells are mutated, false
	 *         otherwise.
	 */
	public boolean isOutbreakOccurring() 
	{
		int x, y;
		double livingcount=0, mutatedcount=0;
		for( x = 0; x < setsize; x++) {
			for( y = 0; y < setsize; y++ ) {
				if( isNormalCell(x, y) )
					livingcount++;
				else if( isMutatedCell(x, y) ) {
					livingcount++;mutatedcount++;
				}
			}
		}
		
		double outbreak = mutatedcount / livingcount;
		if( outbreak >= 0.1 )
			return true;
		return false;
	}
	
	/**
	 * Check if the cell at the given coordinates is alive and non-mutated.
	 * 
	 * @param x
	 *            the x coordinate (column) of the cell
	 * @param y
	 *            the y coordinate (row) of the cell
	 * @return true if the cell at the given coordinates is alive and
	 *         non-mutated, false otherwise.
	 */
	public boolean isNormalCell(int x, int y)
	{
		// There is no need to change this method
		return (getCell(x, y) instanceof NormalCell);
	}

	/**
	 * Check if the cell at the given coordinates is alive and mutated.
	 * 
	 * @param x
	 *            the x coordinate (column) of the cell
	 * @param y
	 *            the y coordinate (row) of the cell
	 * @return true if the cell at the given coordinates is alive and mutated,
	 *         false otherwise.
	 */
	public boolean isMutatedCell(int x, int y)
	{
		// There is no need to change this method
		return (getCell(x, y) instanceof MutatedCell);
	}
}
