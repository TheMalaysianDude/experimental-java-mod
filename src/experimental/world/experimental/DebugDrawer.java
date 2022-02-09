package experimental.world.experimental;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

import experimental.world.draw.*;

public class DebugDrawer extends Block{
	public static ExDrawBlock[] drawers = {
		new DrawLiquidScale()
	};
	
	public DebugDrawer(String name){
		super(name);
		configurable = true;
		update = true;
		drawDisabled = false;
		autoResetEnabled = false;
		group = BlockGroup.none;
		
	}
	
	public class DebugDrawerBuild extends Building{
		public @Nullable ExDrawBlock drawer;
		public Float progress = 0f;
		
		public ExDrawBlock drawer(){
			return drawer;
		}
		
		public Float progress(){
			return progress;
		}
		
		@Override
		public void draw(){
			if(drawer != null) drawer.draw(this);
		}
		
		@Override
		public void buildConfiguration(Table table){
			table.slider(0f, 1f, 0.01f, progress, value -> {
				progress = value;
			}).growX().row();
			table.pane(it -> {
				for (ExDrawBlock drawBlock : drawers){
					it.button(drawBlock.getClass().getSimpleName(), () -> {
						drawer = drawBlock;
					}).growX()
					it.row();
				}
			});
		}
	}
}
