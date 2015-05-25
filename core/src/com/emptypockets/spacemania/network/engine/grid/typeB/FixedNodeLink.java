package com.emptypockets.spacemania.network.engine.grid.typeB;

import com.badlogic.gdx.math.Vector2;

public class FixedNodeLink extends NodeLink {
	GridNode node;
	NodeLinkSettings cfg;
	float initialDist;

	Vector2 temp = new Vector2();
	float currentDist;

	public FixedNodeLink(GridNode node, NodeLinkSettings settings) {
		this.node = node;
		this.cfg = settings;
		updateRestPos();
	}

	public void solve() {
		temp.x = node.pos.x - node.restPos.x;
		temp.y = node.pos.y - node.restPos.y;
		currentDist = temp.len();
		if (currentDist * currentDist < 0.001f) {
			return;
		}
		temp.scl((initialDist - currentDist) / currentDist);

		temp.x = temp.x * cfg.stiffness - (node.vel.x) * cfg.damping;
		temp.y = temp.y * cfg.stiffness - (node.vel.y) * cfg.damping;

		node.applyForce(temp);
	}

	public void reset() {
		node.pos.set(node.restPos);
		node.vel.set(0,0);
		node.acl.set(0,0);
	}

	@Override
	public void updateRestPos() {
		this.initialDist = node.pos.dst(this.node.restPos);
	}
}