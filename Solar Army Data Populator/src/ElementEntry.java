
public class ElementEntry {
	String atomicSymbol;
	String salt;
	String concentration;
	
	ElementEntry(String atomicSymbol, String salt, String concentration){
		this.atomicSymbol = atomicSymbol.trim();
		this.atomicSymbol = atomicSymbol.toLowerCase();
		this.salt = salt;
		this.concentration = concentration;
	}
	
	@Override public String toString(){
		return "Atomic Symbol: " + atomicSymbol + '\n'
			 + "Salt Used: " + salt + '\n'
			 + "Concentration: " + concentration + '\n';
	}
}
