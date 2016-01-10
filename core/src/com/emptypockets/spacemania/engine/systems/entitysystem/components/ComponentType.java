package com.emptypockets.spacemania.engine.systems.entitysystem.components;

import com.badlogic.gdx.utils.Bits;
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
	WEAPON(11),
	AI(12);
	
	public int id = 0;
	public Bits bitMask;
	public static final int COMPONENT_TYPES = 13;


	private ComponentType(int id) {
		this.id = id;
		bitMask = new Bits();
		bitMask.set(id);
	}
	
	public static EntityComponent[] getComponentHolder() {
		return new EntityComponent[COMPONENT_TYPES];
	}

	public static ComponentState[] getComponentDataHolder() {
		return new ComponentState[COMPONENT_TYPES];
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
		case 12:return AI;
		}
		
		throw new RuntimeException("Missing Id");
	}



	public Bits getMask() {
		return bitMask;
	}

	public void addAbility(Bits mask) {
		if(mask.getAndSet(id)){
			System.out.println(BitUtilities.toString(mask) + " - Current ");
			System.out.println(BitUtilities.toString(bitMask) + " - Mask To Add ");
			printMasks();
			throw new RuntimeException("Component already in use ");
		}
	}

	public void removeAbility(Bits mask) {
		if(!mask.getAndClear(id)){
			throw new RuntimeException("Component does not have ability");
		}
	}

	public String getMaskString() {
		return BitUtilities.toString(bitMask);
	}
	

	public static void printMasks() {
		for (ComponentType type : ComponentType.values()) {
			System.out.printf("%s - %s\n", type.getMaskString(), type.name());
		}
	}



}
