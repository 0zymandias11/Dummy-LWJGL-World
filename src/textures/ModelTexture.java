package textures;

public class ModelTexture {
	
	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	private boolean hasTransperancy=false;
	private boolean useFakeLighting=false;

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public ModelTexture(int texture){
		this.textureID = texture;
	}

	public void setHasTransperancy(boolean hasTransperancy) {
		this.hasTransperancy = hasTransperancy;
	}

	public boolean isHasTransperancy() {
		return hasTransperancy;
	}

	public int getID(){
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	

}
