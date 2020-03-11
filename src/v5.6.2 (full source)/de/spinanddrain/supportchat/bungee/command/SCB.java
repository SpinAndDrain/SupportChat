package de.spinanddrain.supportchat.bungee.command;

import java.util.List;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.Placeholder;
import de.spinanddrain.supportchat.bungee.chat.Chat;
import de.spinanddrain.supportchat.bungee.chat.ChatDisplayResult;
import de.spinanddrain.supportchat.bungee.chat.ChatMessage;
import de.spinanddrain.supportchat.bungee.chat.ClickableChatMessage;
import de.spinanddrain.supportchat.bungee.chat.EmptyLine;
import de.spinanddrain.supportchat.bungee.chat.MessageHandler;
import de.spinanddrain.supportchat.bungee.conversation.Conversation;
import de.spinanddrain.supportchat.bungee.request.Request;
import de.spinanddrain.supportchat.bungee.supporter.Supporter;
import de.spinanddrain.supportchat.spigot.request.RequestState;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SCB extends Command {

	/*
	 * Created by SpinAndDrain on 20.12.2019
	 */

	public SCB() {
		super("scb");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		try {
			if (sender instanceof ProxiedPlayer && Permissions.SUPPORT.hasPermission(sender)) {
				ProxiedPlayer p = (ProxiedPlayer) sender;
				switch (args[0]) {
				case "display":
					int page = Integer.parseInt(args[1]);
					int req = BungeePlugin.provide().getVisibleRequests().size();
					int pages = (int) Math.ceil((double) req / 5);
					Chat chat = Chat.of(p);
					if (req == 0) {
						BungeePlugin.sendPluginMessage(p, "no-requests");
						return;
					}
					if (page <= pages) {
						chat.clean();
						chat.sendPluginMessage(new MessageHandler(
								new ChatMessage("§8§l+---------------------------+", true), new EmptyLine(true),
								new ChatMessage(BungeePlugin.getMessage("gui-header",false), true), new EmptyLine(true)));
						chat.sendPluginMessage(new MessageHandler(convertRequests(req, page, pages)));
						chat.sendRelatedPluginMessage(new MessageHandler(
								new ClickableChatMessage("§a<--", String.valueOf((page <= 1 ? 1 : page - 1)),
										"/scb display " + String.valueOf((page <= 1 ? 1 : page - 1)),
										Action.RUN_COMMAND),
								new ClickableChatMessage(BungeePlugin.getMessage("gui-update",false),
										BungeePlugin.getMessage("gui-page",false).replace("[page]", String.valueOf(page)),
										"/scb display " + page, Action.RUN_COMMAND),
								new ClickableChatMessage("§a-->", String.valueOf((page >= pages ? pages : page + 1)),
										"/scb display " + String.valueOf((page >= pages ? pages : page + 1)),
										Action.RUN_COMMAND)));
						chat.sendPluginMessage(new MessageHandler(new ChatMessage("§8§l+---------------------------+", true)));
					} else
						execute(p, new String[] { "display", String.valueOf(pages) });
					break;
				case "open":
					Request r = BungeePlugin.provide().getRequestOf(args[1]);
					int cpage = Integer.parseInt(args[2]);
					chat = Chat.of(p);
					if (r != null) {
						if (r.getState() == RequestState.OPEN || r.getState() == RequestState.HANDLE) {
							Conversation conv = BungeePlugin.provide().getConversationOf(r);
							chat.clean();
							chat.sendPluginMessage(
									new MessageHandler(new ChatMessage("§8§l+---------------------------+", true),
											new EmptyLine(false),
											new ChatMessage("§6§l"
													+ r.getRequestor().getName() + " §7- §a" + r.getReason(), true), new EmptyLine(false),
											(r.getState() == RequestState.HANDLE
													? new ChatMessage(
															BungeePlugin.getMessage("gui-edit",false)
																	.replace("[supporter]",
																			conv.getHandler().getSupporter().getName())
																	.replace("[id]", String.valueOf(conv.getId())),
															true)
													: new ChatMessage(new String(), false)), new EmptyLine(false),
											new ClickableChatMessage(BungeePlugin.getMessage("gui-accept",false),
													BungeePlugin.getMessage("gui-accept",false),
													"/scb accept " + r.getRequestor().getName(), Action.RUN_COMMAND),
											new EmptyLine(false),
											new ClickableChatMessage(BungeePlugin.getMessage("gui-back",false),
													BungeePlugin.getMessage("gui-back",false), "/scb display " + cpage,
													Action.RUN_COMMAND),
											new EmptyLine(false),
											new ClickableChatMessage(BungeePlugin.getMessage("gui-listen",false),
													BungeePlugin.getMessage("gui-listen",false),
													"/scb listen " + r.getRequestor().getName(), Action.RUN_COMMAND),
											new EmptyLine(false),
											new ClickableChatMessage(BungeePlugin.getMessage("gui-deny",false),
													BungeePlugin.getMessage("gui-deny",false),
													"/scb deny " + r.getRequestor().getName(), Action.RUN_COMMAND),
											new EmptyLine(false),
											new ChatMessage("§8§l+---------------------------+", true)));
						} else
							BungeePlugin.sendPluginMessage(p, "request-no-longer-availabe");
					} else
						BungeePlugin.sendPluginMessage(p, "request-no-longer-availabe");
					break;
				case "accept":
					r = BungeePlugin.provide().getRequestOf(args[1]);
					Supporter s = Supporter.cast(p);
					if(!BungeePlugin.provide().isInConversation(p)) {
						if(r.getState() != RequestState.FINISHED) {
							if(r.getState() == RequestState.OPEN) {
								r.setState(RequestState.HANDLE);
								s.setTalking(true);
								Conversation conversation = new Conversation(BungeePlugin.provide().getConversations().size(), r, s);
								conversation.setRunning(true);
								BungeePlugin.provide().getConversations().add(conversation);
								BungeePlugin.sendPluginMessage(p, "you-are-now-in-a-conversation", new Placeholder("[player]", r.getRequestor().getName()));
								BungeePlugin.sendPluginMessage(r.getRequestor(), "you-are-now-in-a-conversation", new Placeholder("[player]", p.getName()));
							} else
								BungeePlugin.sendPluginMessage(p, "conversation-already-running");
						} else
							BungeePlugin.sendPluginMessage(p, "request-no-longer-availabe");
					} else
						BungeePlugin.sendPluginMessage(p, "already-in-conversation");
					break;
				case "listen":
					r = BungeePlugin.provide().getRequestOf(args[1]);
					if(Permissions.LISTEN.hasPermission(p)) {
						if(!BungeePlugin.provide().isInConversation(p)) {
							if(r != null && r.getState() != RequestState.FINISHED) {
								if(r.getState() == RequestState.HANDLE) {
									BungeeCord.getInstance().getPluginManager().dispatchCommand(p, "listen " + BungeePlugin.provide().getConversationOf(r).getId());
								} else
									BungeePlugin.sendPluginMessage(p, "conversation-not-started");
							} else
								BungeePlugin.sendPluginMessage(p, "request-no-longer-availabe");
						} else
							BungeePlugin.sendPluginMessage(p, "already-in-conversation");
					} else
						BungeePlugin.sendPluginMessage(p, "no-permission");
					break;
				case "deny":
					r = BungeePlugin.provide().getRequestOf(args[1]);
					if(!BungeePlugin.provide().isInConversation(p)) {
						if(r.getState() != RequestState.FINISHED) {
							if(r.getState() == RequestState.OPEN) {
								r.setState(RequestState.FINISHED);
								BungeePlugin.sendPluginMessage(p, "successfully-denied");
								BungeePlugin.sendPluginMessage(r.getRequestor(), "you-got-denied");
							} else
								BungeePlugin.sendPluginMessage(p, "conversation-already-running");
						} else
							BungeePlugin.sendPluginMessage(p, "request-no-longer-availabe");
					} else
						BungeePlugin.sendPluginMessage(p, "already-in-conversation");
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
		}
	}

	private ChatDisplayResult[] convertRequests(int req, int page, int pages) {
		int mod = (page < pages ? 0 : (req % 5));
		ChatDisplayResult[] res = new ChatDisplayResult[(mod == 0 ? 5 : mod) * 2];
		List<Request> t = BungeePlugin.provide().getVisibleRequests();
		for (int i = 0; i < res.length; i += 2) {
			Request r = t.get((5 * (page - 1)) + (i/2));
			res[i] = new ClickableChatMessage((i == 0 ? " " : "") + "§7§l" + (((5 * (page - 1)) + (i/2)) + 1) + ". " +
					(r.getState() == RequestState.OPEN ? "§c§l" : "§e§l") + r.getRequestor().getName(),
					BungeePlugin.getMessage("gui-reason",false).replace("[reason]", r.getReason()),
					"/scb open " + r.getRequestor().getName() + " " + page, Action.RUN_COMMAND);
			res[i + 1] = new EmptyLine(false);
		}
		return res;
	}

}
