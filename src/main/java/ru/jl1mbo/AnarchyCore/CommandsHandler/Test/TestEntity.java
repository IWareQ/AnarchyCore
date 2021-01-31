package ru.jl1mbo.AnarchyCore.CommandsHandler.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.nimbusds.jose.util.StandardCharset;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.PlayerSkinPacket;
import cn.nukkit.utils.Utils;
import ru.jl1mbo.AnarchyCore.Main;

public class TestEntity extends Entity {

	protected UUID uuid;

	protected byte[] rawUUID;

	protected Skin skin;

	public TestEntity(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);

	}

	@Override
	public final int getNetworkId() {
		return EntityHuman.NETWORK_ID;
	}

	public UUID getUniqueId() {
		return this.uuid;
	}

	public byte[] getRawUniqueId() {
		return this.rawUUID;
	}

	public Skin getSkin() {
		return this.skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	public String getSkinName() {
		return "cubetest";
	}

	protected void initEntity() {
		super.initEntity();

		skin = generate(getSkinName());
		uuid = Utils.dataToUUID(String.valueOf(this.getId()).getBytes(StandardCharsets.UTF_8), this.getSkin().getSkinData().data, this.getNameTag().getBytes(StandardCharsets.UTF_8));

		setMaxHealth(30);
		setHealth(getMaxHealth());
		spawnToAll();
	}

	@Override
	public void spawnTo(Player player) {
		if (!this.hasSpawned.containsKey(player.getLoaderId())) {
			this.hasSpawned.put(player.getLoaderId(), player);

			server.updatePlayerListData(getUniqueId(), getId(), getName(), skin, new Player[] {player});

			AddPlayerPacket pk = new AddPlayerPacket();
			pk.uuid = getUniqueId();
			pk.username = getName();
			pk.entityUniqueId = getId();
			pk.entityRuntimeId = getId();
			pk.x = (float) x;
			pk.y = (float) y;
			pk.z = (float) z;
			pk.speedX = (float) motionX;
			pk.speedY = (float) motionY;
			pk.speedZ = (float) motionZ;
			pk.yaw = (float) yaw;
			pk.pitch = (float) pitch;
			pk.item = Item.get(0);
			pk.metadata = dataProperties;
			player.dataPacket(pk);

			this.server.removePlayerListData(getUniqueId(), new Player[] {player});

			PlayerSkinPacket packet = new PlayerSkinPacket();
			packet.skin = skin;
			packet.newSkinName = getSkinName();
			packet.oldSkinName = "steve";
			packet.uuid = getUniqueId();
			player.dataPacket(packet);

			super.spawnTo(player);
		}
	}

	private Skin generate(String skinName) {
		try {
			Path path = Path.of(Main.getInstance().getDataFolder() + "/Skin");
			Skin skin = new Skin();

			skin.setSkinId(skinName);
			skin.setTrusted(true);
			skin.setSkinData(ImageIO.read(path.resolve(skinName + ".png").toFile()));
			skin.setGeometryName("geometry." + skinName);
			skin.setGeometryData(new String(Files.readAllBytes(path.resolve(skinName + ".json")), StandardCharset.UTF_8));

			return skin;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
