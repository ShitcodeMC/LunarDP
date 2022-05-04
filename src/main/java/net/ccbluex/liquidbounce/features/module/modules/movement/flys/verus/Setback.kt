package net.ccbluex.liquidbounce.features.module.modules.movement.flys.verus
import net.ccbluex.liquidbounce.utils.PacketUtils.sendPacketNoEvent
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.*
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.*
import net.minecraft.util.AxisAlignedBB
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.Packet
import net.minecraft.block.BlockAir
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType

class Setback : FlyMode("Lunar-Setback") {
    private val speedValue = FloatValue("${valuePrefix}Speed", 9f, 1f, 9.89f)
	private val timerValue = FloatValue("${valuePrefix}Timer", 1f, 0.1f, 1f)
	private val tickValue = FloatValue("${valuePrefix}Ticks", 13f, 1f, 20f)
	private val verusValue = BoolValue("${valuePrefix}Verass", false)
	private val aefwoekfrweValue = BoolValue("lemonnsukaxenium", false)	
    private var ticks = 0
	private var tickglide = 0
    private val timer = MSTimer()
    
    override fun onEnable() {
		ticks=0
		tickglide=0
		mc.thePlayer.motionY=0.0
		mc.thePlayer.motionX=0.0
		mc.thePlayer.motionZ=0.0
        sendPacketNoEvent(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ, false))
    }
	override fun onMotion(event: MotionEvent) {
        if (ticks > 0 && !verusValue.get()) {
            mc.thePlayer.motionY = -if(tickglide % 2 == 0) {
                0.17
            } else {
                0.10
            }
            if(tickglide == 0) {
                mc.thePlayer.motionY = -0.07
            }
            tickglide++
        }
    }
    override fun onDisable() {
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ, false))
		mc.timer.timerSpeed=1f;
    }
    override fun onUpdate(event: UpdateEvent) {
		mc.timer.timerSpeed = timerValue.get()
    	if (ticks > 0) {
			if (!verusValue.get()) {
				mc.thePlayer.motionY=0.0
			}
			
			
            MovementUtils.strafe(speedValue.get())
			ticks--;
        } else {
			mc.thePlayer.motionX=0.0
			mc.thePlayer.motionZ=0.0
        }
    }
    	
    override fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if ((packet is C03PacketPlayer || packet is C05PacketPlayerLook || packet is C06PacketPlayerPosLook) && aefwoekfrweValue.get()) {
			sendPacketNoEvent(packet as Packet<INetHandlerPlayServer>)
		}
        if (packet is S08PacketPlayerPosLook) {
            LiquidBounce.hud.addNotification(Notification("Setback Fly", "Setback detected.", NotifyType.SUCCESS))
			ticks = tickValue.get().toInt()
        }
    }
    override fun onBlockBB(event: BlockBBEvent) {
        if (event.block is BlockAir && event.y <= fly.launchY && verusValue.get()) {
            event.boundingBox = AxisAlignedBB.fromBounds(event.x.toDouble(), event.y.toDouble(), event.z.toDouble(), event.x + 1.0, fly.launchY, event.z + 1.0)
        }
    }
    override fun onJump(event: JumpEvent) {
        event.cancelEvent()
    }
}