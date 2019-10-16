/*
 *  Copyright (c) 2019, Lefteris Harteros, All rights reserved.
 *
 */

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;


public final class PL_Horn {
	
	//The propositional logic horn algorithm
	public static boolean Pl_Horn(CNFClause KB, Literal a){

		CNFClause clauses =new CNFClause();// a CNFClause that contains all the clauses of our Knowledge Base
		clauses.getSubclauses().addAll(KB.getSubclauses());
		//a Dictionary indexed by subclause that contains how many literals each subclause needs so that the literal that is not negated can be concluded
		Hashtable<CNFSubClause,Integer> count = new Hashtable<CNFSubClause,Integer>();
		//a Dictionary indexed by the name of the literal that shows which literal where inferred (initially all false)
		Hashtable<String,Boolean> inferred = new Hashtable<String,Boolean>();
		//a list with literal that are known to be true (facts)
		HashSet<Literal> agenda = new HashSet<Literal>();

		for(int i=0; i<clauses.size(); i++){

			if(clauses.get(i).size()==1){//if sub clause size is 1 then its a fact
				agenda.addAll(clauses.get(i).getLiterals());//add the fact to the agenda
				Literal fin=clauses.get(i).getLiteralsList().next();
				
				if(fin.getName().equals(a.getName())){
					System.out.println("Facts can always be concluded");
					System.out.println(fin.getName()+" is a fact");
					System.out.println("**************************");
					return true;
				}
			}else{//if not a fact
				int c=0;
				boolean exists=false;
				Iterator<Literal> iter = clauses.get(i).getLiteralsList();//iterator for the literals of the subclause "i"

				while(iter.hasNext()){//while there are more literals
					Enumeration<String> f=inferred.keys();//enumerator for the keys of the inferred dictionary
					Literal l = iter.next();
					if(l.getNegation()){//if the literal is on the left side of the sentence(its negated) 
						c++;
					}
					for(int j=0; j<inferred.size(); j++){//for all the different literals
						if(f.nextElement().equals(l.getName())){//if that literal is already in the in dictionary
							exists=true;
							break;
						}
					}
					if(!exists){//if the literal is not in the dictionary with the literals that need to be inferred
						inferred.put(l.getName(),false);//add it
					}

				}
				count.put(clauses.get(i),c);//add to the count the number of the negated literals of the subclause
			}
		}

		while(!agenda.isEmpty()){

			Iterator<Literal> agenda_iterator= agenda.iterator(); 

			while(agenda_iterator.hasNext()){

				Literal agenda_literal = agenda_iterator.next();//get a literal from the agenda
				agenda_iterator.remove();//remove the literal of the agenda

				System.out.println("Poped : "+agenda_literal.getName());


				if(!inferred.get(agenda_literal.getName())){//if its not inferred

					System.out.println("Conluding : "+agenda_literal.getName());
					inferred.put(agenda_literal.getName(),true);//make its value in the dictionary true



					Enumeration<CNFSubClause> keys=count.keys();//enumerates the subclauses of the dictionary count
					for(int i=0; i<count.size(); i++){ //for each <key,value> pair in count

						CNFSubClause sub_clause=keys.nextElement();//get a sublcause 
						Iterator<Literal> literal_iterator= sub_clause.getLiteralsList();//iterates through the literals of the subclause
						Literal positive=new Literal("-1",false);//literal for the not negated part of the subclause

						while(literal_iterator.hasNext()){//while the subclause has more literals

							Literal sub_clause_literal = literal_iterator.next();//get a literal

							if(sub_clause_literal.getNegation()){//if the literal is negated

								if(sub_clause_literal.getName().equals(agenda_literal.getName())){//if the literal that we took from the agenda exists in the subclause
									count.put(sub_clause,count.get(sub_clause)-1);//reduce the number of literals needed to conclude the sentence

									if(!positive.getName().equals("-1")){//if we have found the literal that is not negated
										if(count.get(sub_clause)==0){//if we can conclude the sentence 

											if(positive.getName().equals(a.getName())){//if the non-negated literal of the sentence is the one we want to conclude
												print_tree(count);
												System.out.println("**************************");
												return true;
											}
											else{//if its not the one we want to conclude
												if(!inferred.get(positive.getName())){//if its not inferred
													agenda.add(positive);//add it to the agenda
												}
											}
										}
									}
								}
							}else{//if the literal is not negated (positive)
								positive=sub_clause_literal;
								if(count.get(sub_clause)==0){//if we can conclude the sentence 

									if(sub_clause_literal.getName().equals(a.getName())){//if the non-negated literal of the sentence is the one we want to conclude
										print_tree(count);
										System.out.println("**************************");
										return true;
									}
									else{
										if(!inferred.get(positive.getName())){//if its not inferred
											agenda.add(positive);//add it to the agenda
										}
									}
								}

							}

						}
					}
					print_tree(count);
					System.out.println("**************************");

				}else{//if the literal has been inferred
					System.out.println("Already concluded : "+agenda_literal.getName());	
				}

			}
		}
		System.out.println("**************************");
		return false;
	}

	private static void print_tree(Hashtable<CNFSubClause,Integer> count){
		
		System.out.println("**************************");
		Enumeration<CNFSubClause> d=count.keys();//enumerated the subclauses of the dictionary count 
		for(int i=0; i<count.size(); i++){//for each subclause
			CNFSubClause fe=d.nextElement();//get a subclause
			Iterator<Literal> it= fe.getLiteralsList();//list of the literals of the subclause
			Literal positive=new Literal("-1",false);
			int j=0;
			while(it.hasNext()){//while there are more literals in the subclause
				Literal lw = it.next();
				if(lw.getNegation()){
					if(j==0){
						System.out.print(lw.getName());
						j++;
					}else{
						System.out.print("^"+lw.getName());
					}
				}else{
					positive=lw;
				}
			}
			System.out.print("=>"+positive.getName());
			System.out.print("  score: "+count.get(fe));
			System.out.println();	
		}
	}

}
