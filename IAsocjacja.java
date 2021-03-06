package Laboratorium;

import java.io.Serializable;

public interface IAsocjacja{
	
	public <X, Y> boolean verifyInstance(X o1, Y o2);
	
	public IAsocjacja getOpposite();
	
	public int getMaxCardinality();
	
}
