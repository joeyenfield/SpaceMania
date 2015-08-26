package com.emptypockets.spacemania.engine.systems.entitysystem.components;

import javax.management.RuntimeErrorException;

import com.emptypockets.spacemania.utils.BitUtilities;

public enum ComponentType {
	LINEAR_TRANSFORM(0),
	ANGULAR_TRANSFORM(1),
	LINEAR_MOVEMENT(2),
	ANGULAR_MOVEMENT(3),
	PARTITION(4),
	CONSTRAINED_MOVEMENT(5),
	RENDER(6),
	NETWORK_DATA(7),
	DESTRUCTION(8),
	COLLISSION(9),
	CONTROL(10),
	WEAPON(11);
	int mask = 0;
	public int id = 0;
	public static final int COMPONENT_TYPES = 12;

	public static EntityComponent[] getComponentHolder() {
		return new EntityComponent[COMPONENT_TYPES];
	}

	public static ComponentData[] getComponentDataHolder() {
		return new ComponentData[COMPONENT_TYPES];
	}
	
	public static ComponentType getById(int i) {
		switch (i) {
		case 0:return LINEAR_TRANSFORM;
		case 1:return ANGULAR_TRANSFORM;
		case 2:return LINEAR_MOVEMENT;
		case 3:return ANGULAR_MOVEMENT;
		case 4:return PARTITION;
		case 5:return CONSTRAINED_MOVEMENT;
		case 6:return RENDER;
		case 7:return NETWORK_DATA;
		case 8:return DESTRUCTION;
		case 9:return COLLISSION;
		case 10:return CONTROL;
		case 11:return WEAPON;
		}
		
		throw new RuntimeException("Missing Id");
	}


	private ComponentType(int id) {
		this.mask = 1 << id;
		this.id = id;
	}

	public int getMask() {
		return mask;
	}

	public int addAbility(int currentAbility) {
		if ((currentAbility & mask) != 0) {
			System.out.println(BitUtilities.toString(currentAbility) + " - Current ");
			System.out.println(BitUtilities.toString(mask) + " - Mask To Add ");
			printMasks();
			throw new RuntimeException("Component already in use ");
		}
		return currentAbility | mask;
	}

	public int removeAbility(int currentAbility) {
		if ((currentAbility & mask) == 0) {
			throw new RuntimeException("Component does not have ability");
		}
		return currentAbility ^ mask;
	}

	public String getMaskString() {
		return BitUtilities.toString(mask);
	}
	

	public static void printMasks() {
		for (ComponentType type : ComponentType.values()) {
			System.out.printf("%s - %s\n", type.getMaskString(), type.name());
		}
	}



}
