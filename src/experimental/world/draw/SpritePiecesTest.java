package experimental.world.draw;

import arc.*;
import arc.math.*;
import arc.util.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.gen.*;

import experimental.world.experimental.DebugDrawer.*;

public class SpritePiecesTest extends ExDrawBlock{
	public int split = 2;
	public TextureRegion sprite;
	public TextureRegion[][] pieces;
	
	@Override
	public void draw(DebugDrawerBuild build){
		Block type = (Block)build.block;
		
		int width = sprite.width;
		int height = sprite.height;
		
		Draw.rect(type.region, build.x, build.y);
		for(int x = 0; x < split; x++){
			for(int y = 0; y < split; y++){
				//var magnitude = build.block.size * 8 * build.progress / split;
				float index = split * y + x;
				
				/*
				split = 2
				process = {
				 -0    - 0.25
				 -0.25 - 0.5
				 -0.5  - 0.75
				 -0.75 - 1
				}
				
				progress = 0 - 1
				
				Mathf.clamp(build.progress, (1/(float)(split*split))*index, (1/(float)(split*split))*(index+1)
				*/
				
				//basically 0 to 1 for each piece
				float process = (index+1)/(float)(split*split);
				float progress = Mathf.clamp(build.progress, (1/(float)(split*split))*index, (1/(float)(split*split))*(index+1));
				var powerX = x - (split - 1) / 2f;
				var powerY = (split - 1) / 2f - y;
				
				if (build.progress >= process){
					TextureRegion piece = pieces[x][y];
					Draw.rect(piece, 
						build.x + powerX + piece.width * powerX / 4,
						build.y + powerY + piece.height * powerY / 4,
						piece.width/4 * progress,
						piece.height/4 * progress
					);
				}
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
