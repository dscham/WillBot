package at.fhcampuswien.cyberpirates.WillBot.objects;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Query {

    private int postCode;
    private boolean buy;
    private  Map<String,String> others = new HashMap<>();
    private  DoubleRange price = new DoubleRange();
    private  DoubleRange livingArea = new DoubleRange();
    private  IntegerRange roomCount = new IntegerRange();

}
