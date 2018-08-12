package de.jlab.minecraft.mods.adventuremode.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.jlab.minecraft.mods.adventuremode.entity.monster.EntitySpiderBoss;

@SideOnly(Side.CLIENT)
public class RenderSpiderBoss extends RenderSpider {

    /** Scale of the model to use */
    private float scale;

    public RenderSpiderBoss()
    {
        super();
    }

    /**
     * Applies the scale to the transform matrix
     */
    protected void preRenderScale(EntitySpiderBoss entity, float par2) {
    	this.scale = (float)entity.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e();
        GL11.glScalef(this.scale, this.scale, this.scale);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
    {
        this.preRenderScale((EntitySpiderBoss)par1EntityLivingBase, par2);
    }
}