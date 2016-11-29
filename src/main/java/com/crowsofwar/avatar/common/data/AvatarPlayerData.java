/* 
  This file is part of AvatarMod.
    
  AvatarMod is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  AvatarMod is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with AvatarMod. If not, see <http://www.gnu.org/licenses/>.
*/

package com.crowsofwar.avatar.common.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.crowsofwar.avatar.AvatarLog;
import com.crowsofwar.avatar.AvatarLog.WarningType;
import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.bending.BendingManager;
import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.network.Networker;
import com.crowsofwar.avatar.common.network.Networker.Property;
import com.crowsofwar.avatar.common.network.Transmitters;
import com.crowsofwar.avatar.common.network.packets.PacketCPlayerData;
import com.crowsofwar.avatar.common.util.AvatarUtils;
import com.crowsofwar.gorecore.data.DataSaver;
import com.crowsofwar.gorecore.data.DataSaverDontSave;
import com.crowsofwar.gorecore.data.PlayerData;
import com.crowsofwar.gorecore.data.PlayerDataFetcher;
import com.crowsofwar.gorecore.data.PlayerDataFetcherServer;
import com.crowsofwar.gorecore.data.PlayerDataFetcherSided;
import com.google.common.collect.ImmutableList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class AvatarPlayerData extends PlayerData {
	
	// TODO change player data lists into sets, when applicable
	
	public static final Networker.Property<List<BendingController>> KEY_CONTROLLERS = new Property<>(1);
	public static final Networker.Property<List<BendingState>> KEY_STATES = new Property<>(2);
	public static final Networker.Property<Map<BendingAbility, AbilityData>> KEY_ABILITY_DATA = new Property<>(
			3);
	public static final Networker.Property<Set<StatusControl>> KEY_STATUS_CONTROLS = new Property<>(4);
	
	private static PlayerDataFetcher<AvatarPlayerData> fetcher;
	
	private Map<BendingType, BendingController> bendingControllers;
	private List<BendingController> bendingControllerList;
	
	private Map<BendingType, BendingState> bendingStates;
	private List<BendingState> bendingStateList;
	
	private Set<StatusControl> statusControls;
	
	private Map<BendingAbility, AbilityData> abilityData;
	
	private PlayerState state;
	
	private final Networker networker;
	private final boolean isClient;
	
	public AvatarPlayerData(DataSaver dataSaver, UUID playerID, EntityPlayer player) {
		super(dataSaver, playerID, player);
		isClient = dataSaver instanceof DataSaverDontSave;
		bendingControllers = new HashMap<BendingType, BendingController>();
		bendingControllerList = new ArrayList<BendingController>();
		bendingStates = new HashMap<BendingType, BendingState>();
		bendingStateList = new ArrayList<BendingState>();
		statusControls = new HashSet<>();
		abilityData = new HashMap<>();
		state = new PlayerState();
		
		networker = new Networker(!isClient, PacketCPlayerData.class, net -> new PacketCPlayerData(net, playerID));
		networker.register(bendingControllerList, Transmitters.CONTROLLER_LIST, KEY_CONTROLLERS);
		networker.register(bendingStateList, Transmitters.STATE_LIST, KEY_STATES);
		networker.register(abilityData, Transmitters.ABILITY_DATA_MAP, KEY_ABILITY_DATA);
		networker.register(statusControls, Transmitters.STATUS_CONTROLS, KEY_STATUS_CONTROLS);
		
	}
	
	@Override
	protected void readPlayerDataFromNBT(NBTTagCompound readFrom) {
		
		AvatarPlayerData playerData = this;
		AvatarUtils.readList(bendingControllerList,
				compound -> BendingController.find(compound.getInteger("ControllerID")), readFrom,
				"BendingControllers");
		
		AvatarUtils.readList(bendingStateList, compound -> {
			
			return BendingState.find(playerData, compound);
			
		}, readFrom, "BendingStates");
		
		bendingControllers.clear();
		for (BendingController controller : bendingControllerList) {
			bendingControllers.put(controller.getType(), controller);
		}
		
		bendingStates.clear();
		for (BendingState state : bendingStateList) {
			bendingStates.put(state.getType(), state);
		}
		
		AvatarUtils.readList(statusControls, nbtTag -> StatusControl.lookup(nbtTag.getInteger("Id")),
				readFrom, "StatusControls");
		
		AvatarUtils.readMap(abilityData, nbt -> BendingManager.getAbility(nbt.getInteger("Id")), nbt -> {
			BendingAbility ability = BendingManager.getAbility(nbt.getInteger("AbilityId"));
			AbilityData data = new AbilityData(this, ability);
			data.readFromNbt(nbt);
			return data;
		}, readFrom, "AbilityData");
		
	}
	
	@Override
	protected void writePlayerDataToNBT(NBTTagCompound writeTo) {
		
		AvatarUtils.writeList(bendingControllerList,
				(compound, controller) -> compound.setInteger("ControllerID", controller.getID()), writeTo,
				"BendingControllers");
		AvatarUtils.writeList(bendingStateList, (compound, bendingState) -> bendingState.write(compound),
				writeTo, "BendingStates");
		AvatarUtils.writeList(statusControls, (nbtTag, control) -> nbtTag.setInteger("Id", control.id()),
				writeTo, "StatusControls");
		
		AvatarUtils.writeMap(abilityData, (nbt, ability) -> nbt.setInteger("Id", ability.getId()),
				(nbt, data) -> {
					nbt.setInteger("AbilityId", data.getAbility().getId());
					data.writeToNbt(nbt);
				}, writeTo, "AbilityData");
		
	}
	
	@Override
	protected void saveChanges() {
		super.saveChanges();
	}
	
	/**
	 * Synchronizes all <b>changed</b> values with the client.
	 */
	public void sync() {
		networker.sendUpdated();
	}
	
	public boolean isBender() {
		return !bendingControllers.isEmpty();
	}
	
	/**
	 * Check if the player has that type of bending
	 * 
	 * @deprecated Use {@link #hasBending(BendingType)}
	 */
	@Deprecated
	public boolean hasBending(int id) {
		return hasBending(BendingType.find(id));
	}
	
	/**
	 * Check if the player has that type of bending
	 */
	public boolean hasBending(BendingType type) {
		return bendingControllers.containsKey(type);
	}
	
	/**
	 * If the bending controller is not already present, adds the bending
	 * controller.
	 * <p>
	 * Also adds the state if it isn't present.
	 */
	public void addBending(BendingController bending) {
		if (!hasBending(bending.getType())) {
			bendingControllers.put(bending.getType(), bending);
			bendingControllerList.add(bending);
			networker.markChanged(KEY_CONTROLLERS, bendingControllerList);
			saveChanges();
		} else {
			AvatarLog.warn(WarningType.INVALID_CODE,
					"Cannot add BendingController " + bending + "' because player already has instance.");
		}
		
		if (!hasBendingState(bending)) {
			addBendingState(bending.createState(this));
			saveChanges();
		}
	}
	
	/**
	 * If the bending controller is not already present, adds the bending
	 * controller.
	 */
	public void addBending(BendingType type) {
		addBending(BendingManager.getBending(type));
	}
	
	/**
	 * If the bending controller is not already present, adds the bending
	 * controller.
	 * 
	 * @deprecated Use {@link #addBending(BendingType)}
	 */
	@Deprecated
	public void addBending(int bendingID) {
		addBending(BendingManager.getBending(bendingID));
	}
	
	/**
	 * Remove the specified bending controller and its associated state. Please
	 * note, this will be saved, so is permanent (unless another bending
	 * controller is added).
	 */
	public void removeBending(BendingController bending) {
		if (hasBending(bending.getType())) {
			// remove state before controller- getBendingState only works with
			// controller present
			BendingState state = getBendingState(bending);
			removeBendingState(state);
			bendingControllers.remove(bending.getType());
			bendingControllerList.remove(bending);
			networker.markChanged(KEY_CONTROLLERS, bendingControllerList);
			saveChanges();
		} else {
			AvatarLog.warn(WarningType.INVALID_CODE, "Cannot remove BendingController '" + bending
					+ "' because player does not have that instance.");
		}
	}
	
	/**
	 * Remove the bending controller and its state with that type.
	 * 
	 * @see #removeBending(BendingController)
	 */
	public void removeBending(BendingType type) {
		removeBending(getBendingController(type));
	}
	
	/**
	 * hashtag aman. Will be saved.
	 */
	public void takeBending() {
		
		List<BendingController> copy = new ArrayList<>(bendingControllerList);
		for (BendingController controller : copy) {
			removeBending(controller);
		}
		networker.markChanged(KEY_CONTROLLERS, bendingControllerList);
		saveChanges();
		
	}
	
	public List<BendingController> getBendingControllers() {
		return bendingControllerList;
	}
	
	/**
	 * Get the BendingController with that ID. Returns null if there is no
	 * bending controller for that ID.
	 * 
	 * @deprecated Use {@link #getBendingController(BendingType)}.
	 * 
	 * @param id
	 * @return
	 */
	@Deprecated
	public BendingController getBendingController(int id) {
		return getBendingController(BendingType.find(id));
	}
	
	/**
	 * Get the BendingController with that type. Returns null if there is no
	 * bending controller for that type.
	 */
	public BendingController getBendingController(BendingType type) {
		return bendingControllers.get(type);
	}
	
	public PlayerState getState() {
		return state;
	}
	
	/**
	 * Gets extra metadata for the given bending controller with that type, or
	 * null if there is no bending controller.
	 * <p>
	 * Will automatically create a state and sync changes if the controller is
	 * present.
	 */
	public BendingState getBendingState(BendingType type) {
		if (!hasBending(type)) {
			AvatarLog.warn(WarningType.INVALID_CODE, "Tried to access BendingState with Type " + type
					+ ", but player does not have the BendingController");
		}
		if (hasBending(type) && !hasBendingState(BendingManager.getBending(type))) {
			addBendingState(getBendingController(type).createState(this));
			saveChanges();
			sync();
		}
		return hasBending(type) ? bendingStates.get(type) : null;
	}
	
	/**
	 * Gets extra metadata for the given bending controller with that ID, or
	 * null if there is no bending controller.
	 * 
	 * @deprecated Use {@link #getBendingState(BendingType)}
	 */
	@Deprecated
	public BendingState getBendingState(int id) {
		return getBendingState(BendingType.find(id));
	}
	
	/**
	 * Get extra metadata for the given bending controller, returns null if no
	 * Bending controller.
	 * 
	 * @see #getBendingState(BendingType)
	 */
	public BendingState getBendingState(BendingController controller) {
		return getBendingState(controller.getType());
	}
	
	/**
	 * Returns whether a bending state for the bending controller is present.
	 * Does not add one if necessary.
	 */
	public boolean hasBendingState(BendingController controller) {
		return bendingStates.containsKey(controller.getType());
	}
	
	public List<BendingState> getAllBendingStates() {
		return bendingStateList;
	}
	
	public void clearBendingStates() {
		bendingStateList.clear();
		bendingStates.clear();
	}
	
	/**
	 * Adds the bending state to this player data, replacing the existing one of
	 * that type if necessary.
	 */
	public void addBendingState(BendingState state) {
		bendingStates.put(state.getType(), state);
		bendingStateList.add(state);
		networker.markChanged(KEY_STATES, bendingStateList);
	}
	
	/**
	 * Removes that bending state from this player data. Note: Must be the exact
	 * instance already present to successfully occur.
	 */
	public void removeBendingState(BendingState state) {
		bendingStates.remove(state.getType());
		bendingStateList.remove(state);
		networker.markChanged(KEY_STATES, bendingStateList);
	}
	
	public Set<StatusControl> getActiveStatusControls() {
		return Collections.unmodifiableSet(statusControls);
	}
	
	public boolean hasStatusControl(StatusControl status) {
		return statusControls.contains(status);
	}
	
	public void addStatusControl(StatusControl control) {
		statusControls.add(control);
		networker.markChanged(KEY_STATUS_CONTROLS, statusControls);
		saveChanges();
	}
	
	public void removeStatusControl(StatusControl control) {
		statusControls.remove(control);
		networker.markChanged(KEY_STATUS_CONTROLS, statusControls);
		saveChanges();
	}
	
	public void clearStatusControls() {
		statusControls.clear();
		networker.markChanged(KEY_STATUS_CONTROLS, statusControls);
		saveChanges();
	}
	
	/**
	 * Retrieves data about the given ability. Will create data if necessary.
	 */
	public AbilityData getAbilityData(BendingAbility ability) {
		if (!abilityData.containsKey(ability)) {
			abilityData.put(ability, new AbilityData(this, ability));
		}
		return abilityData.get(ability);
	}
	
	/**
	 * Gets a list of all ability data contained in this player data. The list
	 * is immutable.
	 */
	public List<AbilityData> getAllAbilityData() {
		return ImmutableList.copyOf(abilityData.values());
	}
	
	/**
	 * Gets the map of ability data. <b>This is the exact instance used
	 * internally...</b>
	 */
	Map<BendingAbility, AbilityData> abilityData() {
		return abilityData;
	}
	
	public void clearAbilityData() {
		abilityData.clear();
	}
	
	/**
	 * Send the given bending state to the client.
	 */
	public void sendBendingState(BendingState state) {
		networker.changeAndSync(KEY_STATES, bendingStateList);
	}
	
	public Networker getNetworker() {
		return networker;
	}
	
	public static void initFetcher(PlayerDataFetcher<AvatarPlayerData> clientFetcher) {
		fetcher = new PlayerDataFetcherSided<AvatarPlayerData>(clientFetcher,
				new PlayerDataFetcherServer<AvatarPlayerData>(AvatarWorldData.FETCHER));
	}
	
	public static PlayerDataFetcher<AvatarPlayerData> fetcher() {
		return fetcher;
	}
	
}
