package simulation;

import core.Cell;

/**
 * A DeadCell is a cell that is dead. A DeadCell does not have stamina.
 */
public class DeadCell extends Cell
{
	@Override
	public Cell getNextStage(int numNormalNeighbours, int numMutatedNeighbours)
	{
		if( numNormalNeighbours == 3 ) 
			return new NormalCell();
		if( numMutatedNeighbours == 2 )
			return new MutatedCell();
		return this;
	}
}
