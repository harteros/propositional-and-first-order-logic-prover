/*
 *  Copyright (c) 2019, Lefteris Harteros, All rights reserved.
 *
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class Algorithms {

	public static ReadClauses reader=new ReadClauses();

	public void run_algorithm(int choice,String filename,CNFSubClause a) throws FileNotFoundException, IOException{

		Iterator<Literal> iter=a.getLiteralsList();
		if(iter.hasNext()){
			Literal literal=iter.next();
			//check for errors in the input
			if((choice==1 || choice==2)  && (filename.contains("PKL") || literal.getArguments()!=null)){
				if(filename.contains("PKL")){
					System.out.println("Wrong filename. Filename must not contain \"PKL\" ending.");
					System.out.println("**************************");
				}else{
					System.out.println("Wrong literal for conclusion. Literal for conclusion must not have arguments.");
					System.out.println("**************************");
				}
			}else if(choice==3 && (!filename.contains("PKL") || literal.getArguments()==null)){
				if(!filename.contains("PKL")){
					System.out.println("Wrong Filename. Filename must contain \"PKL\" ending.");
					System.out.println("**************************");
				}else{
					System.out.println("Wrong Literal for conclusion. Literal for conclusion must have arguments.");	
					System.out.println("**************************");
				}		
			}else if((choice==2 || choice==3) && a.size()!=1){
				System.out.println("Wrong Literal for conclusion.The subclause should contain only ONE Literal for conclusion not more.");	
				System.out.println("**************************");
				//end error check
			}else{

				reader.setData(filename);//read file
				if(reader.size()!=0){
					CNFClause KB = new CNFClause();
					for(int i=0; i<reader.size(); i++){//for each sublcause
						System.out.println("This is a sub clause...");
						reader.get(i).print();
						KB.getSubclauses().add(reader.get(i));//add subclause to knowledge base
						System.out.println("**************************");
						System.out.println();
					}
					System.out.println("----------------------------------------------------");
					System.out.println();
					System.out.println("Algorithm started");
					System.out.println("**************************");
					if(choice==1){//propositional logic resolution
						boolean b = PL_Resolution.Pl_Resolution(KB, a);
						System.out.println("Algorithm finished");
						System.out.println("**************************");
						System.out.println("The non-negated subclause of :");
						a.print();
						System.out.println("is " + b);
						System.out.println("**************************");

					}else if(choice==2){//propositional logic horn
						boolean b = PL_Horn.Pl_Horn(KB, literal);
						System.out.println("Algorithm finished");
						System.out.println("**************************");
						a.print();
						System.out.println("is " + b);
						System.out.println("**************************");

					}else if(choice==3){//first-order logic horn
						boolean b = Fol_Horn.PKL_Horn(KB, literal);
						System.out.println("Algorithm finished");
						System.out.println("**************************");
						a.print();
						System.out.println("is " + b);
						System.out.println("**************************");

					}else{
						System.out.println("Invalid choice. Program will now exit");
					}
				}
			}

		}else{
			System.out.println("No literals given for conclusion.");

		}

	}
}
