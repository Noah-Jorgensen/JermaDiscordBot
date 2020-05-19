package jermaBot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;

public class JermaBot {
	public static JDA jda;
	public static String prefix = "!"; //bot's prefix
	//public static String guildID = "jerma's dsicord server ID here"; //jerma official server (not currently needed)
	public static String streamChannel = "stream channel ID here"; //"stream" channel in the server (whatever channel ID you want to use !sus in)
	
	public static String sus = ""; //default sus message (empty = none)
	public static int susCooldown = 2; //default cooldown in minutes (0 = none)
	
	//begin
	public static void main(String[] args) throws LoginException{
		//BUILD BOT
		jda = new JDABuilder(AccountType.BOT).setToken("BOT TOKEN HERE").build();
		jda.getPresence().setStatus(OnlineStatus.ONLINE); //set bot to online status
		try {
			jda.awaitReady(); //wait for builder to finish
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//LOAD COMMANDS
		Commands commands = new Commands(); //create commands
		commands.startTimers(); //starts command timers (including sus)
		jda.addEventListener(commands); //load commands into jda
	}
}
