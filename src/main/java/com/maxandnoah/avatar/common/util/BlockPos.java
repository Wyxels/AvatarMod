package com.maxandnoah.avatar.common.util;

import io.netty.buffer.ByteBuf;

public class BlockPos {
	
	public int x, y, z;
	
	public BlockPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}
	
	public static BlockPos fromBytes(ByteBuf buf) {
		return new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	public double dist(BlockPos pos) {
		return dist(this, pos);
	}
	
	public double distSq(BlockPos pos) {
		return distSq(this, pos);
	}
	
	public static double dist(BlockPos pos1, BlockPos pos2) {
		return Math.sqrt(distSq(pos1, pos2));
	}
	
	public static double distSq(BlockPos pos1, BlockPos pos2) {
		double dx = pos2.x - pos1.x;
		double dy = pos2.y - pos1.y;
		double dz = pos2.z - pos1.z;
		return dx * dx + dy * dy + dz * dz;
	}
	
}
