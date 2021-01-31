package ru.jl1mbo.AnarchyCore.Utils;

import cn.nukkit.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Utils {
    private static final String accessToken = "fa4b98ffb89a364cbffd6a855f47e42ae669ba5bb76a2a6b943a5340351d9fd28c4d572767f5ba16ae0a9";

    public static void sendMessageToChat(String message, int peerId) {
        try {
            String url = "https://api.vk.com/method/messages.send?peer_id=" + peerId + "&random_id=" + new Random().nextInt(Integer.MAX_VALUE) + "&access_token=" + accessToken + "&message=" + URLEncoder.encode(
                    message, "UTF-8") + "&v=5.124";
            URL(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSecond(int seconds) {
        int preLastDigit = seconds % 100 / 10;
        if (preLastDigit == 1) {
            return " §fсекунд";
        }
        if (seconds % 10 == 1) {
            return " §fсекунду";
        } else if (seconds % 10 == 2 || seconds % 10 == 3 || seconds % 10 == 4) {
            return " §fсекунды";
        }
        return " §fсекунд";
    }

    private static void URL(String url) {
        try {
            URL request = new URL(url);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.openStream()));
            StringBuilder stringBuilder;
            stringBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) stringBuilder.append(inputLine);
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDate() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
    }

    public static String implode(String[] args, int start) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            stringBuilder.append(" ").append(args[i]);
        }
        return stringBuilder.substring(1);
    }

    public static int rand(int min, int max) {
        if (min == max) {
            return max;
        }
        return new Random().nextInt(max + 1 - min) + min;
    }

    public static double rand(double min, double max) {
        if (min == max) {
            return max;
        }
        return min + Math.random() * (max - min);
    }

    public static boolean isDouble(String string) {
        return string.matches("^[0.1-9.0]+$");
    }

    public static String getOnlineString(String playerName) {
        return Server.getInstance().getPlayerExact(playerName) == null ? "§cОффлайн" : "§aОнлайн";
    }

    public static boolean isInteger(String string) {
        return string.matches("^[0-9]+$");
    }
}