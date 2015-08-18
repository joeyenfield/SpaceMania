package com.emptypockets.spacemania.engine.systems.entitysystem.components;

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
	int id = 0;
	public static final int COMPONENT_TYPES = 12;

	public static EntityComponent[] getComponentHolder() {
		return new EntityComponent[COMPONENT_TYPES];
	}

	public static ComponentData[] getComponentDataHolder() {
		return new ComponentData[COMPONENT_TYPES];
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
