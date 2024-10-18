package org.example.currency;

import lombok.Data;
import org.example.bot.CurrencyTelegramBot;
import org.example.model.UserSettings;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

@Data
public class CurrencyService {
    private Set<UserSettings.Currency> hashSetCurrencies= new HashSet<>(2);
    private CurrencyTelegramBot bot;

    public CurrencyService(CurrencyTelegramBot bot) {
        this.bot = bot;
        hashSetCurrencies.add(UserSettings.Currency.USD);
    }


    public void addCurrency(UserSettings.Currency currency) {
        hashSetCurrencies.add(currency);

    }

    public void removeCurrency(UserSettings.Currency currency){
        hashSetCurrencies.remove(currency);
        if ( hashSetCurrencies.isEmpty() ) {
            hashSetCurrencies.add(UserSettings.Currency.USD);
        }
    }


    public String chooseCurrenciesForSettingsPB(StringBuilder stringBuilder, String s) {
        for (UserSettings.Currency select : hashSetCurrencies) {
            if (s.contains("EUR") && select == UserSettings.Currency.EUR) {
                stringBuilder.append(s).append("\n\n");
            }
            if (s.contains("USD") && select == UserSettings.Currency.USD) {
                stringBuilder.append(s).append("\n\n");
            }
        }
        return stringBuilder.toString();
    }

    public String chooseCurrenciesForSettingsMono(StringBuilder sbRates, String s) {
        for (UserSettings.Currency select : hashSetCurrencies) {
            if (s.contains("USD") && select.equals(UserSettings.Currency.USD)) {
                sbRates.append(" || ")
                        .append(s).append("\n\n");
            }
            if (s.contains("EUR") && select.equals(UserSettings.Currency.EUR)) {
                sbRates.append(" ||")
                        .append(s).append("\n\n");
            }
        }
        return sbRates.toString();
    }

    public String chooseCurrenciesForSettingsNBU(StringBuilder sb, JSONObject obj, String currencyCode) {
        for (UserSettings.Currency select : hashSetCurrencies) {
            String format = "";

            format =bot.getNumberForDecimalPlaces().rateWithDecimalPlacesForNBU(obj, format);
            if (currencyCode.equals("USD") && select == UserSettings.Currency.USD) {
                sb.append("\n");
                sb.append("UAH || USD  >>> ").append(format).append("\n");
            }
            if (currencyCode.equals("EUR") && select == UserSettings.Currency.EUR) {
                sb.append("\n");
                sb.append("UAH || EUR  >>> ").append(format).append("\n");
            }
        }
        return sb.toString();
    }

}