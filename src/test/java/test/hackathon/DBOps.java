package test.hackathon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class DBOps {

	static Connection connection; 

	public static void main(String[] args) throws Exception{

		
		System.out.println(getSavedRecipeIds());

//		Statement statement = getConn().createStatement();
//
//		ResultSet resultSet = statement.executeQuery("SELECT * FROM LFV_Elimination");
//
//		while (resultSet.next())
//		{
//			System.out.println("Recipe_Name > "+resultSet.getString("Recipe_Name"));
//		}
//
//		insertRecipe(new RecipeVo());

		connection.close();
	}

	public static List<Integer> getSavedRecipeIds() 
	{

		List<Integer> savedIds=new ArrayList<Integer>();
		try {


			Statement statement = getConn().createStatement();

			ResultSet resultSet = statement.executeQuery("SELECT \"Recipe_ID\" FROM LFV_Elimination");

			while (resultSet.next())
			{
				savedIds.add(resultSet.getInt("Recipe_ID"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return savedIds;

	}


	public static void insertRecipe(RecipeVo recipeVo) {


		try {
			PreparedStatement ps = null;
			String sql = "INSERT INTO LFV_Elimination("
					+ "	\"Recipe_ID\", \"Recipe_Name\", \"Recipe_Category\", \"Food_Category\", \"Ingredients\", \"Preparation_Time\", \"Cooking_Time\", \"Tag\", \"No_of_servings\", "
					+ "\"Cuisine_category\", \"Recipe_Description\", \"Preparation_method\", \"Nutrient_values\", \"Recipe_URL\")\n"
					+ "	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = getConn().prepareStatement(sql);

			int ctr=1;

			ps.setInt(ctr++, Integer.parseInt(recipeVo.getRecipe_ID().replaceAll("Recipe#", "").replaceAll("Recipe #", "").trim()));
			ps.setString(ctr++, recipeVo.getRecipe_Name());
			ps.setString(ctr++, StringUtils.join( recipeVo.getRecipe_Category()));
			ps.setString(ctr++, recipeVo.getFood_Category());
			ps.setString(ctr++, StringUtils.join( recipeVo.getIngredients()));


			ps.setString(ctr++, recipeVo.getPreparation_Time());
			ps.setString(ctr++, recipeVo.getCooking_Time());
			ps.setString(ctr++, StringUtils.join(recipeVo.getTags()));
			ps.setString(ctr++, recipeVo.getNo_of_servings());
			ps.setString(ctr++, recipeVo.getCuisine_category());

			ps.setString(ctr++, recipeVo.getRecipe_Description());
			ps.setString(ctr++, StringUtils.join(recipeVo.getPreparation_method()));
			ps.setString(ctr++, StringUtils.join(recipeVo.getNutrient_values()));
			ps.setString(ctr++, recipeVo.getRecipe_URL());

			int count= ps.executeUpdate();
			System.out.println("Inserted : "+count);
		} catch (Exception e) {
			System.out.println("Error while inserting or duplicate !");
		}


	}


	private static Connection getConn() throws Exception {

		if(connection==null)
		{
			String jdbcUrl = "jdbc:postgresql://localhost:5433/team4_hackathon";
			String username = "postgres";
			String password = "";

			Class.forName("org.postgresql.Driver");

			connection = DriverManager.getConnection(jdbcUrl, username, password);
		}

		return connection;
	}


}
