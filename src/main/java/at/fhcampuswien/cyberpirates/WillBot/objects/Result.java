package at.fhcampuswien.cyberpirates.WillBot.objects;

import java.util.HashMap;
import java.util.Map;

public class Result {

    private int postCode;
    private boolean buy;
    private  Map<String,String> others = new HashMap<>();
    private  DoubleRange price = new DoubleRange();
    private  DoubleRange livingArea = new DoubleRange();
    private  IntegerRange roomCount = new IntegerRange();


}
