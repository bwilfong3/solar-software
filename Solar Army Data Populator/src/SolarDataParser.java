import java.io.File;
import java.sql.PreparedStatement;
import java.util.Hashtable;
import java.util.Vector;

public class SolarDataParser {
	
	ExcelParser ep;
	Hashtable<IntPair, ExcelCellData> resultsData;

	
	static final int TEMPLATE_BOUNDARY_X1 = 2;
	static final int TEMPLATE_BOUNDARY_Y1 = 1;
	static final int TEMPLATE_BOUNDARY_X2 = 8;
	static final int TEMPLATE_BOUNDARY_Y2 = 7;
	
	static final int ELEMENT_INFO_START_X = 1;
	
	SolarDataParser(){
		ep = new ExcelParser();
	}
	
	public Vector<String> parseTemplateRatioData(File f){
		Vector<String> ratios = new Vector<String>();
		
		ep.parseData(f,0);	
		resultsData = ep.getCellDataTable();
		//System.out.println(resultsData.get(new IntPair(0,0)).getData());

		for (int x = TEMPLATE_BOUNDARY_X1; x < TEMPLATE_BOUNDARY_X2; x++){
			for (int y = TEMPLATE_BOUNDARY_Y1; y < TEMPLATE_BOUNDARY_Y2; y++){
				System.out.print(resultsData.get(new IntPair(x,y)).getData() + " ");
				ratios.add(resultsData.get(new IntPair(x,y)).getData());
			}
			
			System.out.println();
		}
		
		
		resultsData.clear(); // empty out data
		return ratios;	
	}
	
	public Vector<ElementEntry> parseTemplateElementData(File f){
		int endX;
		String atomicSymbol, salt, concentration;
		
		Vector<ElementEntry> elements;
		elements = new Vector<ElementEntry>();
		
		ep.parseData(f, 1);
		resultsData = ep.getCellDataTable();
		
		endX = resultsData.size() / 3; // divided by 3 columns so we know how many rows to check.
									   // template files can have a variable number of elements involved.
		
		for (int x = ELEMENT_INFO_START_X; x < endX; x++){
			
			atomicSymbol = resultsData.get(new IntPair(x,0)).getData();
			salt = resultsData.get(new IntPair(x,1)).getData();
			concentration = resultsData.get(new IntPair(x,2)).getData();

			if(atomicSymbol.length() > 2){
				System.out.println("Atomic symbol is listed as: " + atomicSymbol + ", please input the correct atomic symbol.");
				atomicSymbol = System.console().readLine().toLowerCase();
				System.out.println("Now enter the correct chemical formula for " + salt);
				salt = System.console().readLine();
			}
			
			elements.add(new ElementEntry(atomicSymbol,salt,concentration));

			}
		
		
		for (int i = 0; i < elements.size(); i++)
			System.out.println(elements.get(i).toString());
		
		resultsData.clear(); // empty out data
		return elements;
	}
	
	public Vector<Double> parseResultsData(File f, int x1, int y1, int x2, int y2){
		String output;
		
		Vector<Double> readings = new Vector<Double>();
		
		ep.parseData(f, 0);
		resultsData = ep.getCellDataTable();
		
		for(int i = x1; i < x2; i++){
			for (int j = y1; j < y2; j++){
				output = resultsData.get(new IntPair(i,j)).getData();
				try{
				readings.add(Double.parseDouble(output));
				}
				catch(NumberFormatException e){
					readings.add(-1.00); // bad input
				}
				if(output.length() > 7)
					System.out.print(output.substring(0, 7) + " ");
				else
				{
					int padding = 7 - output.length();
					
					for (int g = 0; g < padding; g++)
						output = output + "0"; // pad with 0's for formatting
					
					System.out.print(output + " ");
				}
			}
			System.out.println();
		}
		
		resultsData.clear();
		
		return readings;
	}
}
