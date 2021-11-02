package experimental.world.experimental;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.game.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.logic.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.meta.*;
import mindustry.entities.units.*;

import static mindustry.Vars.*;

public class PayloadDrill extends BlockProducer{
	public float hardnessDrillMultiplier = 50f;

    protected final ObjectIntMap<Item> oreCount = new ObjectIntMap<>();
    protected final Seq<Item> itemArray = new Seq<>();
	
	/* Payload */
	public float buildSpeed = 0.4f;
    public int minBlockSize = 1, maxBlockSize = 2;

    /** Maximum tier of blocks this drill can mine. */
    public int tier;
    /** Base time to drill one ore, in frames. */
    public float drillTime = 300;
    /** How many times faster the drill will progress when boosted by liquid. */
    public float liquidBoostIntensity = 1.6f;
    /** Speed at which the drill speeds up. */
    public float warmupSpeed = 0.015f;

    //return variables for countOre
    protected @Nullable Item returnItem;
    protected int returnCount;

    /** Whether to draw the item this drill is mining. */
    public boolean drawMineItem = true;
    /** Effect played when an item is produced. This is colored. */
    public Effect drillEffect = Fx.mine;
    /** Drill effect randomness. Block size by default. */
    public float drillEffectRnd = -1f;
    /** Speed the drill bit rotates at. */
    public float rotateSpeed = 2f;
    /** Effect randomly played while drilling. */
    public Effect updateEffect = Fx.pulverizeSmall;
    /** Chance the update effect will appear. */
    public float updateEffectChance = 0.02f;

    public boolean drawRim = false;
    public boolean drawSpinSprite = true;
	
	public Color heatColor = Color.valueOf("ff5512");
	public TextureRegion rimRegion, rotatorRegion, topRegion, itemRegion;
	
	public PayloadDrill(String name){
        super(name);
		
        update = true;
        solid = true;
		rotate = true;
		outputsPayload = true;
        group = BlockGroup.drills;
		hasItems = true;
        hasLiquids = true;
        itemCapacity = 100;
        liquidCapacity = 100f;
        ambientSound = Sounds.drill;
        ambientSoundVolume = 0.018f;
		
		configClear((PayloadDrillBuild tile) -> tile.recipe = null);
        config(Block.class, (PayloadDrillBuild tile, Block block) -> {
            if(tile.recipe != block) tile.progress = 0f;
            if(canProduce(block)){
                tile.recipe = block;
            }
        });
    }
	
	@Override
	public void load(){
		super.load();
		
		rimRegion = Core.atlas.find(name + "-rim");
		rotatorRegion = Core.atlas.find(name + "-rotator");
		topRegion = Core.atlas.find(name + "-top");
		itemRegion = Core.atlas.find(name + "item");
	}

    @Override
    public void init(){
        super.init();
        if(drillEffectRnd < 0) drillEffectRnd = size;
    }

    @Override
    public void drawRequestConfigTop(BuildPlan req, Eachable<BuildPlan> list){
        if(!req.worldContext) return;
        Tile tile = req.tile();
        if(tile == null) return;

        countOre(tile);
        if(returnItem == null || !drawMineItem) return;

        Draw.color(returnItem.color);
        Draw.rect(itemRegion, req.drawx(), req.drawy());
        Draw.color();
    }

    @Override
    public void setBars(){
        super.setBars();

        bars.add("drillspeed", (PayloadDrillBuild e) ->
             new Bar(() -> Core.bundle.format("bar.drillspeed", Strings.fixed(e.lastDrillSpeed * 60 * e.timeScale, 2)), () -> Pal.ammo, () -> e.warmup));
		bars.add("progress", (PayloadDrillBuild entity) -> new Bar(() -> Core.bundle.format("bar.items", entity.payload == null ? 0 : entity.payload.build.items.total()), () -> Pal.items, entity::fraction));
		bars.add("progress", (PayloadDrillBuild entity) -> new Bar("bar.progress", Pal.ammo, () -> entity.recipe() == null ? 0f : (entity.progress / entity.recipe().buildCost)));
    }

    public Item getDrop(Tile tile){
        return tile.drop();
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        if(isMultiblock()){
            for(Tile other : tile.getLinkedTilesAs(this, tempTiles)){
                if(canMine(other)){
                    return true;
                }
            }
            return false;
        }else{
            return canMine(tile);
        }
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);

        Tile tile = world.tile(x, y);
        if(tile == null) return;

        countOre(tile);

        if(returnItem != null){
            float width = drawPlaceText(Core.bundle.formatFloat("bar.drillspeed", 60f / (drillTime + hardnessDrillMultiplier * returnItem.hardness) * returnCount, 2), x, y, valid);
            float dx = x * tilesize + offset - width/2f - 4f, dy = y * tilesize + offset + size * tilesize / 2f + 5, s = iconSmall / 4f;
            Draw.mixcol(Color.darkGray, 1f);
            Draw.rect(returnItem.fullIcon, dx, dy - 1, s, s);
            Draw.reset();
            Draw.rect(returnItem.fullIcon, dx, dy, s, s);

            if(drawMineItem){
                Draw.color(returnItem.color);
                Draw.rect(itemRegion, tile.worldx() + offset, tile.worldy() + offset);
                Draw.color();
            }
        }else{
            Tile to = tile.getLinkedTilesAs(this, tempTiles).find(t -> t.drop() != null && t.drop().hardness > tier);
            Item item = to == null ? null : to.drop();
            if(item != null){
                drawPlaceText(Core.bundle.get("bar.drilltierreq"), x, y, valid);
            }
        }
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.drillTier, StatValues.blocks(b -> b instanceof Floor f && f.itemDrop != null && f.itemDrop.hardness <= tier));

        stats.add(Stat.drillSpeed, 60f / drillTime * size * size, StatUnit.itemsSecond);
        if(liquidBoostIntensity != 1){
            stats.add(Stat.boostEffect, liquidBoostIntensity * liquidBoostIntensity, StatUnit.timesSpeed);
        }
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{region, rotatorRegion, topRegion};
    }

    void countOre(Tile tile){
        returnItem = null;
        returnCount = 0;

        oreCount.clear();
        itemArray.clear();

        for(Tile other : tile.getLinkedTilesAs(this, tempTiles)){
            if(canMine(other)){
                oreCount.increment(getDrop(other), 0, 1);
            }
        }

        for(Item item : oreCount.keys()){
            itemArray.add(item);
        }

        itemArray.sort((item1, item2) -> {
            int type = Boolean.compare(!item1.lowPriority, !item2.lowPriority);
            if(type != 0) return type;
            int amounts = Integer.compare(oreCount.get(item1, 0), oreCount.get(item2, 0));
            if(amounts != 0) return amounts;
            return Integer.compare(item1.id, item2.id);
        });

        if(itemArray.size == 0){
            return;
        }

        returnItem = itemArray.peek();
        returnCount = oreCount.get(itemArray.peek(), 0);
    }

    public boolean canMine(Tile tile){
        if(tile == null || tile.block().isStatic()) return false;
        Item drops = tile.drop();
        return drops != null && drops.hardness <= tier;
    }
	
    public boolean canProduce(Block b){
        return b instanceof StorageBlock && b.isVisible() && b.size >= minBlockSize && b.size <= maxBlockSize && !(b instanceof CoreBlock) && !state.rules.bannedBlocks.contains(b);
    }

	public class PayloadDrillBuild extends BlockProducerBuild{
		public @Nullable Block recipe;
		
		public float drillProgress;
        public float warmup;
        public float timeDrilled;
        public float lastDrillSpeed;
		
        public int dominantItems;
        public Item dominantItem;
		
		public boolean shouldExport(){
            return payload != null && (
                (payload.block().hasItems && payload.block().separateItemCapacity && content.items().contains(i -> payload.build.items.get(i) >= payload.block().itemCapacity)));
        }
		
        @Override
        public @Nullable Block recipe(){
            return recipe;
        }
		
        @Override
        public void buildConfiguration(Table table){
            ItemSelection.buildTable(PayloadDrill.this, table, content.blocks().select(PayloadDrill.this::canProduce), () -> recipe, this::configure);
        }

        @Override
        public boolean onConfigureTileTapped(Building other){
            if(this == other){
                deselect();
                configure(null);
                return false;
            }

            return true;
        }
		
        @Override
        public Object config(){
            return recipe;
        }
		
        public float fraction(){
            return payload == null ? 0f : payload.build.items.total() / (float)payload.build.block.itemCapacity;
        }
		
        @Override
        public boolean acceptItem(Building source, Item item){
            return items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public int getMaximumAccepted(Item item){
            if(recipe() == null) return 0;
            for(ItemStack stack : recipe().requirements){
                if(stack.item == item) return stack.amount * 2;
            }
            return 0;
        }
		
        @Override
        public boolean acceptPayload(Building source, Payload payload){
            return false;
        }

        @Override
        public boolean shouldConsume(){
            return items.total() < itemCapacity && enabled;
        }

        @Override
        public boolean shouldAmbientSound(){
            return efficiency() > 0.01f && items.total() < itemCapacity;
        }

        @Override
        public float ambientVolume(){
            return efficiency() * (size * size) / 4f;
        }

        @Override
        public void drawSelect(){
            if(dominantItem != null){
                float dx = x - size * tilesize/2f, dy = y + size * tilesize/2f, s = iconSmall / 4f;
                Draw.mixcol(Color.darkGray, 1f);
                Draw.rect(dominantItem.fullIcon, dx, dy - 1, s, s);
                Draw.reset();
                Draw.rect(dominantItem.fullIcon, dx, dy, s, s);
            }
        }

        @Override
        public void pickedUp(){
            dominantItem = null;
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();

            countOre(tile);
            dominantItem = returnItem;
            dominantItems = returnCount;
        }

        @Override
        public void updateTile(){
			if(payload != null){
				payload.update(false);
			}
			if(shouldExport()){
				moveOutPayload();
			}
			
			var recipe = recipe();
			boolean produce = recipe != null && consValid() && payload == null;
			
			if(produce){
                progress += buildSpeed * edelta();

                if(progress >= recipe.buildCost){
                    consume();
                    payload = (T)(new BuildPayload(recipe, team));
                    Fx.placeBlock.at(x, y, payload.size() / tilesize);
                    payVector.setZero();
                    progress %= 1f;
                }
            }
			
			heat = Mathf.lerpDelta(heat, Mathf.num(produce), 0.15f);
            time += heat * delta();
			
			if(payload != null){
				
				if(dominantItem == null){
					return;
				}
				
				timeDrilled += warmup * delta();
				
				if(payload.items.total() < payload.block.itemCapacity && dominantItems > 0 && consValid()){
					
					float speed = 1f;
					
					if(cons.optionalValid()){
						speed = liquidBoostIntensity;
					}
					
					speed *= efficiency(); // Drill slower when not at full power
					
					lastDrillSpeed = (speed * dominantItems * warmup) / (drillTime + hardnessDrillMultiplier * dominantItem.hardness);
					warmup = Mathf.approachDelta(warmup, speed, warmupSpeed);
					drillProgress += delta() * dominantItems * speed * warmup;

					if(Mathf.chanceDelta(updateEffectChance * warmup))
						updateEffect.at(x + Mathf.range(size * 2f), y + Mathf.range(size * 2f));
				}else{
					lastDrillSpeed = 0f;
					warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
					return;
				}
				
				float delay = drillTime + hardnessDrillMultiplier * dominantItem.hardness;
				
				if(dominantItems > 0 && drillProgress >= delay && payload.items.total() < payload.block.itemCapacity){
					if(payload.build.acceptItem(payload.build, item)){
						payload.build.handleItem(payload.build, item);
					}

					drillProgress %= delay;
					
					drillEffect.at(x + Mathf.range(drillEffectRnd), y + Mathf.range(drillEffectRnd), dominantItem.color);
				}
			}
		}

        @Override
        public double sense(LAccess sensor){
            if(sensor == LAccess.drillProgress && dominantItem != null) return Mathf.clamp(drillProgress / (drillTime + hardnessDrillMultiplier * dominantItem.hardness));
            return super.sense(sensor);
        }

        @Override
        public void drawCracks(){}

        @Override
        public void draw(){
            float s = 0.3f;
            float ts = 0.6f;

            Draw.rect(region, x, y);
            super.drawCracks();

            if(drawRim){
                Draw.color(heatColor);
                Draw.alpha(warmup * ts * (1f - s + Mathf.absin(Time.time, 3f, s)));
                Draw.blend(Blending.additive);
                Draw.rect(rimRegion, x, y);
                Draw.blend();
                Draw.color();
            }

            if(drawSpinSprite){
                Drawf.spinSprite(rotatorRegion, x, y, timeDrilled * rotateSpeed);
            }else{
                Draw.rect(rotatorRegion, x, y, timeDrilled * rotateSpeed);
            }

            Draw.rect(topRegion, x, y);

            if(dominantItem != null && drawMineItem){
                Draw.color(dominantItem.color);
                Draw.rect(itemRegion, x, y);
                Draw.color();
            }
        }

        @Override
        public byte version(){
            return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(drillProgress);
            write.f(warmup);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            if(revision >= 1){
                drillProgress = read.f();
                warmup = read.f();
            }
        }
	}
}
