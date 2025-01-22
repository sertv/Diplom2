package data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderData {

    private List<String> ingredients;

    public OrderData(String[] ingredients) {
        this.ingredients = new ArrayList<>(Arrays.asList(ingredients));
    }


}
