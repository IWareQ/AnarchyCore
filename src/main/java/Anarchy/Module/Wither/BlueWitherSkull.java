package Anarchy.Module.Wither;

import Anarchy.Utils.RandomUtils;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;

public class BlueWitherSkull extends EntityProjectile implements EntityExplosive {
	private static final int NETWORK_ID = 89;

	@Override
	public int getNetworkId() {
		return NETWORK_ID;
	}

	@Override
	public float getWidth() {
		return 0.25f;
	}

	@Override
	public float getLength() {
		return 0.25f;
	}

	@Override
	public float getHeight() {
		return 0.25f;
	}

	@Override
	public float getGravity() {
		return 0.01f;
	}

	@Override
	public float getDrag() {
		return 0.01f;
	}

	public BlueWitherSkull(FullChunk chunk, CompoundTag nbt) {
		this(chunk, nbt, null);
	}

	public BlueWitherSkull(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
		this(chunk, nbt, shootingEntity, false);
	}

	public BlueWitherSkull(FullChunk chunk, CompoundTag nbt, Entity shootingEntity, boolean critical) {
		super(chunk, nbt, shootingEntity);
	}

	@Override
	public boolean onUpdate(int currentTick) {
		if (this.closed) {
			return false;
		}
		if (this.age > 1200 || this.hadCollision) {
			this.explode();
			this.close();
		} else {
			this.level.addParticle(new SmokeParticle(this.add(this.getWidth() / 2 + RandomUtils.rand(-100.0, 100.0) / 500, this.getHeight() / 2 + RandomUtils.rand(-100.0, 100.0) / 500, this.getWidth() / 2 + RandomUtils.rand(-100.0, 100.0) / 500)));
		}
		return super.onUpdate(currentTick);
	}

	@Override
	public void onCollideWithEntity(Entity entity) {
		super.onCollideWithEntity(entity);
		entity.addEffect(Effect.getEffect(Effect.WITHER).setAmplifier(1).setDuration(140));
	}

	@Override
	public void explode() {
		ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, 1.1);
		Server.getInstance().getPluginManager().callEvent(ev);
		if (!ev.isCancelled()) {
			BlueWitherSkullExplosion blueWitherSkullExplosion = new BlueWitherSkullExplosion(this, (float) ev.getForce(), this.shootingEntity);
			if (ev.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
				blueWitherSkullExplosion.explodeA();
			}
			blueWitherSkullExplosion.explodeB();
		}
	}
}