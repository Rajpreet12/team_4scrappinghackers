package com.recipe.vos;

import java.util.Arrays;
import java.util.List;

public class FilterVo {

	
	//LFV, LCHFE..
	String filterName;
	
	public String getFilterName() {
		return filterName;
	}
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	public static List<String> getCuisineCategory() {
		return CuisineCategory;
	}
	public static void setCuisineCategory(List<String> cuisineCategory) {
		CuisineCategory = cuisineCategory;
	}
	public static List<String> CuisineCategory = Arrays.asList("South Indian","Rajathani","Punjabi","Bengali","orissa",
			"Gujarati","Maharashtrian","Andhra","Kerala","Goan","Kashmiri","Himachali","Tamil nadu","Karnataka","Sindhi",
			"Chhattisgarhi","Madhya pradesh","Assamese","Manipuri","Tripuri","Sikkimese","Mizo","Arunachali","uttarakhand",
			"Haryanvi","Awadhi","Bihari","Uttar pradesh","Delhi","North Indian","Indian");
	
	
	List<String> To_Add_If_notFullyVgean;
	List<String> recipeToAvoid;
	List<String> optinalRecipes;
	List<String> lstEliminate;
	List<String> lstAdd;
	//List<String> ketorecipeToAvoid;
		List<String> ketofoodprocessing;
	
	public List<String> getTo_Add_If_notFullyVgean() {
		return To_Add_If_notFullyVgean;
	}
	public void setTo_Add_If_notFullyVgean(List<String> to_Add_If_notFullyVgean) {
		To_Add_If_notFullyVgean = to_Add_If_notFullyVgean;
	}
	

	public List<String> getRecipeToAvoid() {
		return recipeToAvoid;
	}
	public void setRecipeToAvoid(List<String> recipeToAvoid) {
		this.recipeToAvoid = recipeToAvoid;
	}
	public List<String> getOptinalRecipes() {
		return optinalRecipes;
	}
	public void setOptinalRecipes(List<String> optinalRecipes) {
		this.optinalRecipes = optinalRecipes;
	}
	
	public List<String> getLstEliminate() {
		return lstEliminate;
	}
	public void setLstEliminate(List<String> lstEliminate) {
		this.lstEliminate = lstEliminate;
	}
	public FilterVo(List<String> lstEliminate, List<String> lstAdd) {
		super();
		this.lstEliminate = lstEliminate;
		this.lstAdd = lstAdd;
	}
	public List<String> getLstAdd() {
		return lstAdd;
	}
	public void setLstAdd(List<String> lstAdd) {
		this.lstAdd = lstAdd;
	}

/*
 * public List<String> getketorecipeToAvoid() { return ketorecipeToAvoid; }
 * public void setketorecipeToAvoid(List<String> ketorecipeToAvoid) {
 * this.ketorecipeToAvoid = ketorecipeToAvoid; }
 */

public List<String> getfoodprocessing() {
	return ketofoodprocessing;
}


public void setketofoodprocessing(List<String> foodprocessing) {
	this.ketofoodprocessing = foodprocessing;
	
}

}

