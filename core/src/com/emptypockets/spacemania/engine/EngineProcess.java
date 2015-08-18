package com.emptypockets.spacemania.engine;

public interface EngineProcess<ENG extends GameEngine> {
	public void preProcess(ENG engine);

	public void process(ENG engine);

	public void postProcess(ENG engine);
}
