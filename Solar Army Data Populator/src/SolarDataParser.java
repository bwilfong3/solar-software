import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

public class SolarDataParser {
	
	ExcelParser ep;
	Hashtable<IntPair, ExcelCellData> resultsData;
	
	static final int TEMPLATE_BOUNDARY_X1 = 2;
	static final int TEMPLATE_BOUNDARY_Y1 = 1;
	static final int TEMPLATE_BOUNDARY_X2 = 8;
	static final int TEMPLATE_BOUNDARY_Y2 = 7;
	
	SolarDataParser(){
		ep = new ExcelParser();
	}
	
	public void parseTemplateElementData(File f){
		ep.parseData(f,0);	
		resultsData = ep.getCellDataTable();
		//System.out.println(resultsData.get(new IntPair(0,0)).getData());
		
		for (int x = TEMPLATE_BOUNDARY_X1; x < TEMPLATE_BOUNDARY_X2; x++){
			for (int y = TEMPLATE_BOUNDARY_Y1; y < TEMPLATE_BOUNDARY_Y2; y++)
				System.out.print(resultsData.get(new IntPair(x,y)).getData() + " ");
			
			System.out.println();
		}
			
	}
	
	public void parseTemplateRatioData(File f){
		ep.parseData(f, 1);
		resultsData = ep.getCellDataTable();
	}
	
	public void parseResultsData(File f, int x1, int y1, int x2, int y2){
		ep.parseData(f, 1);
		resultsData = ep.getCellDataTable();
	}
}
