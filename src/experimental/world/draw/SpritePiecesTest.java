package experimental.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.gen.*;

import experimental.world.experimental.DebugDrawer.*;

public class SpritePiecesTest extends ExDrawBlock{
	public TextureRegion sprite;
	public int split = 2;
	//public float[] positions = {};
	
	@Override
	public void draw(DebugDrawerBuild build){
		Block type = (Block)build.block;
		
		int width = sprite.width;
		int height = sprite.height;
		
		Draw.rect(type.region, build.x, build.y);
		for(int i = 0; i < type.size; i++){
			Draw.rect(sprite, build.x - (width/(split*4)), build.y - (height/(split*4)));
		}
	}
	
	@Override
	public void load(Block block){
		sprite = Core.atlas.find(block.name + "-sprite");
	}
	
	@Override
	public TextureRegion[] icons(Block block){
		return new TextureRegion[]{block.region, sprite};
	}
}
