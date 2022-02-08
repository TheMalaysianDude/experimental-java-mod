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

import experimental.world.draw.*;

public class DebugDrawer extends Block{
	public static DrawBlock[] drawers = {
		new DrawLiquidScale()
	};
	
	public DebugDrawer(String name){
		super(name);
		configurable = true;
		update = true;
		drawDisabled = false;
		autoResetEnabled = false;
		group = BlockGroup.none;
		
		config(float.class, (DebugDrawerBuild tile, float value) -> tile.progress = value);
		drawer(DrawBlock.class, (DebugDrawerBuild tile, DrawBlock value) -> tile.drawer = value);
	}
	
	public class DebugDrawerBuild extends Building{
		public @Nullable DrawBlock drawer;
		public Float progress;
		
		public DrawBlock drawer(){
			return drawer;
		}
		
		public float progress(){
			return progress;
		}
		
		@Override
		public void draw(){
			drawer.draw(this);
		}
		
		@Override
		public void buildConfiguration(Table table){
			table.slider(0f, 1f, 0.01f, progress, value -> {
				configure(value);
			}).growX().row();
			table.pane(it -> {
				for (DrawBlock drawBlock : drawers){
					it.button(drawBlock.getClass().getSimpleName(), () -> {
						configure(drawBlock);
					}).growX().row();
				}
			});
		}
	}
}
