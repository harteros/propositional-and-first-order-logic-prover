/*
 *  Copyright (c) 2019, Lefteris Harteros, All rights reserved.
 *
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main
{ 

	private static Algorithms algs=new Algorithms();
	private static Scanner in = new Scanner(System.in);
	public static void main(String[] args) throws FileNotFoundException, IOException {

		while(true){
			System.out.println("Select one of the following methods by choosing the corresponding number");
			System.out.println("1. Propositional logic resolution");
			System.out.println("2. Propositional logic horn");
			System.out.println("3. First-order logic horn");
			System.out.println("0. Exit");
			System.out.print("Enter your choice : ");
			String choice=in.nextLine();
			System.out.println("**************************");
			CNFSubClause conclusion=new CNFSubClause();

			if(choice.equals("1")){//propositional logic resolution

				String next="-1";
				System.out.println("Enter the negation of the subclause you want to prove in CNF.");
				do{
					System.out.print("Enter Literal name for conclusion : ");
					String name=in.nextLine();
					System.out.println("**************************");

					String negation="-1";
					do{
						System.out.print("Enter Literal negation (0 for false, 1 for true) : ");
						negation=in.nextLine();
						System.out.println("**************************");

					}while(!negation.equals("0") && !negation.equals("1"));			
					Literal a=new Literal(name,negation.equals("1"));
					conclusion.getLiterals().add(a);				
					do{
						System.out.print("Add more literals [Y/N] ? : ");
						next=in.nextLine();
						System.out.println("**************************");

					}while(!next.equals("Y") && !next.equals("N"));
				}while(next.equals("Y"));
				System.out.println("First-order logic files must not have the ending \"_PKL.json\"");
				System.out.print("Enter Knowleadge Base filename : ");
				String filename=in.nextLine();
				System.out.println("**************************");
				algs.run_algorithm(Integer.valueOf(choice),filename,conclusion);	

			}else if(choice.equals("2")){//propositional logic horn

				System.out.print("Enter Literal name for conclusion : ");
				String name=in.nextLine();		
				System.out.println("**************************");
				Literal a=new Literal(name,false);
				conclusion.getLiterals().add(a);
				System.out.println("First-order logic files must not have the ending \"_PKL.json\"");
				System.out.print("Enter Knowleadge Base filename : ");
				String filename=in.nextLine();
				System.out.println("**************************");
				algs.run_algorithm(Integer.valueOf(choice),filename,conclusion);	

			}else if(choice.equals("3")){//first-order logic horn

				System.out.print("Enter Literal name for conclusion : ");
				String name=in.nextLine();
				System.out.println("**************************");
				System.out.println("Enter Literal arguments separated by \",\" ");
				System.out.println("For functions or constants first character must be upper case eg. Father(Tom),John");
				System.out.println("For variables first character must be lower case eg. x,y");
				System.out.print("Enter the arguments : ");
				String arguments=in.nextLine();	
				System.out.println("**************************");
				Literal a=new Literal(name,arguments.split(","),false);
				conclusion.getLiterals().add(a);
				System.out.println("First-order logic files must have the ending \"_PKL.json\"");
				System.out.print("Enter Knowleadge Base filename : ");
				String filename=in.nextLine();
				System.out.println("**************************");
				algs.run_algorithm(Integer.valueOf(choice),filename,conclusion);

			}else if(choice.equals("0")){//exit

				System.out.println("Program will now quit");
				break;
			}
		}
	
	}
}