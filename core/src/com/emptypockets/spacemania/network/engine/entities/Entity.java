package com.emptypockets.spacemania.network.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.network.engine.partitioning.cell.PartitionEntity;

public abstract class Entity implements Poolable,PartitionEntity{
	EntityState state;
	EntityType type;

	boolean alive = true;
	boolean explodes = false;

	protected Color color;
	float inverseMass = 1;
	float radius = 15;
	float maxVelocity = 400;
	float maxForce = 400;

	Vector2 lastPosition = new Vector2();
	float lastMovementDist = 0;
	Vector2 fovTemp1 = new Vector2();
	Vector2 fovTemp2 = new Vector2();

	float damping = 0;

	Vector2 forceAculumator = new Vector2();
	boolean bounceOffWalls = true; // Indicate if an enttity should stop dead or
									// bounce off walls
	protected long creationTime;
	protected long lifeTime = 0;

	Vector2 lastServerOffset = new Vector2();
	
	int partitionTag = 0;
	
	public Entity(EntityType type) {
		this.type = type;
		state = new EntityState();
		color = Color.GREEN.cpy();
	}

	
	public void setLifeTime(long time) {
		this.lifeTime = time;
	}

	public boolean isInsideFOV(Entity ent, float fovInDeg) {

		fovTemp1.set(getVel()).nor();
		fovTemp2.set(ent.getPos()).sub(getPos()).nor();
		float angle = Math.abs(fovTemp1.angle(fovTemp2.nor()));
		return angle < fovInDeg / 2;
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
		lastPosition.set(getPos());
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

	public Vector2 getVel() {
		return state.getVel();
	}

	public EntityType getType() {
		return type;
	}

	public EntityState getState() {
		return state;
	}

	public int getId() {
		return state.getId();
	}

	public void update(float deltaTime) {
		lastPosition.set(getPos());
		// Convert the Force to Velocity
		forceAculumator.limit(maxForce);
		forceAculumator.scl(inverseMass * deltaTime);

		// Update the velocity
		getVel().add(forceAculumator);
		if (damping > 0) {
			getVel().scl((float) Math.pow(damping, deltaTime));
		}
		getVel().limit(maxVelocity);
		forceAculumator.x = 0;
		forceAculumator.y = 0;
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
		lastMovementDist = lastPosition.dst(getPos());

		if (lifeTime != 0) {
			if (getAge() > lifeTime) {
				setAlive(false);
			}
		}
	}

	public float getMaxVelocity() {
		return maxVelocity;
	}

	public float getInverseMass() {
		return inverseMass;
	}

	public float getMaxForce() {
		return maxForce;
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

	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}

	public void setDamping(float damping) {
		this.damping = damping;
	}

	@Override
	public void reset() {
		/*
		 * Alive is set false here otherwise when the entity release its set to alive again
		 */
		alive = false;
		explodes = false;
	}

	public void resetForce() {
		forceAculumator.set(0, 0);
	}

	public void applyForce(Vector2 force) {
		this.forceAculumator.add(force);
	}

	public boolean isBounceOffWall() {
		return bounceOffWalls;
	}

	public void setBounceOffWalls(boolean boundeWalls) {
		this.bounceOffWalls = boundeWalls;
	}

	public float dst2(Entity entity) {
		return getPos().dst2(entity.getPos());
	}

	public void setVel(float x, float y) {
		getVel().set(x, y);
	}

	public void setVel(Vector2 vel) {
		getVel().set(vel);
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

	public float getLastMovementDist() {
		return lastMovementDist;
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

}
