package com.emptypockets.spacemania.network.old.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.network.old.engine.entities.states.EntityState;
import com.emptypockets.spacemania.network.old.engine.partitioning.cell.PartitionEntity;

public abstract class Entity<STATE extends EntityState> implements Poolable,PartitionEntity{
	protected STATE state;
	protected EntityType type;

	protected boolean alive = true;
	protected boolean explodes = false;

	protected Color color;
	float inverseMass = 1;
	float radius = 15;

	protected long creationTime;
	protected long lifeTime = 0;
	
	float lastMovementDist = 0;
	Vector2 lastPosition = new Vector2();

	Vector2 lastServerOffset = new Vector2();
	
	int partitionTag = 0;
	
	public Entity(EntityType type, STATE state) {
		this.type = type;
		this.state = state;
		color = Color.GREEN.cpy();
	}

	
	public void setLifeTime(long time) {
		this.lifeTime = time;
	}

	
	public void setType(EntityType type) {
		this.type = type;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean contact(Entity entity) {
		boolean contact = false;
		float radSize = getRadius() + entity.getRadius();
		float interactRadius = radSize + lastMovementDist + entity.lastMovementDist;
		float dist2 = getPos().dst2(entity.getPos());

		if (dist2 < radSize * radSize) {
			contact = true;
		} else if (dist2 > interactRadius * interactRadius) {
			contact = false;
		} else {
			if (Intersector.intersectSegmentCircle(lastPosition, getPos(), entity.getPos(), radSize * radSize)) {
				contact = true;
			} else if (Intersector.intersectSegmentCircle(entity.lastPosition, entity.getPos(), getPos(), radSize * radSize)) {
				contact = true;
			}
		}
		return contact;
	}

	public void setPos(float x, float y) {
		state.getPos().x = x;
		state.getPos().y = y;
	}

	public float getRadius() {
		return radius;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Vector2 getPos() {
		return state.getPos();
	}

	public EntityType getType() {
		return type;
	}

	public STATE getState() {
		return state;
	}

	public int getId() {
		return state.getId();
	}

	public void update(float deltaTime) {
		
		// Update the state
		state.delta(deltaTime);

		float dX = 0;
		float dY = 0;
		if (lastServerOffset.len2() > 1) {
			dX = lastServerOffset.x * 0.2f;
			dY = lastServerOffset.y * 0.2f;
			lastServerOffset.x -= dX;
			lastServerOffset.y -= dY;
			getPos().add(dX, dY);
		}

		if (lifeTime != 0) {
			if (getAge() > lifeTime) {
				setAlive(false);
			}
		}
	}

	public float getInverseMass() {
		return inverseMass;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color.cpy();
	}

	public void setInverseMass(float inverseMass) {
		this.inverseMass = inverseMass;
	}

	
	@Override
	public void reset() {
		/*
		 * Alive is set false here otherwise when the entity release its set to alive again
		 */
		alive = false;
		explodes = false;
	}

	public float dst2(Entity entity) {
		return getPos().dst2(entity.getPos());
	}
	
	
	public void tagCreationTime() {
		creationTime = System.currentTimeMillis();
	}

	public long getAge() {
		return System.currentTimeMillis() - creationTime;
	}
	
	public float getLifeProgress(){
		if(lifeTime == 0){
			return 1;
		}
		return getAge()/(float)lifeTime;
	}


	public boolean intersects(Rectangle viewport) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isExplodes() {
		return explodes;
	}

	public void setExplodes(boolean explodes) {
		this.explodes = explodes;
	}

	public Vector2 getLastServerOffset() {
		return lastServerOffset;
	}


	public int getPartitionTag() {
		return partitionTag;
	}


	public void setPartitionTag(int partitionTag) {
		this.partitionTag = partitionTag;
	}


	public boolean isInsideFOV(Entity entity, float fov) {
		return false;
	}

}
