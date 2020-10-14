package entities;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjglx.util.vector.Vector3f;
import renderEngine.DisplayManager;

import static org.lwjgl.glfw.GLFW.*;

public class Camera extends GLFWCursorPosCallback {
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 10;
	private float yaw ;
	private float roll;
	private double mouseDX=0;
	private double mouseDY=0;
	private  DisplayManager displayManager=new DisplayManager();
	private float distanceFromPlayer=50;
	private  float angleAroundPlayer=0;

	public Camera() {

	}



	Player player;
	public Camera(Player player){
		this.player=player;
	}

	public void move(){

		if (glfwGetKey(DisplayManager.window, GLFW_KEY_UP)==1)
			position.y+=0.4f;
		if (glfwGetKey(DisplayManager.window, GLFW_KEY_DOWN)==1)
			position.y-=0.4f;
		calculateAngleAroundPlayer();
		calculatePitch();
		float horizontalDistance=calculateHorizontalDistance();
		float verticalDistance=calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance,verticalDistance);
    }



	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer*Math.cos(Math.toRadians(pitch)));
	}
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer*Math.sin(Math.toRadians(pitch)));
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	private void calculateCameraPosition(float horizontalDistance,float verticalDistance){
		float theta=player.getRotY()+angleAroundPlayer;
		float offsetX= (float) (horizontalDistance*Math.sin(Math.toRadians(theta)));
		float offsetZ= (float) (horizontalDistance*Math.cos(Math.toRadians(theta)));

		position.x=player.getPosition().x-offsetX;
		position.z=player.getPosition().z-offsetZ;
		position.y=player.getPosition().y+verticalDistance;
		this.yaw=180-(player.getRotY()+angleAroundPlayer);
	}

private void calculatePitch(){

		if(glfwGetMouseButton(DisplayManager.window,GLFW_MOUSE_BUTTON_1)==1){
			System.out.println("yo");
			float pitchChange= (float) (DisplayManager.MouseDY*0.001f);
			pitch+=pitchChange;}
	if(glfwGetMouseButton(DisplayManager.window,GLFW_MOUSE_BUTTON_2)==1){
		System.out.println("yo");
		float pitchChange= (float) (DisplayManager.MouseDY*0.001f);
		pitch-=pitchChange;}

}
	private void calculateAngleAroundPlayer(){
			double currX=DisplayManager.MouseDX;
			float angleChange= (float) ((DisplayManager.prevDX*0.005f)- (currX*0.005f));
			DisplayManager.prevDX=currX;
			angleAroundPlayer+=angleChange;
	}

	@Override
	public void invoke(long window, double xpos, double ypos) {
//		System.out.println("2");
//		System.out.println(xpos+" "+ypos);
		DisplayManager.MouseDX=xpos;
		DisplayManager.MouseDY=ypos;
	}
}
