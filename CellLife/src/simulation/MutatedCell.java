package simulation;

import core.Cell;

/**
 * A MutatedCell is a special type of cell that is alive, but has mutated to
 * live by different rules. A MutatedCell does not have stamina.
 */
public class MutatedCell extends Cell
{
	@Override
	public Cell getNextStage(int numNormalNeighbours, int numMutatedNeighbours)
	{
		if( numNormalNeighbours >= 2 )
			return new NormalCell();
		if( 2 <= numMutatedNeighbours && numMutatedNeighbours <= 4 )
			return this;
		return new DeadCell();
	}
}
