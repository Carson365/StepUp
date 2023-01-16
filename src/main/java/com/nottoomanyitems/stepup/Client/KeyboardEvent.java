package com.nottoomanyitems.stepup.Client;

import com.nottoomanyitems.stepup.StepUp;
import com.nottoomanyitems.stepup.worker.StepChanger;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)

@Mod.EventBusSubscriber(modid = StepUp.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyboardEvent {
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent e) {
        StepChanger.myKey = new KeyMapping("key.stepup.desc", GLFW.GLFW_KEY_H, "key.categories.stepup");
        e.register(StepChanger.myKey);
        //System.out.println("registering keys"); // to debug this
        // registering
    }

}
