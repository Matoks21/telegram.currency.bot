package org.example.model;

import lombok.Data;

@Data
public class UserSettings  {
    private DecimalPlaces decimalPlaces;
    private Currency currency;
    private ChoiceBank banks;
    private String notificationTime;


    // Ініціалізація полів за замовчуванням, якщо юзер ше не вибрав
    public UserSettings() {
        decimalPlaces = DecimalPlaces.TWO;
        currency = Currency.USD;
        banks = ChoiceBank.NBU;
        notificationTime = "off";
    }

    public enum Currency {
        USD,
        EUR
    }

    public enum ChoiceBank {
        NBU,
        PRIVATBANK,
        MONOBANK



    }
    public enum DecimalPlaces {
        TWO,
        THREE,
        FOUR

    }
}