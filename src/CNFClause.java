/*
 *  Copyright (c) 2019, Lefteris Harteros, All rights reserved.
 *
 */

import java.util.ArrayList;

/*
 * A CNFClause consists of a conjunction of CNFSubClauses
 * And each CNFSubClause in turn consists of a disjunction of Literals
 */
public class CNFClause 
{
    public ArrayList<CNFSubClause> theClauses = new ArrayList<CNFSubClause>();
    
    public ArrayList<CNFSubClause> getSubclauses()
    {
        return theClauses;
    }
    
    public int size(){
    	return theClauses.size();
    }
    
    public CNFSubClause get(int i){
    	return theClauses.get(i);
    }
    
    public boolean contains(CNFSubClause newS)
    {
        for(int i = 0; i < theClauses.size(); i ++)
        {
            if(theClauses.get(i).getLiterals().equals(newS.getLiterals()))
            {
                return true;
            }
        }
        return false;
    }
}
