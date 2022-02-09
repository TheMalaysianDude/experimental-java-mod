package experimental.world.draw;

import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.world.*;
import experimental.world.experimental.*;

/** An implementation of custom rendering behavior for a block.
 * This is used mostly for mods. */
 public class ExDrawBlock{
	 protected static final Rand rand = new Rand();

    /** Draws the block. */
    public void draw(DebugDrawerBuild build){
        Draw.rect(build.block.region, build.x, build.y, build.block.rotate ? build.rotdeg() : 0);
    }

    /** Draws any extra light for the block. */
    public void drawLight(DebugDrawerBuild build){

    }

    /** Load any relevant texture regions. */
    public void load(Block block){

    }

    /** @return the generated icons to be used for this block. */
    public TextureRegion[] icons(Block block){
        return new TextureRegion[]{block.region};
    }
 }