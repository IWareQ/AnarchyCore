package Anarchy.Module.Wither;

import Anarchy.Utils.RandomUtils;
import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;

public class EntityWither extends EntityLiving {
	private Entity followTarget = null;
	private Vector3 target = null;
	private boolean canAttack = true;
	private int ageTime = 7000;
	private int attackDelay = 0;
	private int stayTime = 0;
	private int moveTime = 0;

	private static final int NETWORK_ID = 52;

	EntityWither(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
	}

	private boolean isKnockback() {
		return this.attackTime > 0;
	}

	private double getSpeed() {
		return 1.2;
	}

	@Override
	public int getNetworkId() {
		return NETWORK_ID;
	}

	@Override
	public float getWidth() {
		return 0.9f;
	}

	@Override
	public float getHeight() {
		return 3.5f;
	}

	@Override
	public void initEntity() {
		super.initEntity();
		this.fireProof = true;
		this.setMaxHealth(600);
		this.setHealth(600);
	}

	@Override
	public Item[] getDrops() {
		return new Item[] {
			Item.get(Item.NETHER_STAR, 0, 1)
		};
	}

	@Override
	public boolean onUpdate(int currentTick) {
		if (this.server.getDifficulty() < 1) {
			this.close();
			return false;
		}

		if (!this.isAlive()) {
			if (++this.deadTicks >= 23) {
				this.close();
				return false;
			}
			return true;
		}

		int tickDiff = currentTick - this.lastUpdate;
		this.lastUpdate = currentTick;
		this.entityBaseTick(tickDiff);
		Vector3 target = this.updateMove(tickDiff);
		if (target instanceof Entity) {
			if (target != this.followTarget || this.canAttack) {
				this.attackEntity((Entity) target);
			}
		} else if (target != null && (Math.pow(this.x - target.x, 2) + Math.pow(this.z - target.z, 2)) <= 1) {
			this.moveTime = 0;
		}
		return true;
	}

	private void checkTarget() {
		if (this.isKnockback()) {
			return;
		}

		Vector3 target = this.target;
		if (! (target instanceof EntityCreature) || !this.targetOption((EntityCreature) target, this.distanceSquared(target))) {
			double near = Integer.MAX_VALUE;

			for (Entity entity: this.getLevel().getEntities()) {
				if (entity == this || !(entity instanceof EntityCreature)) {
					continue;
				}

				EntityCreature creature = (EntityCreature) entity;
				double distance = this.distanceSquared(creature);
				if (distance > near || !this.targetOption(creature, distance)) {
					continue;
				}
				near = distance;

				this.moveTime = 0;
				this.target = creature;
			}
		}

		if (this.target instanceof EntityCreature && ((EntityCreature) this.target).isAlive()) {
			return;
		}

		int x,
		y,
		z;
		int maxY = Math.max(this.getLevel().getHighestBlockAt((int) this.x, (int) this.z) + 15, 120);
		if (this.stayTime > 0) {
			if (RandomUtils.rand(1, 100) > 5) {
				return;
			}

			x = RandomUtils.rand(10, 30);
			z = RandomUtils.rand(10, 30);
			if (this.y > maxY) {
				y = RandomUtils.rand( - 12, -4);
			} else {
				y = RandomUtils.rand( - 10, 10);
			}
			this.target = this.add(RandomUtils.rand() ? x: -x, y, RandomUtils.rand() ? z: -z);
		} else if (RandomUtils.rand(1, 100) == 1) {
			x = RandomUtils.rand(10, 30);
			z = RandomUtils.rand(10, 30);
			if (this.y > maxY) {
				y = RandomUtils.rand( - 12, -4);
			} else {
				y = RandomUtils.rand( - 10, 10);
			}
			this.stayTime = RandomUtils.rand(100, 200);
			this.target = this.add(RandomUtils.rand() ? x: -x, y, RandomUtils.rand() ? z: -z);
		} else if (this.moveTime <= 0 || this.target != null) {
			x = RandomUtils.rand(20, 100);
			z = RandomUtils.rand(20, 100);
			if (this.y > maxY) {
				y = RandomUtils.rand( - 12, -4);
			} else {
				y = RandomUtils.rand( - 10, 10);
			}
			this.stayTime = 0;
			this.moveTime = RandomUtils.rand(100, 200);
			this.target = this.add(RandomUtils.rand() ? x: -x, y, RandomUtils.rand() ? z: -z);
		}
	}

	private void attackEntity(Entity player) {
		if (this.attackDelay > 23 && RandomUtils.rand(1, 5) < 3 && this.distance(player) <= 100) {
			this.attackDelay = 0;

			double f = 1;
			double yaw = this.yaw + RandomUtils.rand( - 120.0, 120.0) / 10;
			double pitch = this.pitch + RandomUtils.rand( - 70.0, 70.0) / 10;
			Location pos = new Location(this.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight(), this.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.level);
			Entity k = Entity.createEntity("BlueWitherSkull", pos, this);
			if (! (k instanceof BlueWitherSkull)) {
				return;
			}

			BlueWitherSkull blueskull = (BlueWitherSkull) k;
			blueskull.setMotion(new Vector3( - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f, Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));

			ProjectileLaunchEvent launch = new ProjectileLaunchEvent(blueskull);
			this.server.getPluginManager().callEvent(launch);
			if (launch.isCancelled()) {
				blueskull.kill();
			} else {
				blueskull.spawnToAll();
				this.level.addSound(this, Sound.MOB_WITHER_SHOOT);
			}
		}
	}

	private boolean targetOption(EntityCreature creature, double distance) {
		if (creature instanceof Player) {
			Player player = (Player) creature;
			return player.spawned && player.isAlive() && !player.closed && (player.isSurvival() || player.isAdventure()) && distance <= 200;
		}
		return creature.isAlive() && !creature.closed && distance <= 200;
	}

	private Vector3 updateMove(int tickDiff) {
		if (!isImmobile()) {
			if (this.isKnockback()) {
				this.move(this.motionX * tickDiff, this.motionY * tickDiff, this.motionZ * tickDiff);
				this.updateMovement();
				return null;
			}

			if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive()) {
				double x = this.followTarget.x - this.x;
				double y = this.followTarget.y - this.y;
				double z = this.followTarget.z - this.z;

				double diff = Math.abs(x) + Math.abs(z);
				if (this.stayTime > 0 || this.distance(this.followTarget) <= (this.getWidth() + 0.0d) / 2 + 0.05) {
					this.motionX = 0;
					this.motionZ = 0;
				} else {
					this.motionX = this.getSpeed() * 0.15 * (x / diff);
					this.motionZ = this.getSpeed() * 0.15 * (z / diff);
					this.motionY = this.getSpeed() * 0.27 * (y / diff);
				}
				if (this.stayTime <= 0 || RandomUtils.rand()) this.yaw = Math.toDegrees( - Math.atan2(x / diff, z / diff));
			}

			Vector3 before = this.target;
			this.checkTarget();
			if (this.target instanceof EntityCreature || before != this.target) {
				double x = this.target.x - this.x;
				double y = this.target.y - this.y;
				double z = this.target.z - this.z;

				double diff = Math.abs(x) + Math.abs(z);
				if (this.stayTime > 0 || this.distance(this.target) <= (this.getWidth() + 0.0d) / 2 + 0.05) {
					this.motionX = 0;
					this.motionZ = 0;
				} else {
					this.motionX = this.getSpeed() * 0.15 * (x / diff);
					this.motionZ = this.getSpeed() * 0.15 * (z / diff);
					this.motionY = this.getSpeed() * 0.27 * (y / diff);
				}
				if (this.stayTime <= 0 || RandomUtils.rand()) this.yaw = Math.toDegrees( - Math.atan2(x / diff, z / diff));
			}

			double dx = this.motionX * tickDiff;
			double dy = this.motionY * tickDiff;
			double dz = this.motionZ * tickDiff;
			Vector3 target = this.target;
			if (this.stayTime > 0) {
				this.stayTime -= tickDiff;
				this.move(0, dy, 0);
			} else {
				Vector2 be = new Vector2(this.x + dx, this.z + dz);
				this.move(dx, dy, dz);
				Vector2 af = new Vector2(this.x, this.z);

				if (be.x != af.x || be.y != af.y) {
					this.moveTime -= 90 * tickDiff;
				}
			}

			if (this.isOnGround()) {
				this.motionY = RandomUtils.rand(0.15, 0.20);
			} else {
				this.motionY = RandomUtils.rand( - 0.15, 0.15);
			}

			this.updateMovement();
			return target;
		}
		return null;
	}

	@Override
	protected DataPacket createAddEntityPacket() {
		AddEntityPacket addEntity = new AddEntityPacket();
		addEntity.type = this.getNetworkId();
		addEntity.entityUniqueId = this.getId();
		addEntity.entityRuntimeId = this.getId();
		addEntity.yaw = (float) this.yaw;
		addEntity.headYaw = (float) this.yaw;
		addEntity.pitch = (float) this.pitch;
		addEntity.x = (float) this.x;
		addEntity.y = (float) this.y;
		addEntity.z = (float) this.z;
		addEntity.speedX = (float) this.motionX;
		addEntity.speedY = (float) this.motionY;
		addEntity.speedZ = (float) this.motionZ;
		addEntity.metadata = this.dataProperties;
		addEntity.attributes = new Attribute[] {
			Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(200).setValue(200)
		};
		return addEntity;
	}

	@Override
	public boolean entityBaseTick(int tickDiff) {
		super.entityBaseTick(tickDiff);
		if (getServer().getDifficulty() == 0 || ageTime < 0) {
			this.close();
			return true;
		}

		ageTime--;
		if (this.attackDelay < 400) {
			this.attackDelay++;
		}

		return true;
	}
}