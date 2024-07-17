package com.LFV;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterVo {

    private static final Logger logger = LoggerFactory.getLogger(FilterVo.class);

    String filterName;
    public static List<String> CuisineCategory = Arrays.asList(
            "South Indian", "Rajathani", "Punjabi", "Bengali", "orissa",
            "Gujarati", "Maharashtrian", "Andhra", "Kerala", "Goan", "Kashmiri",
            "Himachali", "Tamil nadu", "Karnataka", "Sindhi", "Chhattisgarhi",
            "Madhya pradesh", "Assamese", "Manipuri", "Tripuri", "Sikkimese",
            "Mizo", "Arunachali", "uttarakhand", "Haryanvi", "Awadhi", "Bihari",
            "Uttar pradesh", "Delhi", "North Indian", "Indian");

    List<String> To_Add_If_notFullyVgean;
    List<String> recipeToAvoid;
    List<String> optinalRecipes;
    List<String> lstEliminate;
    List<String> lstAdd;

    public FilterVo(List<String> lstEliminate, List<String> lstAdd) {
        logger.debug("Initializing FilterVo with lstEliminate: {} and lstAdd: {}", lstEliminate, lstAdd);
        this.lstEliminate = lstEliminate;
        this.lstAdd = lstAdd;
    }

    public String getFilterName() {
        logger.debug("Getting filterName: {}", filterName);
        return filterName;
    }

    public void setFilterName(String filterName) {
        logger.debug("Setting filterName: {}", filterName);
        this.filterName = filterName;
    }

    public static List<String> getCuisineCategory() {
        logger.debug("Getting CuisineCategory: {}", CuisineCategory);
        return CuisineCategory;
    }

    public static void setCuisineCategory(List<String> cuisineCategory) {
        logger.debug("Setting CuisineCategory: {}", cuisineCategory);
        CuisineCategory = cuisineCategory;
    }

    public List<String> getTo_Add_If_notFullyVgean() {
        logger.debug("Getting To_Add_If_notFullyVgean: {}", To_Add_If_notFullyVgean);
        return To_Add_If_notFullyVgean;
    }

    public void setTo_Add_If_notFullyVgean(List<String> to_Add_If_notFullyVgean) {
        logger.debug("Setting To_Add_If_notFullyVgean: {}", to_Add_If_notFullyVgean);
        To_Add_If_notFullyVgean = to_Add_If_notFullyVgean;
    }

    public List<String> getRecipeToAvoid() {
        logger.debug("Getting recipeToAvoid: {}", recipeToAvoid);
        return recipeToAvoid;
    }

    public void setRecipeToAvoid(List<String> recipeToAvoid) {
        logger.debug("Setting recipeToAvoid: {}", recipeToAvoid);
        this.recipeToAvoid = recipeToAvoid;
    }

    public List<String> getOptinalRecipes() {
        logger.debug("Getting optinalRecipes: {}", optinalRecipes);
        return optinalRecipes;
    }

    public void setOptinalRecipes(List<String> optinalRecipes) {
        logger.debug("Setting optinalRecipes: {}", optinalRecipes);
        this.optinalRecipes = optinalRecipes;
    }

    public List<String> getLstEliminate() {
        logger.debug("Getting lstEliminate: {}", lstEliminate);
        return lstEliminate;
    }

    public void setLstEliminate(List<String> lstEliminate) {
        logger.debug("Setting lstEliminate: {}", lstEliminate);
        this.lstEliminate = lstEliminate;
    }

    public List<String> getLstAdd() {
        logger.debug("Getting lstAdd: {}", lstAdd);
        return lstAdd;
    }

    public void setLstAdd(List<String> lstAdd) {
        logger.debug("Setting lstAdd: {}", lstAdd);
        this.lstAdd = lstAdd;
    }
}
