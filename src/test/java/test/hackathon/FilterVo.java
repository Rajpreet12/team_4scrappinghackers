package test.hackathon;

import java.util.Arrays;
import java.util.List;

public class FilterVo {
	
	public static List<String> CuisineCategory = Arrays.asList("South Indian","Rajathani","Punjabi","Bengali","orissa","Gujarati","Maharashtrian","Andhra","Kerala","Goan","Kashmiri","Himachali","Tamil nadu","Karnataka","Sindhi","Chhattisgarhi","Madhya pradesh","Assamese","Manipuri","Tripuri","Sikkimese","Mizo","Arunachali","uttarakhand","Haryanvi","Awadhi","Bihari","Uttar pradesh","Delhi","North Indian","Indian");

	//microwave meal, "microwave meals... 
	public static List<String> LFV_Avoid = Arrays.asList("cracker","fried food","chips","microwave meal","ready meal");

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
