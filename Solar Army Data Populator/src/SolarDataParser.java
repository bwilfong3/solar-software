import java.io.File;
import java.util.Vector;

public class SolarDataParser {
	
	ExcelParser ep;
	Vector<ExcelCellData> resultsData;
	
	SolarDataParser(){
		ep = new ExcelParser();
	}
	
	public void parseTemplateElementData(File f){
		ep.parseData(f, 2);	
		resultsData = ep.getCellDataVector();
	}
	
	public void parseTemplateRatioData(File f){
		ep.parseData(f, 1);
		resultsData = ep.getCellDataVector();
	}
	
	public void parseResultsData(File f){
		ep.parseData(f, 1);
		resultsData = ep.getCellDataVector();
	}
}
