package terramine.client.render.trinket.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import terramine.TerraMine;
import terramine.client.render.RenderTypes;

public class GlowingCurioRenderer extends TrinketRenderer {

    private final ResourceLocation glowTexture;

    public GlowingCurioRenderer(String name, HumanoidModel<LivingEntity> model) {
        super(String.format("%s/%s", name, name), model);
        this.glowTexture = TerraMine.id(String.format("textures/entity/trinket/%s/%s_glow.png", name, name));
    }

    private ResourceLocation getGlowTexture() {
        return glowTexture;
    }

    @Override
    protected void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, boolean hasFoil) {
        super.render(poseStack, multiBufferSource, light, hasFoil);
        RenderType renderType = RenderTypes.unlit(getGlowTexture());
        VertexConsumer builder = ItemRenderer.getFoilBuffer(multiBufferSource, renderType, false, hasFoil);
        getModel().renderToBuffer(poseStack, builder, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
