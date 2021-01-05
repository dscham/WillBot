package at.fhcampuswien.cyberpirates.WillBot.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegerRange {
    private Integer from;
    private Integer to;
}
