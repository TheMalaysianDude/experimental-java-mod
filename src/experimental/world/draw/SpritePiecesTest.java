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
		//offset from center
		int splitWidth = width/(split*8);
		int splitHeight = height/(split*8);
		
		Draw.rect(type.region, build.x, build.y);
		for(int x = 0; x < split; x++){
			for(int y = 0; y < split; y++){
				Draw.rect(sprite, build.x - splitWidth + splitWidth*x, build.y - splitHeight + splitHeight*y);
			}
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
