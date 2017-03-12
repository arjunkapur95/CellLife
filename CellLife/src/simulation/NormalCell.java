package simulation;

import core.Cell;

/**
 * A NormalCell is a special type of cell that is healthy. A NormalCell must keep
 * track of the amount of stamina it has. All NormalCells will start life with
 * 100% stamina. If the stamina gets to 1% or lower, the cell will die.
 */
public class NormalCell extends Cell
{	
	public double stamina = 100.00;
	
	/**
	 * Get the amount of stamina left in this NormalCell expressed as a percentage.
	 * 
	 * 0.0 represents 0% and 1.0 represents 100%.
	 * 
	 * @return the percent of stamina left in this cell
	 */
	public double getPercentStamina() {
		return stamina/100.0;
	}
	
	@Override
	public Cell getNextStage(int numNormalNeighbours, int numMutatedNeighbours)
	{
		// calc stamina
		stamina = ( stamina * 3 * numNormalNeighbours ) / ( 8 + 2 * numMutatedNeighbours );
		if( stamina <= 1.0 )
			return new DeadCell();
		if( numNormalNeighbours == 2 || numNormalNeighbours == 3 )
			return this;
		return new DeadCell();
	}
}
