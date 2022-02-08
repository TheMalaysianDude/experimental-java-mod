package experimental.world.experimental;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class OpacityBlock extends Block {
	public TextureRegion topRegion;
	
	public OpacityBlock(String name){
		super(name);
		update = true;
        configurable = true;
        saveConfig = true;
		
		config(Float.class, (OpacityBuild tile, Float value) -> tile.opacity = value);
	}
	
	@Override
	public void load(){
		super.load();
		topRegion = Core.atlas.find(name + "-top");
	}
	
	public class OpacityBuild extends Building{
		public Float opacity = 0f;
		
		@Override
		public void draw(){
			super.draw();
			Draw.alpha(opacity);
			Draw.rect(topRegion, x, y);
			Draw.reset();
		}
		
		@Override
        public void buildConfiguration(Table table){
			table.slider(0f, 1f, 0.1f, opacity, value -> {
				configure(value);
			}).width(80f).height(25f); 
        }
		
		@Override
        public Object config(){
            return opacity;
        }
		
		@Override
        public void write(Writes write){
            super.write(write);
            write.f(opacity);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            opacity = read.f();
        }
	}
}