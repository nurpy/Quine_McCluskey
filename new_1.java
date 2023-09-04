import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.lang.Math;
class new_1
{
	public static void main(String[] args)
	{
		final int MSB=4;
		Scanner scan = new Scanner(System.in);
		equation quine = new equation(scan,MSB);
		quine.minimizeExpression();
		System.out.println(quine);
		System.out.println("----next line-----");
		quine.minimizeQuine();
		System.out.println(quine);
		System.out.println("----next line-----");
		quine.minimizeQuine();
		System.out.println(quine);
		System.out.println("----next line-----");
		quine.minimizeQuine();
		System.out.println(quine);
		
		

	}



}
class functions
{
	public static String convertExpression(int minterm,Set<Integer> bitExclusion){
		char[] letters = {'a','b','c','d'};
		String returnString="";
		int tempMinterm=minterm;
		
		//bitExclusion = new HashSet<Integer>();
		int count=0;
		
		for(int i=1; i<Math.pow(letters.length,2);i<<=1){
			if(bitExclusion.contains(count)){
				returnString+="-";
				count++;
				continue;
			}
			if((tempMinterm&i) !=0){
				returnString+=letters[count];
			}
			else{
				returnString+=letters[count];
				returnString+="'";
			}
			count++;
		}
		return returnString;
			
		
		
	}
	public static List<Integer> parseInts(Scanner scan){
		List<Integer> nums = new ArrayList<Integer>();
		while(scan.hasNextInt()){
			nums.add(scan.nextInt());
		}
		return nums;
	}
	public static int detemrineImplicant(expression n1, expression n2){
		int n=Math.abs(n2.minterm-n1.minterm);
		int count=0;
		for(int i=1; i<n;i<<=1){
			count++;
			if(n==i)
				break;
		}
		
		return count;
	}
	public static Set<Integer> primeImplicants(int num,int MSB,Set<Integer> bitExclusion){
		System.out.println(bitExclusion);
		bitExclusion =new HashSet<Integer>();
		Set<Integer> n =new HashSet<Integer>();
		int temp=num;
		int count=0;
		for(int i=1; i<Math.pow(MSB,2);i<<=1){
			if(bitExclusion.contains(count)){
				temp=temp&~(1<<count);
			}
			n.add(temp^i);
			count++;
		}
		return n;
		
		
	}
	
	
}
class equation{
	List<expression> minterms;
	List<expression> implicants;
	Set<expression> connections;

	public int MSB;
	equation(Scanner scan,int MSB){
		this.MSB=MSB;
		implicants = new ArrayList<expression>();
		List<Integer> parsednums= functions.parseInts(scan);
		minterms=new ArrayList<expression>();
		for( int num: parsednums){
			minterms.add(new expression(num, MSB));
		}
		
	}
	
	public void minimizeExpression(){
		for(expression n: minterms){
			for(expression h: minterms){
				if(n.possibleMatches.contains(h.minterm)|| h.possibleMatches.contains(n.minterm)){
					expression newExpression = new expression(n.minterm,MSB,functions.detemrineImplicant(n,h));
					newExpression.getExpression();
					implicants.add(newExpression);
				}
				
			}
		}
		minterms.clear();
		
		for(expression i: implicants){
			if(!minterms.contains(i))
				minterms.add(i);
		}
				
		implicants.clear();
	}
	public void minimizeQuine(){
		for(expression n: minterms){
			for(expression h: minterms){
				if(n.possibleMatches.contains(h.minterm) || h.possibleMatches.contains(n.minterm)){
					int excludeBit=functions.detemrineImplicant(n,h);
					h.exclusionBits.addAll(n.exclusionBits);
					h.exclusionBits.add(excludeBit);
					expression newExpression = new expression(n.minterm,MSB,h.exclusionBits);
					newExpression.getExpression();
					
					implicants.add(newExpression);
				}
				
			}
		}
		minterms.clear();
		
		for(expression i: implicants){
			System.out.println(i.rep);
			if(!minterms.contains(i))
				minterms.add(i);
		}
				
		implicants.clear();
		
	}
	
	
	public String toString(){
		String returnString="";
		for(expression n: minterms){
			//n.getExpression();
			returnString+=Integer.toString(n.minterm);
			returnString+=", ";
			returnString+= n.exclusionBits;
			returnString+=", ";
			returnString+= n.rep;
			returnString+=", ";
		}
		implicants.clear();
		return returnString;
	}
}
class expression{
	public Set<Integer> possibleMatches;
	int minterm;
	int MSB;
	String rep;
	public Set<Integer> exclusionBits;
	expression(int minterm, int MSB){
		this.MSB=MSB;
		this.minterm=minterm;
		createPrimeImplicants();
	}
	expression(int minterm, int MSB,int exclusionBit){
		this.MSB=MSB;
		this.minterm=minterm;
		exclusionBits= new HashSet<Integer>();
		exclusionBits.add(exclusionBit);
		createPrimeImplicants();
	}
	expression(int minterm, int MSB,Set<Integer> exclusionBits){
		this.MSB=MSB;
		this.minterm=minterm;
		this.exclusionBits= exclusionBits;
		createPrimeImplicants();
	}
	String getExpression(){
	
		String s =functions.convertExpression(minterm,exclusionBits);
		
		this.rep=s;
		return s;
		
	}
	void createPrimeImplicants(){
		possibleMatches=functions.primeImplicants(minterm,MSB,exclusionBits);
	}
	public boolean equals(Object obj){
		if (this == obj){
			return true;
		}
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		expression trueOBJ = (expression) obj;
		if(trueOBJ.rep.equals(this.rep))
			return true;
		return false;
		
	}
	
}

