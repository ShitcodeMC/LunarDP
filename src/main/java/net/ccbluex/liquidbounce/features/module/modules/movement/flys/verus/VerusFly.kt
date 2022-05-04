package net.ccbluex.liquidbounce.features.module.modules.movement.flys.verus

import net.ccbluex.liquidbounce.event.BlockBBEvent
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.block.BlockAir
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.util.AxisAlignedBB

class VerusFly : FlyMode("VerusDamage") {
    private val speedValue = FloatValue("${valuePrefix}Speed", 1.5f, 0f, 3f)

    private var flyable = false
    private val timer = MSTimer()

    override fun onEnable() {
        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ)
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.35, mc.thePlayer.posZ, false))
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true))
        mc.thePlayer.motionX = 0.0
        mc.thePlayer.motionY = 0.0
        mc.thePlayer.motionZ = 0.0
        timer.reset()
        fly.launchY += 0.42
    }

    override fun onUpdate(event: UpdateEvent) {
        if (!flyable && mc.thePlayer.hurtTime > 0) {
            flyable=true
        }

        if (flyable && timer.hasTimePassed(100)) {
            MovementUtils.strafe(speedValue.get())
        } else if (!timer.hasTimePassed(100)) {
            MovementUtils.strafe(0)
        }
    }


    override fun onJump(event: JumpEvent) {
        event.cancelEvent()
    }
}
