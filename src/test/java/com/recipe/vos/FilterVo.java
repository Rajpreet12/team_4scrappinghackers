package com.recipe.vos;

import java.util.Arrays;
import java.util.List;

public class FilterVo {

	public static List<String> CuisineCategory = Arrays.asList("South Indian","Rajathani","Punjabi","Bengali","orissa",
			"Gujarati","Maharashtrian","Andhra","Kerala","Goan","Kashmiri","Himachali","Tamil nadu","Karnataka","Sindhi",
			"Chhattisgarhi","Madhya pradesh","Assamese","Manipuri","Tripuri","Sikkimese","Mizo","Arunachali","uttarakhand",
			"Haryanvi","Awadhi","Bihari","Uttar pradesh","Delhi","North Indian","Indian");

	List<String> recipeToAvoid;
	List<String> optinalRecipes;

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
	List<String> lstEliminate;
	List<String> lstAdd;
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

}
