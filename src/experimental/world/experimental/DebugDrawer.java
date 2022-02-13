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
		new DrawLiquidScale(),
		new SpritePiecesTest()
	};
	
	public DebugDrawer(String name){
		super(name);
		configurable = true;
		update = true;
		drawDisabled = false;
		autoResetEnabled = false;
		group = BlockGroup.none;
		size = 3;
		rotate = true;
		
		config(Integer.class, (DebugDrawerBuild entity, Integer value) -> entity.drawer = value);
		config(Float.class, (DebugDrawerBuild entity, Float value) -> entity.progress = value);
	}
	
	@Override
	public void load(){
		super.load();
		
		for (ExDrawBlock drawBlock : drawers){
			drawBlock.load(this);
		}
	}
	
	public class DebugDrawerBuild extends Building{
		public Integer drawer = -1;
		public Float progress = 0f;
		
		public Integer drawer(){
			return drawer;
		}
		
		public Float progress(){
			return progress;
		}
		
		@Override
		public void draw(){
			if(drawer != -1){
				drawers[drawer].draw(this); 
				return;
			}
			
			Draw.rect(region, x, y);
		}
		
		@Override
		public void buildConfiguration(Table table){
			table.slider(0f, 1f, 0.01f, progress, value -> {
				configure(value);
			}).growX().row();
			
			table.pane(it -> {
				for(int i = 0; i < drawers.length; i++){
					int current = i;
					it.button(drawers[current].getClass().getSimpleName(), () -> {
						if(drawer == current){
							 configure(-1); 
							 return;
						}
						configure(current);
						
					}).grow();
					it.row();
				}
			}).grow();
		}
		
		@Override
        public byte version(){
            return 1;
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			
            progress = read.f();
			drawer = read.i();
        }

        @Override
        public void write(Writes write){
            super.write(write);

            write.f(progress);
			write.i(drawer);
        }
	}
}
