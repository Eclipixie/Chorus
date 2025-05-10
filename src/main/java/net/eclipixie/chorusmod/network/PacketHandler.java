package net.eclipixie.chorusmod.network;

import net.eclipixie.chorusmod.ChorusMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(ChorusMod.MOD_ID, "main"))
            .serverAcceptedVersions((version) -> true)
            .clientAcceptedVersions((version) -> true)
            .networkProtocolVersion(() -> String.valueOf(1))
            .simpleChannel();

    public static void register() {
        // client to server is prefixed by 's'
        // server to client is prefixed by 'c'
        INSTANCE.messageBuilder(sSonicKeybindPacket.class, NetworkDirection.PLAY_TO_SERVER.ordinal())
                .encoder(sSonicKeybindPacket::encode)
                .decoder(sSonicKeybindPacket::new)
                .consumerMainThread(sSonicKeybindPacket::handle)
                .add();
    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), msg);

    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void sendToAllClients(Object msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }
}
