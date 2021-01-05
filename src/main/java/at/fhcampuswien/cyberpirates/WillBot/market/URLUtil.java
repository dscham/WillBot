package at.fhcampuswien.cyberpirates.WillBot.market;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class URLUtil {

    public String paramMapToQueryString(Map<String, String> params) {
        return "?" + params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }
}
