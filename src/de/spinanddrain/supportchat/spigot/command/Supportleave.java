package de.spinanddrain.supportchat.spigot.command;

import java.sql.SQLException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.spinanddrain.sql.Value;
import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.configuration.Messages;
import de.spinanddrain.supportchat.spigot.configuration.Placeholder;

public class Supportleave extends SpigotCommand {

	public Supportleave() {
		super("supportleave", Permissions.SEND_REQUEST, PLAYER);
	}
	
	@Override
	public void runCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if(args.length == 0) {
			if(SpigotPlugin.provide().hasRequested(player)) {
				SpigotPlugin.provide().getRequests().remove(SpigotPlugin.provide().getRequestOf(player.getName()));
				SpigotPlugin.provide().applyLastRequestToNow(player.getUniqueId());
				player.sendMessage(Messages.QUEUE_LEFT.getMessage());
				if(SpigotPlugin.provide().getSaver().use() && SpigotPlugin.provide().getSql().isConnected()) {
					try {
						SpigotPlugin.provide().getSql().delete(new Value("id", player.getUniqueId().toString()),
								SpigotPlugin.provide().getTable());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} else
				player.sendMessage(Messages.NOT_REQUESTED.getMessage());
		} else
			player.sendMessage(Messages.SYNTAX.getWithPlaceholder(Placeholder.create("[command]",
					Messages.SYNTAX_LEAVE.getWithoutPrefix())));
	}

}
