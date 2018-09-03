package net.braunly.ponymagic.network.packets;

import io.netty.buffer.ByteBuf;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SwishPacket implements IMessage, IMessageHandler<SwishPacket, IMessage> {
	private double x = 0;
	private double y = 0;
	private double z = 0;

	public SwishPacket() {
	}

	public SwishPacket(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
	}

	@Override
	public IMessage onMessage(SwishPacket message, MessageContext ctx) {
		IThreadListener thread = PonyMagic.proxy.getListener(ctx);
		final EntityPlayer player = PonyMagic.proxy.getPlayer(ctx);

		thread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if (player != null) {
					player.motionX += message.x;// * d10;
		            player.motionY += message.y;// * d10;
		            player.motionZ += message.z;// * d10;
				}
			}
		});
		return null;
	}

}