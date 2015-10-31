package com.emptypockets.spacemania.network.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.objenesis.instantiator.ObjectInstantiator;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.input.PlayerInputData;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.controls.ControlState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction.DestructionState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.AngularMovementState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.transform.AngularTransformState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.transform.LinearTransformState;
import com.emptypockets.spacemania.network.common.data.engine.GameEngineState;
import com.emptypockets.spacemania.network.common.data.engine.entity.EntitySystemState;
import com.emptypockets.spacemania.network.common.data.engine.entity.GameEntityAdded;
import com.emptypockets.spacemania.network.common.data.engine.entity.GameEntityNetworkSync;
import com.emptypockets.spacemania.network.common.data.engine.entity.GameEntityRemoved;
import com.emptypockets.spacemania.utils.PoolsManager;
import com.esotericsoftware.kryo.Kryo;

public class NetworkProtocall {
	public static void register(Kryo kryo) {
		register(kryo, ArrayList.class);
		register(kryo, HashMap.class);
		register(kryo, HashSet.class);
		register(kryo, Vector2.class);
		register(kryo, Rectangle.class);
		register(kryo, ComsType.class);
		
		/**
		 * NEW CLASS TYPES
		 */
		
		register(kryo, GameEngineState.class);
		register(kryo, EntitySystemState.class);
		register(kryo, GameEntityAdded.class);
		register(kryo, GameEntityRemoved.class);
		register(kryo, GameEntityNetworkSync.class);
		register(kryo, ComponentType.class);
		register(kryo, LinearMovementState.class);
		register(kryo, LinearTransformState.class);
		register(kryo, AngularMovementState.class);
		register(kryo, AngularTransformState.class);
		register(kryo, ControlState.class);

		register(kryo, PlayerInputData.class);
		register(kryo, GameEntityType.class);
		register(kryo, DestructionState.class);
	}

	public static <T> void register(Kryo kryo, final Class<T> classType) {
		kryo.register(classType).setInstantiator(new ObjectInstantiator<T>() {

			@Override
			public T newInstance() {
				return PoolsManager.obtain(classType);
			}
		});
	}
}
