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
	
	//draw rotators (not rotator)
	public boolean drawRotators;
	//rotators speed per tick
	public float rotatorsSpeed;
	//draw rotators geometrically
	public int rotatorsSide;
	//rotators x offset
	public float xOffset;
	//rotatos y offset
	public float yOffset;
	
	public TextureRegion rotatorsRegion, rotatorsTopRegion;
	
	public PayloadDrill(String name){
		super(name);
		
		configurable = true;
		rotate = true;
		
		config(Block.class, (PayloadDrillBuild tile, Block block) -> {
			if(tile.recipe != block) tile.progress = 0f;
			if(canProduce(block)){
				tile.recipe = block;
			}
		});
	}
	
	@Override
	public void load(){
		super.load();
		
		rotatorsRegion = Core.atlas.find(name + "-rotators");
		rotatorsTopRegion = Core.atlas.find(name + "-rotators-top");
	}
	
	@Override
	public TextureRegion[] icons(){
		return new TextureRegion[]{region, topRegion}
	}
	
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
			if(payload != null){
				dumpPayload(payload);
				payload = null;
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
					payload.build.items.add(dominantItem, recipe().itemCapacity);
				}

                progress %= delay;

                drillEffect.at(x + Mathf.range(size), y + Mathf.range(size), dominantItem.color);
            }
        }
		
		@Override
		public void draw(){
            float s = 0.3f;
            float ts = 0.6f;

            Draw.rect(region, x, y);
            super.drawCracks();

            if(drawRim){
                Draw.color(heatColor);
                Draw.alpha(warmup * ts * (1f - s + Mathf.absin(Time.time, 3f, s)));
                Draw.blend(Blending.additive);
                Draw.rect(rimRegion, x, y);
                Draw.blend();
                Draw.color();
            }
			
			if(drawRotators){
				for(int i = 0; i < rotatorsSide; i++){
					float xs = Angles.trnsx((i * (360/(float)rotatorsSide)) + (timeDrilled * rotateSpeed), xOffset, yOffset);
					float ys = Angles.trnsy((i * (360/(float)rotatorsSide)) + (timeDrilled * rotateSpeed), xOffset, yOffset);
					
					Draw.rect(rotatorsRegion, x + xs, y + ys, timeDrilled * rotatorsSpeed);
					Draw.rect(rotatorsTopRegion, x + xs, y + ys);
				}
			}else{
				Draw.rect(rotatorRegion, x, y, timeDrilled * rotateSpeed);
			}
			
			Draw.rect(outRegion, x, y, rotdeg());
			
			//draw recipe
			if(recipe != null){
				Draw.draw(Layer.blockBuilding, () -> {
					Draw.color(Pal.accent);
				
					for(TextureRegion region : recipe.getGeneratedIcons()){
						Shaders.blockbuild.region = region;
						Shaders.blockbuild.progress = progress / recipe.buildCost;
						
						Draw.rect(region, x, y, recipe.rotate ? rotdeg() : 0);
						Draw.flush();
					}
					
					Draw.color();
				});
			}
			
			
		}
		
		/*
				Draw.rect(block.outRegion, this.x, this.y, this.rotdeg())
				
				//draw payload
				Draw.draw(Layer.blockBuilding, () => {
					Draw.color(Pal.accent);
					for(let i = 0; i < regions.length; i++){
						let region = regions[i];
						
						Shaders.blockbuild.region = region;
						Shaders.blockbuild.progress = this.progress / this.delay();
						
						Draw.rect(region, this.x, this.y);
						Draw.flush();
					};
				
					Draw.color();
				});
				Draw.reset();
				Draw.z(Layer.blockBuilding + 1)
				
				this.drawPayload();
				
				Draw.rect(block.topRegion, this.x, this.y);
				
				if(this.dominantItem != null && block.drawMineItem){
					Draw.color(this.dominantItem.color);
					Draw.rect(block.itemRegion, this.x, this.y);
					Draw.color();
				}
			},
		*/
		
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