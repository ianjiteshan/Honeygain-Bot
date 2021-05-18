package me.genix;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author github.com/genixzero
 * @created 02/05/2021 - 10:52 AM
 */
public class Main {

    private static String accessToken;
    private static boolean repeatRequest;

    public static void main(String[] args) throws Exception {
        switch (args.length) {
            case 2:
                accessToken = args[0];
                if (accessToken.startsWith("\"") && accessToken.endsWith("\"")) {
                    accessToken.substring(1, accessToken.length() - 1);
                }
                repeatRequest = Boolean.parseBoolean(args[1]);
                break;
            case 3:
                JSONObject object = (JSONObject) new JSONParser().parse(sendRequest("https://dashboard.honeygain.com/api/v1/users/tokens",
                        "POST",
                        "{\"email\":\"" + args[0] + "\",\"password\":\"" + args[1].replaceAll("\"", "\\\"") + "\"}",
                        false));
                accessToken = (String) ((JSONObject)object.get("data")).get("access_token");
                repeatRequest = Boolean.parseBoolean(args[2]);
                break;
            default:
                System.err.println("Invalid Arguments");
                return;
        }

        JSONObject data = (JSONObject) ((JSONObject) new JSONParser().parse(sendRequest("https://dashboard.honeygain.com/api/v1/users/me", "GET", null, true))).get("data");
        System.out.println("Successfully logged into " + data.get("email") + " - " + data.get("total_devices") + " devices connected.\n");

        if (repeatRequest) {
            openJar();
            Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT"));
            c.add(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            System.out.println("Sleeping for " + (c.getTimeInMillis() - System.currentTimeMillis()) / 3600000.0 + " hours.");
            Thread.sleep(c.getTimeInMillis() - System.currentTimeMillis());

            while (true) {
                openJar();
                Thread.sleep(86401000);
            }
        } else {
            openJar();
        }
    }

    private static void openJar() {
        try {
            JSONObject credits = (JSONObject) ((JSONObject) new JSONParser().parse(sendRequest("https://dashboard.honeygain.com/api/v1/contest_winnings", "POST", null, true))).get("data");
            System.out.println("[" + new SimpleDateFormat("dd-MM HH:mm:ss").format(Calendar.getInstance().getTime()) + "] Redeemed " + credits.get("credits") + " credits.");
        } catch (IOException e) {
            System.err.println("There was an error opening the honey jar, this may be because you've opened it today.");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static String sendRequest(String url, String method, String data, boolean auth) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        if (auth) {
            con.setRequestProperty("authorization", "Bearer " + accessToken);
        }
        if (data != null) {
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            try (DataOutputStream stream = new DataOutputStream(con.getOutputStream())) {
                stream.write(data.getBytes(StandardCharsets.UTF_8));
            }
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            return br.readLine();
        }
    }
}
