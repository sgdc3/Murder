/*
 * Copyright 2013-2016 inventivetalent. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and contributors and should not be interpreted as representing official policies,
 *  either expressed or implied, of anybody else.
 */

package org.inventivetalent.murder.command;

import org.bukkit.entity.Player;
import org.inventivetalent.murder.Murder;
import org.inventivetalent.murder.arena.Arena;
import org.inventivetalent.murder.game.Game;
import org.inventivetalent.pluginannotations.PluginAnnotations;
import org.inventivetalent.pluginannotations.command.Command;
import org.inventivetalent.pluginannotations.command.JoinedArg;
import org.inventivetalent.pluginannotations.command.Permission;
import org.inventivetalent.pluginannotations.message.MessageLoader;

public class ToggleCommands {

	static MessageLoader MESSAGE_LOADER = PluginAnnotations.MESSAGE.newMessageLoader(Murder.instance, "config.yml", "messages.command.arena.editor", null);

	private Murder plugin;

	public ToggleCommands(Murder plugin) {
		this.plugin = plugin;
	}

	@Command(name = "murderEnable",
			 aliases = {
					 "mEnable",
					 "me" },
			 usage = "<Arena Name>")
	@Permission("murder.arena.toggle")
	public void enable(Player sender, @JoinedArg String name) {
		Arena arena = plugin.arenaManager.getArenaByName(name);
		if (arena == null) {
			sender.sendMessage(MESSAGE_LOADER.getMessage("error.notFound", "error.notFound"));
			return;
		}
		plugin.arenaManager.removeArena(arena);
		arena.disabled = false;
		plugin.arenaManager.addArena(arena);

		sender.sendMessage(MESSAGE_LOADER.getMessage("toggle.enable", "toggle.enable"));
	}

	@Command(name = "murderDisable",
			 aliases = {
					 "mDisable",
					 "md" },
			 usage = "<Arena Name>")
	@Permission("murder.arena.toggle")
	public void disable(Player sender, @JoinedArg String name) {
		Arena arena = plugin.arenaManager.getArenaByName(name);
		if (arena == null) {
			sender.sendMessage(MESSAGE_LOADER.getMessage("error.notFound", "error.notFound"));
			return;
		}
		Game game = plugin.gameManager.getGameForArenaId(arena.id);
		if (game != null) { game.kickAllPlayers(); }

		plugin.arenaManager.removeArena(arena);
		arena.disabled = true;
		plugin.arenaManager.addArena(arena);
		sender.sendMessage(MESSAGE_LOADER.getMessage("toggle.disable", "toggle.disable"));
	}

}