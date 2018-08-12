package de.jlab.minecraft.mods.adventuremode.entity.ai;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTargetSorter;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIOnARampage extends EntityAIBase {

	private EntityCreature taskOwner;
    private final Class targetClass;

    /** Instance of EntityAINearestAttackableTargetSorter. */
    private final EntityAINearestAttackableTargetSorter theNearestAttackableTargetSorter;
    private final IEntitySelector targetEntitySelector;
    private EntityLivingBase targetEntity;
	
	public EntityAIOnARampage(EntityCreature entity, Class targetClass, double aggroRange) {
		this.taskOwner = entity;
		
		// SET ATTACK RANGE OF ALL MONSTERS TO RADIUS
		this.taskOwner.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(aggroRange);
				
		this.targetClass = targetClass;
        this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTargetSorter(entity);
        this.setMutexBits(1);
        this.targetEntitySelector = new IEntitySelector() {
    		@Override
    		public boolean isEntityApplicable(Entity entity) {
    			return entity instanceof EntityPlayer || entity instanceof EntityAnimal;
    		}
    	};
	}
	
    public boolean shouldExecute() {
/*
    	EntityLivingBase entitylivingbase = this.taskOwner.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        } else if (!entitylivingbase.isEntityAlive()) {
            return false;
        } else {
            double d0 = this.func_111175_f();

            if (this.taskOwner.getDistanceSqToEntity(entitylivingbase) > d0 * d0) {
                return false;
            } else {
                return true;
            }
        }
*/        
            
		List list = getPotentialTargetList();
        if (list.isEmpty()) {
            return false;
        } else {
            this.targetEntity = (EntityLivingBase)list.get(0);
            return true;
        }
           
    }
    
    protected double func_111175_f() {
        AttributeInstance attributeinstance = this.taskOwner.func_110148_a(SharedMonsterAttributes.field_111265_b);
        return attributeinstance == null ? 16.0D : attributeinstance.func_111126_e();
    }
    
    public void startExecuting() {
		this.taskOwner.setAttackTarget(this.targetEntity);
    }
    
    private List getPotentialTargetList() {
    	double aggroRange = this.func_111175_f();
        List list = this.taskOwner.worldObj.selectEntitiesWithinAABB(this.targetClass, this.taskOwner.boundingBox.expand(aggroRange, 4.0D, aggroRange), this.targetEntitySelector);
        Collections.sort(list, this.theNearestAttackableTargetSorter);
        return list;
    }
    
    /**
     * Updates the task
     */
    public void updateTask() {
    	/*
    	if (entitylivingbase == null) {
            return false;
        } else if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        */
    }

}
