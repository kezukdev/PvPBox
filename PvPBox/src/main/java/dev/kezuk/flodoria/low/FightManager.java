package dev.kezuk.flodoria.low;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import dev.kezuk.flodoria.enums.DelayerState;
import dev.kezuk.flodoria.enums.FightStatus;

public class FightManager {
	
	public UUID uuid;
	public UUID opponent;
	public FightStatus fightState;
	public DelayerState delayerStatus;
	public static int inFight = 0;
	public static int countdown = 0;
	public static Map<UUID, FightManager> inbox = Maps.newConcurrentMap();
	
	public FightManager(UUID uuid) {
		this.uuid = uuid;
		this.fightState = FightStatus.FREE;
		inbox.put(uuid, this);	
		this.updateDelayer();
	}
	
	public static void setInFight(int inFight) {
		FightManager.inFight = inFight;
	}
	
	public static int getCountdown() {
		return countdown;
	}
	
	public static int getInFight() {
		return inFight;
	}
	
	public void setDelayerStatus(DelayerState delayerStatus) {
		this.delayerStatus = delayerStatus;
	}
	
	public DelayerState getDelayerStatus() {
		return delayerStatus;
	}
	
	public void setOpponent(UUID opponent) {
		this.opponent = opponent;
	}
	
	public UUID getOpponent() {
		return opponent;
	}
	
	public void setFightState(FightStatus fightState) {
		this.fightState = fightState;
	}
	
	public FightStatus getFightState() {
		return fightState;
	}
	
	public static Map<UUID, FightManager> getInbox() {
		return inbox;
	}
	
	
	private void updateDelayer() {
		if (FightManager.getInbox().size() >= 10) {
			setDelayerStatus(DelayerState.NORMAL);
			return;
		}
		else if (FightManager.getInbox().size() >= 25) {
			setDelayerStatus(DelayerState.HIGHER);
			return;
		}
		setDelayerStatus(DelayerState.LOW);
	}

}
