//package com.utilities;
//import java.io.File;
//import java.io.FileInputStream;
//	import java.io.FileOutputStream;
//	import java.io.IOException;
//	import java.util.List;
//	import java.util.Map;
//	import org.apache.poi.xssf.usermodel.XSSFCell;
//	import com.recipe.scraping.model.RecipeDetail;
//	import org.apache.poi.ss.usermodel.*;
//	import org.apache.poi.xssf.usermodel.XSSFRow;
//	import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//	import org.slf4j.Logger;
//	import org.slf4j.LoggerFactory;
//	import com.recipe.scraping.model.RecipeInformation;
//	import java.util.ArrayList;
//	import java.util.Iterator;
//	import java.util.List;
//
//	public class Write_Excel {
//
//	    private static  final String excelFilePath = ReadConfig.loadConfig().getProperty("outputData");
//
//	    public static List<List<String>> readInputConditions(String excelFilePath, String sheetName) throws IOException {
//	        Workbook workbook = WorkbookFactory.create(new File(excelFilePath));
//	        Sheet sheet = workbook.getSheet(sheetName);
//	        workbook.close();
//	        Iterator<Row> rowIterator = sheet.iterator();
//
//	        List<String> eliminatorsList = new ArrayList<>();
//	        List<String> toAddList = new ArrayList<>();
//
//	        while (rowIterator.hasNext()) {
//	            Row currRow = rowIterator.next();
//
//	            // First two rows are headers, hence skip them
//	            if (currRow.getRowNum() == 0 || currRow.getRowNum() == 1) {
//	                continue;
//	            }
//
//	            // Get eliminator
//	            Cell eliminatorCell = currRow.getCell(0);
//	            // Get toAdd
//	            Cell toAddCell = currRow.getCell(1);
//
//	            if(eliminatorCell != null) {
//	                String value = eliminatorCell.getStringCellValue().trim();
//	                if(!value.isEmpty()) {
//	                    eliminatorsList.add(value);
//	                }
//	            }
//
//	            if(toAddCell != null) {
//	                String value = toAddCell.getStringCellValue().trim();
//	                if(!value.trim().isEmpty()) {
//	                    toAddList.add(value);
//	                }
//	            }
//	        }
//
//	        // Create return list
//	        List<List<String>> result = new ArrayList<List<String>>();
//	        result.add(eliminatorsList);
//	        result.add(toAddList);
//	        return result;
//	    }
//
//	    public static void writeInPcosSheet( List<RecipeDetail> outputData) throws IOException{
//	        // Create a Workbook
//	        Workbook workbook;
//	        File file = new File(excelFilePath);
//
//	        if (file.exists()) {
//	            // If the file already exists, read it
//	            FileInputStream fis = new FileInputStream(file);
//	            workbook = new XSSFWorkbook(fis);
//	        } else {
//	            // If the file doesn't exist, create a new Workbook
//
//	            workbook = new XSSFWorkbook();
//	        }
//
//	        // Check if the sheet already exists
//	        Sheet sheet = workbook.getSheet("PCOS");
//
//	        if (sheet == null) {
//	            // If the sheet doesn't exist, create a new one
//	            sheet = workbook.createSheet("PCOS");
//
//	            // add header record, if new sheet
//	            int cellNum = 0;
//	            Row row = sheet.createRow(0);
//	            row.createCell(cellNum++).setCellValue("Recipe ID");
//	            row.createCell(cellNum++).setCellValue("Recipe Name");
//	            row.createCell(cellNum++).setCellValue("Recipe Category(Breakfast/lunch/snack/dinner)");
//	            row.createCell(cellNum++).setCellValue("Food Category(Veg/non-veg/vegan/Jain)");
//	            row.createCell(cellNum++).setCellValue("Ingredients");
//	            row.createCell(cellNum++).setCellValue("Preparation Time");
//	            row.createCell(cellNum++).setCellValue("Cooking Time");
//	            row.createCell(cellNum++).setCellValue("Preparation method");
//	            row.createCell(cellNum++).setCellValue("Nutrient values");
//	            row.createCell(cellNum++).setCellValue("Targetted morbid conditions (Diabeties/Hypertension/Hypothyroidism)");
//	            row.createCell(cellNum++).setCellValue("Recipe URL");
//	        }
//
//	        // Get the last row number
//	        int lastRowNum = sheet.getLastRowNum();
//
//	        // Create a CellStyle for formatting
//	        CellStyle cellStyle = workbook.createCellStyle();
//	        cellStyle.setAlignment(HorizontalAlignment.LEFT);
//
//	        // Iterate through the RecipeData list and write to the Excel sheet
//	        for (RecipeDetail recipeDetail : outputData) {
//	            Row row = sheet.createRow(++lastRowNum);
//	            int cellNum = 0;
//	            // Iterate through the RecipeData fields using getter methods
//	            row.createCell(cellNum++).setCellValue(recipeDetail.getRecipeId());
//	            row.createCell(cellNum++).setCellValue(recipeDetail.getRecipeTitle());
//	            row.createCell(cellNum++).setCellValue(recipeDetail.getRecipeCategory());
//	            row.createCell(cellNum++).setCellValue(recipeDetail.getFoodCategory());
//	            row.createCell(cellNum++).setCellValue(recipeDetail.getNameOfIngredients());
//	            row.createCell(cellNum++).setCellValue(recipeDetail.getPreparationTime());
//	            row.createCell(cellNum++).setCellValue(recipeDetail.getCookTime());
//	            row.createCell(cellNum++).setCellValue(recipeDetail.getPrepMethod());
//	            row.createCell(cellNum++).setCellValue(recipeDetail.getNutrients());
//	            row.createCell(cellNum++).setCellValue("PCOS");
//	            row.createCell(cellNum++).setCellValue(recipeDetail.getRecipeUrl());
//	        }
//
//	        // Write the workbook to the file
//	        try (FileOutputStream fos = new FileOutputStream(file)) {
//	            workbook.write(fos);
//	        }
//
//	        // Close the workbook
//	        workbook.close();
//	    }
//
//	    public void writeInAllergiesSheet(Map<String, List<RecipeInformation>> recipeInformationMapByAllergy, List<String> allergies, String allergiesOutputDataFileName) {
//	    }
//	}
//
//
//}
