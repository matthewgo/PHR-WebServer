package phr.models;

public class Food {

	Integer entryID;
	String name;
	double calorie;
	double protein;
	double fat;
	double carbohydrate;
	String serving;
	Integer restaurantID;
	Boolean fromFatsecret;
	int countUsed;

	public Food(Integer entryID) {
		super();
		this.entryID = entryID;
	}
	
	public Food(Integer entryID, String name, double calorie, double protein,
			double fat, double carbohydrate, String serving,
            Integer restaurantID, Boolean fromFatsecret, int countUsed) {
		super();
		this.entryID = entryID;
		this.name = name;
		this.calorie = calorie;
		this.protein = protein;
		this.fat = fat;
		this.carbohydrate = carbohydrate;
		this.serving = serving;
		this.restaurantID = restaurantID;
		this.fromFatsecret = fromFatsecret;
		this.countUsed = countUsed;
	}

	public Food(String name, double calorie, double protein, double fat,
			double carbohydrate, String serving,
			Integer restaurantID, Boolean fromFatsecret, int countUsed) {
		super();
		this.name = name;
		this.calorie = calorie;
		this.protein = protein;
		this.fat = fat;
		this.carbohydrate = carbohydrate;
		this.serving = serving;
		this.restaurantID = restaurantID;
		this.fromFatsecret = fromFatsecret;
		this.countUsed = countUsed;
	}

	public Integer getEntryID() {
		return entryID;
	}

	public void setEntryID(Integer entryID) {
		this.entryID = entryID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCalorie() {
		return calorie;
	}

	public void setCalorie(double calorie) {
		this.calorie = calorie;
	}

	public double getProtein() {
		return protein;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public double getFat() {
		return fat;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public double getCarbohydrate() {
		return carbohydrate;
	}

	public void setCarbohydrate(double carbohydrate) {
		this.carbohydrate = carbohydrate;
	}

	public String getServing() {
		return serving;
	}

	public void setServing(String serving) {
		this.serving = serving;
	}

	public Integer getRestaurantID() {
		return restaurantID;
	}

	public void setRestaurantID(Integer restaurantID) {
		this.restaurantID = restaurantID;
	}

	public Boolean getFromFatsecret() {
		return fromFatsecret;
	}

	public void setFromFatsecret(Boolean fromFatsecret) {
		this.fromFatsecret = fromFatsecret;
	}

	public int getCountUsed() {
		return countUsed;
	}

	public void setCountUsed(int countUsed) {
		this.countUsed = countUsed;
	}
	
}
