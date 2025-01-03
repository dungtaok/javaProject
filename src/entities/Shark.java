package entities;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;


import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;


public class Shark extends Enemy {


    private int attackBoxOffsetX;

    public Shark(float x, float y) {

        super(x, y, SHARK_WIDTH, SHARK_HEIGHT, SHARK);
        initHitbox(18, 22);
        initAttackBox(20,20,20);

    }




    public void update(int[][] lvlData, Player player) {
        updateBehaviour(lvlData, player);
        updateAnimationTick();
        updateAttackBoxFlip();

    }

    protected void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    private void updateBehaviour(int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);

        }
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (state) {
                case IDLE:
                    newState(RUNNING);
                    break;

                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseAttack(player))
                            newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;

                    if (aniIndex == 3 && !attackChecked)
                        checkEnmyHit(attackBox, player);
                    break;
                case HIT:
                    break;
            }

        }
    }
    public void drawAttackBox(Graphics g,int xLvlOffset){
        g.setColor(Color.red);
        g.drawRect((int)(attackBox.x-xLvlOffset),(int)(attackBox.y),(int)attackBox.width,(int)attackBox.height);

    }
    public int flipX(){
        if(walkDir==RIGHT)
            return width;
        else
            return  0;
    }
    public int flipY(){
        if(walkDir==RIGHT)
            return -1;
        else{
            return 1;
        }
    }

}


