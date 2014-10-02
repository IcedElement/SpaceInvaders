import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.ReadOnlyObjectWrapper;
public class Person {
    
    private float money_spent;
    private float money_to_get = 0;
    private float money_to_give = 0;
    private final SimpleStringProperty name;
    private SimpleStringProperty money_show;
    
    Person(String name, float money_spent){
        this.name = new SimpleStringProperty(name);
        this.money_spent = money_spent;
        String money_show = Float.toString(money_spent);
        this.money_show = new SimpleStringProperty(money_show);
    }

    // Hack to make it work with FinanceGUI :P
    public ReadOnlyObjectWrapper<String> nameProperty() {
       return new ReadOnlyObjectWrapper<String>(name.get());
    }

    // Hack to make it work with FinanceGUI :P
    public ReadOnlyObjectWrapper<String> money_showProperty() {
        return new ReadOnlyObjectWrapper<String>(money_show.get());
    }

    public String get_name() {
        return name.get();
    }

    public void set_money(float new_money_spent) {
        money_spent = new_money_spent;
        String money_show = Float.toString(money_spent);
        this.money_show = new SimpleStringProperty(money_show);
    }

    public void set_name(String new_name) {
        name.set(new_name); 
    }

    public float get_money_spent() {
        return money_spent;
    }

    public String get_money_to_show() {
        return money_show.get();
    }

    public void update_money (float normalised_sum){
        if (normalised_sum < money_spent) {
            money_to_get = money_spent - normalised_sum;
        }
        else {
            money_to_give = normalised_sum - money_spent;
        }
    }

    public float get_money_to_get() {
        return money_to_get;
    }

    public float get_money_to_give() {
        return money_to_give;
    }

    public void set_money_to_get(float new_amount) {
        money_to_get = new_amount;
    }

    public void set_money_to_give(float new_amount) {
        money_to_give = new_amount;
    }

    public boolean needs_money () {
        return (money_to_get > 0);
    }

    public boolean owes_money () {
        return (money_to_give > 0);
    }

    public String exchange_money(Person other) {
        String result = "";
        if (this.needs_money()) {
            float available_money = other.get_money_to_give();
            if (available_money >= money_to_get) {
                float rest = available_money - this.money_to_get;
                result = other.get_name() + " must give " + money_to_get + 
                                                    " euros to " + this.get_name() + ".\n";
                other.set_money_to_give(rest);
                this.set_money_to_get(0);
            }
            else {
                other.set_money_to_give(0);
                float rest = this.money_to_get - available_money;
                result = other.get_name() + " must give " +
                                 available_money + " euros to " + this.get_name() + ".\n" ;
                this.set_money_to_get(rest);
            } 

        }
        else 
        {
            result = other.exchange_money(this);
        }
        return result;
    }
}