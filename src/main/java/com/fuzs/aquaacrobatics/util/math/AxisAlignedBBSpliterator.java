package com.fuzs.aquaacrobatics.util.math;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class AxisAlignedBBSpliterator extends Spliterators.AbstractSpliterator<AxisAlignedBB> {

    @Nullable
    private final Entity entity;
    private final AxisAlignedBB aabb;
    private final CubeCoordinateIterator cubeCoordinateIterator;
    private final World reader;
    private boolean isEntityPresent;
    private final BiPredicate<IBlockState, BlockPos> statePositionPredicate;

    public AxisAlignedBBSpliterator(World reader, @Nullable Entity entity, AxisAlignedBB aabb) {
        
        this(reader, entity, aabb, (state, pos) -> true);
    }

    public AxisAlignedBBSpliterator(World reader, @Nullable Entity entity, AxisAlignedBB aabb, BiPredicate<IBlockState, BlockPos> statePositionPredicate) {
        
        super(Long.MAX_VALUE, Spliterator.NONNULL | Spliterator.IMMUTABLE);
        this.reader = reader;
        this.isEntityPresent = entity != null;
        this.entity = entity;
        this.aabb = aabb;
        this.statePositionPredicate = statePositionPredicate;
        int startX = MathHelper.floor(aabb.minX - 1.0E-7D) - 1;
        int endX = MathHelper.floor(aabb.maxX + 1.0E-7D) + 1;
        int startY = MathHelper.floor(aabb.minY - 1.0E-7D) - 1;
        int heightY = MathHelper.floor(aabb.maxY + 1.0E-7D) + 1;
        int startZ = MathHelper.floor(aabb.minZ - 1.0E-7D) - 1;
        int endZ = MathHelper.floor(aabb.maxZ + 1.0E-7D) + 1;
        this.cubeCoordinateIterator = new CubeCoordinateIterator(startX, startY, startZ, endX, heightY, endZ);
    }

    public boolean tryAdvance(Consumer<? super AxisAlignedBB> consumer) {
        
        return this.isEntityPresent && this.isEntityOutsideOfBorder(consumer) || this.isAABBColliding(consumer);
    }

    private boolean isAABBColliding(Consumer<? super AxisAlignedBB> consumer) {

        BlockPos.PooledMutableBlockPos mutablePos = BlockPos.PooledMutableBlockPos.retain();
        while (this.cubeCoordinateIterator.hasNext()) {

            int x = this.cubeCoordinateIterator.getX();
            int y = this.cubeCoordinateIterator.getY();
            int z = this.cubeCoordinateIterator.getZ();

            int boundariesTouched = this.cubeCoordinateIterator.numBoundariesTouched();
            if (boundariesTouched == 3) {

                continue;
            }

            mutablePos.setPos(x, y, z);
            if (!this.reader.isBlockLoaded(mutablePos)) {

                continue;
            }

            // piston check is new, not sure if it really helps in this version
            IBlockState blockstate = this.reader.getBlockState(mutablePos);
            if (!this.statePositionPredicate.test(blockstate, mutablePos) || boundariesTouched == 2 && blockstate.getBlock() != Blocks.PISTON_EXTENSION) {

                continue;
            }

            // just needs to be non-null for our purpose
            AxisAlignedBB collisionBoundingBox = blockstate.getCollisionBoundingBox(this.reader, mutablePos);
            if (blockstate.isFullCube()) {

                // second check probably not necessary
                AxisAlignedBB aabbOffset = Block.FULL_BLOCK_AABB.offset(x, y, z);
                if (!this.aabb.intersects(aabbOffset) || this.entity != null && !this.entity.getEntityBoundingBox().intersects(aabbOffset)) {

                    continue;
                }

                consumer.accept(collisionBoundingBox);
                mutablePos.release();
                return true;
            }

            List<AxisAlignedBB> collidingBoxes = Lists.newArrayList();
            this.getCollisionBoxList(collidingBoxes, blockstate, mutablePos);
            if (collidingBoxes.isEmpty()) {

                continue;
            }

            consumer.accept(collisionBoundingBox);
            mutablePos.release();
            return true;
        }

        mutablePos.release();
        return false;
    }

    private void getCollisionBoxList(List<AxisAlignedBB> collidingBoxes, IBlockState blockstate, BlockPos.PooledMutableBlockPos mutablePos) {

        // only retain boxes colliding with both the area of interest and the current entity if present
        blockstate.addCollisionBoxToList(this.reader, mutablePos, this.aabb, collidingBoxes, this.entity, false);
        if (this.entity != null) {

            List<AxisAlignedBB> entityCollidingBoxes = Lists.newArrayList();
            blockstate.addCollisionBoxToList(this.reader, mutablePos, this.entity.getEntityBoundingBox(), entityCollidingBoxes, this.entity, false);
            collidingBoxes.retainAll(entityCollidingBoxes);
        }
    }

    private boolean isEntityOutsideOfBorder(Consumer<? super AxisAlignedBB> consumer) {
        
        Objects.requireNonNull(this.entity);
        this.isEntityPresent = false;
        WorldBorder worldborder = this.reader.getWorldBorder();
        AxisAlignedBB axisalignedbb = this.entity.getEntityBoundingBox();
        if (!isBoundingBoxWithinBorder(worldborder, axisalignedbb)) {

            AxisAlignedBB borderShape = new AxisAlignedBB(worldborder.minX(), Double.NEGATIVE_INFINITY, worldborder.minZ(), worldborder.maxX(), Double.POSITIVE_INFINITY, worldborder.maxZ());
            consumer.accept(borderShape);
            return true;
        }

        return false;
    }

    public static boolean isBoundingBoxWithinBorder(WorldBorder worldBorder, AxisAlignedBB entityBoundingBox) {
        
        double minX = MathHelper.floor(worldBorder.minX());
        double minZ = MathHelper.floor(worldBorder.minZ());
        double maxX = MathHelper.ceil(worldBorder.maxX());
        double maxZ = MathHelper.ceil(worldBorder.maxZ());
        return entityBoundingBox.minX > minX && entityBoundingBox.minX < maxX && entityBoundingBox.minZ > minZ && entityBoundingBox.minZ < maxZ && entityBoundingBox.maxX > minX && entityBoundingBox.maxX < maxX && entityBoundingBox.maxZ > minZ && entityBoundingBox.maxZ < maxZ;
    }

}
