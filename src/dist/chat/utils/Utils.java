package dist.chat.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	/**
	 * String to separate sender's name of message from its content
	 */
	public static final String SEPARATOR = ":\n   ";
	
	/**
	 * Validate ip address with regular expression
	 * ({@link http://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/})
	 * @param ip
	 *            ip address for validation
	 * @return true valid ip address, false invalid ip address
	 */
	public static boolean isValidIp4Addr(String ip) {
		String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(IPADDRESS_PATTERN);
		matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	public static String getLocalIpAddr() {
		InetAddress local = null;
		try {
			local = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String ipAddr = local.getHostAddress();
		return ipAddr;
		
	}
}
