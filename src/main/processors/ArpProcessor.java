package src.main.processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArpProcessor {
    public static List<String[]> getArps() throws IOException {
        String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)"; // ipregex
        String MACADDRESS_PATTERN = "([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})"; // macregex
        List<String[]> arps = new ArrayList<>();

        // Get current IP address
        InetAddress addr = InetAddress.getLocalHost();
        String currentIp = addr.getHostAddress();

        // Add current IP and MAC to the list
        String[] currentRow = new String[2];
        currentRow[0] = currentIp;
        currentRow[1] = getMacAddress(currentIp);
        arps.add(currentRow);

        // Process ARP table
        ProcessBuilder pb = new ProcessBuilder("arp", "-a");
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), "CP866"))) { // get line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = new String[2];
                Pattern ipPattern = Pattern.compile(IPADDRESS_PATTERN);
                Matcher ipMatcher = ipPattern.matcher(line);
                if (ipMatcher.find()) {
                    row[0] = ipMatcher.group(0).replaceAll("[()]", ""); // search for ip
                }
                Pattern macPattern = Pattern.compile(MACADDRESS_PATTERN);
                Matcher macMatcher = macPattern.matcher(line);
                if (macMatcher.find()) {
                    row[1] = macMatcher.group(0).replaceAll("[()]", ""); // search for mac
                }
                if (row[0] != null && row[1] != null)
                    arps.add(row);
            }
        }
        return arps;
    }

    // Helper method to get MAC address for a given IP
    private static String getMacAddress(String ip) {
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getByName(ip));
            byte[] mac = network.getHardwareAddress();
            if (mac != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X", mac[i]));
                    if (i < mac.length - 1) {
                        sb.append(":");
                    }
                }
                return sb.toString();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
