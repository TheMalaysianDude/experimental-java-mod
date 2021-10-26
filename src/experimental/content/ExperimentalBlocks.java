package experimental.content;

import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import experimental.world.experimental.*;

import static mindustry.type.ItemStack.*;

public class ExperimentalBlocks implements ContentList{
	public static Block
	
	//experimental
	testBlock, testDrill;
	
	@Override
	public void load(){
		
		//experimental
		testBlock = new ConfigBlock("test-block"){{
			requirements(Category.effect, with(
				Items.copper, 1,
				Items.lead, 1
			));
			
			size = 1;
		}};
		
		testDrill = new PayloadDrill("test-drill"){{
			requirements(Category.production, with(
				Items.copper, 1,
				Items.lead, 1
			));
			
			maxBlockSize = 3;
			size = 5;
			
			drillTime = 5000f;
			drawRim = false;
		}};
	}
}