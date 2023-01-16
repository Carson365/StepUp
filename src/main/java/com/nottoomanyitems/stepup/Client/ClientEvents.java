package com.nottoomanyitems.stepup.Client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.KeyboardInput;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nottoomanyitems.stepup.StepUp;
import com.nottoomanyitems.stepup.worker.StepChanger;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.Key;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(value = Dist.CLIENT,  modid = StepUp.MODID)
public class ClientEvents {
	public static boolean init = true;
	private static final Logger LOGGER = LogManager.getLogger("StepUp");
	
	@SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public static void clientTickEvent(final PlayerTickEvent event) {
    	if (event.player != null) {
    		StepChanger.TickEvent(event);
    	}
    	if (Minecraft.getInstance().isWindowActive() && init == true) {
    		init=false;
    		StepChanger.init();
    	}
    }
    
	
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
    	if(StepChanger.firstRun == false) {
    		StepChanger.onKeyInput(event);
    	}
    }
    
    
    @SubscribeEvent
    public static void joinWorld(final EntityJoinLevelEvent event) {
    }
    
    @SubscribeEvent
    public static void unload (LevelEvent.Unload event) {
    	LOGGER.info("WorldEvent.Unload");
    	init=true;
    }
    
    @SubscribeEvent
    public static void load (LevelEvent.Load event) {
    	LOGGER.info("WorldEvent.Load");
    }
}
