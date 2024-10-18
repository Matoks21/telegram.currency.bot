package org.example.json;

import lombok.Data;
import org.example.model.UserSettings;

import java.util.Currency;

@Data
public class JsonPB {
    private Currency ccy;
    private Currency base_ccy;
    private float buy;
    private float sale;

}