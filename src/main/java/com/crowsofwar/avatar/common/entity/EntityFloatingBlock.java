package com.crowsofwar.avatar.common.entity;

import static net.minecraft.network.datasync.EntityDataManager.createKey;

import java.util.List;
import java.util.Random;

import com.crowsofwar.avatar.common.entity.EntityFloatingBlock.OnBlockLand;
import com.crowsofwar.avatar.common.util.AvatarDataSerializers;
import com.crowsofwar.gorecore.util.Vector;
import com.google.common.base.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFloatingBlock extends Entity implements IPhysics {
	// EntitySkeleton
	
	/*
	 * 
	 * Block#getIdFromBlock, Block#getBlockById
	 * 
	 * 
	 */
	
	public static final Block DEFAULT_BLOCK = Blocks.STONE;
	
	private static final DataParameter<Boolean> SYNC_GRAVITY_ENABLED = createKey(EntityFloatingBlock.class,
			DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> SYNC_ENTITY_ID = createKey(EntityFloatingBlock.class,
			DataSerializers.VARINT);
	private static final DataParameter<Vector> SYNC_VELOCITY = createKey(EntityFloatingBlock.class,
			AvatarDataSerializers.SERIALIZER_VECTOR);
	private static final DataParameter<Float> SYNC_FRICTION = createKey(EntityFloatingBlock.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Boolean> SYNC_CAN_FALL = createKey(EntityFloatingBlock.class,
			DataSerializers.BOOLEAN);
	private static final DataParameter<Byte> SYNC_ON_LAND = createKey(EntityFloatingBlock.class,
			DataSerializers.BYTE);
	private static final DataParameter<Optional<IBlockState>> SYNC_BLOCK = createKey(
			EntityFloatingBlock.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	
	private static final DataParameter<FloatingBlockBehavior> SYNC_BEHAVIOR = createKey(
			EntityFloatingBlock.class, FloatingBlockBehavior.DATA_SERIALIZER);
	
	private static int nextBlockID = 0;
	
	/**
	 * Holds the current velocity. Please don't use this field directly, as it is designed to be
	 * synced. Use {@link #getVelocity()} and {@link #setVelocity(Vector)}.
	 */
	private Vector velocity;
	private final Vector internalPosition;
	
	private EntityPlayer owner;
	
	/**
	 * Whether or not to drop an ItemBlock when the floating block has been destroyed. Does not
	 * matter on client.
	 */
	private boolean enableItemDrops;
	
	public EntityFloatingBlock(World world) {
		super(world);
		setSize(0.95f, 0.95f);
		velocity = new Vector(0, 0, 0);
		setGravityEnabled(false);
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			setID(nextBlockID++);
		}
		this.internalPosition = new Vector(0, 0, 0);
		
		this.enableItemDrops = true;
		
	}
	
	public EntityFloatingBlock(World world, IBlockState blockState) {
		this(world);
		setBlockState(blockState);
	}
	
	public EntityFloatingBlock(World world, IBlockState blockState, EntityPlayer owner) {
		this(world, blockState);
		setOwner(owner);
	}
	
	// Called from constructor of Entity class
	@Override
	protected void entityInit() {
		
		dataManager.register(SYNC_GRAVITY_ENABLED, false);
		dataManager.register(SYNC_ENTITY_ID, 0);
		dataManager.register(SYNC_VELOCITY, Vector.ZERO);
		dataManager.register(SYNC_FRICTION, 1f);
		dataManager.register(SYNC_CAN_FALL, false);
		dataManager.register(SYNC_ON_LAND, OnBlockLand.DO_NOTHING.getId());
		dataManager.register(SYNC_BLOCK, Optional.of(DEFAULT_BLOCK.getDefaultState()));
		dataManager.register(SYNC_BEHAVIOR, new FloatingBlockBehavior.DoNothing());
		
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		setBlockState(
				Block.getBlockById(nbt.getInteger("BlockId")).getStateFromMeta(nbt.getInteger("Metadata")));
		setGravityEnabled(nbt.getBoolean("Gravity"));
		setVelocity(nbt.getDouble("VelocityX"), nbt.getDouble("VelocityY"), nbt.getDouble("VelocityZ"));
		setFriction(nbt.getFloat("Friction"));
		setCanFall(nbt.getBoolean("CanFall"));
		setOnLandBehavior(nbt.getByte("OnLand"));
		setItemDropsEnabled(nbt.getBoolean("DropItems"));
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setInteger("BlockId", Block.getIdFromBlock(getBlock()));
		nbt.setInteger("Metadata", getBlock().getMetaFromState(getBlockState()));
		nbt.setBoolean("Gravity", isGravityEnabled());
		Vector velocity = getVelocity();
		nbt.setDouble("VelocityX", velocity.x());
		nbt.setDouble("VelocityY", velocity.y());
		nbt.setDouble("VelocityZ", velocity.z());
		nbt.setFloat("Friction", getFriction());
		nbt.setBoolean("CanFall", canFall());
		nbt.setByte("OnLand", getOnLandBehaviorId());
		nbt.setBoolean("DropItems", areItemDropsEnabled());
	}
	
	public Block getBlock() {
		return getBlockState().getBlock();
	}
	
	public void setBlock(Block block) {
		setBlockState(block.getDefaultState());
	}
	
	public IBlockState getBlockState() {
		Optional<IBlockState> obs = dataManager.get(SYNC_BLOCK);
		return obs.get();
	}
	
	public void setBlockState(IBlockState state) {
		dataManager.set(SYNC_BLOCK, Optional.of(state));
	}
	
	public boolean isGravityEnabled() {
		return dataManager.get(SYNC_GRAVITY_ENABLED);
	}
	
	public void setGravityEnabled(boolean gravity) {
		if (!worldObj.isRemote) dataManager.set(SYNC_GRAVITY_ENABLED, gravity);
	}
	
	/**
	 * Get the ID of this floating block. Each instance has its own unique ID. Synced between client
	 * and server.
	 */
	public int getID() {
		return dataManager.get(SYNC_ENTITY_ID);
	}
	
	public void setID(int id) {
		if (!worldObj.isRemote) dataManager.set(SYNC_ENTITY_ID, id);
	}
	
	public static EntityFloatingBlock getFromID(World world, int id) {
		for (int i = 0; i < world.loadedEntityList.size(); i++) {
			Entity e = (Entity) world.loadedEntityList.get(i);
			if (e instanceof EntityFloatingBlock && ((EntityFloatingBlock) e).getID() == id)
				return (EntityFloatingBlock) e;
		}
		return null;
	}
	
	/**
	 * Returns whether the floating block drops the block as an item when it is destroyed. Only used
	 * on server-side. By default, is true.
	 */
	public boolean areItemDropsEnabled() {
		return enableItemDrops;
	}
	
	/**
	 * Set whether the block should be dropped when it is destroyed.
	 */
	public void setItemDropsEnabled(boolean enable) {
		this.enableItemDrops = enable;
	}
	
	/**
	 * Disable dropping an item when the floating block is destroyed.
	 */
	public void disableItemDrops() {
		setItemDropsEnabled(false);
	}
	
	private void spawnCrackParticle(double x, double y, double z, double mx, double my, double mz) {
		worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x, y, z, mx, my, mz,
				Block.getStateId(getBlockState()));
	}
	
	@Override
	public void onUpdate() {
		if (isGravityEnabled()) {
			addVelocity(new Vector(0, -9.81 / 20, 0));
			Vector vel = getVelocity();
			if (!canFall() && vel.y() < 0) {
				vel.setY(0);
				setVelocity(vel);
			}
		}
		
		if (ticksExisted == 1) {
			
			for (int i = 0; i < 10; i++) {
				double spawnX = posX + (rand.nextDouble() - 0.5);
				double spawnY = posY - 0;
				double spawnZ = posZ + (rand.nextDouble() - 0.5);
				spawnCrackParticle(spawnX, spawnY, spawnZ, 0, -0.1, 0);
			}
			
		}
		
		if (!worldObj.isRemote) setVelocity(getVelocity().times(getFriction()));
		
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		Vector velocity = getVelocity();
		moveEntity(velocity.x() / 20, velocity.y() / 20, velocity.z() / 20);
		motionX = velocity.x() / 20;
		motionY = velocity.y() / 20;
		motionZ = velocity.z() / 20;
		
		int x = (int) Math.floor(posX);
		int y = (int) Math.floor(posY);
		int z = (int) Math.floor(posZ);
		
	}
	
	/**
	 * Called when the block collides with another block or an entity
	 */
	public void onCollision() {
		// Spawn particles
		Random random = new Random();
		for (int i = 0; i < 7; i++) {
			spawnCrackParticle(posX, posY + 0.3, posZ, random.nextGaussian() * 0.1,
					random.nextGaussian() * 0.1, random.nextGaussian() * 0.1);
		}
		
		if (!worldObj.isRemote && areItemDropsEnabled()) {
			List<ItemStack> drops = getBlock().getDrops(worldObj, new BlockPos(this), getBlockState(), 0);
			for (ItemStack is : drops) {
				EntityItem ei = new EntityItem(worldObj, posX, posY, posZ, is);
				worldObj.spawnEntityInWorld(ei);
			}
		}
	}
	
	@Override
	public void addVelocity(Vector force) {
		setVelocity(getVelocity().plus(force));
	}
	
	@Override
	public Vector getVelocity() {
		velocity = dataManager.get(SYNC_VELOCITY);
		return velocity;
	}
	
	@Override
	public void setVelocity(Vector velocity) {
		if (!worldObj.isRemote) {
			dataManager.set(SYNC_VELOCITY, velocity);
		}
	}
	
	@Override
	public Vector getVecPosition() {
		internalPosition.setX(posX);
		internalPosition.setY(posY);
		internalPosition.setZ(posZ);
		return internalPosition;
	}
	
	public float getFriction() {
		return dataManager.get(SYNC_FRICTION);
	}
	
	public void setFriction(float friction) {
		if (!worldObj.isRemote) dataManager.set(SYNC_FRICTION, friction);
	}
	
	public boolean canFall() {
		return dataManager.get(SYNC_CAN_FALL);
	}
	
	public void setCanFall(boolean falls) {
		dataManager.set(SYNC_CAN_FALL, falls);
	}
	
	public byte getOnLandBehaviorId() {
		return dataManager.get(SYNC_ON_LAND);
	}
	
	public OnBlockLand getOnLandBehavior() {
		return OnBlockLand.getFromId(getOnLandBehaviorId());
	}
	
	public void setOnLandBehavior(byte id) {
		dataManager.set(SYNC_ON_LAND, id);
	}
	
	public void setOnLandBehavior(OnBlockLand onLand) {
		setOnLandBehavior(onLand.getId());
	}
	
	public boolean canBeDestroyed() {
		return getOnLandBehavior() == OnBlockLand.BREAK;
	}
	
	/**
	 * Drop the block - enable gravity, can fall, and can be destroyed.
	 */
	public void drop() {
		setGravityEnabled(true);
		setCanFall(true);
		setOnLandBehavior(OnBlockLand.BREAK);
	}
	
	public EntityPlayer getOwner() {
		return owner;
	}
	
	public void setOwner(EntityPlayer owner) {
		this.owner = owner;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double d) {
		return true;
	}
	
}
