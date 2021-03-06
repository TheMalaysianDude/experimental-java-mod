package experimental.content;

import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import mindustry.type.*;
import mindustry.content.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.world.draw.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.entities.part.*;

import static mindustry.type.ItemStack.*;

public class ExperimentalBlocks{
	public static Block
	
	//region Turrets
	clevicure;
	
	public static void load(){
		
		clevicure = new ItemTurret("clevicure"){{
			requirements(Category.turret, BuildVisibility.sandboxOnly, with());
			ammo(
				Items.copper, Bullets.standardCopper
			);
			
			drawer = new DrawTurret("reinforced-"){{
				parts.addAll(
					new RegionPart("-mid-bottom"){{
						progress = PartProgress.warmup;
						moves.add(new PartMove(PartProgress.recoil, 0f, -3f, 0f));
					}}
				);
			}};
			
			size = 6;
			alwaysUnlocked = true;
		}};
		
		
	}
	
	/*
	diffuse = new ItemTurret("diffuse"){{
            requirements(Category.turret, with(Items.beryllium, 150, Items.silicon, 200, Items.graphite, 200, Items.tungsten, 50));

            ammo(
            Items.graphite, new BasicBulletType(8f, 41){{
                knockback = 4f;
                width = 25f;
                hitSize = 7f;
                height = 20f;
                shootEffect = Fx.shootBigColor;
                smokeEffect = Fx.shootSmokeSquareSparse;
                ammoMultiplier = 1;
                hitColor = backColor = trailColor = Color.valueOf("ea8878");
                frontColor = Pal.redLight;
                trailWidth = 6f;
                trailLength = 3;
                hitEffect = despawnEffect = Fx.hitSquaresColor;
                buildingDamageMultiplier = 0.2f;
            }}
            );

            shoot = new ShootSpread(15, 4f);

            coolantMultiplier = 6f;

            inaccuracy = 0.2f;
            velocityRnd = 0.17f;
            shake = 1f;
            ammoPerShot = 3;
            maxAmmo = 30;
            consumeAmmoOnce = true;

            drawer = new DrawTurret("reinforced-"){{
                parts.add(new RegionPart("-front"){{
                    progress = PartProgress.warmup;
                    moveRot = -10f;
                    mirror = true;
                    moves.add(new PartMove(PartProgress.recoil, 0f, -3f, -5f));
                    heatColor = Color.red;
                }});
            }};
            shootY = 5f;
            outlineColor = Pal.darkOutline;
            size = 3;
            envEnabled |= Env.space;
            reload = 30f;
            recoil = 2f;
            range = 125;
            shootCone = 40f;
            scaledHealth = 210;
            rotateSpeed = 3f;

            coolant = consume(new ConsumeLiquid(Liquids.water, 15f / 60f));
            limitRange();
        }};
	*/
}