package experimental.world.experimental;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.units.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.world.consumers.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;

import static mindustry.Vars.*;

public class PayloadDrill extends PayloadBlock {
	public int minBlockSize = 1, maxBlockSize = 3;
	public float buildSpeed = 0.4f;
	
	public PayloadDrill(String name){
		super(name);
		
		size = 5;
        update = true;
        hasItems = true;
        hasPower = true;
		
		configurable = true;
		rotate = true;
		outputsPayload = true;
		
		consumes.add(new ConsumeItemDynamic((PayloadDrillBuild e) -> e.recipe != null ? e.recipe.requirements : ItemStack.empty));
		
		config(Block.class, (PayloadDrillBuild tile, Block block) -> {
			if(tile.recipe != block) tile.progress = 0f;
			if(canProduce(block)){
				tile.recipe = block;
			}
		});
	}
	
	public boolean canProduce(Block b){
		return b instanceof StorageBlock && b.isVisible() && b.size >= minBlockSize && b.size <= maxBlockSize;
	}
	
	public class PayloadDrillBuild extends PayloadBlock.PayloadBlockBuild{
		public @Nullable Block recipe;
		public float progress, time, heat;
		
        @Override
        public void buildConfiguration(Table table){
            ItemSelection.buildTable(table, content.blocks().select(PayloadDrill.this::canProduce), () -> recipe, this::configure);
        }
		
		@Override
        public boolean onConfigureTileTapped(Building other){
            if(this == other){
                deselect();
                configure(null);
                return false;
            }

            return true;
        }
		
        @Override
        public Object config(){
            return recipe;
        }
		
		@Override
        public boolean acceptItem(Building source, Item item){
            return items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public int getMaximumAccepted(Item item){
            if(recipe() == null) return 0;
            for(ItemStack stack : recipe().requirements){
                if(stack.item == item) return stack.amount * 2;
            }
            return 0;
        }
		
		@Override
        public void updateTile(){
            super.updateTile();
            boolean produce = recipe != null && consValid() && payload == null;

            if(produce){
                progress += buildSpeed * edelta();

                if(progress >= recipe.buildCost){
                    consume();
                    payload = new BuildPayload(recipe, team);
                    Fx.placeBlock.at(x, y, payload.size() / tilesize);
                    payVector.setZero();
                    progress %= 1f;
                }
            }

            heat = Mathf.lerpDelta(heat, Mathf.num(produce), 0.15f);
            time += heat * delta();

            moveOutPayload();
        }

        @Override
        public void draw(){
            Draw.rect(region, x, y);
            Draw.rect(outRegion, x, y, rotdeg());
			
            if(recipe != null){
                Drawf.shadow(x, y, recipe.size * tilesize * 2f, progress / recipe.buildCost);
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
                Draw.z(Layer.blockBuilding + 1);
                Draw.color(Pal.accent, heat);

                Lines.lineAngleCenter(x + Mathf.sin(time, 10f, Vars.tilesize / 2f * recipe.size + 1f), y, 90, recipe.size * Vars.tilesize + 1f);

                Draw.reset();
            }

            drawPayload();

            Draw.z(Layer.blockBuilding + 1.1f);
            Draw.rect(topRegion, x, y);
        }
		
		@Override
        public void write(Writes write){
            super.write(write);
            write.s(recipe == null ? -1 : recipe.id);
			write.f(progress);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            recipe = Vars.content.block(read.s());
			progress = read.f();
        }
	}
}