/*
 *  Copyright (c) 2019, Lefteris Harteros, All rights reserved.
 *
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


public final class Fol_Horn {

	private static Unification un=new Unification();

	public static boolean PKL_Horn(CNFClause KB, Literal a){
		CNFClause clauses =new CNFClause();//copy of KB
		clauses.getSubclauses().addAll(KB.getSubclauses());
		HashSet<Literal> new_item = new HashSet<Literal>();//set that contains facts we learned in each iteration
		ArrayList<Literal> facts = new ArrayList<Literal>();//facts in the knowleadgw base

		for(int i=0; i<clauses.size(); i++){
			if(clauses.get(i).size()==1){//if subclause size is 1 its a fact
				facts.addAll(clauses.get(i).getLiterals());
			}
		}
		int times=0;
		do{

			new_item.clear();
			for(int i=0; i<clauses.size(); i++){//for each clause
				if(clauses.get(i).size()!=1){//if subclause is a sentence
					times++;
					CNFSubClause temp=new_vars(clauses.get(i),times);//add new vars to the sentence
					Iterator<Literal> iter=temp.getLiteralsList();//iterator for each literal of the subclause
					ArrayList<String> theta=new ArrayList<String>();//unification theta
					int counter=0;
					boolean error_un=false;
					Literal conclusion=null;
					while(iter.hasNext()){//while there are literal in the subclause
						Literal lit=iter.next();
						for(int j=0; j<facts.size(); j++){//for each fact we know
							if(!lit.getNegation()==facts.get(j).getNegation() && lit.getName().equals(facts.get(j).getName())){//if sentence literal equals 
								//knowleadge base literal
								String[] th=un.unify(lit.getArguments(),facts.get(j).getArguments());//try and unify these 2 literals
								if(!th[0].equals("fail")){//if unification didnt fail
									counter++;
									for(int k=0; k<th.length; k=k+2){
										if(th[k]!=null){
											if(!theta.contains(th[k]) && !theta.contains(th[k+1])){//if variable is not already in theta add it
												theta.add(th[k]);
												theta.add(th[k+1]);
											}else if(!theta.contains(th[k]) || !theta.contains(th[k+1])){//if only one of the two variables is in theta
												error_un=true;											//we cant unify cause for one variable we got 
											}															//two values
										}else break;
									}
								}
							}else if(!lit.getNegation()){//if literal is not negated its the conclusion if the sentence
								conclusion=lit;
							}
						}
					}
					if(!error_un){//if no error happened during unification
						System.out.println("**************************");

						for(int f=0; f<theta.size(); f++){
							if(f%2==0)
								System.out.print(theta.get(f)+"----->");
							else{
								System.out.print(theta.get(f));
								System.out.println();
							}
						}
						System.out.println("**************************");
						if(counter==temp.size()-1){//if counter equals with the number of the literal on the left side of the sentence its conclusion becomes a fact
							Literal b=subst(theta,conclusion);//substitute the variables in the conclusion with theta
							boolean exists=false;
							for(int k=0; k<facts.size(); k++){//check if the literal exists in the facts we know
								if(b.equals(facts.get(k))){
									exists=true;
									break;
								}
							}
							if(!exists){//if what we learned does not exists in the KB add it to the new items learned
								new_item.add(b);
								System.out.println("Examining :");
								b.print();

								
								String[] check_match=un.unify(b.getArguments(),a.getArguments());//try and unify the new fact with what we want to conclude
								if(check_match[0].equals("empty") && b.getName().equals(a.getName())){//if they unify perfectly (empty list returned = no errors found and everything the same) finish
									System.out.println("**************************");
									System.out.println("Unification successful");
									System.out.println("Literal concluded");
									System.out.println("**************************");
									return true;
								}else{//if what we concluded is not what we are searching for
									System.out.println("**************************");
									System.out.println("Literals cant unify");
									System.out.println("**************************");

									System.out.println("Added :");
									b.print();
									System.out.println("**************************");

									facts.add(b);//add item to the facts
								}
							}else{//if literal exists in KB
								System.out.println("Examining :");
								b.print();
								System.out.println("**************************");
								System.out.println("Literal already exists");
								System.out.println("**************************");

							}
						}

					}else{//if error happened during unification
						System.out.println("**************************");
						System.out.println("Unification failed");
						System.out.println("**************************");

					}
				}
			}


		}while(!new_item.isEmpty());
		return false;
	}
	
	//changes variables of literal b with the ones in theta
	private static Literal subst(ArrayList<String> theta,Literal b){
		String[] args=b.getArguments();
		for(int i=0; i<theta.size(); i=i+2){
			for(int j=0; j<args.length; j++){

				if(theta.get(i).equals(args[j])){//if the literal argument does not contain function
					args[j]=theta.get(i+1);

				}else if(args[j].contains("(") && args[j].substring(args[j].lastIndexOf("(")+1,args[j].indexOf(")")).equals(theta.get(i))){//if it contains function 
					String r=args[j].substring(args[j].lastIndexOf("(")+1,args[j].indexOf(")"));
					args[j]=args[j].replace(r,theta.get(i+1));

				}
			}
		}
		Literal new_b=b;
		new_b.setArguments(args);
		return new_b;
	}


	//changes variables of a subclause to new ones 
	private static CNFSubClause new_vars(CNFSubClause s,int k){
		CNFSubClause sub=s;
		ArrayList<String> args=new ArrayList<String>();

		Iterator<Literal> iter = sub.getLiteralsList(); //iterator for the literals of the subclause
		while(iter.hasNext()){//while there are more literals

			Literal l = iter.next();
			for(int j=0; j<l.getArguments().length; j++){//for each argument of the literal

				String ar=l.getArguments()[j];
				if(!args.contains(ar)){

					if(ar.contains("(") && Character.isLowerCase(ar.charAt(ar.lastIndexOf("(")+1))){//if the argument is in a function and its a variable(start with lower case char after the last parenthesis)
						args.add(Character.toString(ar.charAt(ar.lastIndexOf("(")+1)));//add it to the arguments
					}else if((!ar.contains("(")) && Character.isLowerCase(ar.charAt(0))){//if the argument is not in a function and its a variable (start with lower case char)
						args.add(ar);//add it to the arguments
					}
				}

			}
		}
		//for the second iteration after the variables have been found			
		replace(args,sub,k);//replace the existing variables of the literal with new ones

		sub.print();
		return sub;
	}

	//replaces the arguments of the subclause which are not constant (they are variables) 
	//with the same ones plus a number next to them (input k) based on a counter which is constantly growing
	private static void replace(ArrayList<String> args,CNFSubClause sub,int k){
		for(int i=0; i<args.size(); i++){
			Iterator<Literal> iter = sub.getLiteralsList();  
			while(iter.hasNext()){	
				Literal l = iter.next();
				for(int j=0; j<l.getArguments().length; j++){
					if(l.getArguments()[j].equals(args.get(i))){//if arguments are the same change the name of the argument 
						l.getArguments()[j]=Character.toString(args.get(i).charAt(0))+Integer.toString(k);
					}else if(l.getArguments()[j].contains("(") && Character.toString(l.getArguments()[j].charAt(l.getArguments()[j].lastIndexOf("(")+1)).equals(args.get(i))){//if the argument is in a function 
						String r=l.getArguments()[j].substring(l.getArguments()[j].lastIndexOf("(")+1,l.getArguments()[j].indexOf(")"));//get the argument inside the function and change its name 
						l.getArguments()[j]=l.getArguments()[j].replace(r,Character.toString(args.get(i).charAt(0))+Integer.toString(k));
					}
				}
			}
		}
	}

}
