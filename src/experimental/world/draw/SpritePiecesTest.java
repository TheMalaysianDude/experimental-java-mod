package experimental.world.draw;

import arc.*;
import arc.util.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.gen.*;

import experimental.world.experimental.DebugDrawer.*;

public class SpritePiecesTest extends ExDrawBlock{
	public int split = 2;
	public TextureRegion sprite;
	public TextureRegion[][] pieces = new TextureRegion[split-1][split-1];
	//public float[,] positions = {};
	
	@Override
	public void draw(DebugDrawerBuild build){
		Block type = (Block)build.block;
		
		int width = sprite.width;
		int height = sprite.height;
		//offset from center
		//int splitWidth = width/(split*4);
		//int splitHeight = height/(split*4);
		
		Draw.rect(type.region, build.x, build.y);
		for(int y = 0; y < split; y++){
			for(int x = 0; x < split; x++){
				TextureRegion piece = pieces[x][y];
				Draw.rect(piece, 
					build.x + piece.width*x,
					build.y + piece.height*y
				);
			}
		}
	}
	
	@Override
	public void load(Block block){
		sprite = Core.atlas.find(block.name + "-sprite");
		
		pieces = sprite.split(sprite.width/split, sprite.height/split);
	}
	
	@Override
	public TextureRegion[] icons(Block block){
		return new TextureRegion[]{block.region, sprite};
	}
}
