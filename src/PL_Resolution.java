/*
 *  Copyright (c) 2019, Lefteris Harteros, All rights reserved.
 *
 */

import java.util.ArrayList;

public final class PL_Resolution {

	//The resolution algorithm
		public static boolean Pl_Resolution(CNFClause KB, CNFSubClause a)
		{
			//a clause that contains all the clauses of our Knowledge Base
			CNFClause clauses = new CNFClause();
			clauses.getSubclauses().addAll(KB.getSubclauses());
			//add the negation of the subclause we want to prove to our Knowledge Base
			clauses.getSubclauses().add(a);

			System.out.println("We want to prove...");
			a.print();

			boolean stop = false;
			int step = 1;
			//We will try resolution till either we reach a contradiction or cannot produce any new clauses
			while(!stop)
			{
				ArrayList<CNFSubClause> newsubclauses = new ArrayList<CNFSubClause>();
				ArrayList<CNFSubClause> subclauses = clauses.getSubclauses();

				System.out.println("Step:" + step);
				step++;
				//For every pair of clauses Ci, Cj...
				for(int i = 0; i < subclauses.size(); i++)
				{
					CNFSubClause Ci = subclauses.get(i);

					for(int j = i+1; j < subclauses.size(); j++)
					{
						CNFSubClause Cj = subclauses.get(j);

						//...we try to apply resolution and we collect any new clauses
						ArrayList<CNFSubClause> new_subclauses_for_ci_cj = CNFSubClause.resolution(Ci, Cj);

						//We check the new subclauses...
						for(int k = 0; k < new_subclauses_for_ci_cj.size(); k++)
						{
							CNFSubClause newsubclause = new_subclauses_for_ci_cj.get(k);

							//...and if an empty subclause has been generated we have reached contradiction; and the literal has been proved
							if(newsubclause.isEmpty()) 
							{   
								System.out.println("----------------------------------");
								System.out.println("Resolution between");
								Ci.print();
								System.out.println("and");
								Cj.print();
								System.out.println("produced:");
								System.out.println("Empty subclause!!!");
								System.out.println("----------------------------------");
								return true;
							}

							//All clauses produced that don't exist already are added
							if(!newsubclauses.contains(newsubclause) && !clauses.contains(newsubclause))
							{
								System.out.println("----------------------------------");
								System.out.println("Resolution between");
								Ci.print();
								System.out.println("and");
								Cj.print();
								System.out.println("produced:");
								newsubclause.print();
								newsubclauses.add(newsubclause);
								System.out.println("----------------------------------");
							}
						}                           
					}
				}

				boolean newClauseFound = false;

				//Check if any new clauses were produced in this loop
				for(int i = 0; i < newsubclauses.size(); i++)
				{
					if(!clauses.contains(newsubclauses.get(i)))
					{
						clauses.getSubclauses().addAll(newsubclauses);                    
						newClauseFound = true;
					}                        
				}

				//If not then Knowledge Base does not logically infer the literal we wanted to prove
				if(!newClauseFound)
				{
					System.out.println("Not found new clauses");
					stop = true;
				}   
			}
			return false;
		} 
}
