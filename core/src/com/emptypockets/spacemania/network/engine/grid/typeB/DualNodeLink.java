package com.emptypockets.spacemania.network.engine.grid.typeB;

import com.badlogic.gdx.math.Vector2;

public class DualNodeLink extends NodeLink{
	GridNode nodeA;
	GridNode nodeB;
	float initialDist;
	NodeLinkSettings cfg;
	Vector2 temp = new Vector2();
	float currentDist;

	public DualNodeLink(GridNode nodeA, GridNode nodeB, NodeLinkSettings cfg){
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.cfg = cfg;
		updateRestPos();
	}
	
	public void solve(){
		temp.x = nodeB.pos.x-nodeA.pos.x;
		temp.y = nodeB.pos.y-nodeA.pos.y;
		currentDist = temp.len();
		
		if(currentDist < 0.01f){
			return;
		}
		temp.scl((initialDist-currentDist)/currentDist);
		temp.x = temp.x*cfg.stiffness-(nodeB.vel.x-nodeA.vel.x)*cfg.damping;
		temp.y = temp.y*cfg.stiffness-(nodeB.vel.y-nodeA.vel.y)*cfg.damping;
		
		nodeB.applyForce(temp);
		temp.x*=-1;
		temp.y*=-1;
		nodeA.applyForce(temp);
	}

	@Override
	public void updateRestPos() {
		this.initialDist = nodeB.pos.dst(nodeA.pos);
	}
}