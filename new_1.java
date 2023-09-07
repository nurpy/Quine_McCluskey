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
		final int MSB=5;
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
		char[] letters = {'b','a','d','c','e'};
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
		System.out.println("enter numbers");
		List<Integer> nums = new ArrayList<Integer>();
		while(scan.hasNextInt()){
			nums.add(scan.nextInt());
		}
		return nums;
	}
	public static int detemrineImplicant(expression n1, expression n2){
		int n=Math.abs(n2.minterm-n1.minterm);
		int count=0;
		if(n==0)
			return -1;
		for(int i=1; i<n;i<<=1){
			count++;
			if(n==i)
				break;
		}
		
		return count;
	}
	public static Set<Integer> primeImplicants(int num,int MSB,Set<Integer> bitExclusion){
		
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
	public int iterations=0;
	public int MSB;
	equation(Scanner scan,int MSB){
		this.MSB=MSB;
		
		
		
		implicants = new ArrayList<expression>();
		List<Integer> parsednums= functions.parseInts(scan);
		
		
		minterms=new ArrayList<expression>();
		for( int num: parsednums){
			expression n = new expression(num, MSB);
			n.dontCare=false;
			minterms.add(n);
		}
		System.out.println("entered");
		
		List<Integer> parsednums2=(functions.parseInts(scan));
		for( int num: parsednums2){
			expression n = new expression(num, MSB);
			n.dontCare=true;
			minterms.add(n);
		}
		
	}
	
	public void minimizeExpression(){
		for(expression n: minterms){
			for(expression h: minterms){
				

				if(n.possibleMatches.contains(h.minterm)|| h.possibleMatches.contains(n.minterm) || n.equals(h)){
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
		
		iterations++;	
		implicants.clear();
	}
	public void minimizeQuine(){
		List<expression> tempList = new ArrayList<expression>();
		for(expression n: minterms){
			for(expression h: minterms){
				//System.out.println(n.rep);
				
				if((n.possibleMatches.contains(h.minterm) || h.possibleMatches.contains(n.minterm)|| n.equals(h))){
					String temp=n.rep.replace("'","");
					int numOfDashes= temp.length()-temp.replace("-","").length();
					if(numOfDashes >iterations-1){
						tempList.add(n);
						continue;
					}
					
					int excludeBit=functions.detemrineImplicant(n,h);
					n.exclusionBits.add(excludeBit);
					expression newExpression = new expression(n.minterm,MSB,n.exclusionBits);
					newExpression.getExpression();
					
					implicants.add(newExpression);
				}
				
			}
		}
		if(tempList.equals(minterms)){
			implicants.addAll(tempList);
		}
		
		
		minterms.clear();
		iterations++;
		System.out.println(iterations);
		for(expression i: implicants){
			
			
			if(!minterms.contains(i)){
					minterms.add(i);
			}
		}
				
		implicants.clear();
		
	}
	
	
	public String toString(){
		String returnString="";
		for(expression n: minterms){
			returnString+= n.rep.replace("-","");
			returnString+=" + ";
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
	public boolean dontCare=false;
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
	public String toString(){
		String returnString = rep;
		return returnString;
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
		if(trueOBJ.rep == null || this.rep == null)
			return false;
		if(trueOBJ.rep.equals(this.rep)) 
			return true;
		
		return false;
		}
		
	}
	


