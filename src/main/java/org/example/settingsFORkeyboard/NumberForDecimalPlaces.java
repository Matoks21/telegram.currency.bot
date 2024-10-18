package org.example.settingsFORkeyboard;


import lombok.Data;
import org.example.bot.CurrencyTelegramBot;
import org.example.json.JsonMB;
import org.example.json.JsonPB;
import org.example.model.UserSettings;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;


@Data
public class NumberForDecimalPlaces {
    private final Set<UserSettings.DecimalPlaces> hashSetDecimalPlaces = new HashSet<>(1);
    private final CurrencyTelegramBot bot;

    public NumberForDecimalPlaces(CurrencyTelegramBot bot) {
        this.bot = bot;
        hashSetDecimalPlaces.add(UserSettings.DecimalPlaces.TWO);
    }

    public String rateWithDecimalPlacesForNBU(JSONObject obj, String format) {
        for (UserSettings.DecimalPlaces places : hashSetDecimalPlaces) {
            if (places == UserSettings.DecimalPlaces.TWO) {
                DecimalFormat df = new DecimalFormat("#.00");
                format = df.format(obj.getFloat("rate"));
            }
            if (places == UserSettings.DecimalPlaces.THREE) {
                DecimalFormat df = new DecimalFormat("#.000");
                format = df.format(obj.getFloat("rate"));
            }

            if (places == UserSettings.DecimalPlaces.FOUR) {
                DecimalFormat df = new DecimalFormat("#.0000");
                format = df.format(obj.getFloat("rate"));
            }
        }
        return format;

    }

    public String rateWithDecimalPlacesForMono(JsonMB rate) {
        String format = "";
        for (UserSettings.DecimalPlaces places : hashSetDecimalPlaces) {
            if (places == UserSettings.DecimalPlaces.TWO) {
                DecimalFormat df = new DecimalFormat("#.00");
                format = df.format(rate.getRateBuy());
            }
            if (places == UserSettings.DecimalPlaces.THREE) {
                DecimalFormat df = new DecimalFormat("#.000");
                format = df.format(rate.getRateBuy());
            }
            if (places == UserSettings.DecimalPlaces.FOUR) {
                DecimalFormat df = new DecimalFormat("#.0000");
                format = df.format(rate.getRateBuy());
            }
        }
        return format;
    }

    public String rateWihthDecimalPlacesPB(StringBuilder sb, String append, JsonPB rate) {
        for (UserSettings.DecimalPlaces places : hashSetDecimalPlaces) {
            if (places == UserSettings.DecimalPlaces.TWO) {
                DecimalFormat df = new DecimalFormat("#.00");
                String format = df.format(rate.getBuy());
                append = sb.append("UAH || ").append(rate.getCcy()).append("  >>>  ").append(format).append("\n\n").toString();
            }
            if (places == UserSettings.DecimalPlaces.THREE) {
                DecimalFormat df = new DecimalFormat("#.000");
                String format = df.format(rate.getBuy());
                append = sb.append("UAH || ").append(rate.getCcy()).append("   >>>     ").append(format).append("\n\n").toString();
            }
            if (places == UserSettings.DecimalPlaces.FOUR) {
                DecimalFormat df = new DecimalFormat("#.0000");
                String format = df.format(rate.getBuy());
                append = sb.append("UAH || ").append(rate.getCcy()).append("   >>>     ").append(format).append("\n\n").toString();
            }
        }
        return append;
    }

    public void addDecimalPlaces(UserSettings.DecimalPlaces decimalPlaces) {
        hashSetDecimalPlaces.clear();
        hashSetDecimalPlaces.add(decimalPlaces);
    }


}