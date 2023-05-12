package src.main.processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArpProcessor {
    public static List<String[]> getArps() throws IOException {
        String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)"; // ipregex
        String MACADDRESS_PATTERN = "([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})"; // macregex

        List<String[]> arps = new ArrayList<>();

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
}
