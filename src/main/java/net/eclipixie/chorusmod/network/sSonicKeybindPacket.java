package net.eclipixie.chorusmod.network;

import net.eclipixie.chorusmod.item.custom.SculkSonicChannelerItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class sSonicKeybindPacket {
    public enum SonicAbilityType {
        DASH,
        ROAR,
        BLAST
    }

    private final SonicAbilityType abilityType;

    public sSonicKeybindPacket(SonicAbilityType abilityType) {
        this.abilityType = abilityType;
    }

    // for the love of god, remember the encoding order when decoding

    public sSonicKeybindPacket(FriendlyByteBuf buffer) {
        this(SonicAbilityType.values()[buffer.readByte()]);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByte(this.abilityType.ordinal());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        ServerPlayer player = context.getSender();

        if (player == null) return;

        switch (abilityType) {
            case DASH -> SculkSonicChannelerItem.sonicDash(player);
            case ROAR -> SculkSonicChannelerItem.sonicRoar(player);
            case BLAST -> SculkSonicChannelerItem.sonicBlast(player);
            default -> { System.out.println("Invalid sonic ability, how tf did this happen"); }
        }
    }
}
