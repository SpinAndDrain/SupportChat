package de.spinanddrain.supportchat.spigot.command;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.external.StringExtensions;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.configuration.Messages;
import de.spinanddrain.supportchat.spigot.configuration.Placeholder;
import de.spinanddrain.supportchat.spigot.configuration.Reasons;
import de.spinanddrain.supportchat.spigot.configuration.Reasons.Mode;
import de.spinanddrain.supportchat.spigot.request.Request;
import de.spinanddrain.supportchat.spigot.request.RequestState;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class Support extends SpigotCommand {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */

	public Support() {
		super("support", Permissions.SEND_REQUEST, PLAYER);
	}
	
	@Override
	public void runCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if(args.length == 0 && Reasons.MODE.asMode() == Mode.ABSOLUTE_DISABLED) {
			run(player, "-");
		} else if(args.length == 1) {
			String reason = args[0];
			if(Reasons.MODE.asMode() == Mode.ENABLED) {
				List<String> reasons = Reasons.REASONS.asList();
				if(!reasons.contains(reason)) {
					player.sendMessage(Messages.SYNTAX_REASONS.getWithPlaceholder(Placeholder.create("[reasons]",
							StringExtensions.bind(reasons.toArray(new String[reasons.size()]), ", "))));
					return;
				}
			}
			run(player, reason);
		} else
			player.sendMessage(Messages.SYNTAX.getWithPlaceholder(Placeholder.create("[command]", Messages.SYNTAX_SUPPORT.getWithoutPrefix())));
	}
	
	private void run(Player player, String reason) {
		Supporter su = Supporter.cast(player);
		if(su == null || !su.isLoggedIn()) {
			if(!SpigotPlugin.provide().hasRequested(player)) {
				if(SpigotPlugin.provide().getRequestOf(player.getName()) != null) {
					Request r = SpigotPlugin.provide().getRequestOf(player.getName());
					r.setState(RequestState.OPEN);
					r.setReason(reason);
				} else
					SpigotPlugin.provide().getRequests().add(new Request(player, reason));
				for(Supporter s : SpigotPlugin.provide().getOnlineSupporters()) {
					if(s.isLoggedIn()) {
						s.getSupporter().sendMessage(Messages.NEW_REQUEST.getMessage());
					}
				}
				player.sendMessage(Messages.QUEUE_JOINED.getMessage());
			} else
				player.sendMessage(Messages.ALREADY_IN_QUEUE.getMessage());
		} else
			player.sendMessage(Messages.CRWL.getMessage());
	}
	
}

