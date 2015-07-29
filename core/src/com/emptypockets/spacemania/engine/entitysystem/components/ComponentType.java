package com.emptypockets.spacemania.engine.entitysystem.components;

import com.emptypockets.spacemania.utils.BitUtilities;

public enum ComponentType {

	LINEAR_TRANSFORM(0, 1 << 0),
	ANGULAR_TRANSFORM(1, 1 << 1),
	LINEAR_MOVEMENT(2, 1 << 2),
	ANGULAR_MOVEMENT(3, 1 << 3),
	PARTITION(4, 1 << 4),
	CONSTRAINED_MOVEMENT(5, 1 << 5),
	RENDER(6, 1 << 6);

	int mask = 0;
	int updateOrder = 0;

	private ComponentType(int updateOrder, int mask) {
		this.mask = mask;
		this.updateOrder = updateOrder;
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

	public int getUpdateOrder() {
		return updateOrder;
	}
}
