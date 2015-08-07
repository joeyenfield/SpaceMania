package com.emptypockets.spacemania.network.old.engine.entities.building;

import com.emptypockets.spacemania.network.old.engine.entities.EntityType;
import com.emptypockets.spacemania.network.old.engine.entities.StaticEntity;

public class BuildingEntity extends StaticEntity {

	public BuildingEntity() {
		super(EntityType.Base);
		setRadius(30);
	}

}
