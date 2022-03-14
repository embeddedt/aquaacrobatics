package com.fuzs.aquaacrobatics.entity;

import com.fuzs.aquaacrobatics.proxy.CommonProxy;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityDrowned extends EntityZombie implements IEntitySwimmer {
    private boolean swimmingUp;
    protected final PathNavigateSwimmer waterNavigator;
    protected final PathNavigateGround groundNavigator;

    public EntityDrowned(World worldIn) {
        super(worldIn);
        this.stepHeight = 1.0F;
        this.moveHelper = new EntityDrowned.MoveHelper(this);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.waterNavigator = new PathNavigateSwimmer(this, worldIn);
        this.groundNavigator = new PathNavigateGround(this, worldIn);
    }

    protected void initEntityAI() {
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.tasks.addTask(1, new EntityDrowned.AIGoToWater());
        this.tasks.addTask(2, new EntityDrowned.AIAttack(this, 1.0D, false));
        this.tasks.addTask(5, new EntityDrowned.AIGoToBeach(this, 1.0D));
        this.tasks.addTask(6, new EntityDrowned.AISwimUp(this, 1.0D, this.world.getSeaLevel()));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityDrowned.class));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 10, true, false, this::shouldAttack));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityVillager.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityIronGolem.class, true));
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        if (this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty() && this.rand.nextFloat() < 0.03F) {
            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(CommonProxy.itemNautilusShell));
            this.inventoryHandsDropChances[EntityEquipmentSlot.OFFHAND.getIndex()] = 2.0F;
        }
        return livingdata;
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        if ((double)this.rand.nextFloat() > 0.9D) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
        }
    }

    private boolean wantsToSwim() {
        if (this.swimmingUp) {
            return true;
        } else {
            EntityLivingBase entitylivingbase = this.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isInWater();
        }
    }

    public void travel(float strafe, float vertical, float forward) {
        if (this.isServerWorld() && this.isInWater() && this.wantsToSwim()) {
            this.moveRelative(strafe, vertical, forward, 0.01F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9F;
            this.motionY *= 0.9F;
            this.motionZ *= 0.9F;
        } else {
            super.travel(strafe, vertical, forward);
        }

    }

    public void setSwimmingUp(boolean p_204713_1_) {
        this.swimmingUp = p_204713_1_;
    }

    @Override
    public boolean canSwim() {
        return DefaultEntitySwimming.defaultCanSwim(this);
    }

    public void updateSwimming() {
        if (!this.world.isRemote) {
            if (this.isServerWorld() && this.isInWater() && this.wantsToSwim()) {
                this.navigator = this.waterNavigator;
                this.setSwimming(true);
            } else {
                this.navigator = this.groundNavigator;
                this.setSwimming(false);
            }
        }
    }

    @Override
    public boolean isSwimming() {
        return DefaultEntitySwimming.defaultIsSwimming(this);
    }

    @Override
    public void setSwimming(boolean flag) {
        DefaultEntitySwimming.defaultSetSwimming(this, flag);
    }

    private boolean isBelowSeaLevel() {
        return this.getEntityBoundingBox().minY < (double)(this.world.getSeaLevel() - 5);
    }

    @Override
    public boolean getCanSpawnHere() {
        Biome biome = world.getBiome(new BlockPos(this.posX, this.posY, this.posZ));
        if (biome != Biomes.RIVER && biome != Biomes.FROZEN_RIVER) {
            return this.rand.nextInt(40) == 0 && this.isBelowSeaLevel() && super.getCanSpawnHere();
        } else {
            return this.rand.nextInt(15) == 0 && super.getCanSpawnHere();
        }
    }

    public boolean isNotColliding()
    {
        return this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this);
    }

    @Override
    public void setBreakDoorsAItask(boolean enabled) {
        super.setBreakDoorsAItask(false);
    }

    @Override
    public boolean isBreakDoorsTaskSet() {
        return false;
    }

    @Override
    protected ItemStack getSkullDrop() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean shouldAttack(@Nullable EntityLivingBase p_204714_1_) {
        if (p_204714_1_ != null) {
            return !this.world.isDaytime() || p_204714_1_.isInWater();
        } else {
            return false;
        }
    }

    protected boolean isCloseToPathTarget() {
        Path path = this.getNavigator().getPath();
        if (path != null) {
            PathPoint pathpoint = path.getTarget();
            if (pathpoint != null) {
                double d0 = this.getDistanceSq(pathpoint.x, pathpoint.y, pathpoint.z);
                if (d0 < 4.0D) {
                    return true;
                }
            }
        }

        return false;
    }

    static class AIAttack extends EntityAIZombieAttack {
        private final EntityDrowned field_204726_g;

        public AIAttack(EntityDrowned p_i48913_1_, double p_i48913_2_, boolean p_i48913_4_) {
            super(p_i48913_1_, p_i48913_2_, p_i48913_4_);
            this.field_204726_g = p_i48913_1_;
        }

        public boolean shouldExecute() {
            return super.shouldExecute() && this.field_204726_g.shouldAttack(this.field_204726_g.getAttackTarget());
        }

        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && this.field_204726_g.shouldAttack(this.field_204726_g.getAttackTarget());
        }
    }

    static class AIGoToBeach extends EntityAIMoveToBlock {
        private final EntityDrowned drowned;

        public AIGoToBeach(EntityDrowned p_i48911_1_, double p_i48911_2_) {
            super(p_i48911_1_, p_i48911_2_, 8);
            this.drowned = p_i48911_1_;
        }

        public boolean shouldExecute() {
            return super.shouldExecute() && !this.drowned.world.isDaytime() && this.drowned.isInWater() && this.drowned.posY >= (double)(this.drowned.world.getSeaLevel() - 3);
        }

        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting();
        }

        protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
            BlockPos blockpos = pos.up();
            return worldIn.isAirBlock(blockpos) && worldIn.isAirBlock(blockpos.up()) && worldIn.getBlockState(pos).isTopSolid();
        }

        public void startExecuting() {
            this.drowned.setSwimmingUp(false);
            this.drowned.navigator = this.drowned.groundNavigator;
            super.startExecuting();
        }

        public void resetTask() {
            super.resetTask();
        }
    }

    class AIGoToWater extends EntityAIBase {
        private double movX;
        private double movY;
        private double movZ;

        public AIGoToWater() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            if (!world.isDaytime()) {
                return false;
            } else if (isInWater()) {
                return false;
            } else {
                Vec3d vec3d = this.getTargetPosition();
                if (vec3d == null) {
                    return false;
                } else {
                    this.movX = vec3d.x;
                    this.movY = vec3d.y;
                    this.movZ = vec3d.z;
                    return true;
                }
            }
        }

        public boolean shouldContinueExecuting() {
            return !getNavigator().noPath();
        }

        public void startExecuting() {
            getNavigator().tryMoveToXYZ(this.movX, this.movY, this.movZ, 1.0);
        }

        @Nullable
        private Vec3d getTargetPosition() {
            Random random = getRNG();
            BlockPos blockpos = new BlockPos(posX, getEntityBoundingBox().minY, posZ);

            for(int i = 0; i < 10; ++i) {
                BlockPos block = blockpos.add(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
                if (FluidloggedUtils.getFluidState(world, block).getFluid() == FluidRegistry.WATER) {
                    return new Vec3d(block.getX(), block.getY(), block.getZ());
                }
            }

            return null;
        }
    }

    static class AISwimUp extends EntityAIBase {
        private final EntityDrowned drowned;
        private final double speed;
        private final int targetY;
        private boolean obstructed;

        public AISwimUp(EntityDrowned drowned, double speed, int targetY) {
            this.drowned = drowned;
            this.speed = speed;
            this.targetY = targetY;
        }

        public boolean shouldExecute() {
            return !this.drowned.world.isDaytime() && this.drowned.isInWater() && this.drowned.posY < (double)(this.targetY - 2);
        }

        public boolean shouldContinueExecuting() {
            return this.shouldExecute() && !this.obstructed;
        }

        public void updateTask() {
            if (this.drowned.posY < (double)(this.targetY - 1) && (this.drowned.getNavigator().noPath() || this.drowned.isCloseToPathTarget())) {
                Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.drowned, 4, 8, new Vec3d(this.drowned.posX, this.targetY - 1, this.drowned.posZ));
                if (vec3d == null) {
                    this.obstructed = true;
                    return;
                }

                this.drowned.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, this.speed);
            }

        }

        public void startExecuting() {
            this.drowned.setSwimmingUp(true);
            this.obstructed = false;
        }

        public void resetTask() {
            this.drowned.setSwimmingUp(false);
        }
    }

    static class MoveHelper extends EntityMoveHelper {
        private final EntityDrowned drowned;

        public MoveHelper(EntityDrowned drowned) {
            super(drowned);
            this.drowned = drowned;
        }

        public void onUpdateMoveHelper() {
            EntityLivingBase entitylivingbase = this.drowned.getAttackTarget();
            if (this.drowned.wantsToSwim() && this.drowned.isInWater()) {
                if (entitylivingbase != null && entitylivingbase.posY > this.drowned.posY || this.drowned.swimmingUp) {
                    this.drowned.motionY += 0.002D;
                }

                if (this.action != EntityMoveHelper.Action.MOVE_TO || this.drowned.getNavigator().noPath()) {
                    this.drowned.setAIMoveSpeed(0.0F);
                    return;
                }

                double d0 = this.posX - this.drowned.posX;
                double d1 = this.posY - this.drowned.posY;
                double d2 = this.posZ - this.drowned.posZ;
                double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 = d1 / d3;
                float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.drowned.rotationYaw = this.limitAngle(this.drowned.rotationYaw, f, 90.0F);
                this.drowned.renderYawOffset = this.drowned.rotationYaw;
                float f1 = (float)(this.speed * this.drowned.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                this.drowned.setAIMoveSpeed(this.drowned.getAIMoveSpeed() + (f1 - this.drowned.getAIMoveSpeed()) * 0.125F);
                this.drowned.motionY += (double)this.drowned.getAIMoveSpeed() * d1 * 0.1D;
                this.drowned.motionX += (double)this.drowned.getAIMoveSpeed() * d0 * 0.005D;
                this.drowned.motionZ += (double)this.drowned.getAIMoveSpeed() * d2 * 0.005D;
            } else {
                if (!this.drowned.onGround) {
                    this.drowned.motionY -= 0.008D;
                }

                super.onUpdateMoveHelper();
            }

        }
    }
}
