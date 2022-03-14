package com.fuzs.aquaacrobatics.tile;

import com.fuzs.aquaacrobatics.proxy.CommonProxy;
import com.google.common.collect.Lists;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TileEntityConduit extends TileEntity implements ITickable {
    public int ticksExisted;
    private float activeRotation;
    private boolean active;
    private boolean eyeOpen;
    private final List<BlockPos> prismarinePositions = Lists.newArrayList();
    private EntityLivingBase target;
    private UUID targetUuid;

    private static final Block[] SURROUDING_BLOCKS = new Block[]{
            Blocks.PRISMARINE,
            Blocks.SEA_LANTERN
    };

    public TileEntityConduit() {

    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
        NBTTagCompound tag = pkt.getNbtCompound();
        this.readFromNBT(tag);
    }

    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        if (this.target != null) {
            compound.setString("target_uuid", this.target.getUniqueID().toString());
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("target_uuid")) {
            this.targetUuid = UUID.fromString(compound.getString("target_uuid"));
        } else {
            this.targetUuid = null;
        }
    }


    private void addEffectsToPlayers() {
        int i = this.prismarinePositions.size();
        int j = i / 7 * 16;
        int k = this.pos.getX();
        int l = this.pos.getY();
        int i1 = this.pos.getZ();
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(k, l, i1, k + 1, l + 1, i1 + 1)).grow(j).expand(0.0D, this.world.getHeight(), 0.0D);
        List<EntityPlayer> list = this.world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
        if (!list.isEmpty()) {
            for(EntityPlayer entityplayer : list) {
                if (this.pos.getDistance(MathHelper.floor(entityplayer.posX), MathHelper.floor(entityplayer.posY), MathHelper.floor(entityplayer.posZ)) <= (double)j && entityplayer.isWet()) {
                    entityplayer.addPotionEffect(new PotionEffect(CommonProxy.effectConduitPower, 260, 0, true, true));
                }
            }
        }
    }

    private void attackMobs() {
        EntityLivingBase entitylivingbase = this.target;
        int i = this.prismarinePositions.size();
        if (i < 42) {
            this.target = null;
        } else if (this.target == null && this.targetUuid != null) {
            this.target = this.findExistingTarget();
            this.targetUuid = null;
        } else if (this.target == null) {
            List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getAreaOfEffect(), (entity) -> {
                return entity instanceof IMob && entity.isWet();
            });
            if (!list.isEmpty()) {
                this.target = list.get(this.world.rand.nextInt(list.size()));
            }
        } else if (!this.target.isEntityAlive() || this.pos.getDistance(MathHelper.floor(this.target.posX), MathHelper.floor(this.target.posY), MathHelper.floor(this.target.posZ)) > 8.0D) {
            this.target = null;
        }

        if (this.target != null) {
            this.target.attackEntityFrom(DamageSource.MAGIC, 4.0F);
        }

        if (entitylivingbase != this.target) {
            IBlockState iblockstate = this.blockType.getDefaultState();
            this.world.notifyBlockUpdate(this.pos, iblockstate, iblockstate, 2);
        }

    }

    private void updateClientTarget() {
        if (this.targetUuid == null) {
            this.target = null;
        } else if (this.target == null || !this.target.getUniqueID().equals(this.targetUuid)) {
            this.target = this.findExistingTarget();
            if (this.target == null) {
                this.targetUuid = null;
            }
        }

    }

    @Nullable
    private EntityLivingBase findExistingTarget() {
        List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getAreaOfEffect(), (entity) -> {
            return entity.getUniqueID().equals(this.targetUuid);
        });
        return list.size() == 1 ? list.get(0) : null;
    }

    private AxisAlignedBB getAreaOfEffect() {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        return (new AxisAlignedBB(i, j, k, i + 1, j + 1, k + 1)).grow(8.0D);
    }

    @Override
    public void update() {
        ++this.ticksExisted;
        long i = this.world.getWorldTime();
        if (i % 40L == 0L) {
            this.setActive(this.shouldBeActive());
            if (!this.world.isRemote && this.isActive()) {
                this.addEffectsToPlayers();
                this.attackMobs();
            }
        }

        if (this.world.isRemote) {
            this.updateClientTarget();
            this.spawnParticles();
            if (this.isActive()) {
                ++this.activeRotation;
            }
        }
    }

    private void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }

    @SideOnly(Side.CLIENT)
    public boolean isEyeOpen() {
        return this.eyeOpen;
    }

    private void setEyeOpen(boolean p_207736_1_) {
        this.eyeOpen = p_207736_1_;
    }

    @SideOnly(Side.CLIENT)
    public float getActiveRotation(float p_205036_1_) {
        return (this.activeRotation + p_205036_1_) * -0.0375F;
    }

    private void spawnParticles() {
        Random random = this.world.rand;
        float f = MathHelper.sin((float)(this.ticksExisted + 35) * 0.1F) / 2.0F + 0.5F;
        f = (f * f + f) * 0.3F;
        Vec3d vec3d = new Vec3d((float)this.pos.getX() + 0.5F, (float)this.pos.getY() + 1.5F + f, (float)this.pos.getZ() + 0.5F);

        for(BlockPos blockpos : this.prismarinePositions) {
            if (random.nextInt(50) == 0) {
                float f1 = -0.5F + random.nextFloat();
                float f2 = -2.0F + random.nextFloat();
                float f3 = -0.5F + random.nextFloat();
                BlockPos blockpos1 = blockpos.subtract(this.pos);
                Vec3d vec3d1 = (new Vec3d(f1, f2, f3)).add(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, vec3d.x, vec3d.y, vec3d.z, vec3d1.x, vec3d1.y, vec3d1.z);
            }
        }

        if (this.target != null) {
            Vec3d vec3d2 = new Vec3d(this.target.posX, this.target.posY + (double)this.target.getEyeHeight(), this.target.posZ);
            float f4 = (-0.5F + random.nextFloat()) * (3.0F + this.target.width);
            float f5 = -1.0F + random.nextFloat() * this.target.height;
            float f6 = (-0.5F + random.nextFloat()) * (3.0F + this.target.width);
            Vec3d vec3d3 = new Vec3d(f4, f5, f6);
            this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, vec3d2.x, vec3d2.y, vec3d2.z, vec3d3.x, vec3d3.y, vec3d3.z);
        }

    }

    private boolean shouldBeActive() {
        this.prismarinePositions.clear();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                for(int k = -1; k <= 1; ++k) {
                    BlockPos blockpos = this.pos.add(i, j, k);
                    if (FluidloggedUtils.getFluidState(this.world, blockpos).getFluid() != FluidRegistry.WATER) {
                        return false;
                    }
                }
            }
        }

        for(int j1 = -2; j1 <= 2; ++j1) {
            for(int k1 = -2; k1 <= 2; ++k1) {
                for(int l1 = -2; l1 <= 2; ++l1) {
                    int i2 = Math.abs(j1);
                    int l = Math.abs(k1);
                    int i1 = Math.abs(l1);
                    if ((i2 > 1 || l > 1 || i1 > 1) && (j1 == 0 && (l == 2 || i1 == 2) || k1 == 0 && (i2 == 2 || i1 == 2) || l1 == 0 && (i2 == 2 || l == 2))) {
                        BlockPos blockpos1 = this.pos.add(j1, k1, l1);
                        IBlockState iblockstate = this.world.getBlockState(blockpos1);

                        for(Block block : SURROUDING_BLOCKS) {
                            if (iblockstate.getBlock() == block) {
                                this.prismarinePositions.add(blockpos1);
                            }
                        }
                    }
                }
            }
        }

        this.setEyeOpen(this.prismarinePositions.size() >= 42);
        return this.prismarinePositions.size() >= 16;
    }
}
