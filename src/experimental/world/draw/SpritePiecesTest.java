package experimental.world.draw;

import arc.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.gen.*;

import experimental.world.experimental.DebugDrawer.*;

public class SpritePiecesTest extends ExDrawBlock{
	public int split = 2;
	
	public TextureRegion sprite, armBase, armHead, armSide;
	public TextureRegion[][] pieces;
	public Vec2 priorityPosition = new Vec2();
	
	@Override
	public void draw(DebugDrawerBuild build){
		Block type = (Block)build.block;
		
		if(!sprite.found() && pieces.length == 0) return;
		
		Draw.rect(type.region, build.x, build.y);
		for(int cx = 0; cx < split; cx++){
			for(int cy = 0; cy < split; cy++){
				
				float index = split * cy + cx;
				float row = 1f/(split*split);
				
				//distance between progress and index and clamp to prevent size overflow
				float progress = Mathf.clamp((Math.abs((row*index) - build.progress)) / row, 0, 1);
				
				float powerX = cx - (split - 1) / 2f;
				float powerY = (split - 1) / 2f - cy;
				float x = Angles.trnsx(build.rotdeg(), powerX, powerY);
				float y = Angles.trnsy(build.rotdeg(), powerX, powerY);
				
				//draw if progress is higher than row*index (like 0 or 0.5)
				if(build.progress >= row*index){
					TextureRegion piece = pieces[cx][cy];
					
					Draw.rect(piece, 
						build.x + piece.width * x / 4,
						build.y + piece.height * y / 4,
						piece.width/4 * progress,
						piece.height/4 * progress,
						build.rotation
					);
					
					priorityPosition.set(x, y);
				}
			}
		}
		
		float x = Angles.trnsx(build.rotdeg(), priorityPosition.x, type.size * 4);
		float y = Angles.trnsy(build.rotdeg(), 0, type.size * 4);
		
		Draw.rect(armBase,
			build.x + x,
			build.y + y,
			build.rotation
		);
	}
	
	public void loadSprite(String name){
		TextureRegion texture = Core.atlas.find(name);
		
		if(!texture.found()) return;
		sprite = texture;
		pieces = sprite.texture.split(sprite.width/split, sprite.height/split);
	}
	
	@Override
	public void load(Block block){
		split = Math.max(block.size - 1, 1);
		
		sprite = Core.atlas.find(block.name + "-sprite");
		armBase = Core.atlas.find(block.name + "-arm-base");
		armHead = Core.atlas.find(block.name + "-arm-head");
		armSide = Core.atlas.find(block.name + "-arm-side");
		
		loadSprite(sprite);
	}
	
	@Override
	public TextureRegion[] icons(Block block){
		return new TextureRegion[]{block.region, sprite};
	}
}
