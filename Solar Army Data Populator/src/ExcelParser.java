// This class is a reusable one that has a method to open an excel file
// and dump the results into a vector of ExcelCellData objects, which
// contain the row and column index as well as a string for the data.

import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelParser {
	
	File excelFile;
	FileInputStream fis;
	public Hashtable<IntPair, ExcelCellData> parseResults;
	
	ExcelParser(){
		parseResults = new Hashtable<IntPair, ExcelCellData>();
	}
	
	public boolean parseData(File f, int sheetNumber){
		
		boolean successfulParse = false;
		Workbook workbook;
		Sheet sheet;
		Iterator rowIterator, cellIterator;
		Row row;
		Cell cell;
		
		parseResults.clear(); // purge the vector before beginning parse again
		
		try{
			fis = new FileInputStream(f.getAbsolutePath());
			
			workbook = new HSSFWorkbook(fis);
			
			sheet = workbook.getSheetAt(sheetNumber);
			
			rowIterator = sheet.iterator();
			
			while(rowIterator.hasNext()){
				
				row = (Row)rowIterator.next();
				
				cellIterator = row.cellIterator();
				while(cellIterator.hasNext()){
					cell = (Cell)cellIterator.next();
					
					int cellType = cell.getCellType();
					
					switch(cellType){
						case Cell.CELL_TYPE_BLANK : 
							//System.out.println("Cell " + cell.getRowIndex() + "," + cell.getColumnIndex() + " Type is blank");
							parseResults.put(new IntPair(cell.getRowIndex(), cell.getColumnIndex()),
											 new ExcelCellData(cell.getRowIndex(), cell.getColumnIndex(), "blank"));
						break;
						
						case Cell.CELL_TYPE_BOOLEAN : 
							//System.out.println("Cell " + cell.getRowIndex() + "," + cell.getColumnIndex() 
							//				 + " Type is boolean: " + cell.getBooleanCellValue());
							parseResults.put(new IntPair(cell.getRowIndex(), cell.getColumnIndex()),
									 new ExcelCellData(cell.getRowIndex(), cell.getColumnIndex(), "" + cell.getBooleanCellValue()));
						break;
						
						case Cell.CELL_TYPE_ERROR : 
							//System.out.println("Cell " + cell.getRowIndex() + "," + cell.getColumnIndex() + " Type is error: " + cell.getErrorCellValue());
							parseResults.put(new IntPair(cell.getRowIndex(), cell.getColumnIndex()),
									 new ExcelCellData(cell.getRowIndex(), cell.getColumnIndex(), "Error"));		
						break;
						
						case Cell.CELL_TYPE_FORMULA : 
							//System.out.println("Cell " + cell.getRowIndex() + "," + cell.getColumnIndex() + " Type is formula: " + cell.getCellFormula());
							parseResults.put(new IntPair(cell.getRowIndex(), cell.getColumnIndex()),
									 new ExcelCellData(cell.getRowIndex(), cell.getColumnIndex(), cell.getCellFormula()));
						break;
						
						case Cell.CELL_TYPE_NUMERIC : 
							//System.out.println("Cell " + cell.getRowIndex() + "," + cell.getColumnIndex() + " Type is numeric: " + cell.getNumericCellValue());
							parseResults.put(new IntPair(cell.getRowIndex(), cell.getColumnIndex()),
									 new ExcelCellData(cell.getRowIndex(), cell.getColumnIndex(), cell.getNumericCellValue() + ""));						
						break;
						
						case Cell.CELL_TYPE_STRING : 
							//System.out.println("Cell " + cell.getRowIndex() + "," + cell.getColumnIndex() + " Type is string: " + cell.getStringCellValue());
							parseResults.put(new IntPair(cell.getRowIndex(), cell.getColumnIndex()),
									 new ExcelCellData(cell.getRowIndex(), cell.getColumnIndex(), cell.getStringCellValue()));
						break;
					}
				}	
			}			
			successfulParse = true;
		}
		catch(Exception e){
			e.printStackTrace();
			successfulParse = false;
		}
		
		return successfulParse;
	}
	
	Hashtable<IntPair, ExcelCellData> getCellDataTable(){
		return parseResults;
	}
}
