package entities;

import models.TexturedModel;
import org.lwjglx.util.Display;
import org.lwjglx.util.vector.Vector3f;
import renderEngine.DisplayManager;

import java.util.Arrays;
import java.util.Collections;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {

    public static  final int RUN_SPEED=20;
    public static final int TURN_SPEED=160;
    public static   final float GRAVITY=-50;
    public static   final float JUMP_POWER=30;
    public static final float TERRAIN_HEIGHT=0;
    public  static float upwardSpeed=0;

    private float currentSpeed=0.0f;
    private float currentTurnSpeed=0.0f;

    private boolean inAir=false;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(){

        checkInput();
        super.increaseRotation(0,currentTurnSpeed*DisplayManager.getFrameTimeSeconds(),0);
        float distance=currentSpeed*DisplayManager.getFrameTimeSeconds();
        float dx= (float) (distance *Math.sin(Math.toRadians(super.getRotY())));
        float dz= (float) (distance *Math.cos(Math.toRadians(super.getRotY())));
//        System.out.println(dx+" "+dz+" "+distance+" yo "+DisplayManager.getFrameTimeSeconds());
        upwardSpeed+=GRAVITY*DisplayManager.getFrameTimeSeconds();
        super.increasePosition(dx,upwardSpeed*DisplayManager.getFrameTimeSeconds(),dz);
        if(super.getPosition().y<TERRAIN_HEIGHT){
            upwardSpeed=0;
            inAir=false;
            super.getPosition().y=TERRAIN_HEIGHT;
        }
        }

        private void jump(){
        if(!inAir)
        {upwardSpeed=JUMP_POWER;
            inAir=true;}
        }
    private void checkInput(){
        if(glfwGetKey(DisplayManager.window,GLFW_KEY_W)==1)
            this.currentSpeed=RUN_SPEED;

        else if(glfwGetKey(DisplayManager.window,GLFW_KEY_S)==1)
            this.currentSpeed=-RUN_SPEED;

        else
            this.currentSpeed=0;

        if(glfwGetKey(DisplayManager.window,GLFW_KEY_A)==1)
            this.currentTurnSpeed=TURN_SPEED;
        else if(glfwGetKey(DisplayManager.window,GLFW_KEY_D)==1)
            this.currentTurnSpeed=-TURN_SPEED;
        else
            this.currentTurnSpeed=0;

        if(glfwGetKey(DisplayManager.window,GLFW_KEY_SPACE)==1)
            jump();
    }
}
