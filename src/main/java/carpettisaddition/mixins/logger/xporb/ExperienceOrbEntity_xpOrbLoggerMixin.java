package carpettisaddition.mixins.logger.xporb;

import carpettisaddition.logging.loggers.entity.XPOrbLogger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntity_xpOrbLoggerMixin extends Entity
{
	private boolean flagDied = false;
	private boolean flagDespawned = false;

	public ExperienceOrbEntity_xpOrbLoggerMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=6000"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ExperienceOrbEntity;remove()V"
			)
	)
	void onDespawned(CallbackInfo ci)
	{
		if (!this.world.isClient && !this.flagDespawned)
		{
			XPOrbLogger.getInstance().onEntityDespawn((ExperienceOrbEntity)(Object)this);
			this.flagDespawned = true;
		}
	}

	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ExperienceOrbEntity;remove()V"
			)
	)
	void onDied(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		if (!this.world.isClient && !this.flagDied)
		{
			XPOrbLogger.getInstance().onEntityDied((ExperienceOrbEntity)(Object)this, source, amount);
			this.flagDied = true;
		}
	}
}
