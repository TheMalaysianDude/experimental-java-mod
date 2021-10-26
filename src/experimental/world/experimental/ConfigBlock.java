package experimental.world.experimental;

import arc.graphics.g2d.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class ConfigBlock extends Block{
	public TextureRegion onRegion;
	
	public ConfigBlock(String name){
		super(name);
		configurable = true;
		update = true;
		drawDisabled = false;
		autoResetEnable = false;
		group = BlockGroup.logic;
		
		config(Boolean.class, (ConfigBuild entity, Boolean b) -> {
			entity.enabled = b;
		});
	}
	
	@Override
	public void load(){
		super.load();
		onRegion = Core.atlas.find(name + "-on");
	}
	
	public class ConfigBuild extends Building{
		
		@Override
		public boolean configTapped(){
			configure(!enabled);
			Sounds.click.at(this);
			return false;
		}
		
		@Override
		public void draw(){
			super.draw();
			
			if(enabled){
				Draw.rect(onRegion, x, y);
			}
		}
		
		@Override
		public Boolean config(){
			return enabled;
		}
		
        @Override
        public byte version(){
            return 1;
        }

        @Override
        public void readAll(Reads read, byte revision){
            super.readAll(read, revision);

            if(revision == 1){
                enabled = read.bool();
            }
        }

        @Override
        public void write(Writes write){
            super.write(write);

            write.bool(enabled);
        }
	}
}