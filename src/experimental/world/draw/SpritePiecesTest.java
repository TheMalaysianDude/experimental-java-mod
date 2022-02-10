package experimental.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.gen.*;

import experimental.world.experimental.DebugDrawer.*;

public class SpritePiecesTest extends ExDrawBlock{
	public int split = 2;
	public TextureRegion sprite;
	public TextureRegion[split*split] pieces;
	//public float[,] positions = {};
	
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
				TextureRegion piece = pieces[split * y + x];
				Draw.rect(piece,
					build.x + build.x - splitWidth/2 + splitWidth*x, build.y - splitHeight/2 + splitHeight*y,
					0
				);
				/*
				Draw.rect(sprite,
					build.x - splitWidth/2 + splitWidth*x, build.y - splitHeight/2 + splitHeight*y,
					splitWidth, splitHeight,
					splitWidth*x, splitHeight*y,
					0
				);
				*/
			}
		}
	}
	
	@Override
	public void load(Block block){
		sprite = Core.atlas.find(block.name + "-sprite");
		
		for(int x = 0; x < split; x++){
			for(int y = 0; y < split; y++){
				pieces[split * y + x] = new TextureRegion(sprite, 
					sprite.u/2 * x, sprite.v/2 * y, 
					sprite.u2/2 * (x + 1), sprite.v2 * (y + 1)
				);
			}
		}
	}
	
	@Override
	public TextureRegion[] icons(Block block){
		return new TextureRegion[]{block.region, sprite};
	}
}
