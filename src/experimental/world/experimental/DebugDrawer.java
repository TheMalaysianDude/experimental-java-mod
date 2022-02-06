package experimental.world.experimental;

import arc.struct.*;
import mindustry.world.*;
import mindustry.gen.*;
import mindustry.world.draw.*;
import experimental.world.draw.*;

public class DebugDrawer extends Block{
	public static DrawBlock[] drawers = {
		DrawLiquidScale = new DrawLiquidScale
	};
	
	public DebugDrawer(String name){
		super(name);
	}
	
	public class DebugDrawerBuild extends Building{
		
	}
}