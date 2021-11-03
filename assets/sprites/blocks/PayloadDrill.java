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

public class PayloadDrill extends PayloadBlock {
	
	public PayloadDrill(String name){
		super(name);
		
		configurable = true;
		rotate = true;
		outputsPayload = true;
		
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
		public float progress;
		
		
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