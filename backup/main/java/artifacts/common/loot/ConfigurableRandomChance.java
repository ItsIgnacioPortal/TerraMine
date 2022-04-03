package terracraft.common.loot;

import terracraft.Artifacts;
import terracraft.common.init.ModLootConditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class ConfigurableRandomChance implements LootItemCondition {

    private final float defaultProbability;

    private ConfigurableRandomChance(float defaultProbability) {
        this.defaultProbability = defaultProbability;
    }

    public LootItemConditionType getType() {
        return ModLootConditions.CONFIGURABLE_ARTIFACT_CHANCE;
    }

    public boolean test(LootContext context) {
        if (Artifacts.CONFIG.worldgen.artifactRarity >= 9999) {
            return false;
        }
        float c = Artifacts.CONFIG.worldgen.artifactRarity;
        float p = defaultProbability;
        return context.getRandom().nextFloat() < p / (p + c - c * p);
    }

    public static LootItemCondition.Builder configurableRandomChance(float probability) {
        return () -> new ConfigurableRandomChance(probability);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ConfigurableRandomChance> {

        public void serialize(JsonObject object, ConfigurableRandomChance condition, JsonSerializationContext context) {
            object.addProperty("default_probability", condition.defaultProbability);
        }

        public ConfigurableRandomChance deserialize(JsonObject object, JsonDeserializationContext context) {
            return new ConfigurableRandomChance(GsonHelper.getAsFloat(object, "default_probability"));
        }
    }
}
