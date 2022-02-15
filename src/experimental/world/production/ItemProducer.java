package experimental.world.production;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.logic.*;
import mindustry.world.*;
import mindustry.world.meta.*;

/** Output single Item **/
public class ItemProducer extends Block{
	
	public @Nullable ItemStack outputItem;
	
	public float waitTime = 15f;
	
	public ItemProducer(String name){
		super(name);
		update = true;
		solid = true;
		hasItems = true;
		sync = true;
		flags = EnumSet.of(BlockFlag.factory);
	}
	
	@Override
	public void setStats(){
		stats.timePeriod = waitTime;
		super.setStats();
		stats.add(Stat.productionTime, waitTime / 60f, StatUnit.seconds);
		
		if(outputItem != null) stats.add(Stat.output, outputItem);
	}
	
	@Override
	public boolean outputsItems(){
		return outputItem != null;
	}
	
	public class ItemProducerBuild extends Building{
		public float progress;
		
		@Override
		public void draw(){
			Draw.rect(region, x, y);
			if(outputItem != null){
				TextureRegion icon = outputItem.item.fullIcon;
				Draw.rect(icon, x, y, icon.width/4, icon.height/4);
			}
		}
		
		@Override
		public void updateTile(){
			progress += getProgressIncrease(waitTime);
			
			if(progress >= 1f) startOutput();
			
			dumpOutput();
		}
		
		public void startOutput(){
			if(outputItem != null) offload(outputItem.item);
			
			progress %= 1f;
		}
		
		public void dumpOutput(){
			if(outputItem != null && timer(timerDump, dumpTime / timeScale)) dump(outputItem.item);
		}
		
		@Override
		public double sense(LAccess sensor){
			if(sensor == LAccess.progress) return Mathf.clamp(progress);
			return super.sense(sensor);
		}
		
		@Override
        public int getMaximumAccepted(Item item){
            return itemCapacity;
        }
		
		@Override
        public void write(Writes write){
            super.write(write);
            write.f(progress);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            progress = read.f();
        }
	}
}