package com.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.recipe.vos.FilterVo;

public class ExcelReader {

	public static FilterVo read(String path,String sheetName) {

		FileInputStream fis;
		XSSFWorkbook workBook;
		XSSFSheet sheet;

		List<String> lstEliminate=new ArrayList<String>();
		List<String> lstAdd=new ArrayList<String>();
		List<String> recipeToAvoid=new ArrayList<String>();
		//List<String> optinalRecipes=new ArrayList<String>();

		try {


			fis = new FileInputStream(path);

			workBook = new XSSFWorkbook(fis);

			sheet = workBook.getSheet(sheetName);

			int rowCount=sheet.getPhysicalNumberOfRows();

			for(int rowNum=2;rowNum<rowCount;rowNum++)
			{
				if(!getCellData(sheet.getRow(rowNum).getCell(0)).toLowerCase().trim().isEmpty())
					lstEliminate.add(getCellData(sheet.getRow(rowNum).getCell(0)).toLowerCase().trim());
				
				if(!getCellData(sheet.getRow(rowNum).getCell(1)).toLowerCase().trim().isEmpty())
					lstAdd.add(getCellData(sheet.getRow(rowNum).getCell(1)).toLowerCase().trim());
				
				if(!getCellData(sheet.getRow(rowNum).getCell(3)).toLowerCase().trim().isEmpty())
					recipeToAvoid.add(getCellData(sheet.getRow(rowNum).getCell(3)).toLowerCase().trim());
				
			//	if(!getCellData(sheet.getRow(rowNum).getCell(4)).toLowerCase().trim().isEmpty())
			//		optinalRecipes.add(getCellData(sheet.getRow(rowNum).getCell(4)).toLowerCase().trim());

			}

			try {
				workBook.close();
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		FilterVo out=new FilterVo(lstEliminate,lstAdd);
		//out.setOptinalRecipes(optinalRecipes);
		out.setRecipeToAvoid(recipeToAvoid);
		
		return out ;
	}


	public static String getCellData(XSSFCell cell) throws IOException {

		CellType type=cell.getCellType();

		switch(type)
		{
		case FORMULA:
			return cell .getStringCellValue();
		case NUMERIC:
			try {
				return String.valueOf((int)cell .getNumericCellValue());
			} catch (Exception e) {
				return "";
			} 
		case BOOLEAN:
			try {
				return String.valueOf(cell .getBooleanCellValue());
			} catch (Exception e) {
				return "";
			} 
		case STRING:
			return cell .getStringCellValue();
		case BLANK:
			return "";
		}
		return cell .getStringCellValue();

	}



}
