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
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;

/**
 *
 * @author pengcheng jin id: 201447767
 */
public class Arkanoid extends SimpleApplication implements PhysicsCollisionListener {

    private Geometry ball1, ball2, ball3, ball4, ball5, ball6, ball7, ball8, ball9, ball10, fightball,
            border1, border2, border3, border4, floor, floor2, paddle;
    private RigidBodyControl rigidBodyball1, rigidBodyball2, rigidBodyball3, rigidBodyball4,
            rigidBodyball5, rigidBodyball6, rigidBodyball7, rigidBodyball8, rigidBodyball9, rigidBodyball10,
            rigidBodyFight, rigidBodypaddle;
    private BulletAppState bulletAppState;
    private AudioNode audioGun, audioBounce;
    private float x;
    private float y;
    private float a;
    private float b;
    private Vector3f velocity;
    private BitmapText text1, text2, text3, text4, text5, text6, text7, text8, text9, text10;
    private int level = 1;
    private int life = 3;
    private int score = 0;
    private int countball5 = 3;
    private int countball6 = 3;
    private int countball7 = 3;
    GameStatus gameStatus = GameStatus.RUNNING;
    IsFire isFire = IsFire.FALSE;
    private Vector3f ballVelocity;

    public enum GameStatus {
        RUNNING, PAUSED;
    }

    public enum IsFire {
        TRUE, FALSE;
    }

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1920, 1080);//set fix resolution ratio all the time
        settings.setGammaCorrection(true);  //turn gamma correction on all the time
        Arkanoid app = new Arkanoid();
        app.start();
        app.setSettings(settings);  //apply fix resolution and gamma correction
        app.restart();//restart program that use fix resolution
    }

    @Override
    public void simpleInitApp() {
        //move camera
        cam.setLocation(new Vector3f(0, 0, 10f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        flyCam.setEnabled(false);//fix camera
        //delete information in left bottom
        setDisplayFps(false);
        setDisplayStatView(false);

        initText();
        initLight();
        initAudio();
        initEnginee();
        initBall1();
        initBorder();
        initInput();

    }

    private void initText() {    //add text to show level, score and life
        text1 = guiFont.createLabel("LEVEL\n" + level);
        text2 = guiFont.createLabel("SCORE\n" + score);
        text3 = guiFont.createLabel("LIFE\n" + life);
        text4 = guiFont.createLabel("<-Left   Right->");
        text5 = guiFont.createLabel("SPACE to FIRE \n\nP to PAUSE \n\nENTER to CONTINUE");

        text1.setSize(70);
        text1.setColor(ColorRGBA.Red);
        text2.setSize(70);
        text2.setColor(ColorRGBA.Red);
        text3.setSize(70);
        text3.setColor(ColorRGBA.Red);
        text4.setSize(30);
        text4.setColor(ColorRGBA.White);
        text5.setSize(50);
        text5.setColor(ColorRGBA.White);

        x = (cam.getWidth()) * 0.5f;//get the screen centre
        y = (cam.getHeight()) * 0.5f;

        text1.setLocalTranslation(x + 400, y + 350, 0);
        text2.setLocalTranslation(x + 400, y + 100, 0);
        text3.setLocalTranslation(x + 400, y - 150, 0);
        text4.setLocalTranslation(x - 90, y - 420, 0);
        text5.setLocalTranslation(x - 900, y + 100, 0);

        guiNode.attachChild(text1);
        guiNode.attachChild(text2);
        guiNode.attachChild(text3);
        guiNode.attachChild(text4);
        guiNode.attachChild(text5);

    }

    private void initLight() {
        //add direction light
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-5, -5, 0));

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

    private void initEnginee() { // init physical enginee
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);//add collision
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0f, 0f, 0f));//gravity=0
        PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();
    }

    private void initBall1() {// add ball for level1
        //use it to fire ball random direction
        a = ((float) (Math.random() - 0.5f));//( -0.5f) to avoid horizon fire
        b = (float) (Math.sqrt(1 - a * a));//get y that x,y,z is unit vector(z=0)
        velocity = new Vector3f(a, b, 0);

        ball1 = createSphere("ball1", ColorRGBA.Green);  //add green ball, it's name is ball1
        rootNode.attachChild(ball1);  //add ball1 to rootNode
        ball1.move(-1.5f, 1.5f, 0);   //set ball1 location
        rigidBodyball1 = new RigidBodyControl(0f);   //add 0 kg rigidbady 
        ball1.addControl(rigidBodyball1);//combine rigidbady and ball
        rigidBodyball1.setCollisionShape(new SphereCollisionShape(0.2f));//rigidbady shape
        rigidBodyball1.setRestitution(1.0f);//set elasticity
        bulletAppState.getPhysicsSpace().add(rigidBodyball1);//add rigidbady to bulletAppState

        ball2 = createSphere("ball2", ColorRGBA.Green);
        rootNode.attachChild(ball2);
        ball2.move(-0.5f, 1.5f, 0);
        rigidBodyball2 = new RigidBodyControl(0f);
        ball2.addControl(rigidBodyball2);
        rigidBodyball2.setCollisionShape(new SphereCollisionShape(0.2f));
        rigidBodyball2.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball2);

        ball3 = createSphere("ball3", ColorRGBA.Green);
        rootNode.attachChild(ball3);
        ball3.move(0.5f, 1.5f, 0);
        rigidBodyball3 = new RigidBodyControl(0f);
        ball3.addControl(rigidBodyball3);
        rigidBodyball3.setCollisionShape(new SphereCollisionShape(0.2f));
        rigidBodyball3.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball3);

        ball4 = createSphere("ball4", ColorRGBA.Green);
        rootNode.attachChild(ball4);
        ball4.move(1.5f, 1.5f, 0);
        rigidBodyball4 = new RigidBodyControl(0f);
        ball4.addControl(rigidBodyball4);
        rigidBodyball4.setCollisionShape(new SphereCollisionShape(0.2f));
        rigidBodyball4.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball4);

        fightball = createSphere("fightball", ColorRGBA.Red);
        rootNode.attachChild(fightball);
        fightball.move(0f, -2.4f, 0);
        rigidBodyFight = new RigidBodyControl(500f);//add 500 kg rigidbody to fire
        fightball.addControl(rigidBodyFight);
        rigidBodyFight.setCollisionShape(new SphereCollisionShape(0.2f));
        rigidBodyFight.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyFight);
    }

    private void initBall2() {// add ball for level 2

        ball5 = createSphere("ball5", ColorRGBA.Green);
        rootNode.attachChild(ball5);
        ball5.move(-1f, 0.8f, 0);
        rigidBodyball5 = new RigidBodyControl(0f);
        ball5.addControl(rigidBodyball5);
        rigidBodyball5.setCollisionShape(new SphereCollisionShape(0.2f));
        rigidBodyball5.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball5);

        ball6 = createSphere("ball6", ColorRGBA.Green);
        rootNode.attachChild(ball6);
        ball6.move(0f, 0.8f, 0);
        rigidBodyball6 = new RigidBodyControl(0f);
        ball6.addControl(rigidBodyball6);
        rigidBodyball6.setCollisionShape(new SphereCollisionShape(0.2f));
        rigidBodyball6.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball6);

        ball7 = createSphere("ball7", ColorRGBA.Green);
        rootNode.attachChild(ball7);
        ball7.move(1f, 0.8f, 0);
        rigidBodyball7 = new RigidBodyControl(0f);
        ball7.addControl(rigidBodyball7);
        rigidBodyball7.setCollisionShape(new SphereCollisionShape(0.2f));
        rigidBodyball7.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball7);

        ball8 = createSphere("ball8", ColorRGBA.Green);
        rootNode.attachChild(ball8);
        ball8.move(-0.5f, 0.1f, 0);
        rigidBodyball8 = new RigidBodyControl(0f);
        ball8.addControl(rigidBodyball8);
        rigidBodyball8.setCollisionShape(new SphereCollisionShape(0.2f));
        rigidBodyball8.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball8);

        ball9 = createSphere("ball9", ColorRGBA.Green);
        rootNode.attachChild(ball9);
        ball9.move(0.5f, 0.1f, 0);
        rigidBodyball9 = new RigidBodyControl(0f);
        ball9.addControl(rigidBodyball9);
        rigidBodyball9.setCollisionShape(new SphereCollisionShape(0.2f));
        rigidBodyball9.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball9);

        ball10 = createSphere("ball10", ColorRGBA.Green);
        rootNode.attachChild(ball10);
        ball10.move(0f, -0.6f, 0);
        rigidBodyball10 = new RigidBodyControl(0f);
        ball10.addControl(rigidBodyball10);
        rigidBodyball10.setCollisionShape(new SphereCollisionShape(0.2f));
        rigidBodyball10.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyball10);

        text6 = guiFont.createLabel("" + countball5);//add this text for multiple fight one green ball
        text7 = guiFont.createLabel("" + countball6);
        text8 = guiFont.createLabel("" + countball7);

        text6.setSize(40);
        text6.setColor(ColorRGBA.White);
        text7.setSize(40);
        text7.setColor(ColorRGBA.White);
        text8.setSize(40);
        text8.setColor(ColorRGBA.White);
        x = (cam.getWidth()) * 0.5f;//get the screen centre
        y = (cam.getHeight()) * 0.5f;
        text6.setLocalTranslation(x - 140, y + 130, 0);
        text7.setLocalTranslation(x - 10, y + 130, 0);
        text8.setLocalTranslation(x + 120, y + 130, 0);

        guiNode.attachChild(text6);
        guiNode.attachChild(text7);
        guiNode.attachChild(text8);
    }

    private void initBorder() {  //add border
        border1 = createbox("border1", ColorRGBA.Gray, 0.2f, 3f, 0.2f);//create box, and it's name is border1
        border1.move(-2, 0, 0);  //set border location
        RigidBodyControl rigidBody1 = new RigidBodyControl(0f);  //border is 0 KG
        border1.addControl(rigidBody1);   //combine border to rigid body
        rigidBody1.setCollisionShape(new BoxCollisionShape(new Vector3f(0.2f, 3f, 0.2f)));
        rigidBody1.setRestitution(1.0f);
        rootNode.attachChild(border1);
        bulletAppState.getPhysicsSpace().add(rigidBody1);

        border2 = createbox("border2", ColorRGBA.Gray, 2f, 0.2f, 0.2f);
        rootNode.attachChild(border2);
        border2.move(0, 2.8f, 0);
        RigidBodyControl rigidBody2 = new RigidBodyControl(0f);
        border2.addControl(rigidBody2);
        rigidBody2.setCollisionShape(new BoxCollisionShape(new Vector3f(2f, 0.2f, 0.2f)));
        rigidBody2.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBody2);

        border3 = createbox("border3", ColorRGBA.Gray, 0.2f, 3f, 0.2f);
        rootNode.attachChild(border3);
        border3.move(2, 0, 0);
        RigidBodyControl rigidBody3 = new RigidBodyControl(0f);
        border3.addControl(rigidBody3);
        rigidBody3.setCollisionShape(new BoxCollisionShape(new Vector3f(0.2f, 3f, 0.2f)));
        rigidBody3.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBody3);

        border4 = new Geometry("border4", new Box(2f, 0.2f, 0.2f));// at the bottom to detect out of border
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", new ColorRGBA(0, 0, 0, 0f));//to create transparent box
        mat.setBoolean("UseMaterialColors", true);
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);// to create transparent box
        border4.setMaterial(mat);
        rootNode.attachChild(border4);
        border4.move(0, -3.2f, 0);
        border4.setQueueBucket(Bucket.Translucent);// to create transparent box
        RigidBodyControl rigidBody4 = new RigidBodyControl(0f);
        border4.addControl(rigidBody4);
        rigidBody4.setCollisionShape(new BoxCollisionShape(new Vector3f(2f, 0.2f, 0.2f)));
        rigidBody4.setRestitution(0f);
        bulletAppState.getPhysicsSpace().add(rigidBody4);

        floor = new Geometry("floor", new Box(2f, 3f, 0.2f));
        Material mat1 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat1.setColor("Diffuse", new ColorRGBA(0, 0, 0, 0f));//to create transparent box
        mat1.setBoolean("UseMaterialColors", true);
        mat1.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);// to create transparent box
        floor.setMaterial(mat1);
        rootNode.attachChild(floor);
        floor.move(0, 0f, -0.4f);
        floor.setQueueBucket(Bucket.Translucent);// to create transparent box
        RigidBodyControl rigidBodyfloor = new RigidBodyControl(0f);
        floor.addControl(rigidBodyfloor);
        rigidBodyfloor.setCollisionShape(new BoxCollisionShape(new Vector3f(2f, 3f, 0.2f)));
        rigidBodyfloor.setRestitution(0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyfloor);

        floor2 = new Geometry("floor2", new Box(2f, 3f, 0.2f));//Ceiling
        Material mat2 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat2.setColor("Diffuse", new ColorRGBA(0, 0, 0, 0f));// to create transparent box
        mat2.setBoolean("UseMaterialColors", true);
        mat2.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);// to create transparent box
        floor2.setMaterial(mat2);
        rootNode.attachChild(floor2);
        floor2.move(0, 0f, 0.4f);
        floor2.setQueueBucket(Bucket.Translucent);// to create transparent box
        RigidBodyControl rigidBodyfloor2 = new RigidBodyControl(0f);
        floor2.addControl(rigidBodyfloor2);
        rigidBodyfloor2.setCollisionShape(new BoxCollisionShape(new Vector3f(2f, 3f, 0.2f)));
        rigidBodyfloor2.setRestitution(0f);
        bulletAppState.getPhysicsSpace().add(rigidBodyfloor2);

        paddle = createbox("paddle", ColorRGBA.Gray, 0.3f, 0.1f, 0.3f);
        rootNode.attachChild(paddle);
        paddle.move(0f, -2.8f, 0f);
        rigidBodypaddle = new RigidBodyControl(0f);
        paddle.addControl(rigidBodypaddle);
        rigidBodypaddle.setCollisionShape(new BoxCollisionShape(new Vector3f(0.3f, 0.1f, 0.3f)));
        rigidBodypaddle.setRestitution(1.0f);
        bulletAppState.getPhysicsSpace().add(rigidBodypaddle);
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
        Geometry geom = new Geometry(name, new Sphere(60, 60, 0.2f));
        geom.setMaterial(mat);

        return geom;
    }

    private Geometry createbox(String name, ColorRGBA colour, float x, float y, float z) {

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", colour);
        mat.setColor("Ambient", colour);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 1);
        //add texture
        Texture tex = assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg");
        mat.setTexture("DiffuseMap", tex);

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
        public void onAnalog(String name, float value, float tpf) {// let paddle not in outside
            if (rigidBodypaddle.getPhysicsLocation().x > 1.45f) {
                rigidBodypaddle.setPhysicsLocation(new Vector3f(1.45f, -2.8f, 0));
            }
            if (rigidBodypaddle.getPhysicsLocation().x < -1.45f) {
                rigidBodypaddle.setPhysicsLocation(new Vector3f(-1.45f, -2.8f, 0));
            }
            if (name.equals("Left") && gameStatus == GameStatus.RUNNING) {//move paddle
                Vector3f left = new Vector3f(-3f * tpf, 0f, 0f);
                rigidBodypaddle.setPhysicsLocation(rigidBodypaddle.getPhysicsLocation().add(left));
            } else if (name.equals("Right") && gameStatus == GameStatus.RUNNING) {
                Vector3f right = new Vector3f(3f * tpf, 0f, 0f);
                rigidBodypaddle.setPhysicsLocation(rigidBodypaddle.getPhysicsLocation().add(right));
            } else if (name.equals("Pause") && gameStatus == GameStatus.RUNNING) {//pause game
                pauseGame();
            } else if (name.equals("Continue") && gameStatus == GameStatus.PAUSED) {//continue game
                resumeGame();
            } else if (name.equals("Fire") && isFire == IsFire.FALSE) {//FIRE fight ball
                rigidBodyFight.setLinearVelocity(velocity.mult(3f));// set fight ball initial velocity
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

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code

        //give fight ball fix velocity(never change)
        if (gameStatus == GameStatus.RUNNING) {
            rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().normalize().mult(3f));
        }
        // go level 2
        if (score == 4 && level == 1) {//if score=4, go to next level
            bulletAppState.getPhysicsSpace().remove(rigidBodyFight);
            rootNode.detachChild(fightball);
            initBall1();  //add new ball1
            initBall2();  //add new ball2
            level += 1;   //it can just update once
            isFire = IsFire.FALSE;//set fire ball status
            rigidBodypaddle.setPhysicsLocation(new Vector3f(0, -2.8f, 0));//move paddle in middle
        }
        if (life == 0 && score != 20) {//if life =0,lose
            text9 = guiFont.createLabel("   Sorry,you lose \n  Press Press Esc");
            text9.setSize(70);
            text9.setColor(ColorRGBA.Red);
            x = (cam.getWidth()) * 0.5f;//get the screen centre
            y = (cam.getHeight()) * 0.5f;
            text9.setLocalTranslation(x - 270, y + 120, 0);
            guiNode.attachChild(text9);
            //stop the fight ball
            rigidBodyFight.setPhysicsLocation(new Vector3f(0f, -2.4f, 0));
            rigidBodyFight.setLinearVelocity(velocity.mult(0));
        }
        if (score == 20 && life > 0) {//if score=4+16=20,win
            text10 = guiFont.createLabel("Congratulations,you Win  \n     Please Press Esc ");
            text10.setSize(70);
            text10.setColor(ColorRGBA.Red);
            x = (cam.getWidth()) * 0.5f;//get the screen centre
            y = (cam.getHeight()) * 0.5f;
            text10.setLocalTranslation(x - 320, y + 120, 0);
            guiNode.attachChild(text10);
            //stop the fight ball
            rigidBodyFight.setLinearVelocity(velocity.mult(0));
        }
        //update text for level score life
        text1.setText("LEVEL\n" + level);
        text2.setText("SCORE\n" + score);
        text3.setText("LIFE\n" + life);
        if (score > 4) {//updete multiple hits count in  green balls
            text6.setText("" + countball5);
            text7.setText("" + countball6);
            text8.setText("" + countball7);
        }
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {//collision detect

        if ((event.getNodeA().getName().equals("ball1")) || event.getNodeB().getName().equals("ball1")) {//if collision with ball1
            bulletAppState.getPhysicsSpace().remove(rigidBodyball1);//delete rigidbody ball
            rootNode.detachChild(ball1);//delete geom ball
            audioGun.playInstance();//play audio
            score += 1;//score
        }
        if ((event.getNodeA().getName().equals("ball2")) || event.getNodeB().getName().equals("ball2")) {
            bulletAppState.getPhysicsSpace().remove(rigidBodyball2);
            rootNode.detachChild(ball2);
            audioGun.playInstance();
            score += 1;
        }
        if ((event.getNodeA().getName().equals("ball3")) || event.getNodeB().getName().equals("ball3")) {
            bulletAppState.getPhysicsSpace().remove(rigidBodyball3);
            rootNode.detachChild(ball3);
            audioGun.playInstance();
            score += 1;
        }

        if ((event.getNodeA().getName().equals("ball4")) || event.getNodeB().getName().equals("ball4")) {
            bulletAppState.getPhysicsSpace().remove(rigidBodyball4);
            rootNode.detachChild(ball4);
            audioGun.playInstance();
            score += 1;
        }

        if ((event.getNodeA().getName().equals("ball5")) || event.getNodeB().getName().equals("ball5")) {
            audioGun.playInstance();
            score += 1;
            countball5 = countball5 - 1;//updete multiple hits count in  green balls
            if (countball5 == 0) {//until count =0, remove this ball
                bulletAppState.getPhysicsSpace().remove(rigidBodyball5);
                rootNode.detachChild(ball5);
                text6.removeFromParent();
            }

        }

        if ((event.getNodeA().getName().equals("ball6")) || event.getNodeB().getName().equals("ball6")) {
            audioGun.playInstance();
            score += 1;
            countball6 = countball6 - 1;//updete multiple hits count in  green balls
            if (countball6 == 0) {
                bulletAppState.getPhysicsSpace().remove(rigidBodyball6);
                rootNode.detachChild(ball6);
                text7.removeFromParent();
            }
        }

        if ((event.getNodeA().getName().equals("ball7")) || event.getNodeB().getName().equals("ball7")) {
            audioGun.playInstance();
            score += 1;
            countball7 = countball7 - 1;//updete multiple hits count in  green balls
            if (countball7 == 0) {
                bulletAppState.getPhysicsSpace().remove(rigidBodyball7);
                rootNode.detachChild(ball7);
                text8.removeFromParent();
            }
        }

        if ((event.getNodeA().getName().equals("ball8")) || event.getNodeB().getName().equals("ball8")) {
            bulletAppState.getPhysicsSpace().remove(rigidBodyball8);
            rootNode.detachChild(ball8);
            audioGun.playInstance();
            score += 1;
        }

        if ((event.getNodeA().getName().equals("ball9")) || event.getNodeB().getName().equals("ball9")) {
            bulletAppState.getPhysicsSpace().remove(rigidBodyball9);
            rootNode.detachChild(ball9);
            audioGun.playInstance();
            score += 1;
        }

        if ((event.getNodeA().getName().equals("ball10")) || event.getNodeB().getName().equals("ball10")) {
            bulletAppState.getPhysicsSpace().remove(rigidBodyball10);
            rootNode.detachChild(ball10);
            audioGun.playInstance();
            score += 1;
        }

        //avoid angles too close to the horizontal line, if 0.3>LinearVelocity.y>-0.3, fight ball get new LinearVelocity
        if ((event.getNodeA().getName().equals("border1")) || event.getNodeB().getName().equals("border1")) {
            if (rigidBodyFight.getLinearVelocity().y < 0.3 && rigidBodyFight.getLinearVelocity().y >= 0) {
                rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().add(new Vector3f(0, 2f, 0)));
            }
            if (rigidBodyFight.getLinearVelocity().y < 0 && rigidBodyFight.getLinearVelocity().y > -0.3) {
                rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().add(new Vector3f(0, -1f, 0)));
            }
            audioBounce.playInstance();//play sound when collision border
        }

        //avoid angles too close to the vertical line, if 0.3>LinearVelocity.x>-0.3, fight ball get new LinearVelocity
        if ((event.getNodeA().getName().equals("border2")) || event.getNodeB().getName().equals("border2")) {
            if (rigidBodyFight.getLinearVelocity().x < 0.3 && rigidBodyFight.getLinearVelocity().x > -0.3) {
                rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().add(new Vector3f(2f, 0, 0)));
            }
            audioBounce.playInstance();
        }

        //just play sound, because border1 are already figure out too close to the horizontal line
        if ((event.getNodeA().getName().equals("border3")) || event.getNodeB().getName().equals("border3")) {
            if (rigidBodyFight.getLinearVelocity().y < 0.3 && rigidBodyFight.getLinearVelocity().y > -0.3) {
                rigidBodyFight.setLinearVelocity(rigidBodyFight.getLinearVelocity().add(new Vector3f(0, 0, 0)));
            }
            audioBounce.playInstance();
        }

        //if collision with border4(bottom, transparent), fight fire again,and life-1
        if ((event.getNodeA().getName().equals("border4")) || event.getNodeB().getName().equals("border4")) {
            life -= 1;
            rigidBodyFight.setPhysicsLocation(new Vector3f(0f, -2.4f, 0));
            rigidBodyFight.setLinearVelocity(velocity.mult(3f));

        }

        if ((event.getNodeA().getName().equals("paddle")) || event.getNodeB().getName().equals("paddle")) {

            audioBounce.playInstance();
        }

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
