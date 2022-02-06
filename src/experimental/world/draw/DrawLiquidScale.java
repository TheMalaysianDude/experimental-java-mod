package experimental.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.gen.*;
import experimental.world.experimental.*;

public class DrawLiquidScale extends DrawBlock{
	public TextureRegion liquid, top;
	
	@Override
    public void draw(Building build){
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
		
		DebugDrawer.drawers.add(this);
    }

    @Override
    public TextureRegion[] icons(Block block){
        return top.found() ? new TextureRegion[]{block.region, top} : new TextureRegion[]{block.region};
    }
}