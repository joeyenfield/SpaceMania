package com.emptypockets.spacemania.network.engine.grid;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.network.engine.grid.typeA.GridData;
import com.emptypockets.spacemania.network.engine.grid.typeA.GridSettings;
import com.emptypockets.spacemania.network.engine.grid.typeB.Grid2DSettings;
import com.emptypockets.spacemania.network.engine.grid.typeB.GridData2D;
import com.emptypockets.spacemania.utils.event.EventRecorder;

public class SpeedTest {

	public static void main(String input[]) {
		System.out.println("Starting");
		int numX = 100;
		int numY = 100;
		Rectangle region = new Rectangle(0, 0, numX, numY);

		EventRecorder time = new EventRecorder(80);
		time.setEnabled(true);

		int runs = 100;
		int updateLoops =100;
		for (int i = 0; i < runs; i++) {
			System.out.println("Running :"+i+" of "+runs);
			GridData typeA = new GridData();
			GridSettings typeASettings = new GridSettings();
			typeASettings.numX = numX;
			typeASettings.numY = numY;

			GridData2D typeB = new GridData2D();
			Grid2DSettings typeBSettings = new Grid2DSettings();
			typeBSettings.numX = numX;
			typeBSettings.numY = numY;
			typeBSettings.bounds = region;

			time.begin("typea-create");
			typeA.createGrid(region, typeASettings);
			time.end("typea-create");

			time.begin("typeb-create");
			typeB.createGrid(typeBSettings);
			time.end("typeb-create");
			
			time.begin("typeA-update");
			for(int run = 0; run < updateLoops; run++){
				typeA.update(0.1f);
			}
			time.end("typeA-update");
		
			time.begin("typeB-update");
			for(int run = 0; run < updateLoops; run++){
				typeB.update(0.1f);
			}
			time.end("typeB-update");
		}
		time.logData();
		
		
	}
}
