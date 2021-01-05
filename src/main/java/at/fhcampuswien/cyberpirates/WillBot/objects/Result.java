package at.fhcampuswien.cyberpirates.WillBot.objects;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {

    private String href;
    private String title;
    private String postCode;
    private String buy;
    private String price;
    private String livingArea;
    private String roomCount;
    private String provider;
}
