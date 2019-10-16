/*
 *  Copyright (c) 2019, Lefteris Harteros, All rights reserved.
 *
 */

public class Unification {

	public String[] unify(String[] a,String[] b){
		String[] list=new String[a.length*2];
		String[] fail={"fail"};
		String[] empty={"empty"};
		if(a.length!=b.length){//if there are not the same amount of arguments finish
			return fail;
		}else{
			int counter=0;
			for(int i=0; i<a.length; i++){
				boolean var1=false;//false means thats its a constant
				boolean var2=false;
				boolean nested1=false;//false means that its a variable or a constant in the brackets and not another function with argument
				boolean nested2=false;
				//lower case argument means variable
				//upper case argument means constant or
				//upper case argument means function (if function is found if the character after the last "(" bracket is lower case it means variable otherwise constant)
				if(Character.isLowerCase(a[i].charAt(0))){
					var1=true;
				}else if(Character.isUpperCase(a[i].charAt(0)) && a[i].contains("(") && a[i].contains(")")){
					nested1=true;
					if(Character.isLowerCase(a[i].charAt(a[i].lastIndexOf("(")+1))){
						var1=true;
					}
				}
				if(Character.isLowerCase(b[i].charAt(0))){
					var2=true;
				}else if(Character.isUpperCase(b[i].charAt(0)) && b[i].contains("(") && b[i].contains(")")){
					nested2=true;
					if(Character.isLowerCase(b[i].charAt(b[i].lastIndexOf("(")+1))){
						var2=true;
					}
				}

				if(!var1 && !var2){//both constants
					if(!a[i].equals(b[i])){//if constant are not the same
						return fail;
					}
				}else if(var1){//if the first argument is variable
					if(!nested1 && b[i].contains(a[i])){//if the string is contained in the second string array
						return fail;
					}else if(nested1 && b[i].contains(Character.toString(a[i].charAt(a[i].lastIndexOf("(")+1)))){
						return fail;
					}else{//if the string in not contained
					
						counter=add(list,counter,a[i],b[i],nested1 && nested2);
						if(counter==-1) return fail;

					}
				}else if(var2){//if the second argument is variable
					if(!nested2 && a[i].contains(b[i])){//if the string is contained in the second string array
						return fail;
					}else if(nested2 && a[i].contains(Character.toString(b[i].charAt(b[i].lastIndexOf("(")+1)))){
						return fail;
					}else{//if the string in not contained

						counter=add(list,counter,b[i],a[i],nested1 && nested2);//add items that can be unified to the list
						if(counter==-1) return fail;
					}
				}
			}
		}
		if(list[0]==null){//if list is empty it means that the two strings are identical 
			return empty;
		}else{
			check_for_replace(list);//check if we can replace any variable with something we learned after 
			//eg. if we have : x-->John and Parent(x)-->y ==> x-->John y-->Parent(John)

		}
		return list;
	}

	//checks if what we learned can replace a variable and swaps position if a variable becomes constant
	private void check_for_replace(String[] list){
		for(int i=0; i<list.length; i=i+2){//in even spots we have the variables thats why we increase i by two
			if(list[i]!=null){
				for(int j=0; j<list.length; j=j+2){
					if(i!=j){//if we are not talking about the same string
						if(list[j]!=null){
							if(list[j].contains(list[i])){//if the same variable is found in another spot it means we know something more about it
								list[j]=list[j].replace(list[i], list[i+1]);//replace what we know about the variable with the variable
								if(Character.isUpperCase(list[j].charAt(0)) && list[j].contains("(") && list[j].contains(")")){//if its a function where it was contained
									if(Character.isUpperCase(list[j].charAt(list[j].lastIndexOf("(")+1))){//if the variable turned into constant after replacing the strings
										String temp=list[j];//swap the spots (in even spots we have variables in odd spots we have constants)
										list[j]=list[j+1];
										list[j+1]=temp;
									}
								}
							}else if(list[j+1].contains(list[i])){
								list[j+1]=list[j+1].replace(list[i], list[i+1]);//replace what we know about the variable with the variable
							}
						}
					}
				}
			}
		}
	}

	//checks if an array contains a specific string
	private boolean contains(String[] b,String a){	
		for(int i=0; i<b.length; i++){//check if any string of the array b contains the variable a
			if(b[i]!=null){
				if(b[i].equals(a)){
					return true;
				}
			}
		}
		return false;
	}

	private int add(String[] list,int counter,String a,String b,boolean nested){

		if(nested){//if both strings are nested
			int[] spots=sub(a,b);//get from which parenthesis they should be replaced
			if(spots==null) return -1;
			if(!contains(list,a.substring(spots[0],spots[1])) && !contains(list,b.substring(spots[2],spots[3]))){//if the unified variables are not in the list add them
				list[counter]=a.substring(spots[0],spots[1]);
				counter++;
				list[counter]=b.substring(spots[2],spots[3]);
				counter++;
			}else if((contains(list,a.substring(spots[0],spots[1])) || contains(list,b.substring(spots[2],spots[3])))//if one of the two variables already exists in the list
					&& !(contains(list,a.substring(spots[0],spots[1])) && contains(list,b.substring(spots[2],spots[3])))){//it means that it can subsitute two different values
				return -1;																									//which is false 
			}
		}else{//if at least one is not nested
			if(!contains(list, a) && !contains(list, b)){//if the unified variables are not in the list add them
				list[counter]=a;
				counter++;
				list[counter]=b;
				counter++;
			}else if((contains(list,a) || contains(list,b)) && !(contains(list,a) && contains(list,b))){//check if one of the two exists
				return -1;
			}
		}
		return counter;

	}
	//return an array with the indexes from which we should take each variable that is going to be subsituted
	//if both variables are inside functions 
	private int[] sub(String a,String b){
		String pref="\\(";				
		int count1=a.split(pref).length - 1;
		int count2=b.split(pref).length - 1;
		int dif=count2-count1;
		int[] spots={-1,-1,-1,-1};
		if(dif==0){
			if(!a.substring(0, a.lastIndexOf("(")+1).equals(b.substring(0, a.lastIndexOf("(")+1))) return null;
			spots[0]=a.lastIndexOf("(")+1;
			spots[1]=a.indexOf(")");
			spots[2]=b.lastIndexOf("(")+1;
			spots[3]=b.indexOf(")");
		}else if(dif>0){
			if(!a.substring(0, a.lastIndexOf("(")+1).equals(b.substring(0, a.lastIndexOf("(")+1))) return null;
			spots[0]=a.lastIndexOf("(")+1;
			spots[1]=a.indexOf(")");
			spots[2]=a.lastIndexOf("(")+1;
			spots[3]=b.indexOf(")",b.indexOf(")")+dif);
		}else{
			if(!a.substring(0, b.lastIndexOf("(")+1).equals(b.substring(0, b.lastIndexOf("(")+1))) return null;
			spots[0]=b.lastIndexOf("(")+1;
			spots[1]=a.indexOf(")",a.indexOf(")")-dif);
			spots[2]=b.lastIndexOf("(")+1;
			spots[3]=b.indexOf(")");
		}
		return spots;
	}
}
