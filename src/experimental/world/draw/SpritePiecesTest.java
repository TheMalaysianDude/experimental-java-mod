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
	public ObjectMap<String, TextureRegion> sprites;
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
				
				float index = split * y + x;
				float row = 1f/(split*split);
				
				//draw if progress is higher than row*index (like 0 or 0.5)
				if(build.progress >= row*index){
					
					//distance between progress and index and limits to prevent size overflow
					float progress = Mathf.clamp((Math.abs((row*index) - build.progress)) / row, 0, 1);
					
					var powerX = x - (split - 1) / 2f;
					var powerY = (split - 1) / 2f - y;
				
					TextureRegion piece = pieces[x][y];
					
					Draw.rect(piece, 
						build.x + piece.width * powerX / 4,
						build.y + piece.height * powerY / 4,
						piece.width/4 * progress,
						piece.height/4 * progress
					);
				}
				
				
			}
		}
	}
	
	public void loadSprite(String name){
		TextureRegion texture = Core.atlas.find(name);
		
		if(!texture.found()) return;
		sprites[name] = {
			texture,
			texture.flip(texture.width/split, texture.height/split)
		};
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
