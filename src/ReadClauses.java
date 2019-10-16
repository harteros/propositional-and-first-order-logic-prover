/*
 *  Copyright (c) 2019, Lefteris Harteros, All rights reserved.
 *
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadClauses {

	ArrayList<CNFSubClause> clause= new ArrayList<CNFSubClause>();//arraylist with the subclauses from the json file
	
	public void setData(String filename) throws FileNotFoundException, IOException{
		if(!clause.isEmpty()) clause.clear();
		JSONParser parser = new JSONParser();
		FileReader f=null;
		boolean isPKL=false;
		try {
			f = new FileReader(filename);
		} catch (FileNotFoundException ex) {
			System.out.println("File "+filename+" does not exist");
			System.out.println("**************************");
			return;
		}
		try{
			if(filename.contains("PKL")) isPKL=true;
		   Object clause_file = parser.parse(f);//parses the file and puts it into an object
		   JSONArray clauses = (JSONArray)clause_file;//casts object to JSON array
				//for each subclause creates a new CNFSubClause object and adds it to the arraylist with the subclauses
				for (int i = 0; i < clauses.size(); i++) {
					CNFSubClause cl = new CNFSubClause();
					JSONObject sub_clause = getObjectFromArrayAtIndex(clauses, i);
					JSONArray literals = getArrayOfObjectWithId(sub_clause, "CNFSubClause");
					//for each literal in the current subclause
					for (int j = 0; j < literals.size(); j++) {
						if(!isPKL){//if the file has propositional logic variables
							cl.getLiterals().add(new Literal((String) getObjectFromArrayAtIndex(literals, j).get("name"), (Boolean) getObjectFromArrayAtIndex(literals, j).get("neg")));
						}else{//if the file has first-order-logic variables
							String args=(String) getObjectFromArrayAtIndex(literals, j).get("arguments");//get the arguments of the literal
							String arguments[] = args.split(",");
							cl.getLiterals().add(new Literal((String) getObjectFromArrayAtIndex(literals, j).get("name"),arguments, (Boolean) getObjectFromArrayAtIndex(literals, j).get("neg")));

						}
					}
					clause.add(cl);
				}
			
		}catch(ParseException pe){
			System.out.println("position: " + pe.getPosition());
			System.out.println(pe);
		}
		try {			
			f.close();

	    }catch (IOException e) {
	    	System.err.println("Error closing file.");
	    }
	}
	
	public int size(){
		return clause.size();
	}
	public CNFSubClause get(int i){
		return clause.get(i);
	}
	public static JSONObject getObjectFromArrayAtIndex(JSONArray obj,int i){
		return (JSONObject)obj.get(i);
	}
	public static JSONArray getArrayOfObjectWithId(JSONObject obj,String id){
		return (JSONArray)obj.get(id);
	}
}

