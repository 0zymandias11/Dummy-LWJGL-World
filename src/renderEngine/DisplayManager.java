package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;
import org.lwjglx.Sys;
import org.lwjglx.util.vector.Vector3f;
import shaders.StaticShader;
import terrains.Terrain;
import terrains.TerrainTexture;
import textures.ModelTexture;
import textures.TerrainTexturePack;

import java.io.IOException;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class DisplayManager {

    public static int height,width=0;
    public static    long window;
    public static  StaticShader shader=null;
    public static long lastFrameTime;
    public static   float delta;
    public static double MouseDX,prevDX;
    public static double MouseDY,prevDY;

    private GLFWCursorPosCallback cursorPos;

//    public  float[] vertices = {
//            -0.5f,0.5f,-0.5f,
//            -0.5f,-0.5f,-0.5f,
//            0.5f,-0.5f,-0.5f,
//            0.5f,0.5f,-0.5f,
//
//            -0.5f,0.5f,0.5f,
//            -0.5f,-0.5f,0.5f,
//            0.5f,-0.5f,0.5f,
//            0.5f,0.5f,0.5f,
//
//            0.5f,0.5f,-0.5f,
//            0.5f,-0.5f,-0.5f,
//            0.5f,-0.5f,0.5f,
//            0.5f,0.5f,0.5f,
//
//            -0.5f,0.5f,-0.5f,
//            -0.5f,-0.5f,-0.5f,
//            -0.5f,-0.5f,0.5f,
//            -0.5f,0.5f,0.5f,
//
//            -0.5f,0.5f,0.5f,
//            -0.5f,0.5f,-0.5f,
//            0.5f,0.5f,-0.5f,
//            0.5f,0.5f,0.5f,
//
//            -0.5f,-0.5f,0.5f,
//            -0.5f,-0.5f,-0.5f,
//            0.5f,-0.5f,-0.5f,
//            0.5f,-0.5f,0.5f
//
//    };
//    public int indices[]={
//            0,1,3,
//            3,1,2,
//            4,5,7,
//            7,5,6,
//            8,9,11,
//            11,9,10,
//            12,13,15,
//            15,13,14,
//            16,17,19,
//            19,17,18,
//            20,21,23,
//            23,21,22
//    };
//
//    float textureCoords[]={
//            0,0,
//            0,1,
//            1,1,
//            1,0,
//            0,0,
//            0,1,
//            1,1,
//            1,0,
//            0,0,
//            0,1,
//            1,1,
//            1,0,
//            0,0,
//            0,1,
//            1,1,
//            1,0,
//            0,0,
//            0,1,
//            1,1,
//            1,0,
//            0,0,
//            0,1,
//            1,1,
//            1,0
//
//    };

    public static long getCurrentTime(){
        return System.currentTimeMillis();
    }


    public static float getFrameTimeSeconds(){
//        System.out.println(delta);
        return delta;
    }

    public  void createDisplay() throws IOException {
        init();

        loop();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    private  void init()
    {
        GLFWErrorCallback.createPrint(System.err).set();
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(1368, 720, "dummy Window", NULL, NULL);
        if(window==NULL)
            throw new  RuntimeException(("Window not created"));

        glfwSetCursorPosCallback(window,cursorPos=new Camera());
//        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
//            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
//                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
//        });

        try( MemoryStack stack= stackPush())
        {
            IntBuffer pwidth=stack.mallocInt(1);
            IntBuffer pheight=stack.mallocInt(1);
            glfwGetWindowSize(window, pwidth, pheight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidmode != null;
            height=vidmode.height();
            width=vidmode.width();
            glfwSetWindowPos(window, (vidmode.width()-pwidth.get(0))/2, (vidmode.height()-pheight.get(0))/2);

        }
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

    }



    public  void  loop() throws IOException {
        GL.createCapabilities();
//        Loader loader=new Loader();
//        Loader loader2=new Loader();
//        RawModel model=OBJLoader.loadObjectModel("dragon",loader);
//
//
//        shader=new StaticShader();
//
//        //        EntityRenderer renderer2 =new EntityRenderer(shader);
//
//        ModelTexture texture=new ModelTexture(Loader.loadTexture("crate_tex"));
//
//
//        Terrain terrain=new Terrain(0,0,loader,new ModelTexture(Loader.loadTexture("grass")));
//        Terrain terrain2=new Terrain(1,0,loader2,new ModelTexture(Loader.loadTexture("grass")));
//
//
//        shader.loadShineVariable(texture.getShineDamper(),texture.getReflectivity());
//        TexturedModel texturedModel=new TexturedModel(model,texture);
//        ModelTexture newTexture=texturedModel.getModelTexture();
//
//        newTexture.setShineDamper(10);
//        newTexture.setReflectivity(2);
        Loader loader = new Loader();


        RawModel model = OBJLoader.loadObjModel("tree", loader);

        TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));

        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),
                                                new ModelTexture(loader.loadTexture("grassTexture")));

        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern",loader),
                                                new ModelTexture(loader.loadTexture("fern")));

        grass.getTexture().setHasTransperancy(true);
        grass.getTexture().setUseFakeLighting(true);
        fern.getTexture().setHasTransperancy(true);
        TerrainTexture backgroundTexture=new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture=new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture=new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture=new TerrainTexture(loader.loadTexture("path"));

        //PLAYER MODEL

        TexturedModel stanfordBunny=new TexturedModel(OBJLoader.loadObjModel("bunny",loader),
                                                        new ModelTexture(loader.loadTexture("white")));

        Player player=new Player(stanfordBunny,new Vector3f(0,0,-10),0,0,0,0.35f);


        TerrainTexturePack terrainTexturePack=new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
        TerrainTexture blendMap=new TerrainTexture(loader.loadTexture("blendMap"));


        //        fern.getTexture().setUseFakeLighting(true);
        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i=0;i<100;i++){
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
            entities.add(new Entity(grass, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
            entities.add(new Entity(fern, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
        }



        Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));

        Terrain terrain = new Terrain(-1,0,loader,terrainTexturePack,blendMap,"heightmap");
        Terrain terrain2 = new Terrain(0,0,loader,terrainTexturePack,blendMap,"heightmap");

        Camera camera = new Camera(player);
        MasterRenderer renderer = new MasterRenderer();
//        GLFW.glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        lastFrameTime=getCurrentTime();

        //Processing Terrain
        renderer.processTerrain(terrain);
        renderer.processTerrain(terrain2);
        renderer.processEntity(player);

        //Processing Entities
        for(Entity entity:entities){
            renderer.processEntity(entity);
        }
        while ( !glfwWindowShouldClose(window) ) {
            GL11.glClearColor(1,0,0,0);
//            shader.start();
//            shader.loadLight(light);
//            renderer2.prepare();
//            camera.move();
//            shader.loadViewMatrix(camera);
//            renderer2.render();
//            shader.stop();
            camera.move();
            player.move();
            long time=getCurrentTime();
            delta=(time-lastFrameTime)/1000f;
            lastFrameTime=time;
            renderer.render(light, camera);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        renderer.cleanUp();
        loader.cleanUp();
        GLFW.glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }
    public static void main(String[] args) throws IOException {
        DisplayManager disp=new DisplayManager();
        disp.createDisplay();
    }
}
