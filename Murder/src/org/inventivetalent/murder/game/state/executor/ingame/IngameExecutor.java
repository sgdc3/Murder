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

package org.inventivetalent.murder.game.state.executor.ingame;

import org.inventivetalent.murder.Murder;
import org.inventivetalent.murder.Role;
import org.inventivetalent.murder.game.Game;
import org.inventivetalent.murder.game.state.executor.LeavableExecutor;
import org.inventivetalent.murder.player.PlayerData;

import java.util.UUID;

public class IngameExecutor extends LeavableExecutor {

	public IngameExecutor(Game game) {
		super(game);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public boolean finished() {
		if (game.players.size() <= 0) {
			return true;
		}

		UUID murdererId = game.getMurderer();
		PlayerData murdererData = Murder.instance.playerManager.getData(murdererId);
		if (murdererId == null || murdererData == null || murdererData.killed || !murdererData.getOfflinePlayer().isOnline()) {
			//Murderer left or was killed - bystanders won
			game.winner = Role.WEAPON;
			return true;
		}

		int aliveCount = 0;
		for (UUID bystanderId : game.getBystanders(true, true)) {
			PlayerData bystanderData = Murder.instance.playerManager.getData(bystanderId);
			if (bystanderData != null && !bystanderData.killed && bystanderData.getOfflinePlayer().isOnline()) {
				aliveCount++;
			}
		}
		if (aliveCount <= 0) {
			//All bystanders left or were killed by the murderer - murderer won
			game.winner = Role.MURDERER;
			return true;
		}

		return false;
	}
}