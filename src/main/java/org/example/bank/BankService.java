package org.example.bank;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.bot.CurrencyTelegramBot;
import org.example.json.JsonMB;
import org.example.json.JsonPB;
import org.example.model.UserSettings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@Data

public class BankService {

    private CurrencyTelegramBot bot;

    private Set<UserSettings.ChoiceBank> hashSetBank= new HashSet<>(3);


    public BankService(CurrencyTelegramBot bot) {

        this.bot = bot;
        hashSetBank.add(UserSettings.ChoiceBank.NBU);
    }

    public String chDataFromUrl(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    public void addBank(UserSettings.ChoiceBank selectedBank) {
        hashSetBank.add(selectedBank);
    }

    public void removeBank(UserSettings.ChoiceBank selectedBank) {
        hashSetBank.remove(selectedBank);
        if ( hashSetBank.isEmpty() ) {
            hashSetBank.add(UserSettings.ChoiceBank.NBU);
        }

    }

    @NotNull
    public StringBuilder getFinalStringBuilder() throws IOException {
        String ratesPB = getPrivatBankExchangeRates("https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5");
        String ratesNBU = getNbuExchangeRates("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json");
        String ratesMB = getMonoBankExchangeRates("https://api.monobank.ua/bank/currency");

        StringBuilder messageText = new StringBuilder();
        for (UserSettings.ChoiceBank setBank :
                hashSetBank) {

            if (setBank == UserSettings.ChoiceBank.PRIVATBANK) {
                messageText.append("Exchange rates data:\n\n");
                messageText.append("Bank: ").append(setBank).append("\n\n");
                messageText.append(ratesPB).append("\n\n");
            }
            if (setBank == UserSettings.ChoiceBank.NBU) {
                messageText.append("Exchange rates data:\n\n");
                messageText.append("Bank: ").append(setBank).append("\n");
                messageText.append(ratesNBU).append("\n\n");
            }
            if (setBank == UserSettings.ChoiceBank.MONOBANK) {
                messageText.append("Exchange rates data:\n\n");
                messageText.append("Bank: ").append(setBank).append("\n\n");
                messageText.append(ratesMB).append("\n\n");
            }
        }
        return messageText;
    }


    public String getPrivatBankExchangeRates(String url) throws IOException {
        String dataFromUrl = chDataFromUrl(url);
        Gson gson = new Gson();
        Type typeToken = TypeToken
                .getParameterized(List.class, JsonPB.class)
                .getType();
        List<JsonPB> privatBankList = gson.fromJson(dataFromUrl, typeToken);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        String append = "";
        Set<JsonPB> jsonPBSet = new HashSet<>(privatBankList);
        String finalString = "";

        for (JsonPB rate : jsonPBSet) {
            append = bot.getNumberForDecimalPlaces().rateWihthDecimalPlacesPB(sb, append, rate);

        }
        ArrayList<String> list = new ArrayList<>(List.of(append.split("\n\n")));
        for (String s : list) {

            finalString = bot.getCurrencyService().chooseCurrenciesForSettingsPB(stringBuilder, s);

        }
        return finalString;

    }
    public String getMonoBankExchangeRates(String url) throws IOException {
        String dataFromUrl = chDataFromUrl(url);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<JsonMB>>() {
        }.getType();
        String append = "";
        StringBuilder sb = new StringBuilder();
        List<JsonMB> monoBankList = gson.fromJson(dataFromUrl, listType);
        String finalString = "";
        for (JsonMB rate : monoBankList) {
            String format = "";
            format  = bot.getNumberForDecimalPlaces().rateWithDecimalPlacesForMono(rate);

            if (rate.getCurrencyCodeA() == 840 && rate.getCurrencyCodeB() == 980) {
                append = sb.append("UAH  USD ").append(" >>> ")
                        .append(format).append("\n\n").toString();
            }

            if (rate.getCurrencyCodeA() == 978 && rate.getCurrencyCodeB() == 980) {
                append = sb.append("UAH  EUR ").append(" >>> ").append(format).append("\n\n").toString();
            }
        }
        ArrayList<String> list = new ArrayList<>(List.of(append.split("\n\n")));
        StringBuilder sbRates = new StringBuilder();
        for (String s : list) {

            bot.getCurrencyService().chooseCurrenciesForSettingsMono(sbRates, s);
        }
        return sbRates.toString();

    }


    public String getNbuExchangeRates(String data) throws IOException {
        String dataFromUrl = chDataFromUrl(data);
        StringBuilder sb = new StringBuilder();
        String finalString = "";
        try {
            JSONArray jsonArray = new JSONArray(dataFromUrl);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String currencyCode = obj.getString("cc");
                finalString = bot.getCurrencyService().chooseCurrenciesForSettingsNBU(sb, obj, currencyCode);
            }


        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        return finalString;
    }
}
