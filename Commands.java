package jermaBot;

import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.core.entities.MessageChannel;
//import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter{
	
	//create the sus timer
	int susCooldownTime = JermaBot.susCooldown * 60; //initialize the first timer (this can change later)
	Timer susTimer = new Timer();
	TimerTask susTimerTask = new TimerTask() {
		public void run() {
			if(susCooldownTime > 0) {
				susCooldownTime--;
				
				//DEBUG:
				//System.out.println("SUS Cooldown: " + susCooldownTime);
			}else if(susCooldownTime < 0){ //safety catch
				susCooldownTime = 0;
			}
		}
	};
	
	//run all timers (on bot start)
	public void startTimers() {
		susTimer.scheduleAtFixedRate(susTimerTask, 0, 1000); //start sus timer
	}

	//begin looking for commands
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		//get the background info first
		String userMessage = event.getMessage().getContentRaw();
		MessageChannel messageChannelObj = event.getMessage().getChannel();
		String messageChannel = messageChannelObj.getId();
		//Member messageSender = event.getMember();
		TextChannel streamChannel = event.getGuild().getTextChannelById(JermaBot.streamChannel);
		
		//now process the command vvv
		
		//the "!sus" command
		if(userMessage.equalsIgnoreCase(JermaBot.prefix + "sus")) { //if "!sus"
			if(messageChannel.equalsIgnoreCase(JermaBot.streamChannel)) { //if command sent in right channel
				event.getMessage().delete().queue(); //delete the user's "!sus" message
				
				if(JermaBot.sus != "") { //make sure sus has been set
					if(susCooldownTime == 0) { //check cooldown timer is ready
						streamChannel.sendMessage(JermaBot.sus).queue(); //say the current sus
						susCooldownTime = JermaBot.susCooldown * 60; //reset cooldown (with whatever the current cooldown length is)
					}else {
						//DEBUG:
						//streamChannel.sendMessage("Cooldown timer is at " + susCooldownTime).queue();
					}
				}else {
					streamChannel.sendMessage("!sus has not been set").queue(); //if sus is "" (empty)
				}
			}
		}
		
		//the "!commands edit !sus" command
		//THIS COMMAND NEEDS TO BE LOCKED BEHIND MOD/ADMIN ROLES
		//This command could use args instead of startsWith
		String checkSusEdit = JermaBot.prefix + "commands edit " + JermaBot.prefix + "sus "; //messy
		int checkSusEditLen = checkSusEdit.length();
		if(userMessage.startsWith(checkSusEdit)) {
			if(messageChannel.equalsIgnoreCase(JermaBot.streamChannel)) { //if command sent in right channel
				event.getMessage().delete().queue(); //delete the mod's command edit message
				
				String newSus = userMessage.substring(checkSusEditLen);
				JermaBot.sus = newSus;
				
				streamChannel.sendMessage(JermaBot.prefix + "sus has been changed to: " + JermaBot.sus).queue(); //say the new sus
			}
		}
		
		//the "!commands edit !suscooldown" command
		//THIS COMMAND NEEDS TO BE LOCKED BEHIND MOD/ADMIN ROLES
		//This command could use args instead of startsWith
		String checkSusTimerEdit = JermaBot.prefix + "commands edit " + JermaBot.prefix + "suscooldown "; //messy
		int checkSusTimerEditLen = checkSusTimerEdit.length();
		if(userMessage.startsWith(checkSusTimerEdit)) {
			if(messageChannel.equalsIgnoreCase(JermaBot.streamChannel)) { //if command sent in right channel
				event.getMessage().delete().queue(); //delete the mod's command edit message
				
				String newSusCooldown = userMessage.substring(checkSusTimerEditLen);
				int newSusCooldownParse = Integer.parseInt(newSusCooldown);
				JermaBot.susCooldown = newSusCooldownParse;
				susCooldownTime = JermaBot.susCooldown * 60; //safety measure in case timer got set to something MASSIVE by accident
				
				streamChannel.sendMessage(JermaBot.prefix + "sus cooldown has been changed to: " + JermaBot.susCooldown + " minute(s)").queue(); //say the new sus
			}
		}
		
	}

}
