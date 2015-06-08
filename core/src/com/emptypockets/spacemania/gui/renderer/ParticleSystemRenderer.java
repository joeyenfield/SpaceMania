package com.emptypockets.spacemania.gui.renderer;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.engine.particles.Particle;
import com.emptypockets.spacemania.network.engine.particles.ParticleSystem;

public class ParticleSystemRenderer {

	Texture spark;
	Sprite sparkSprite;

	public void render(ParticleSystem particleSystem, final Rectangle viewport, final SpriteBatch batch) {
		if (spark == null) {
			spark = new Texture("spark.png");
			sparkSprite = new Sprite(spark);
			sparkSprite.setOriginCenter();
		}
		batch.begin();
		batch.enableBlending();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		particleSystem.process(new SingleProcessor<Particle>() {
			@Override
			public void process(Particle entity) {
				if (entity.isDead() || !viewport.contains(entity.getPos())) {
					return;
				}

				float velScale = .05f;
				sparkSprite.setColor(entity.getCurrentColor());
				sparkSprite.setPosition(entity.getPos().x - entity.getRadius(), entity.getPos().y - entity.getRadius());
				sparkSprite.setOriginCenter();
				sparkSprite.setRotation(entity.getVel().angle());
				sparkSprite.setSize(entity.getVel().len() * velScale, 3);
				sparkSprite.draw(batch);
			}
		});
		batch.end();
	}
}
