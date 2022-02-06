package experimental.world.experimental;

import arc.struct.*;
import mindustry.world.*;
import mindustry.gen.*;
import mindustry.world.draw.*;

public class DebugDrawer extends Block{
	public Seq<DrawBlock> drawers = new Seq<>();
	
	public DebugDrawer(String name){
		super(name);
	}
	
	public class DebugDrawerBuild extends Building{
		
	}
}