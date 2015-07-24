package com.emptypockets.spacemania.utils;

import com.badlogic.gdx.math.Rectangle;

public class RectangeUtils {

	public static void ensurePositive(Rectangle bounds) {
		// TODO Auto-generated method stub
		if (bounds.width < 0) {
			bounds.x += bounds.width;
			bounds.width = -bounds.width;
		}

		if (bounds.height < 0) {
			bounds.y += bounds.height;
			bounds.height = -bounds.height;
		}
	}
	
	public static float xProgress(Rectangle region, float value){
		return (value-region.x)/region.width;
	}
	public static void clampToScreen(Rectangle screen, Rectangle region, Rectangle result) {
		result.set(region);
		
		if(result.x < screen.x){
			result.width -=screen.x-result.x;
			result.x = screen.x;
		}
		
		if(result.y < screen.y){
			result.height -=screen.y-result.y;
			result.y = screen.y;
		}
		
		if(result.x+result.width > screen.x+screen.width){
			result.width = screen.x+screen.width-result.x;
		}
		
		if(result.y+result.height > screen.y+screen.height){
			result.height = screen.y+screen.height-result.y;
		}
	}
	
	public static boolean overlapsX(Rectangle recA, Rectangle recB){
		return overlap(recA.x, recA.x+recA.width, recB.x, recB.x+recB.width);
	}
	
	public static boolean overlapsY(Rectangle recA, Rectangle recB){
		return overlap(recA.y, recA.y+recA.height, recB.y, recB.y+recB.height);
	}

	public static boolean overlap(float r1Start, float r1End, float r2Start, float r2End) 
	{
	  if(r1Start > r2Start && r1Start < r2End)
	     return true;
	  if(r2Start > r1Start && r2Start < r1End)
	     return true;
	  return false;
	}
	
}
