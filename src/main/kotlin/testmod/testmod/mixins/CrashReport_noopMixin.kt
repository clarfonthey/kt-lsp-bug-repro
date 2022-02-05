package testmod.testmod.mixins;

import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport::class)
public object CrashReport_noopMixin {
    private @Inject(method = ["initCrashReport"], at = [At("HEAD")]) @JvmStatic fun gameStarted(ci: CallbackInfo) {
    }
}
