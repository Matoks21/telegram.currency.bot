package org.example.json;

import lombok.Data;
import java.util.Currency;

@Data
public class JsonNBU {
    private int r030;
    private String txt;
    private float rate;
    private Currency cc;
    private String exchangeDate;
}