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
	switchBlock, opacity; //, rgb;
	
	@Override
	public void load(){
		
		switchBlock = new ConfigBlock("switch"){{
			requirements(Category.effect, with(
				Items.copper, 1
			));
			
			size = 1;
		}};
		
		opacity = new OpacityBlock("opacity"){{
			requirements(Category.effect, with(
				Items.copper, 1
			));
			
			size = 1;
		}};
	}
}