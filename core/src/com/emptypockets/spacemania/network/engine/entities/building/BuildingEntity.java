package com.emptypockets.spacemania.network.engine.entities.building;

import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.entities.StaticEntity;

public class BuildingEntity extends StaticEntity {

	public BuildingEntity() {
		super(EntityType.Base);
		setRadius(30);
	}

}
