package experimental.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.gen.*;

public class DrawLiquidScale extends ExDrawBlock{
	public TextureRegion liquid, top;
	
	@Override
    public void draw(DebugDrawer build){
		Block type = (Block)build.block;
		Float totalLiquids = build.progress;//build.liquids.total() / type.liquidCapacity;
		
		Draw.rect(build.block.region, build.x, build.y);
		
		Draw.color(build.liquids.current().color);
		Draw.rect(liquid, build.x, build.y, 
			(liquid.width/4) * totalLiquids,
			(liquid.height/4) * totalLiquids
		);
		Draw.color();
		
		if(top.found()) Draw.rect(top, build.x, build.y);
    }

    @Override
    public void load(Block block){
        liquid = Core.atlas.find(block.name + "-liquid");
        top = Core.atlas.find(block.name + "-top");
    }

    @Override
    public TextureRegion[] icons(Block block){
        return top.found() ? new TextureRegion[]{block.region, top} : new TextureRegion[]{block.region};
    }
}