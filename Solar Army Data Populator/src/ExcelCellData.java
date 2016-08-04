// This class holds the information obtained from an excel spreadsheet

public class ExcelCellData {
	private int row;
	private int column;
	private String data;
	
	ExcelCellData(int r, int c, String d){
		row = r;
		column = c;
		data = d;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public String getData(){
		return data;
	}
}
