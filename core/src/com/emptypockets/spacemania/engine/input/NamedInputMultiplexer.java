package com.emptypockets.spacemania.engine.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

public class NamedInputMultiplexer implements InputProcessor {
	private Array<InputProcessor> processors = new Array(4);
	private Array<String> names = new Array(4);

	boolean touchUp = true;
	boolean touchDown = true;
	boolean touchDragged = false;
	boolean mouseMoved = false;

	boolean detail = false;
	boolean exit = false;

	public NamedInputMultiplexer() {
	}

	// public NamedInputMultiplexer (InputProcessor... processors) {
	// for (int i = 0; i < processors.length; i++)
	// this.processors.add(processors[i]);
	// }

	public void addProcessor(int index, InputProcessor processor, String name) {
		if (processor == null)
			throw new NullPointerException("processor cannot be null");
		processors.insert(index, processor);
		names.insert(index, name);
	}

	public void removeProcessor(int index) {
		processors.removeIndex(index);
		names.removeIndex(index);
	}

	public void addProcessor(InputProcessor processor, String name) {
		if (processor == null)
			throw new NullPointerException("processor cannot be null");
		processors.add(processor);
		names.add(name);
	}

	public void removeProcessor(InputProcessor processor) {
		int idx = processors.indexOf(processor, true);
		processors.removeValue(processor, true);
		if (idx >= 0) {
			names.removeIndex(idx);
		}
	}

	/** @return the number of processors in this multiplexer */
	public int size() {
		return processors.size;
	}

	public void clear() {
		processors.clear();
		names.clear();
	}

	public boolean keyDown(int keycode) {
		if (detail)
			System.out.println("\nkeyDown - Start");
		for (int i = 0, n = processors.size; i < n; i++)

			if (processors.get(i).keyDown(keycode)) {
				if (exit || detail) {
					System.out.println("keyDown[" + names.get(i) + "]-True");
				}
				return true;
			} else {
				if (detail) {
					System.out.println("keyDown[" + names.get(i) + "]-False");
				}
			}
		if (detail) {
			System.out.println("keyDown - NONE");
		}
		return false;
	}

	public boolean keyUp(int keycode) {
		if (detail)
			System.out.println("\nkeyUp - Start");
		for (int i = 0, n = processors.size; i < n; i++)
			if (processors.get(i).keyUp(keycode)) {
				if (exit || detail) {
					System.out.println("keyUp[" + names.get(i) + "]-True");
				}
				return true;
			} else {
				if (detail) {
					System.out.println("keyUp[" + names.get(i) + "]-False");
				}
			}
		if (detail) {
			System.out.println("keyUp - NONE");
		}
		return false;
	}

	public boolean keyTyped(char character) {
		if (detail)
			System.out.println("\nkeyTyped - Start");
		for (int i = 0, n = processors.size; i < n; i++)
			if (processors.get(i).keyTyped(character)) {
				if (exit || detail) {
					System.out.println("keyTyped[" + names.get(i) + "]-True");
				}
				return true;
			} else {
				if (detail) {
					System.out.println("keyTyped[" + names.get(i) + "]-False");
				}
			}
		if (detail) {
			System.out.println("keyTyped - NONE");
		}
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (detail)
			if (touchDown)
				System.out.println("\ntouchDown - Start");
		for (int i = 0, n = processors.size; i < n; i++)
			if (processors.get(i).touchDown(screenX, screenY, pointer, button)) {
				if (exit || detail) {
					if (touchDown)
						System.out.println("touchDown[" + names.get(i) + "]-True");
				}
				return true;
			} else {
				if (detail) {
					if (touchDown)
						System.out.println("touchDown[" + names.get(i) + "]-False");
				}
			}
		if (detail) {
			if (touchDown)
				System.out.println("touchDown - NONE");
		}
		return false;

	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (detail)
			if (touchUp)
				System.out.println("\ntouchUp - Start");
		for (int i = 0, n = processors.size; i < n; i++)
			if (processors.get(i).touchUp(screenX, screenY, pointer, button)) {
				if (exit || detail) {
					if (touchUp)
						System.out.println("touchUp[" + names.get(i) + "]-True");
				}
				return true;
			} else {
				if (detail) {
					if (touchUp)
						System.out.println("touchUp[" + names.get(i) + "]-False");
				}
			}
		if (detail) {
			if (touchUp)
				System.out.println("touchUp - NONE");
		}
		return false;

	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (detail)
			if (touchDragged)
				System.out.println("\ntouchDragged - Start");
		for (int i = 0, n = processors.size; i < n; i++)
			if (processors.get(i).touchDragged(screenX, screenY, pointer)) {
				if (exit || detail) {
					if (touchDragged)
						System.out.println("touchDragged[" + names.get(i) + "]-True");
				}
				return true;
			} else {
				if (detail) {
					if (touchDragged)
						System.out.println("touchDragged[" + names.get(i) + "]-False");
				}
			}
		if (detail) {
			if (touchDragged)
				System.out.println("touchDragged - NONE");
		}
		return false;

	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (detail)
			if (mouseMoved)
				System.out.println("\nmouseMoved - Start");
		for (int i = 0, n = processors.size; i < n; i++)
			if (processors.get(i).mouseMoved(screenX, screenY)) {
				if (exit || detail) {
					if (mouseMoved)
						System.out.println("mouseMoved[" + names.get(i) + "]-True");
				}
				return true;
			} else {
				if (detail) {
					if (mouseMoved)
						System.out.println("mouseMoved[" + names.get(i) + "]-False");
				}
			}
		if (detail) {
			if (mouseMoved)
				System.out.println("mouseMoved - NONE");
		}
		return false;

	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		if (detail)
			System.out.println("\nscrolled - Start");
		for (int i = 0, n = processors.size; i < n; i++)
			if (processors.get(i).scrolled(amountX, amountY)) {
				if (exit || detail) {
					System.out.println("scrolled[" + names.get(i) + "]-True");
				}
				return true;
			} else {
				if (detail) {
					System.out.println("scrolled[" + names.get(i) + "]-False");
				}
			}
		if (detail) {
			System.out.println("scrolled - NONE");
		}
		return false;
	}

}