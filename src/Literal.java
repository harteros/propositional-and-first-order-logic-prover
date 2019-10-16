/*
 *  Copyright (c) 2019, Lefteris Harteros, All rights reserved.
 *
 */

/*
 * Represents a literal; a variable
 */
public class Literal implements Comparable<Literal>
{
	//The name of the literal
	private String name;
	//Whether or not the literal is negated; if negation is true then it is negated
	private boolean negation;
	//the arguments of each variable in first order logic (used only for FOL-HORN else null) 
	private String[] arguments;
	public Literal(String name, boolean negation)
	{
		this.name = name;
		this.negation = negation;
		this.arguments=null;
	}
	public Literal(String name,String[] args, boolean negation)
	{
		this.name = name;
		this.arguments=args;
		this.negation = negation;
	}
	public Literal(Literal lit)
	{
		this.name = lit.name;
		this.negation = lit.negation;
	}
	public void print()
	{
		if(arguments==null) {
			if (negation)
				System.out.println("NOT " + name);
			else
				System.out.println(name);
		}else{
			for(int i=0; i<arguments.length; i++) {
				if(i==0) {
					if (negation)
						System.out.print("NOT " + name + "("+arguments[i]);
					else
						System.out.print(name + "("+arguments[i]);
				}else{
					System.out.print(","+arguments[i]);
				}
			}
			System.out.println(")");

		}
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public String[] getArguments() {return this.arguments;}

	public void setArguments(String[] args){this.arguments=args;}

	public void setNegation(boolean negation)
	{
		this.negation = negation;
	}

	public boolean getNegation()
	{
		return this.negation;
	}

	//Override
	public boolean equals(Object obj)
	{
		Literal l = (Literal)obj;
		if(arguments==null){
			if(l.getName().compareTo(this.name) ==0 && l.getNegation() == this.negation)
			{
				return true;
			}
			else
			{
				return false;
			}
		}else{
			if(l.getName().compareTo(this.name) ==0 && l.getNegation() == this.negation && l.getArguments().length==this.getArguments().length)
			{
				for(int i=0; i<l.getArguments().length; i++){
					if(!l.getArguments()[i].equals(this.getArguments()[i])){
						return false;
					}
				}
				return true;
			}
			else
			{
				return false;
			}
		}

	}

	//@Override
	public int hashCode()
	{
		int code=0;
		if(arguments!=null){
			for(int i=0; i<arguments.length; i++){
				code+=arguments[i].hashCode();
			}
		}
		if(this.negation)
		{
			return code + this.name.hashCode() + 1;
		}
		else
		{
			return code + this.name.hashCode() + 0;                        
		}
	}

	//@Override
	public int compareTo(Literal x)
	{
		int a = 0;
		int b = 0;
		int check=0;

		if(x.getNegation())
			a = 1;

		if(this.getNegation())
			b = 1;
		if(arguments!=null){

			for(int i=0; i<arguments.length; i++){
				check+=x.getArguments()[i].compareTo(arguments[i]);
			}
		}
		return check + x.getName().compareTo(name) + a-b;


	}    
}
