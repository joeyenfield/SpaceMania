package com.emptypockets.spacemania.gui.tools;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

public class TextRender {
	float prog1 = 0;
	float prog2 = 0;
	float ang1 = 0;
	float ang2 = 0;
	float x1 = 0;
	float y1 = 0;
	float x2 = 0;
	float y2 = 0;
	float x3 = 0;
	float y3 = 0;
	float x4 = 0;
	float y4 = 0;
	boolean lowQuality = false;

	Vector2 tempRenderPos = new Vector2();

	float lineThickness = 0.1f;
	float bottomLineInset = lineThickness / 2;
	float topLineInset = 1 - bottomLineInset;
	int tabSize = 4;
	float characterGap = 0.0f;

	Vector2 currentPos = new Vector2();
	float currentSize = 1;
	Rectangle currentBounds = new Rectangle();
	boolean strictDraw = true;
	public void charBounds(ShapeRenderer render, Vector2 pos, float size) {
		render.rect(pos.x, pos.y, size, size);
	}


	public void render(ShapeRenderer render, CharSequence seq, Vector2 p, float size, Rectangle viewport) {
		render(render, seq, p.x,p.y, size, viewport);
	}
	public void render(ShapeRenderer render, CharSequence seq, float x, float y, float size, Rectangle viewport) {
		tempRenderPos.set(x,y);
		for (int i = 0; i < seq.length(); i++) {
			char c = seq.charAt(i);
			if (c == (char) '\t') {
				tempRenderPos.x += tabSize * (1 + characterGap) * size;
			} else {
				render(render, c, tempRenderPos, size, viewport);
				tempRenderPos.x += (1 + characterGap) * size;
			}
		}
	}

	public void renderBounds(ShapeRenderer render, CharSequence seq, Vector2 pos, float size, Rectangle viewport) {
		updateBounds(pos, size);
		if ((!strictDraw && !viewport.overlaps(currentBounds)) || (strictDraw && !viewport.contains(currentBounds))) {
			return;
		}
		tempRenderPos.set(pos);
		for (int i = 0; i < seq.length(); i++) {
			char c = seq.charAt(i);
			if (c == (char) '\t') {
				for (int j = 0; j < tabSize; j++) {
					charBounds(render, tempRenderPos, size);
					tempRenderPos.x += (1 + characterGap) * size;
				}
			} else {
				charBounds(render, tempRenderPos, size);
				tempRenderPos.x += (1 + characterGap) * size;
			}
		}
	}

	public void render(ShapeRenderer render, char c, Vector2 pos, float size, Rectangle viewport) {
		updateBounds(pos, size);
		if ((!strictDraw && !viewport.overlaps(currentBounds)) || (strictDraw && !viewport.contains(currentBounds))) {
//			System.out.println(viewport);
//			System.out.println(currentBounds);
//			System.out.println("OverLap : "+viewport.overlaps(currentBounds));
//			System.out.println("Contains  : "+viewport.contains(currentBounds));
//			System.out.println("\n");
			return;
		}
		c = Character.toUpperCase(c);
		switch (c) {
		case '0':
			num0(render);
			break;
		case '1':
			num1(render);
			break;
		case '2':
			num2(render);
			break;
		case '3':
			num3(render);
			break;
		case '4':
			num4(render);
			break;
		case '5':
			num5(render);
			break;
		case '6':
			num6(render);
			break;
		case '7':
			num7(render);
			break;
		case '8':
			num8(render);
			break;
		case '9':
			num9(render);
			break;
		case 'A':
			aUpper(render);
			break;
		case 'B':
			bUpper(render);
			break;
		case 'C':
			cUpper(render);
			break;
		case 'D':
			dUpper(render);
			break;
		case 'E':
			eUpper(render);
			break;
		case 'F':
			fUpper(render);
			break;
		case 'G':
			gUpper(render);
			break;
		case 'H':
			hUpper(render);
			break;
		case 'I':
			iUpper(render);
			break;
		case 'J':
			jUpper(render);
			break;
		case 'K':
			kUpper(render);
			break;
		case 'L':
			lUpper(render);
			break;
		case 'M':
			mUpper(render);
			break;
		case 'N':
			nUpper(render);
			break;
		case 'O':
			oUpper(render);
			break;
		case 'P':
			pUpper(render);
			break;
		case 'Q':
			qUpper(render);
			break;
		case 'R':
			rUpper(render);
			break;
		case 'S':
			sUpper(render);
			break;
		case 'T':
			tUpper(render);
			break;
		case 'U':
			uUpper(render);
			break;
		case 'V':
			vUpper(render);
			break;
		case 'W':
			wUpper(render);
			break;
		case 'X':
			xUpper(render);
			break;
		case 'Y':
			yUpper(render);
			break;
		case 'Z':
			zUpper(render);
			break;
		case '?':
			question(render);
			break;
		case ':':
			colinUpper(render);
			break;
		default:
			break;
		}
	}

	private void updateBounds(Vector2 pos, float size) {
		this.currentPos.set(pos);
		this.currentSize = size;
		this.currentBounds.setPosition(this.currentPos);
		this.currentBounds.setSize(size);
	}

	/*
	 * Character Sets
	 */
	public void num0(ShapeRenderer render) {
		arcLine(0.5f, 0.5f, 0.5f - bottomLineInset, lineThickness, 0, 360, 20, render);
		roundline(0.2f, 0.2f, 0.8f, 0.8f, lineThickness, render);
	}

	public void num1(ShapeRenderer render) {
		float inset = 0.2f;
		line(0.5f, 0, 0.5f, 1, lineThickness, render);
		line(0.5f, topLineInset, 0.25f, topLineInset - 0.25f, lineThickness, render);
		line(inset, bottomLineInset, 1 - inset, bottomLineInset, lineThickness, render);
	}

	public void num2(ShapeRenderer render) {
		float inset = 0.1f;
		float radius = 0.3f;
		arcLine(0.5f, topLineInset - radius, radius, lineThickness, -60, 150, 10, render);
		roundline(inset + bottomLineInset, bottomLineInset, 0.65f, 0.41f, lineThickness, render);
		line(inset + bottomLineInset, bottomLineInset, 1 - inset, bottomLineInset, lineThickness, render);
	}

	public void num3(ShapeRenderer render) {
		float inset = 0.2f;
		float radius = 0.31f;
		arcLine(0.5f, bottomLineInset + radius, radius, lineThickness, -140, 120, 20, render);
		roundline(topLineInset-inset, topLineInset, 0.3f, 0.62f, lineThickness, render);
		roundline(inset + bottomLineInset, topLineInset, 1 - inset, topLineInset, lineThickness, render);
	}

	public void num4(ShapeRenderer render) {
		float inset = 0.2f;
		float offsetX = -0.1f;
		line(offsetX + topLineInset - inset, 0, offsetX + topLineInset - inset, 1, lineThickness, render);
		line(offsetX + inset + bottomLineInset, 0.4f, offsetX + topLineInset, 0.4f, lineThickness, render);
		roundline(offsetX + topLineInset - inset, topLineInset, offsetX + inset + bottomLineInset, 0.4f, lineThickness, render);
	}

	public void num5(ShapeRenderer render) {
		float radius = 0.33f;
		arcLine(0.5f, radius, radius - bottomLineInset, lineThickness, -130, 140, 20, render);
		roundline(0.3f, 0.5f, 0.3f, topLineInset, lineThickness, render);
		line(0.3f, topLineInset, 0.8f, topLineInset, lineThickness, render);
	}

	public void num6(ShapeRenderer render) {
		float radius = 0.3f;
		arcLine(0.5f, radius, radius - bottomLineInset, lineThickness, 0, 360, 20, render);
		roundline(0.25f, 0.4f, 0.5f, 0.9f, lineThickness, render);
	}

	public void num7(ShapeRenderer render) {
		line(0.5f, 0, 0.8f, topLineInset, lineThickness, render);
		roundline(0.8f, topLineInset, 0.25f, topLineInset, lineThickness, render);
	}

	public void num8(ShapeRenderer render) {
		arcLine(0.5f, 0.75f, 0.25f - bottomLineInset, lineThickness, 0, 360, 20, render);
		arcLine(0.5f, 0.3f, 0.3f - bottomLineInset, lineThickness, 0, 360, 20, render);
	}

	public void num9(ShapeRenderer render) {
		float radius = 0.3f;
		arcLine(0.5f, 1 - radius, radius - bottomLineInset, lineThickness, 0, 360, 20, render);
		roundline(0.75f, 0.6f, 0.5f, 0.1f, lineThickness, render);
	}

	public void aUpper(ShapeRenderer render) {
		roundline(bottomLineInset, bottomLineInset, 0.5f, topLineInset, lineThickness, render);
		roundline(0.5f, topLineInset, topLineInset, bottomLineInset, lineThickness, render);
		line(0.2f, 0.4f, 0.8f, 0.4f, lineThickness, render);
	}

	public void bUpper(ShapeRenderer render) {

		float topSize = 0.25f;
		float flatBit = 0.3f;
		float offset = 0.2f;
		// Side
		line(offset + bottomLineInset, 0, offset + bottomLineInset, 1, lineThickness, render);
		// Bottom
		line(offset + 0, bottomLineInset, offset + flatBit, bottomLineInset, lineThickness, render);
		// Middle
		line(offset + 0, 0.5f + bottomLineInset, offset + flatBit, 0.5f + bottomLineInset, lineThickness, render);
		// Top
		line(offset + 0, topLineInset, offset + flatBit, topLineInset, lineThickness, render);

		arcLine(offset + flatBit, 0.25f + bottomLineInset, 0.25f, lineThickness, -90, 90, 20, render);
		arcLine(offset + flatBit, 1 - topSize, topSize - bottomLineInset, lineThickness, -90, 90, 20, render);
	}

	public void cUpper(ShapeRenderer render) {
		arcLine(0.5f + bottomLineInset, 0.5f, 0.5f - bottomLineInset, lineThickness, 30, 330, 20, render);
	};

	public void dUpper(ShapeRenderer render) {
		float flatBit = 0.3f;
		float offset = 0.1f;
		// Side
		line(offset + bottomLineInset, 0, offset + bottomLineInset, 1, lineThickness, render);
		arcLine(offset + flatBit, 0.5f, 0.5f - bottomLineInset, lineThickness, -90, 90, 20, render);
		// Bottom
		line(offset + 0, bottomLineInset, offset + flatBit, bottomLineInset, lineThickness, render);
		// Top
		line(offset + 0, topLineInset, offset + flatBit, topLineInset, lineThickness, render);
	};

	public void eUpper(ShapeRenderer render) {
		float flatBit = 0.6f;
		float offset = 0.2f;
		// Side
		line(offset + bottomLineInset, 0, offset + bottomLineInset, 1, lineThickness, render);
		// Bottom
		line(offset + 0, bottomLineInset, offset + flatBit, bottomLineInset, lineThickness, render);
		// Middle
		line(offset + 0, 0.5f, offset + flatBit, 0.5f, lineThickness, render);
		// Top
		line(offset + 0, topLineInset, offset + flatBit, topLineInset, lineThickness, render);
	};

	public void fUpper(ShapeRenderer render) {
		float flatBit = 0.6f;
		float offset = 0.2f;
		// Side
		line(offset + bottomLineInset, 0, offset + bottomLineInset, 1, lineThickness, render);
		// Middle
		line(offset + 0, 0.5f, offset + flatBit, 0.5f, lineThickness, render);
		// Top
		line(offset + 0, topLineInset, offset + flatBit, topLineInset, lineThickness, render);
	};

	public void gUpper(ShapeRenderer render) {
		arcLine(0.5f + bottomLineInset, 0.5f, 0.5f - bottomLineInset, lineThickness, 40, 310, 20, render);
		roundline(0.6f, 0.4f, topLineInset, 0.4f, lineThickness, render);
		roundline(0.9f - bottomLineInset, bottomLineInset, 0.9f - bottomLineInset, 0.4f, lineThickness, render);
	};

	public void hUpper(ShapeRenderer render) {
		float offset = 0.2f;
		roundline(offset + bottomLineInset, bottomLineInset, offset + bottomLineInset, topLineInset, lineThickness, render);
		line(offset, 0.5f, 1 - (offset + bottomLineInset), 0.5f, lineThickness, render);
		roundline(1 - (offset + bottomLineInset), bottomLineInset, 1 - (offset + bottomLineInset), topLineInset, lineThickness, render);

	};

	public void iUpper(ShapeRenderer render) {
		float inset = 0.1f;
		line(0.5f, bottomLineInset, 0.5f, topLineInset, lineThickness, render);
		roundline(inset + bottomLineInset, bottomLineInset, topLineInset - inset, bottomLineInset, lineThickness, render);
		roundline(inset + bottomLineInset, topLineInset, topLineInset - inset, topLineInset, lineThickness, render);

	};

	public void jUpper(ShapeRenderer render) {
		float inset = 0.1f;
		float rad = 0.25f;
		arcLine(rad + bottomLineInset, rad + bottomLineInset, rad, lineThickness, 180, 360, 20, render);
		roundline(2 * rad + bottomLineInset, rad, 2 * rad + bottomLineInset, topLineInset, lineThickness, render);
		roundline(inset + bottomLineInset, topLineInset, topLineInset - inset, topLineInset, lineThickness, render);
	};

	public void kUpper(ShapeRenderer render) {
		float inset = 0.2f;
		line(inset + bottomLineInset, 0, inset + bottomLineInset, 1, lineThickness, render);
		roundline(inset + bottomLineInset, 0.5f, topLineInset - inset, topLineInset, lineThickness, render);
		roundline(inset + bottomLineInset, 0.5f, topLineInset - inset, bottomLineInset, lineThickness, render);
	};

	public void lUpper(ShapeRenderer render) {
		float flatBit = 0.6f;
		float offset = 0.2f;
		// Side
		line(offset + bottomLineInset, 0, offset + bottomLineInset, 1, lineThickness, render);
		// Bottom
		line(offset + 0, bottomLineInset, offset + flatBit, bottomLineInset, lineThickness, render);
	};

	public void mUpper(ShapeRenderer render) {
		float inset = 0.1f;
		line(bottomLineInset+inset, 0, bottomLineInset+inset, 1, lineThickness, render);
		roundline(bottomLineInset+inset, topLineInset, 0.5f, 0.5f, lineThickness, render);
		roundline(0.5f, 0.5f, topLineInset-inset, topLineInset, lineThickness, render);
		line(topLineInset-inset, 0, topLineInset-inset, 1, lineThickness, render);
	};

	public void nUpper(ShapeRenderer render) {
		float inset = 0.1f;
		
		line(bottomLineInset+inset, 0, bottomLineInset+inset, 1, lineThickness, render);
		line(bottomLineInset+inset, topLineInset, topLineInset-inset, bottomLineInset, lineThickness, render);
		line(topLineInset-inset, 0, topLineInset-inset, 1, lineThickness, render);
	};

	public void oUpper(ShapeRenderer render) {
		arcLine(0.5f, 0.5f, 0.5f - bottomLineInset, lineThickness, 0, 360, 20, render);
	};

	public void pUpper(ShapeRenderer render) {
		float topSize = 0.3f;
		float flatBit = 0.3f;
		float offset = 0.2f;
		// Side
		line(offset + bottomLineInset, 0, offset + bottomLineInset, 1, lineThickness, render);
		// Middle
		line(offset + 0, 1-2*topSize + bottomLineInset, offset + flatBit, 1-2*topSize + bottomLineInset, lineThickness, render);
		// Top
		line(offset + 0, topLineInset, offset + flatBit, topLineInset, lineThickness, render);

		arcLine(offset + flatBit, 1 - topSize, topSize - bottomLineInset, lineThickness, -90, 90, 20, render);
	};

	public void qUpper(ShapeRenderer render) {
		arcLine(0.5f, 0.5f, 0.5f - bottomLineInset, lineThickness, 0, 360, 20, render);
		roundline(0.6f, 0.4f, topLineInset, bottomLineInset, lineThickness, render);
	};

	public void rUpper(ShapeRenderer render) {
		float topSize = 0.3f;
		float flatBit = 0.3f;
		float offset = 0.2f;
		// Side
		line(offset + bottomLineInset, 0, offset + bottomLineInset, 1, lineThickness, render);
		// Middle
		line(offset + 0, 1-2*topSize + bottomLineInset, offset + flatBit, 1-2*topSize + bottomLineInset, lineThickness, render);
		// Top
		line(offset + 0, topLineInset, offset + flatBit, topLineInset, lineThickness, render);

		arcLine(offset + flatBit, 1 - topSize, topSize - bottomLineInset, lineThickness, -90, 90, 20, render);
		// line
		roundline(offset +flatBit, 1-2*topSize + bottomLineInset, topLineInset-offset, bottomLineInset, lineThickness, render);
	};

	public void sUpper(ShapeRenderer render) {
		float inset = 0.2f;
		arcLine(inset+0.25f, 0.75f-lineThickness/4, 0.25f-lineThickness/4, lineThickness, 90,270,20,render);
		roundline(inset+0.25f, topLineInset, 1-inset, topLineInset,lineThickness, render);
		
		arcLine(1-(inset+0.25f), 0.25f+lineThickness/4, 0.25f-lineThickness/4, lineThickness, -90,90,20,render);
		roundline(inset, bottomLineInset, 1-(inset+0.25f), bottomLineInset,lineThickness, render);
		line(0.4f,0.5f,0.6f,0.5f, lineThickness, render);
	};

	public void tUpper(ShapeRenderer render) {
		float inset = 0.1f;
		line(0.5f, 0, 0.5f, 1, lineThickness, render);
		line(inset + bottomLineInset, topLineInset, topLineInset - inset, topLineInset, lineThickness, render);
	};

	public void uUpper(ShapeRenderer render) {
		float inset = 0.1f;
		line(inset+lineThickness, 1, inset+lineThickness, 0.5f-inset-lineThickness/2, lineThickness, render);
		line(1-inset-lineThickness, 1, 1-inset-lineThickness, 0.5f-inset-lineThickness/2, lineThickness, render);
		arcLine(0.5f, 0.5f-inset-lineThickness/2, 0.5f-inset-lineThickness, lineThickness, -180, 0, 20, render);
	};

	public void vUpper(ShapeRenderer render) {
		roundline(bottomLineInset, topLineInset, 0.5f, bottomLineInset, lineThickness, render);
		roundline(0.5f, bottomLineInset, topLineInset, topLineInset, lineThickness, render);
	};

	public void wUpper(ShapeRenderer render) {
		roundline(bottomLineInset, topLineInset, 0.25f, bottomLineInset, lineThickness, render);
		roundline(0.25f, bottomLineInset, 0.5f, 0.5f, lineThickness, render);
		roundline(0.5f, 0.5f, 0.75f, bottomLineInset, lineThickness, render);
		roundline(0.75f, bottomLineInset, topLineInset, topLineInset, lineThickness, render);
	};

	public void xUpper(ShapeRenderer render) {
		roundline(bottomLineInset, topLineInset, topLineInset, bottomLineInset, lineThickness, render);
		roundline(bottomLineInset, bottomLineInset, topLineInset, topLineInset, lineThickness, render);
	};

	public void yUpper(ShapeRenderer render) {
		roundline(bottomLineInset, topLineInset, 0.5f, 0.5f, lineThickness, render);
		roundline(0.5f, 0.5f, topLineInset, topLineInset, lineThickness, render);
		line(0.5f, 0.5f, 0.5f, 0, lineThickness, render);
	};

	public void zUpper(ShapeRenderer render) {
		float inset = 0.1f;
		roundline(inset + bottomLineInset, bottomLineInset, topLineInset - inset, topLineInset, lineThickness, render);
		line(inset + bottomLineInset, bottomLineInset, topLineInset - inset, bottomLineInset, lineThickness, render);
		line(inset + bottomLineInset, topLineInset, topLineInset - inset, topLineInset, lineThickness, render);
	};

	public void colinUpper(ShapeRenderer render) {
		circle(0.5f, 0.75f, lineThickness, render);
		circle(0.5f, 0.25f, lineThickness, render);
	};

	public void question(ShapeRenderer render) {
		roundline(0.5f, 0.5f, 0.5f, 0.3f, lineThickness, render);
		arcLine(0.5f, 0.7f, 0.2f, lineThickness, -90, 160, 10, render);
		circle(0.5f, 1.5f * lineThickness, lineThickness / 2, render);
	}

	/*
	 * Utilities Functions
	 */

	public void circle(float x, float y, float radius, ShapeRenderer render) {
		x = currentPos.x + currentSize * x;
		y = currentPos.y + currentSize * y;
		render.circle(x, y, radius * currentSize);
	}

	public void line(float x1, float y1, float x2, float y2, float thick, ShapeRenderer render) {
		x1 = currentPos.x + currentSize * x1;
		y1 = currentPos.y + currentSize * y1;

		x2 = currentPos.x + currentSize * x2;
		y2 = currentPos.y + currentSize * y2;
		render.rectLine(x1, y1, x2, y2, thick * currentSize);
	}

	/**
	 * clockwise
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 * @param render
	 */
	public void rect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, ShapeRenderer render) {
		x1 = currentPos.x + currentSize * x1;
		y1 = currentPos.y + currentSize * y1;

		x2 = currentPos.x + currentSize * x2;
		y2 = currentPos.y + currentSize * y2;

		x3 = currentPos.x + currentSize * x3;
		y3 = currentPos.y + currentSize * y3;

		x4 = currentPos.x + currentSize * x4;
		y4 = currentPos.y + currentSize * y4;
		render.triangle(x1, y1, x2, y2, x3, y3);
		render.triangle(x1, y1, x3, y3, x4, y4);
	}

	public void roundline(float x1, float y1, float x2, float y2, float thick, ShapeRenderer render) {
		if (!lowQuality) {
			circle(x1, y1, thick / 2, render);
			circle(x2, y2, thick / 2, render);
		}
		line(x1, y1, x2, y2, thick, render);
	}

	public void circleLine(float xCenter, float yCenter, float radius, float lineThick, int segments, ShapeRenderer render) {
		arcLine(xCenter, yCenter, radius, lineThick, 0, 360, segments, render);
	}

	public void arcLine(float xCenter, float yCenter, float radius, float lineThick, float startAngleDeg, float endAngleDeg, int segments, ShapeRenderer render) {

		float startAngle = MathUtils.degreesToRadians * startAngleDeg;
		float endAngle = MathUtils.degreesToRadians * endAngleDeg;
		float rMin = radius - lineThick / 2;
		float rMax = radius + lineThick / 2;
		for (int i = 0; i < segments - 1; i++) {
			prog1 = i / (segments - 1f);
			prog2 = (i + 1) / (segments - 1f);
			ang1 = startAngle + prog1 * (endAngle - startAngle);
			ang2 = startAngle + prog2 * (endAngle - startAngle);

			float cAng1 = MathUtils.cos(ang1);
			float sAng1 = MathUtils.sin(ang1);
			float cAng2 = MathUtils.cos(ang2);
			float sAng2 = MathUtils.sin(ang2);

			x1 = xCenter + rMin * cAng1;
			y1 = yCenter + rMin * sAng1;

			x2 = xCenter + rMax * cAng1;
			y2 = yCenter + rMax * sAng1;

			x3 = xCenter + rMax * cAng2;
			y3 = yCenter + rMax * sAng2;

			x4 = xCenter + rMin * cAng2;
			y4 = yCenter + rMin * sAng2;

			rect(x1, y1, x2, y2, x3, y3, x4, y4, render);
		}

	}
}
