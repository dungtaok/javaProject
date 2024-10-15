package entities;

import main.Game;
import utilz.LoadSave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utilz.Constants.Directions.*;
import static utilz.Constants.Directions.DOWN;
import static utilz.Constants.PlayerConstants.*;
import  static  utilz.HelpMethods.*;
public class Player extends  Entity{
    private BufferedImage[][] animations;
    private int aniTick, aniIndex,aniSpeed=25;
    private int playerAction = IDLE;
    private  boolean moving = false,attacking=false;
    private boolean up,down,right,left,jump;
    private float playerSpeed=1.0f*Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset=21* Game.SCALE;
    private  float yDrawOffset= 4*Game.SCALE;
    private float airSpeed = 0f;
    private float gravity = 0.04f*Game.SCALE;
    private float jumpSpeed=-2.25f*Game.SCALE;
    private  float fallSpeedAfterCollision = 0.5f*Game.SCALE;
    private boolean inAir = false;







    public Player(float x, float y,int width,int height) {
        super(x, y,width,height);
        loadAnimations();
        initHitbox(x,y,(int)(20*Game.SCALE),(int)(27*Game.SCALE));
    }
    public void update(){
        updatePos();
        updateAnimationTick();
        setAnimation();

    }
    public void render(Graphics g, int lvlOffset){
        g.drawImage(animations[playerAction][aniIndex],(int)(hitbox.x-xDrawOffset)-lvlOffset,(int)(hitbox.y-yDrawOffset),width,height,null);

      //drawHitbox(g,lvlOffset);
    }






    private void updatePos() {
        moving = false;
        if(jump)
            jump();
        if(!inAir)
            if((!left&&!right)||(right&&left))
                return;

        float xSpeed=0;

if(left)
    xSpeed-=playerSpeed;

 if(right)
    xSpeed+=playerSpeed;

 if(!inAir)
     if(!IsEntityOnFloor(hitbox,lvlData))
         inAir=true;



if(inAir) {
    if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
        hitbox.y += airSpeed;
        airSpeed += gravity;
        updateXPos(xSpeed);
    } else {
        hitbox.y = GetEntitYPosUnderRoofforAboveFloor(hitbox, airSpeed);

        if(airSpeed>0)
            resetInAir();
        else airSpeed = fallSpeedAfterCollision;
        updateXPos(xSpeed);
    }
}
else{
    updateXPos(xSpeed);

}
moving = true;




    }

    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
         if(CanMoveHere(hitbox.x+xSpeed,hitbox.y, hitbox.width,hitbox.height,lvlData)) {
            hitbox.x += xSpeed;
        }else{
             hitbox.x = GetEntityXPosNextToWall(hitbox,xSpeed);
         }
    }

    public void updateGame(){
        updatePos();
        updateAnimationTick();
        setAnimation();

    }

    private void setAnimation() {
        int startAni = playerAction;
        if(moving)
            playerAction=RUNNING;

        else
            playerAction=IDLE;
        if(inAir){
            if(airSpeed<0)
                playerAction=JUMP;
            else
                playerAction = FALLING;
        }
        if(attacking)
            playerAction=ATTACK_1;
        if(startAni!=playerAction)
            resetAniTick();

    }

    private void resetAniTick() {
        aniIndex=0;
        aniTick=0;
    }

    private void updateAnimationTick() {
        aniTick++;
        if(aniTick>=aniSpeed){
            aniTick=0;
            aniIndex++;
            if(aniIndex>=GetSpriteAmount(playerAction)){
                aniIndex=0;
                attacking=false;
            }

        }
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }
public void resetDirBooleans(){
        up=false;
        down=false;
        right=false;
        left=false;

}

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    private void loadAnimations() {

            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

            animations = new BufferedImage[9][6];
            for (int j = 0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);

        }
        public void loadLvlData(int[][]lvlData){
          this.lvlData=lvlData;
          if(!IsEntityOnFloor(hitbox,lvlData))
              inAir = true;
        }


    public void setJump(boolean jump) {
        this.jump=jump;
    }
}




