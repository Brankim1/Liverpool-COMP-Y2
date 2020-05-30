package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.system.AppSettings;

/**
 * Be sure your computer have jme3-test-data library
 * please open GPU accelerate,or it could dropped frame
 * 
 * @author pengcheng jin id: 201447767
 *
 * extend A1 AIArkanoid
 * Use decision tree game AI behaviour model to control paddle. 
 * AI code in the 722-927 line and AI was called from simpleUpdate method.
 *
 */
public class AiArkanoid extends SimpleApplication implements PhysicsCollisionListener {

    private Geometry ball1, ball2, ball3, ball4, ball5, ball6, ball7, ball8, ball9, fightball,
            border1, border2, border3, border4, floor, floor2, UserPaddle, AiPaddle;
    private RigidBodyControl rigidBodyball1, rigidBodyball2, rigidBodyball3, rigidBodyball4, rigidBodyball5, rigidBodyball6, rigidBodyball7, rigidBodyball8, rigidBodyball9, rigidBodyFight,
            rigidBody1, rigidBody2, rigidBody3, rigidBody4, rigidBodyfloor, rigidBodyfloor2, rigidBodyUserPaddle, rigidBodyAiPaddle;
    private BulletAppState bulletAppState;
    private final Node ball = new Node("ball");
    private final Node border = new Node("border");
    private final Node text = new Node("text");
    private final Node textModel = new Node("textModel");
    private AudioNode audioGun, audioBounce;
    private float x;      //screen centre
    private float y;      //screen centre
    private float a;      //fight ball init velocity.x
    private float b;      //fight ball init velocity.y
    private Vector3f velocity;     //fight ball velocity
    private BitmapText text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11;
    private int AiScore = 0;
    private int round = 1;
    private float time = 0;
    private int UserScore = 0;
    private int HitScore = 0;//obstacles, not show it, but will use it to compute Ai paddle and fight ball velocity
    private int k = 0;     //a counter, let statement just loop once in simpleupdate()
    GameStatus gameStatus = GameStatus.RUNNING;   //for pause state
    private Vector3f ballVelocity;      //for record pause fight ball velocity before
    IsFire isFire = IsFire.FALSE;

    public enum GameStatus {
        RUNNING, PAUSED;
    }

    public enum IsFire {
        TRUE, FALSE;
    }

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1920, 1080);//set fix resolution ratio all the time
        settings.setFrequency(60);  //set fix frequency=60
        settings.setGammaCorrection(true);  //turn gamma correction on all the time
        AiArkanoid app = new AiArkanoid();
        app.start();
        app.setSettings(settings);  //apply fix resolution and gamma correction
        app.restart();//restart program that use fix resolution
    }

    @Override
    public void simpleInitApp() {
        //move camera
        cam.setLocation(new Vector3f(0, 0, 30f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        flyCam.setEnabled(false);//fix camera
        //delete information in left bottom
        setDisplayFps(false);
        setDisplayStatView(false);

        initText();
        initLight();
        initAudio();
        initEnginee();
        initFightBall();
        initBall1();
        initBorder();
        initInput();
    }

    private void initText() {    //add text to show information
        guiNode.attachChild(text);
        guiNode.attachChild(textModel);//use it in simpleupdate(), to show easy ,medium, hard model.
        text1 = guiFont.createLabel("Ai SCORE: " + AiScore);
        text2 = guiFont.createLabel("ROUND: " + round + "/3");
        text3 = guiFont.createLabel("TIME: " + time);
        text4 = guiFont.createLabel("Your SCORE: " + UserScore);
        text5 = guiFont.createLabel("<-Left   Right->");
        text6 = guiFont.createLabel("SPACE to FIRE \n\nP to PAUSE \n\nENTER to CONTINUE");

        text1.setSize(50);
        text1.setColor(ColorRGBA.Red);
        text2.setSize(50);
        text2.setColor(ColorRGBA.Red);
        text3.setSize(50);
        text3.setColor(ColorRGBA.Red);
        text4.setSize(50);
        text4.setColor(ColorRGBA.Red);
        text5.setSize(30);
        text5.setColor(ColorRGBA.White);
        text6.setSize(40);
        text6.setColor(ColorRGBA.White);

        x = (cam.getWidth()) * 0.5f;//get the screen centre
        y = (cam.getHeight()) * 0.5f;

        text1.setLocalTranslation(x + 600, y + 350, 0);
        text2.setLocalTranslation(x + 600, y + 150, 0);
        text3.setLocalTranslation(x + 600, y - 50, 0);
        text4.setLocalTranslation(x + 600, y - 250, 0);
        text5.setLocalTranslation(x - 90, y - 420, 0);
        text6.setLocalTranslation(x - 900, y + 100, 0);

        text.attachChild(text1);
        text.attachChild(text2);
        text.attachChild(text3);
        text.attachChild(text4);
        text.attachChild(text5);
        text.attachChild(text6);
    }

    private void initLight() {
        //add direction light
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -1, 0));

        // add environment light
        AmbientLight ambient = new AmbientLight();

        //adjust light strength
        ColorRGBA lightColor = new ColorRGBA();
        sun.setColor(lightColor.mult(0.8f));
        ambient.setColor(lightColor.mult(0.2f));
        //add sharow
        DirectionalLightShadowFilter sharow = new DirectionalLightShadowFilter(assetManager, 1024, 3);
        sharow.setLight(sun);
        sharow.setEnabled(true);
        FilterPostProcessor Processor = new FilterPostProcessor(assetManager);
        Processor.addFilter(sharow);
        viewPort.addProcessor(Processor);

        rootNode.addLight(sun);
        rootNode.addLight(ambient);
    }

    private void initAudio() {
        //add gun audio for ball collision 
        audioGun = new AudioNode(assetManager, "Sound/Effects/Gun.wav", DataType.Buffer);
        audioGun.setLooping(false);// not Loop Playback
        audioGun.setVolume(6);

        rootNode.attachChild(audioGun);

        //add another audio for border collision
        audioBounce = new AudioNode(assetManager, "Sound/Effects/Bang.wav", DataType.Buffer);
        audioBounce.setLooping(false);
        audioBounce.setVolume(6);

        rootNode.attachChild(audioBounce);
    }
    
    private void initEnginee() { // init physical enginee
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);//add collision
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0f, 0f, 0f));//gravity=0
        PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();
    }

    private void initFightBall() {
        rootNode.attachChild(ball);
        
        //use it to fire ball random direction
        a = ((float) (Math.random() - 0.5f));//(x= -0.5~0.5) to avoid horizon fire
        b = (float) (Math.sqrt(1 - a * a));//get y that x,y,z is unit vector(z=0)
        velocity = new Vector3f(a, b, 0);
        
        fightball = createSphere("fightball", ColorRGBA.Red);//ball size, name,colour, sharow, Material are define in createSphere()
        ball.attachChild(fightball);
        fightball.move(0f, -7.45f, 0);
        rigidBodyFight = new RigidBodyControl(500f);//add 500 kg rigidbody to fire
        fightball.addControl(rigidBodyFight);//combine ball and rigidBody
        rigidBodyFight.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBodyFight.setRestitution(1.0f); //perfect elasticity
        bulletAppState.getPhysicsSpace().add(rigidBodyFight);
    }

    private void initBall1() {// add ball for round1

        ball1 = createSphere("ball1", ColorRGBA.Green);
        ball.attachChild(ball1);
        ball1.move(-10f, 0.1f, 0);
        rigidBodyball1 = new RigidBodyControl(0f);
        ball1.addControl(rigidBodyball1);
        rigidBodyball1.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBodyball1.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball1);

        ball2 = createSphere("ball2", ColorRGBA.Green);
        ball.attachChild(ball2);
        ball2.move(0f, 0.1f, 0);
        rigidBodyball2 = new RigidBodyControl(0f);
        ball2.addControl(rigidBodyball2);
        rigidBodyball2.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBodyball2.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball2);

        ball3 = createSphere("ball3", ColorRGBA.Green);
        ball.attachChild(ball3);
        ball3.move(10f, 0.1f, 0);
        rigidBodyball3 = new RigidBodyControl(0f);
        ball3.addControl(rigidBodyball3);
        rigidBodyball3.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBodyball3.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball3);
    }

    private void initBall2() {// add ball for round2

        ball4 = createSphere("ball4", ColorRGBA.Green);
        ball.attachChild(ball4);
        ball4.move(0f, 3f, 0);
        rigidBodyball4 = new RigidBodyControl(0f);
        ball4.addControl(rigidBodyball4);
        rigidBodyball4.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBodyball4.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball4);

        ball5 = createSphere("ball5", ColorRGBA.Green);
        ball.attachChild(ball5);
        ball5.move(0f, -3f, 0);
        rigidBodyball5 = new RigidBodyControl(0f);
        ball5.addControl(rigidBodyball5);
        rigidBodyball5.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBodyball5.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball5);
    }

    private void initBall3() {// add ball for round3
        ball6 = createSphere("ball6", ColorRGBA.Green);
        ball.attachChild(ball6);
        ball6.move(-10f, 3f, 0);
        rigidBodyball6 = new RigidBodyControl(0f);
        ball6.addControl(rigidBodyball6);
        rigidBodyball6.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBodyball6.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball6);

        ball7 = createSphere("ball7", ColorRGBA.Green);
        ball.attachChild(ball7);
        ball7.move(-10f, -3f, 0);
        rigidBodyball7 = new RigidBodyControl(0f);
        ball7.addControl(rigidBodyball7);
        rigidBodyball7.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBodyball7.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball7);

        ball8 = createSphere("ball8", ColorRGBA.Green);
        ball.attachChild(ball8);
        ball8.move(10f, 3f, 0);
        rigidBodyball8 = new RigidBodyControl(0f);
        ball8.addControl(rigidBodyball8);
        rigidBodyball8.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBodyball8.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball8);

        ball9 = createSphere("ball9", ColorRGBA.Green);
        ball.attachChild(ball9);
        ball9.move(10f, -3f, 0);
        rigidBodyball9 = new RigidBodyControl(0f);
        ball9.addControl(rigidBodyball9);
        rigidBodyball9.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBodyball9.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball9);
    }

    private void initBorder() {  //add border
        rootNode.attachChild(border);

        border1 = createbox("border1", ColorRGBA.Gray, 0.2f, 8f, 0.4f);//box size, name,colour, sharow, Material are define in createBox()
        border1.move(-12f, 0, 0);  //set border location
        rigidBody1 = new RigidBodyControl(0f);  //border is 0 KG
        border1.addControl(rigidBody1);   //combine border to rigid body
        rigidBody1.setCollisionShape(new BoxCollisionShape(new Vector3f(0.2f, 8f, 0.4f)));
        rigidBody1.setRestitution(1.0f);
        border.attachChild(border1);
        bulletAppState.getPhysicsSpace().add(rigidBody1);

        border2 = createbox("border2", ColorRGBA.Gray, 0.2f, 8f, 0.4f);
        border2.move(12, 0, 0);
        rigidBody2 = new RigidBodyControl(0f);
        border2.addControl(rigidBody2);
        rigidBody2.setCollisionShape(new BoxCollisionShape(new Vector3f(0.2f, 8f, 0.4f)));
        rigidBody2.setRestitution(1.0f);
        border.attachChild(border2);
        bulletAppState.getPhysicsSpace().add(rigidBody2);

        border3 = new Geometry("border3", new Box(12f, 0.2f, 0.2f));// at the top to detect out of border
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", new ColorRGBA(0, 0, 0, 0f));//to create transparent box
        mat.setBoolean("UseMaterialColors", true);
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);// to create transparent box
        border3.setMaterial(mat);
        border.attachChild(border3);
        border3.move(0, 8.4f, 0);
        border3.setQueueBucket(Bucket.Translucent);// to create transparent box
        rigidBody3 = new RigidBodyControl(0f);
        border3.addControl(rigidBody3);
        rigidBody3.setCollisionShape(new BoxCollisionShape(new Vector3f(12f, 0.2f, 0.2f)));
        rigidBody3.setRestitution(0f);
        bulletAppState.getPhysicsSpace().add(rigidBody3);

        border4 = new Geometry("border4", new Box(12f, 0.2f, 0.2f));// at the bottom to detect out of border
        border4.setMaterial(mat);
        border.attachChild(border4);
        border4.move(0, -8.4f, 0);
        border4.setQueueBucket(Bucket.Translucent);// to create transparent box
        rigidBody4 = new RigidBodyControl(0f);
        border4.addControl(rigidBody4);
        rigidBody4.setCollisionShape(new BoxCollisionShape(new Vector3f(12f, 0.2f, 0.2f)));
        rigidBody4.setRestitution(0f);
        bulletAppState.getPhysicsSpace().add(rigidBody4);

        floor = new Geometry("floor", new Box(12f, 8f, 0.1f));  //below the ball to avoid ball out
        Material mat1 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat1.setColor("Ambient", new ColorRGBA(250, 240, 230, 1f));//to create transparent box
        mat1.setBoolean("UseMaterialColors", true);
        floor.setMaterial(mat1);
        border.attachChild(floor);
        floor.move(0, 0f, -0.5f);
        rigidBodyfloor = new RigidBodyControl(0f);
        floor.addControl(rigidBodyfloor);
        rigidBodyfloor.setCollisionShape(new BoxCollisionShape(new Vector3f(12f, 8f, 0.1f)));
        rigidBodyfloor.setRestitution(0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyfloor);

        floor2 = new Geometry("floor2", new Box(12f, 8f, 0.1f));//Ceiling
        floor2.setMaterial(mat);
        border.attachChild(floor2);
        floor2.move(0, 0f, 0.5f);
        floor2.setQueueBucket(Bucket.Translucent);// to create transparent box
        rigidBodyfloor2 = new RigidBodyControl(0f);
        floor2.addControl(rigidBodyfloor2);
        rigidBodyfloor2.setCollisionShape(new BoxCollisionShape(new Vector3f(12f, 8f, 0.1f)));
        rigidBodyfloor2.setRestitution(0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyfloor2);

        UserPaddle = createbox("UserPaddle", ColorRGBA.Gray, 1f, 0.1f, 0.4f);
        border.attachChild(UserPaddle);
        UserPaddle.move(0f, -8f, 0f);
        rigidBodyUserPaddle = new RigidBodyControl(0f);
        UserPaddle.addControl(rigidBodyUserPaddle);
        rigidBodyUserPaddle.setCollisionShape(new BoxCollisionShape(new Vector3f(1f, 0.1f, 0.4f)));
        rigidBodyUserPaddle.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyUserPaddle);

        AiPaddle = createbox("AiPaddle", ColorRGBA.Gray, 1f, 0.1f, 0.4f);
        border.attachChild(AiPaddle);
        AiPaddle.move(0, 8f, 0);
        rigidBodyAiPaddle = new RigidBodyControl(0f);
        AiPaddle.addControl(rigidBodyAiPaddle);
        rigidBodyAiPaddle.setCollisionShape(new BoxCollisionShape(new Vector3f(1f, 0.1f, 0.4f)));
        rigidBodyAiPaddle.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyAiPaddle);
    }

    private Geometry createSphere(String name, ColorRGBA colour) {
        // add light material
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", colour);
        mat.setColor("Ambient", colour);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 1);

        // apply
        mat.setBoolean("UseMaterialColors", true);

        //add a sphere
        Geometry geom = new Geometry(name, new Sphere(60, 60, 0.4f));
        geom.setMaterial(mat);
        return geom;
    }

    private Geometry createbox(String name, ColorRGBA colour, float x, float y, float z) {

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", colour);
        mat.setColor("Ambient", ColorRGBA.Gray);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 1);

        mat.setBoolean("UseMaterialColors", true);

        Geometry geom = new Geometry(name, new Box(x, y, z));
        geom.setMaterial(mat);
        return geom;
    }

    private void initInput() {  //define key
        inputManager.addMapping("Left",
                new KeyTrigger(KeyInput.KEY_LEFT)); //  left arrow
        inputManager.addMapping("Right",
                new KeyTrigger(KeyInput.KEY_RIGHT)); //  right arrow
        inputManager.addMapping("Pause",
                new KeyTrigger(KeyInput.KEY_P));//pause game
        inputManager.addMapping("Continue",
                new KeyTrigger(KeyInput.KEY_RETURN));//continue game
        inputManager.addMapping("Fire",
                new KeyTrigger(KeyInput.KEY_SPACE));//fire fight ball
        inputManager.addListener(analogListener, "Left", "Right", "Pause", "Continue", "Fire");
    }

    private final AnalogListener analogListener = new AnalogListener() {// add input listener
        @Override
        public void onAnalog(String name, float value, float tpf) {// let paddle not outside
            if (rigidBodyUserPaddle.getPhysicsLocation().x > 11f) {
                rigidBodyUserPaddle.setPhysicsLocation(new Vector3f(11f, -8f, 0));
            }
            if (rigidBodyUserPaddle.getPhysicsLocation().x < -11f) {
                rigidBodyUserPaddle.setPhysicsLocation(new Vector3f(-11f, -8f, 0));
            }
            if (name.equals("Left") && gameStatus == GameStatus.RUNNING) {//move paddle
                Vector3f left = new Vector3f(-17f * tpf, 0f, 0f);
                rigidBodyUserPaddle.setPhysicsLocation(rigidBodyUserPaddle.getPhysicsLocation().add(left));
            } else if (name.equals("Right") && gameStatus == GameStatus.RUNNING) {
                Vector3f right = new Vector3f(17f * tpf, 0f, 0f);
                rigidBodyUserPaddle.setPhysicsLocation(rigidBodyUserPaddle.getPhysicsLocation().add(right));
            } else if (name.equals("Pause") && gameStatus == GameStatus.RUNNING) {//pause game
                pauseGame();
            } else if (name.equals("Continue") && gameStatus == GameStatus.PAUSED) {//continue game
                resumeGame();
            } else if (name.equals("Fire") && isFire == IsFire.FALSE && gameStatus == GameStatus.RUNNING) {//FIRE fight ball
                // set fight ball initial velocity, which related UserScore and HitScore
                //if user win once, next round fire velocity will speed up to increase difficuty
                //the more ball obstacles, the more fire velocity next round.
                rigidBodyFight.setLinearVelocity(velocity.mult(9f + UserScore + HitScore));
                isFire = IsFire.TRUE;

            }
        }
    };

    public void pauseGame() {

        gameStatus = GameStatus.PAUSED;
        ballVelocity = rigidBodyFight.getLinearVelocity();//save fight ball velocity
        rigidBodyFight.setLinearVelocity(new Vector3f(0, 0, 0));//let fight ball static
    }

    public void resumeGame() {

        gameStatus = GameStatus.RUNNING;
        rigidBodyFight.setLinearVelocity(ballVelocity);//revert fight ball velocity

    }

    @Override
    public void collision(PhysicsCollisionEvent event) {//collision detect

        if ((event.getNodeA().getName().equals("ball1")) || event.getNodeB().getName().equals("ball1")) {
            audioGun.playInstance();//play sound
            bulletAppState.getPhysicsSpace().remove(rigidBodyball1);
            ball.detachChild(ball1);
            HitScore += 1;
        }
        if ((event.getNodeA().getName().equals("ball2")) || event.getNodeB().getName().equals("ball2")) {
            audioGun.playInstance();
            bulletAppState.getPhysicsSpace().remove(rigidBodyball2);
            ball.detachChild(ball2);
            HitScore += 1;
        }
        if ((event.getNodeA().getName().equals("ball3")) || event.getNodeB().getName().equals("ball3")) {
            audioGun.playInstance();
            bulletAppState.getPhysicsSpace().remove(rigidBodyball3);
            ball.detachChild(ball3);
            HitScore += 1;
        }
        if ((event.getNodeA().getName().equals("ball4")) || event.getNodeB().getName().equals("ball4")) {
            audioGun.playInstance();
            bulletAppState.getPhysicsSpace().remove(rigidBodyball4);
            ball.detachChild(ball4);
            HitScore += 1;
        }
        if ((event.getNodeA().getName().equals("ball5")) || event.getNodeB().getName().equals("ball5")) {
            audioGun.playInstance();
            bulletAppState.getPhysicsSpace().remove(rigidBodyball5);
            ball.detachChild(ball5);
            HitScore += 1;
        }
        if ((event.getNodeA().getName().equals("ball6")) || event.getNodeB().getName().equals("ball6")) {
            audioGun.playInstance();
            bulletAppState.getPhysicsSpace().remove(rigidBodyball6);
            ball.detachChild(ball6);
            HitScore += 1;
        }
        if ((event.getNodeA().getName().equals("ball7")) || event.getNodeB().getName().equals("ball7")) {
            audioGun.playInstance();
            bulletAppState.getPhysicsSpace().remove(rigidBodyball7);
            ball.detachChild(ball7);
            HitScore += 1;
        }
        if ((event.getNodeA().getName().equals("ball8")) || event.getNodeB().getName().equals("ball8")) {
            audioGun.playInstance();
            bulletAppState.getPhysicsSpace().remove(rigidBodyball8);
            ball.detachChild(ball8);
            HitScore += 1;
        }
        if ((event.getNodeA().getName().equals("ball9")) || event.getNodeB().getName().equals("ball9")) {
            audioGun.playInstance();
            bulletAppState.getPhysicsSpace().remove(rigidBodyball9);
            ball.detachChild(ball9);
            HitScore += 1;
        }
        
        //avoid angles too close to the horizontal line, if 0.3>LinearVelocity.y>-0.3, fight ball get new LinearVelocity
        if ((event.getNodeA().getName().equals("border1")) || event.getNodeB().getName().equals("border1")) {
            if (rigidBodyFight.getLinearVelocity().y < 0.3 && rigidBodyFight.getLinearVelocity().y >= 0) {
                rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().add(new Vector3f(0, 7f, 0)));
            }
            if (rigidBodyFight.getLinearVelocity().y < 0 && rigidBodyFight.getLinearVelocity().y > -0.3) {
                rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().add(new Vector3f(0, -5f, 0)));
            }
            audioBounce.playInstance();//play sound when collision border
        }

        //just play sound, because border1 are already figure out too close to the horizontal line
        if ((event.getNodeA().getName().equals("border2")) || event.getNodeB().getName().equals("border2")) {

            audioBounce.playInstance();
        }
        
        //top transparent box to compute User score
        if ((event.getNodeA().getName().equals("border3")) || event.getNodeB().getName().equals("border3")) {
            rigidBodyFight.setPhysicsLocation(new Vector3f(0f, -7.20f, 0));
            rigidBodyFight.setLinearVelocity(velocity.mult(0));
            isFire = IsFire.FALSE;
            UserScore += 1;
            rigidBodyUserPaddle.setPhysicsLocation(new Vector3f(0, -8, 0));
            rigidBodyAiPaddle.setPhysicsLocation(new Vector3f(0, 8, 0));
        }
        //bottom transparent box to compute AI score
        if ((event.getNodeA().getName().equals("border4")) || event.getNodeB().getName().equals("border4")) {
            rigidBodyFight.setPhysicsLocation(new Vector3f(0f, -7.20f, 0));
            rigidBodyFight.setLinearVelocity(velocity.mult(0));
            isFire = IsFire.FALSE;
            AiScore += 1;
            rigidBodyUserPaddle.setPhysicsLocation(new Vector3f(0, -8, 0));
            rigidBodyAiPaddle.setPhysicsLocation(new Vector3f(0, 8, 0));
        }

        if ((event.getNodeA().getName().equals("UserPaddle")) || event.getNodeB().getName().equals("UserPaddle")) {
            if (rigidBodyFight.getLinearVelocity().x < 0.3 && rigidBodyFight.getLinearVelocity().x > 0) {
                rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().add(new Vector3f(7f, 0, 0)));
            }
            if (rigidBodyFight.getLinearVelocity().x <= 0 && rigidBodyFight.getLinearVelocity().x > -0.3) {
                rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().add(new Vector3f(-7f, 0, 0)));
            }
            audioBounce.playInstance();
        }
        //avoid angles too close to the vertical line, if 0.3>LinearVelocity.x>-0.3, fight ball get new LinearVelocity
        if ((event.getNodeA().getName().equals("AiPaddle")) || event.getNodeB().getName().equals("AiPaddle")) {
            if (rigidBodyFight.getLinearVelocity().x < 0.3 && rigidBodyFight.getLinearVelocity().x > 0) {
                rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().add(new Vector3f(7f, 0, 0)));
            }
            if (rigidBodyFight.getLinearVelocity().x <= 0 && rigidBodyFight.getLinearVelocity().x > -0.3) {
                rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().add(new Vector3f(-7f, 0, 0)));
            }
            audioBounce.playInstance();
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        //add timer, and update in everu round
        if (isFire == IsFire.TRUE && gameStatus == GameStatus.RUNNING) {
            time = time + tpf;
        }
        if (isFire == IsFire.FALSE) {
            time = 0;
            rigidBodyFight.setLinearVelocity(new Vector3f(0, 0, 0));//let fight ball static
        }
        /**
         * 3 round, Ai or User will get 1 score every round.
         *
         * Ai paddle move velocity changed related to obstacles, round and
         * predict collision coordinates. 
         * every round have different Ai paddle init move velocity(it will 
         * continue to change fast or slow in a round according to HitScore). 
         * the more round, the more Ai paddle init move velocity. 
         * obstacles and predict collision coordinates are in the first decision tree.
         *
         * fight ball velocity changed related to time, userscore and hitscore.
         * the more time, the more fight ball velocity. 
         * the more number of user wins, the more fight ball velocity. 
         * the more obstacles(hitscore), the more fight ball velocity.
         */
        switch (AiScore + UserScore) {
            case 0:
                round = 1;
                text7 = guiFont.createLabel("Easy Model");//show text easy model at the top 
                text7.setSize(40);
                text7.setColor(ColorRGBA.Red);
                text7.setLocalTranslation(x - 100, y + 400, 0);
                textModel.attachChild(text7);
                if (gameStatus == GameStatus.RUNNING && isFire == IsFire.TRUE) {
                    //next two lines are 2 decision trees
                    float AiPaddleVelocity = VelocityDecisionTree(0.05f*100*tpf);//use velocitydecisiontree to compute ai paddle move velocity
                    MoveDecisionTree(AiPaddleVelocity);//use MoveDecisionTree to choose left or right to move.
                    //this is a fight ball velocity keeper
                    rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().normalize().mult(9 + time / 3 + UserScore + HitScore ));
                }
                break;
            case 1:
                round = 2;
                //add more ball in every round
                if ( k == 0) {
                   initBall2();
                   k++;
                }
                textModel.detachAllChildren();
                text8 = guiFont.createLabel("Medium Model");
                text8.setSize(40);
                text8.setColor(ColorRGBA.Red);
                text8.setLocalTranslation(x - 100, y + 400, 0);
                textModel.attachChild(text8);
                if (gameStatus == GameStatus.RUNNING && isFire == IsFire.TRUE) {
                     //next two lines are 2 decision trees
                    float AiPaddleVelocity = VelocityDecisionTree(0.09f*100*tpf);
                    MoveDecisionTree(AiPaddleVelocity);
                     //this is a fight ball velocity keeper
                    rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().normalize().mult(9 + time / 3 + UserScore + HitScore ));
                }
                break;
            case 2:
                round = 3;
                //add more ball in every round
                if ( k == 1) {
                   initBall3();
                   k++;
                }
                textModel.detachAllChildren();
                text9 = guiFont.createLabel("Hard Model");
                text9.setSize(40);
                text9.setColor(ColorRGBA.Red);
                text9.setLocalTranslation(x - 100, y + 400, 0);
                textModel.attachChild(text9);
                if (gameStatus == GameStatus.RUNNING && isFire == IsFire.TRUE) {
                     //next two lines are 2 decision trees
                    float AiPaddleVelocity = VelocityDecisionTree(0.2f*100*tpf);
                    MoveDecisionTree(AiPaddleVelocity);
                    //this is a fight ball velocity keeper
                    rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().normalize().mult(9 + time / 3 + UserScore + HitScore ));
                }
                break;
            case 3:
                //round are finished
                gameStatus = GameStatus.PAUSED;
                ballVelocity =new Vector3f(0,0,0);
                rigidBodyFight.setLinearVelocity(new Vector3f(0, 0, 0));//let fight ball static
                isFire = IsFire.TRUE;
                if (AiScore > UserScore) {
                    text10 = guiFont.createLabel("        YOU LOSE!\nPlease press Esc to exit");
                    text10.setSize(70);
                    text10.setColor(ColorRGBA.Red);
                    text10.setLocalTranslation(x - 300, y + 100, 0);
                    text.attachChild(text10);
                } else {
                    text11 = guiFont.createLabel("        YOU WIN!\nPlease press Esc to exit");
                    text11.setSize(70);
                    text11.setColor(ColorRGBA.Red);
                    text11.setLocalTranslation(x - 300, y + 100, 0);
                    text.attachChild(text11);
                }
                break;
            default:
                break;
        }
       
        //update text 
        text1.setText("Ai SCORE: " + AiScore);
        text2.setText("ROUND: " + round + "/3");
        text3.setText("TIME: " + (int) (time));
        text4.setText("Your SCORE: " + UserScore);

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    /**
     * predict collisiton position between Ai paddle and fight ball
     *
     * @return position.x, because positon.y and z are fixed.
     */
    public double CollisionLocationX() {
        double CollisionLocationX = 0;
        rigidBodyFight.getLinearVelocity();
        rigidBodyFight.getPhysicsLocation();
        //algorithm
        //(CollisionLocationX-fightball location.x)/(CollisionLocationY - fightball location.y)=fight ball velocity.x / fightball location.y
        //CollisionLocationY=8.2f
        CollisionLocationX = ((-(rigidBodyFight.getLinearVelocity().x / rigidBodyFight.getLinearVelocity().y) * (-8.2 + rigidBodyFight.getPhysicsLocation().y)) + rigidBodyFight.getPhysicsLocation().x);
        return CollisionLocationX;
    }

    /**
     * test whether predict location are far away from Ai paddle location
     *
     * @return true or false
     */
    public boolean PredictFarToPaddle() {
        boolean PredictFarToPaddle = true;
        if ((CollisionLocationX() - rigidBodyAiPaddle.getPhysicsLocation().x) > 1 || (CollisionLocationX() - rigidBodyAiPaddle.getPhysicsLocation().x) < -1) {
            PredictFarToPaddle = true;
        } else {
            PredictFarToPaddle = false;
        }
        return PredictFarToPaddle;
    }

    /**
     * GoFast behaviour, the more decrease in obstacles, the more Ai paddle velocity
     *
     * @param v
     * @param increment
     * @return v = v + increment;
     */
    public float GoFast(float v, float increment) {
        v = v + increment;
        return v;
    }

    /**
     * GoSlow behaviour, if predict location are not far away Ai paddle
     * location, GoSlow
     *
     * @param v
     * @return v = v - 0.01f;
     */
    public float GoSlow(float v) {
        v = v - 0.01f;
        return v;
    }

    /**
     * the first one decision tree, it contains Gofast and goslow behaviour.
     *
     * the more obstacles,the more Aipaddle velocity. if predict location are
     * close Ai paddle loction, go slow
     *
     * @param v
     * @return new v
     */
    public float VelocityDecisionTree(float v) {
        if (PredictFarToPaddle() == true) {
            if (HitScore > 3) {
                if (HitScore > 5) {
                    v = GoFast(v, 0.04f);
                } else {
                    v = GoFast(v, 0.02f);
                }
            } else {
                v = GoFast(v, 0.01f);
            }
        } else {
            v = GoSlow(v);
        }
        return v;
    }

    /**
     * test whether fight ball are move
     *
     * @return true or false
     */
    public boolean FightballMove() {
        boolean FightballMove = true;
        if (rigidBodyFight.getLinearVelocity().length() != 0) {
            FightballMove = true;
        } else if (rigidBodyFight.getLinearVelocity().length() == 0) {
            FightballMove = false;
        }
        return FightballMove;
    }

    /**
     * test whether fightball are uping, if uping, move ai paddle.
     *
     * @return true or false
     */
    public boolean FightballUp() {
        boolean FightballUp = true;
        if (rigidBodyFight.getLinearVelocity().y >= 0) {
            FightballUp = true;
        } else if (rigidBodyFight.getLinearVelocity().y < 0) {
            FightballUp = false;
        }
        return FightballUp;
    }

    /**
     * test whether predict locition are same or very close to AI paddle
     * location.
     * avoid Flip-Flopping Decisions
     * 
     * @return true or false
     */
    public boolean SameCollision() {
        boolean SameCollision = true;
        if ((CollisionLocationX() - rigidBodyAiPaddle.getPhysicsLocation().x) > -0.2 && (CollisionLocationX() - rigidBodyAiPaddle.getPhysicsLocation().x) < 0.2) {
            SameCollision = true;
        } else {
            SameCollision = false;
        }
        return SameCollision;
    }

    /**
     * test whether predict collision location are in the left of Ai paddle
     * location
     *
     * @return true or false
     */
    public boolean LeftCollision() {
        boolean LeftCollision = true;
        if (CollisionLocationX() < rigidBodyAiPaddle.getPhysicsLocation().x) {
            LeftCollision = true;
        } else if (CollisionLocationX() > rigidBodyAiPaddle.getPhysicsLocation().x) {
            LeftCollision = false;
        }
        return LeftCollision;
    }

    /**
     * Goleft behaviour, set AI paddle left move
     *
     * @param v
     */
    public void GoLeft(float v) {
        if (rigidBodyAiPaddle.getPhysicsLocation().x > -11f) {// give AI paddle a left bound to move
            Vector3f left = new Vector3f(-v, 0f, 0f);
            rigidBodyAiPaddle.setPhysicsLocation(rigidBodyAiPaddle.getPhysicsLocation().add(left));
        }
    }

    /**
     * GoRight behaviour, set AI paddle right move
     *
     * @param v
     */
    public void GoRight(float v) {
        if (rigidBodyAiPaddle.getPhysicsLocation().x < 11f) {// give AI paddle a right bound to move
            Vector3f right = new Vector3f(v, 0f, 0f);
            rigidBodyAiPaddle.setPhysicsLocation(rigidBodyAiPaddle.getPhysicsLocation().add(right));
        }
    }

    /**
     * stop behaviour, set AI paddle stop.
     */
    public void Stop() {
        rigidBodyAiPaddle.getPhysicsLocation();
    }

    /**
     * the second dicision tree, which contains Goleft, Goright, Stop behaviour.
     *
     * there are 4 dicisions. the more details please read document, which have
     * decision tree graph to explain it.
     *
     * @param v
     */
    public void MoveDecisionTree(float v) {

        if (FightballMove() == true) {
            if (FightballUp() == true) {
                if (SameCollision() == true) {
                    Stop();
                } else if (SameCollision() == false) {
                    if (LeftCollision() == true) {
                        GoLeft(v);
                    } else if (LeftCollision() == false) {
                        GoRight(v);
                    }
                }
            } else if (FightballUp() == false) {
                Stop();
            }
        } else if (FightballMove() == false) {
            Stop();
        }
    }
}
