package terracraft.common.item.curio.feet;

import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import terracraft.TerraCraft;
import terracraft.common.init.ModItems;
import terracraft.common.init.ModSoundEvents;
import terracraft.common.item.curio.TrinketTerrariaItem;
import terracraft.common.trinkets.TrinketsHelper;
import terracraft.common.utility.RocketBootHelper;

import java.util.Random;
import java.util.UUID;

public class LightningBootsItem extends TrinketTerrariaItem {

	public static final AttributeModifier SPEED_BOOST_MODIFIER = new AttributeModifier(UUID.fromString("ac7ab816-2b08-46b6-879d-e5dea34ff305"),
			"terracraft:lightning_boots_movement_speed", 0.28, AttributeModifier.Operation.MULTIPLY_TOTAL);
	public static final AttributeModifier SPEED_BOOST_MODIFIER_WALK = new AttributeModifier(UUID.fromString("ac7ab816-2b08-46b6-879d-e5dea34ff306"),
			"terracraft:lightning_boots_movement_speed_walk", 0.08, AttributeModifier.Operation.MULTIPLY_TOTAL);
	public static final AttributeModifier STEP_HEIGHT_MODIFIER = new AttributeModifier(UUID.fromString("7e97cede-a343-411f-b465-14cdf6df3666"),
			"terracraft:lightning_boots_step_height", .5, AttributeModifier.Operation.ADDITION);
	private static final Random RANDOM = new Random();
	public RocketBootHelper rocketHelper = new RocketBootHelper();
	public double speed = 0.08D;

	public LightningBootsItem() {
		rocketHelper.setSoundSettings(ModSoundEvents.SPECTRE_BOOTS, 5f, 1f);
		rocketHelper.setParticleSettings(TerraCraft.BLUE_POOF, ParticleTypes.POOF);
	}

	@Override
	public void curioTick(LivingEntity player, ItemStack stack) {
		if (TrinketsHelper.isEquipped(ModItems.LIGHTNING_BOOTS, player) && !TrinketsHelper.isEquipped(ModItems.FROSTSPARK_BOOTS, player) && !TrinketsHelper.isEquipped(ModItems.TERRASPARK_BOOTS, player)) {
			Minecraft mc = Minecraft.getInstance();
			if (player.isSprinting() && player.isOnGround() && !player.isCrouching()) {
				float random = (RANDOM.nextFloat() - 0.5F) * 0.1F;
				mc.particleEngine.createParticle(ParticleTypes.POOF, player.getX(), player.getY() + 0.2F, player.getZ(), random, -0.2D, random);
			}
		}
		rocketHelper.rocketFly(true, speed, 4, player, this);
	}
	private void fly(LivingEntity player, double y) {
		Vec3 motion = player.getDeltaMovement();
		if (motion.y() >= 0)
		{
			player.moveRelative(1, new Vec3(0, y, 0));
		} else {
			player.moveRelative(1, new Vec3(0, -y + 1, 0));
		}
		//player.moveRelative(1, new Vec3(motion.x(), y, motion.z()));
	}

	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);

		if (movementSpeed == null) {
			TerraCraft.LOGGER.debug("Entity {} missing entity attribute(s)", this);
			return;
		}

		addModifier(movementSpeed, SPEED_BOOST_MODIFIER_WALK);
	}

	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
		AttributeInstance stepHeight = entity.getAttribute(StepHeightEntityAttributeMain.STEP_HEIGHT);

		if (movementSpeed == null || stepHeight == null) {
			TerraCraft.LOGGER.debug("Entity {} missing entity attribute(s)", this);
			return;
		}

		removeModifier(movementSpeed, SPEED_BOOST_MODIFIER);
		removeModifier(movementSpeed, SPEED_BOOST_MODIFIER_WALK);
		removeModifier(stepHeight, STEP_HEIGHT_MODIFIER);
	}
}

