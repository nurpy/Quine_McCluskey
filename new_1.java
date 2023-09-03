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
		quine.minimizeExpression();
		System.out.println(quine);
		

	}



}
class functions
{
	public static List<Integer> parseInts(Scanner scan){
		List<Integer> nums = new ArrayList<Integer>();
		while(scan.hasNextInt()){
			nums.add(scan.nextInt());
		}
		return nums;
	}
	public static int detemrineImplicant(expression n1, expression n2){
		int n=Math.abs(n1.minterm-n2.minterm);
		int count=0;
		for(int i=1; i<n;i<<=1){
			count++;
			if(n==i)
				break;
		}
		System.out.println(count);
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
				System.out.println(n.possibleMatches);
				if(n.possibleMatches.contains(h.minterm)){
					implicants.add(new expression(n.minterm,MSB,functions.detemrineImplicant(n,h)));
				}
				
			}
		}
		minterms.clear();
		for(expression i: implicants){
			minterms.add(i);
		}
		implicants.clear();
	}
	
	
	public String toString(){
		String returnString="";
		for(expression n: minterms){
			returnString+=Integer.toString(n.minterm);
			returnString+=", ";
			returnString+= n.exclusionBits;
			returnString+=", ";
		}
		implicants.clear();
		return returnString;
	}
}
class expression{
	Set<Integer> possibleMatches;
	int minterm;
	int MSB;
	Set<Integer> exclusionBits;
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
	void getExpression(){
		
	}
	void createPrimeImplicants(){
		
		possibleMatches=functions.primeImplicants(minterm,MSB,exclusionBits);
	}
	
}
class Connection{
	
	
	
	
	
}
