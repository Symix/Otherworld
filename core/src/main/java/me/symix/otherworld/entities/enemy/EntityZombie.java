package me.symix.otherworld.entities.enemy;

import me.symix.otherworld.misc.AllTexture;
import me.symix.otherworld.world.World;

public class EntityZombie extends EntityBasicEnemy {

    public EntityZombie(int x, int y, World world){
        super(x, y, 14, 6, 45, AllTexture.zombie, world);
    }
}
