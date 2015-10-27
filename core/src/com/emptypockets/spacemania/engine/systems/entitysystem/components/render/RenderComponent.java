package com.emptypockets.spacemania.engine.systems.entitysystem.components.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ai.AiComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.collission.CollissionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.partition.PartitionComponent;
import com.emptypockets.spacemania.gui.tools.TextRender;
import com.emptypockets.spacemania.utils.PoolsManager;

public class RenderComponent extends EntityComponent<RenderState> {
	boolean showMaskString = false;
	Color renderColor = null;

	public RenderComponent() {
		super(ComponentType.RENDER);
	}

	@Override
	public void reset() {
		super.reset();
		clearColor();
	}

	public void clearColor(){
		if (renderColor != null) {
			PoolsManager.free(renderColor);
		}
		renderColor = null;
	}
	public Color getColor() {
		if (renderColor == null) {
			renderColor = PoolsManager.obtain(Color.class);
		}
		return renderColor;
	}

	public void update(Vector2 offset) {
		Vector2 pos = entity.linearTransform.state.pos;
		float angle = entity.angularTransform.state.ang;
		state.transform.idt();
		state.transform.translate(pos);
		if (offset != null) {
			state.transform.translate(offset);
		}
		state.transform.rotate(angle);
		state.transform.mul(state.baseTransform);
	}

	public boolean needsRender(int pass) {
		return pass == state.renderPass;
	}

	public void render(SpriteBatch batch, int pass) {
		if (renderColor != null) {
			batch.setColor(renderColor);
		} else if (entity.hasAnyOfAbility(ComponentType.DESTRUCTION.getMask())) {
			batch.setColor(Color.RED);
		} else if (entity.hasComponent(ComponentType.AI) && entity.getComponent(ComponentType.AI, AiComponent.class).hasTarget()) {
			batch.setColor(Color.YELLOW);
		} else {
			batch.setColor(Color.WHITE);
		}

		if (pass == state.renderPass) {
			batch.draw(state.region, state.region.getRegionWidth(), state.region.getRegionHeight(), state.transform);
		}
	}

	@Override
	public Class<RenderState> getStateClass() {
		return RenderState.class;
	}

	public void renderDebug(ShapeRenderer render, TextRender textRender, Rectangle screenView, Vector2 offset) {
		float radius = 0;

		for (int i = 0; i < ComponentType.COMPONENT_TYPES; i++) {
			EntityComponent comp = entity.componentStore.component[i];
			if (comp != null && comp.showDebug() && comp.shouldRenderDebug(screenView, offset)) {
				comp.debug(render, textRender, screenView, offset);
			}
		}

		if (showMaskString) {
			textRender.render(render, (entity.getMaskString()), entity.linearTransform.state.pos.x + offset.x, entity.linearTransform.state.pos.y + offset.y + radius, 1f, screenView);
		}
	}
}
