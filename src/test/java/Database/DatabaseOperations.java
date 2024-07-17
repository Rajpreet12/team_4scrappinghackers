package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Hook.Hook;
import com.recipecategory.ReceipeCategory;

public class DatabaseOperations {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseOperations.class);
    static Connection connection;

    public static void main_(String[] args) {
    	dropAlltableContent("LFV");
		dropAlltableContent("LCHFE");
    }

    private static void dropAlltableContent(String string) {
        String filterName = "LCHFE";
        String query = "DROP TABLE IF EXISTS public.<TABLE_NAME>";

        DatabaseOperations.dropTable(query.replace("<TABLE_NAME>", "\"AlreadyCheckedRecipes_" + filterName + "\""));
        DatabaseOperations.dropTable(query.replace("<TABLE_NAME>", filterName + "_elimination"));
        try {
            DatabaseOperations.dropTable(query.replace("<TABLE_NAME>", filterName + "_to_add"));
        } catch (Exception e) {
            logger.error("Error dropping table {}_to_add: {}", filterName, e.getMessage());
        }

        for (String singleAllergyTerm : Hook.allergies) {
            DatabaseOperations.dropTable(query.replace("<TABLE_NAME>", filterName + "_Allergy_" + singleAllergyTerm.replaceAll(" ", "_")));
        }
    }

    public static void dropTable(String query) {
        try {
            Statement statement = getConn().createStatement();
            statement.execute(query);
            logger.info("Dropped table with query: {}", query);
        } catch (Exception e) {
            logger.error("Error dropping table: {}", e.getMessage());
        }
    }

    public static void createTable(String query) {
        try {
            Statement statement = getConn().createStatement();
            statement.execute(query);
            logger.info("Created table with query: {}", query);
        } catch (Exception e) {
            logger.error("Error creating table: {}", e.getMessage());
        }
    }

    // filter == LFV, LCHFE ...
    public static List<Integer> getAlreadyCheckedRecipeIds(String filter) {
        List<Integer> savedIds = new ArrayList<>();
        try {
            Statement statement = getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT \"Recipe_Id\" FROM public.\"AlreadyCheckedRecipes_" + filter + "\";");

            while (resultSet.next()) {
                savedIds.add(resultSet.getInt("Recipe_Id"));
            }

            logger.info("Got {} already checked recipe IDs for filter {}", savedIds.size(), filter);
        } catch (Exception e) {
            logger.error("Error getting already checked recipe IDs: {}", e.getMessage());
        }
        return savedIds;
    }

    public static void insertCheckedRecipeId(int recId, String filter) {
        try {
            PreparedStatement ps = null;
            String sql = "INSERT INTO public.\"AlreadyCheckedRecipes_" + filter + "\"(\"Recipe_Id\") VALUES (?);";
            ps = getConn().prepareStatement(sql);
            ps.setInt(1, recId);
            ps.executeUpdate();
            logger.info("Inserted checked recipe ID {} for filter {}", recId, filter);
        } catch (Exception e) {
            logger.error("Error inserting checked recipe ID: {}", e.getMessage());
        }
    }

    public static void insertRecipe(ReceipeCategory recipeVo, String tableName) {
        try {
            PreparedStatement ps = null;
            String sql = "INSERT INTO " + tableName + " (\"Recipe_ID\", \"Recipe_Name\", \"Recipe_Category\", \"Food_Category\", \"Ingredients\", \"Preparation_Time\", " +
                         "\"Cooking_Time\", \"Tag\", \"No_of_servings\", \"Cuisine_category\", \"Recipe_Description\", \"Preparation_method\", \"Nutrient_values\", \"Recipe_URL\") " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            ps = getConn().prepareStatement(sql);

            int recId = Integer.parseInt(recipeVo.getRecipe_ID().replaceAll("Recipe#", "").replaceAll("Recipe #", "").trim());
            int columnCounter = 1;

            ps.setInt(columnCounter++, recId);
            ps.setString(columnCounter++, recipeVo.getRecipe_Name());
            ps.setString(columnCounter++, StringUtils.join(recipeVo.getRecipe_Category()));
            ps.setString(columnCounter++, recipeVo.getFood_Category());
            ps.setString(columnCounter++, StringUtils.join(recipeVo.getIngredients()));
            ps.setString(columnCounter++, recipeVo.getPreparation_Time());
            ps.setString(columnCounter++, recipeVo.getCooking_Time());
            ps.setString(columnCounter++, StringUtils.join(recipeVo.getTags()));
            ps.setString(columnCounter++, recipeVo.getNo_of_servings());
            ps.setString(columnCounter++, recipeVo.getCuisine_category());
            ps.setString(columnCounter++, recipeVo.getRecipe_Description());
            ps.setString(columnCounter++, StringUtils.join(recipeVo.getPreparation_method()));
            ps.setString(columnCounter++, StringUtils.join(recipeVo.getNutrient_values()));
            ps.setString(columnCounter++, recipeVo.getRecipe_URL());

            ps.executeUpdate();
            logger.info("Inserted recipe {} into table {}", recipeVo.getRecipe_ID(), tableName);
        } catch (Exception e) {
            logger.error("Error inserting recipe: {}", e.getMessage());
        }
    }

    public static Connection getConn() throws Exception {
        if (connection == null) {
            String jdbcUrl = "jdbc:postgresql://localhost:5432/team4_hackathon";
            String username = "postgres";
            String password = "admin123";

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            logger.info("Database connection established");
        }
        return connection;
    }
}
