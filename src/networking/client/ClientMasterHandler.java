package networking.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import logic.weapons.AssaultRifle;
import logic.weapons.Pistol;
import logic.weapons.Weapon;
import networking.shared.Network;
import utils.ResourceHandler;

import java.io.IOException;

import static networking.client.ClientReceiver.*;
import static networking.shared.Network.*;

// Statically handles (we only ever need one per game) all network updates for the client
public class ClientMasterHandler {
    public static Client client;

    public static void init() {
        client = new Client();
        client.start();

        // Register all classes that will be sent
        Network.register(client);

        client.addListener(new Listener() {
            public void connected(Connection connection) {
                System.out.println("Connected!");
            }

            public void received(Connection connection, Object object) {
                if (object instanceof RegisterOtherPlayer) {
                    RegisterOtherPlayer reg = (RegisterOtherPlayer) object;
                    addOtherPlayer(reg.id, reg.pos, reg.displayName);
                }

                else if (object instanceof UpdateOtherPlayerPosition) {
                    UpdateOtherPlayerPosition up = (UpdateOtherPlayerPosition) object;
                    updateOtherPlayerPosition(up.pos, up.id);
                }

                else if (object instanceof UpdateOtherPlayerForward) {
                    UpdateOtherPlayerForward up = (UpdateOtherPlayerForward) object;
                    updateOtherPlayerForward(up.forward, up.id);
                }

                else if (object instanceof RegisterCurrentOtherPlayers) {
                    RegisterCurrentOtherPlayers reg = (RegisterCurrentOtherPlayers) object;
                    addAllCurrentOtherPlayers(reg.ids, reg.displayNames, reg.positions);
                }

                else if (object instanceof OtherPlayerDisconnect) {
                    OtherPlayerDisconnect disc = (OtherPlayerDisconnect) object;
                    disconnectOtherPlayer(disc.id);
                }

                else if (object instanceof AddItemToClient) {
                    AddItemToClient req = (AddItemToClient) object;
                    if (req.type == ITEM_TYPES.PISTOL) {
                        addItemToWorld(new Pistol(
                                req.position,
                                -0.2f,
                                1.5f,
                                req.magazine,
                                req.ammo,
                                24,
                                req.uniqueId));
                    }
                    else if (req.type == ITEM_TYPES.ASSAULT_RIFLE) {
                        addItemToWorld(new AssaultRifle(
                                req.position,
                                -0.2f,
                                1.5f,
                                req.magazine,
                                req.ammo,
                                800,
                                req.uniqueId));
                    }
                }

                else if (object instanceof ItemPickupSuccess) {
                    ItemPickupSuccess succ = (ItemPickupSuccess) object;
                    ClientReceiver.addPickupSuccess(succ.position, succ.uniqueId);
                }

                else if (object instanceof RemoveItemFromClient) {
                    RemoveItemFromClient rem = (RemoveItemFromClient) object;
                    ClientReceiver.removeItemFromWorld(rem.position, rem.uniqueId);
                }
            }

            public void disconnected(Connection connection) {
                System.out.println("Disconnected! :(");
            }
        });

        // For now, just connect to localhost immediately (server must be running of course)
        try {
            client.connect(5000, "85.229.139.37", Network.port); //85.229.139.37
        }
        catch (IOException e ) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        client.stop();
        ClientSender.disconnectPlayer();
    }
}
