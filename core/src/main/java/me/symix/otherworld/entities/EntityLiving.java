package me.symix.otherworld.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.symix.otherworld.misc.EntityTexture;
import me.symix.otherworld.world.World;

public class EntityLiving extends Entity {

    public int damage, armor, hp, maxHp, knockback;

    public EntityLiving(int x, int y, int damage, int armor, int maxHp, EntityTexture texture, World world){
        super(x, y, texture, world);
        this.damage = damage;
        this.armor = armor;
        this.maxHp = maxHp;
        hp = maxHp;
        knockback = 10;
    }

    public void render(SpriteBatch batch){
        super.render(batch);

        world.sr.setColor(Color.RED);
        world.sr.setProjectionMatrix(batch.getProjectionMatrix());

        batch.end();

        world.sr.begin(ShapeRenderer.ShapeType.Filled);
        world.sr.rect(getX() + (getTexture().getWidth() / 2) - 20, getY() - 10, 40, 5);

        world.sr.setColor(Color.LIME);
        int hpWidth = (int)(40 * ((double)hp / maxHp));

        world.sr.rect(getX() + (getTexture().getWidth() / 2) - 20, getY() - 10, hpWidth, 5);

        world.sr.end();

        batch.begin();
    }

    public void takeDamage(int damage){
        hp -= (damage - (armor / 2));
        if(hp <= 0){
            isDead = true;
        }
    }
}
