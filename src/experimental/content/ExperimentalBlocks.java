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

import static mindustry.type.ItemStack.*;

public class ExperimentalBlocks implements ContentList{
	public static Block
	
	//experimental
	testBlock;
	
	@Override
	public void load(){
		
		//experimental
		testBlock = new GenericCrafter("test-block"){{
			requirements(Category.crafting, with(
				Items.copper, 1,
				Items.lead, 1
			));
			
			outputItem = new ItemStack(Items.graphite, 3);
			craftTime  = 27f;
			size = 3;
			hasPower = hasItems = hasLiquids = true;
			itemCapacity = 10;
			
			consumes.item(Items.copper, 1);
			consumes.liquid(Liquids.water, 0.1f);
			consumes.power(0.5f);
		}};
		
	}
}