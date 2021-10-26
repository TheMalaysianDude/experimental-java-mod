package experimental.world.experimental;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import arc.scene.ui.layout.*;;
import mindustry.*;
import mindustry.ui.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;

import static mindustry.Vars.*;

public class PayloadDrill extends Drill{
	public int minBlockSize = 1, maxBlockSize = 2;
	
	public PayloadDrill(String name){
		super(name);
		
		configurable = true;
		
		config(Block.class, (PayloadDrillBuild tile, Block block) -> {
			if(tile.recipe != block) tile.progress = 0f;
			if(canProduce(block)){
				tile.recipe = block;
			}
		});
	}
	
	@Override
	public boolean canProduce(Block b){
		return b instanceof StorageBlock && b.isVisible() && b.size >= minBlockSize && b.size <= maxBlockSize;
	}
	
	public class PayloadDrillBuild<T extends BuildPayload>  extends Drill.DrillBuild{
		public @Nullable T payload;
		public @Nullable Block recipe;
		
		
		public @Nullable Block recipe(){
			return recipe;
		}
		
        @Override
        public void buildConfiguration(Table table){
            ItemSelection.buildTable(table, content.blocks().select(PayloadDrill.this::canProduce), () -> recipe, this::configure);
        }
		
        @Override
        public Object config(){
            return recipe;
        }
		
		@Override
        public void updateTile(){
			if(dominantItem == null){
                return;
            }
			
			timeDrilled += warmup * delta();
			
			if(recipe() != null && payload == null && dominantItems > 0 && consValid()){
				
				float speed = 1f;
				
				if(cons.optionalValid()){
                    speed = liquidBoostIntensity;
                }

                speed *= efficiency(); // Drill slower when not at full power
				
				lastDrillSpeed = (speed * dominantItems * warmup) / (drillTime + hardnessDrillMultiplier * dominantItem.hardness);
                warmup = Mathf.lerpDelta(warmup, speed, warmupSpeed);
                progress += delta() * dominantItems * speed * warmup;
				
				if(Mathf.chanceDelta(updateEffectChance * warmup))
                    updateEffect.at(x + Mathf.range(size * 2f), y + Mathf.range(size * 2f));
			}else{
                lastDrillSpeed = 0f;
                warmup = Mathf.lerpDelta(warmup, 0f, warmupSpeed);
                return;
            }

            float delay = drillTime + hardnessDrillMultiplier * dominantItem.hardness;

            if(dominantItems > 0 && progress >= delay && recipe() != null && payload == null){
                consume();
				payload = (T)(new BuildPayload(recipe(), team));
				
				if(payload.build.acceptItem(payload.build, dominantItem)){
					payload.build.items.add(dominantItem, block.payload.itemCapacity);
				}
				
				dumpPayload(payload);

                progress %= delay;

                drillEffect.at(x + Mathf.range(size), y + Mathf.range(size), dominantItem.color);
            }
        }
		
        @Override
        public void write(Writes write){
            super.write(write);
            write.s(recipe == null ? -1 : recipe.id);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            recipe = Vars.content.block(read.s());
        }
	}
}