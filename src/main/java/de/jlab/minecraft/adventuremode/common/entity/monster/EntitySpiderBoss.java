package de.jlab.minecraft.mods.adventuremode.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.world.World;

public class EntitySpiderBoss extends EntitySpider {

    public EntitySpiderBoss(World par1World)
    {
        super(par1World);
        this.setSize(1.4F * 2.1f, 0.9F * 2.1f);
        
    }

    /* INIT SPECIAL ATTRIBUTES */
    protected void func_110147_ax() {
        super.func_110147_ax();
        
        this.func_110140_aT().func_111150_b(EntityBoss.scale);
        this.func_110148_a(EntityBoss.scale).func_111128_a(3D);
        
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(48D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.800000011920929D);
        this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(6D);
    }
    
}
