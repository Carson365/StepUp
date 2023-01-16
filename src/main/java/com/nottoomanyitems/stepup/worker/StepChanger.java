package com.nottoomanyitems.stepup.worker;

import net.minecraft.network.chat.*;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

import com.nottoomanyitems.stepup.StepUp;
import com.nottoomanyitems.stepup.config.ConfigIO;
import com.nottoomanyitems.stepup.config.VersionChecker;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent.Key;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;



public class StepChanger {
	
	public static Player player;
    public static KeyMapping myKey;
    public static boolean firstRun = true;
    private static boolean forceStepUp = true;

    private static final Minecraft mc = Minecraft.getInstance();

    public StepChanger() {
        
    }

    public static void TickEvent(PlayerTickEvent event) {
    	int autoJumpState = ConfigIO.autoJumpState;
    	player = event.player;
        
		if (player.isCrouching()) {
            player.maxUpStep = .6f;
        } else if (autoJumpState == AutoJumpState.DISABLED.getLevelCode() && player.maxUpStep >= 1.0f && forceStepUp) { //All Disabled
        	player.maxUpStep = .6f;
        	forceStepUp = false;
        } else if (autoJumpState == AutoJumpState.ENABLED.getLevelCode() && player.maxUpStep < 1.0f) { //StepUp Enabled
            player.maxUpStep = 1.25f;
            forceStepUp = true;
        } else if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode() && player.maxUpStep >= 1.0f) { //Minecraft Enabled
            player.maxUpStep = .6f;
            forceStepUp = true;
        }
        autoJump();
    }

    public static void init() {
    	ConfigIO.CheckForServerIP();
    	ConfigIO.updateCFG();
    	int display_update_message = ConfigIO.display_update_message;
    	int display_start_message = ConfigIO.display_start_message;
    	
    	if(firstRun){
    	    //myKey =  new KeyMapping("key.stepup.desc", GLFW.GLFW_KEY_H, "key.categories.stepup");
            //net.minecraftforge.client.ClientRegistry.registerKeyBinding(myKey);
            if (!VersionChecker.isLatestVersion() && display_update_message == 1) {
                updateMessage();
            }
            firstRun = false;
    	}
    	autoJump();
    	if(display_start_message == 1){
    		message();
    	}
    }
    
    //@SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (StepChanger.myKey.isDown()) {
        	int autoJumpState = ConfigIO.autoJumpState;
            if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode()) {
            	ConfigIO.autoJumpState = AutoJumpState.DISABLED.getLevelCode(); //0 StepUp and Minecraft Disabled
            } else if (autoJumpState == AutoJumpState.DISABLED.getLevelCode()) {
            	ConfigIO.autoJumpState = AutoJumpState.ENABLED.getLevelCode(); //1 StepUp Enabled
            } else if (autoJumpState == AutoJumpState.ENABLED.getLevelCode()) {
            	ConfigIO.autoJumpState = AutoJumpState.MINECRAFT.getLevelCode(); //2 Minecraft Enabled
            }
            ConfigIO.updateCFG();
            autoJump();
            message();
        }
    }

    private static void autoJump() {    	
    	net.minecraft.client.Options settings = mc.options;
    	int autoJumpState = ConfigIO.autoJumpState;
    	
        boolean b = settings.autoJump().get();
        if (autoJumpState < AutoJumpState.MINECRAFT.getLevelCode() && b) {
            settings.autoJump().set(false);
        } else if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode() && !b) {
            settings.autoJump().set(true);
        }
    }

    private static void message() {
    	int autoJumpState = ConfigIO.autoJumpState;
        String m = ChatFormatting.DARK_AQUA + "[" + ChatFormatting.YELLOW + StepUp.MOD_NAME + ChatFormatting.DARK_AQUA + "]" + " ";
        if (autoJumpState == AutoJumpState.DISABLED.getLevelCode()) {
            m = m + ChatFormatting.RED + I18n.get(AutoJumpState.DISABLED.getDesc());
        } else if (autoJumpState == AutoJumpState.ENABLED.getLevelCode()) {
            m = m + ChatFormatting.BLUE + I18n.get(AutoJumpState.ENABLED.getDesc());
        } else if (autoJumpState == AutoJumpState.MINECRAFT.getLevelCode()) {
            m = m + ChatFormatting.GREEN + I18n.get(AutoJumpState.MINECRAFT.getDesc());
        }
        player.displayClientMessage(Component.literal(m), true);
    }
    
    private static void updateMessage() {
        String m2 = ChatFormatting.GOLD + I18n.get("msg.stepup.updateAvailable") + ": " + ChatFormatting.DARK_AQUA + "[" + ChatFormatting.YELLOW + "StepUp-" + ChatFormatting.WHITE + VersionChecker.getLatestVersion() + ChatFormatting.DARK_AQUA + "]";
        String url = "https://www.curseforge.com/minecraft/mc-mods/stepup/files";
        ClickEvent versionCheckChatClickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL,url);
        HoverEvent versionCheckChatHoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(I18n.get("msg.stepup.updateTooltip") + "!"));
        MutableComponent component = Component.literal(m2);
        Style s = component.getStyle();
        s.withClickEvent(versionCheckChatClickEvent);	//setClickEvent
        s.withHoverEvent(versionCheckChatHoverEvent);	//setHoverEvent
        component.setStyle(s);

        player.displayClientMessage(component, true);
    }
    
    public enum AutoJumpState
    {
        DISABLED (0,"msg.stepup.disabled"), //StepUp Enabled
        ENABLED (1,"msg.stepup.enabled"), //"All Disabled" 
        MINECRAFT (2,"msg.stepup.minecraft"); //"Minecraft Jump Enabled" 
        
        private final int levelCode;
        private final String desc;

        AutoJumpState(int levelCode, String desc) {
            this.levelCode = levelCode;
            this.desc = desc;
        }
        
        public int getLevelCode() {
            return this.levelCode;
        }

		public String getDesc() {
			return this.desc;
		}
    }

}
