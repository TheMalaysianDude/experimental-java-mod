package experimental.content;

import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.graphics.*;
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
				Items.graphite, new BasicBulletType(2.5f, 9){{
                    width = 7f;
                    height = 9f;
                    lifetime = 60f;
                    ammoMultiplier = 2;
                }}
			);
			
			drawer = new DrawTurret(){{
				parts.addAll(
					new RegionPart("-top"){{
						progress = PartProgress.warmup;
						//moves.add(new PartMove(PartProgress.recoil, 0f, -0, 0f));
						x = 9.25f; y = 14.25f; 
						mirror = true;
						turretShading = true;
					}},
					new RegionPart("-mid"){{
						progress = PartProgress.warmup;
						//moves.add(new PartMove(PartProgress.recoil, 0f, -0, 0f));
						x = 9.25f; y = 0f; 
						mirror = true;
						turretShading = true;
					}},
					new RegionPart("-mid-bottom"){{
						progress = PartProgress.warmup;
						//moves.add(new PartMove(PartProgress.recoil, 0f, -0f, 0f));
						y = -2.75f;
					}},
					new RegionPart("-mid-top"){{
						progress = PartProgress.warmup;
						//moves.add(new PartMove(PartProgress.recoil, 0f, -0f, 0f));
						y = 4.25f;
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