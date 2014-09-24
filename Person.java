class Person {
    
    float money_spent;
    float money_to_get = 0;
    float money_to_give = 0;
    String name;

    Person(String name, float money_spent){
        this.name = name;
        this.money_spent = money_spent;
    }

    public String get_name() {
        return name;
    }

    public float get_money_spent() {
        return money_spent;
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

    public void exchange_money(Person other) {
        if (this.needs_money()) {
            float available_money = other.get_money_to_give();
            if (available_money >= money_to_get) {
                float rest = available_money - this.money_to_get;
                System.out.println("[+] " + other.get_name() + " must give " + money_to_get + 
                                                    " euros to " + this.get_name() + ".");
                other.set_money_to_give(rest);
                this.set_money_to_get(0);
            }
            else {
                other.set_money_to_give(0);
                float rest = this.money_to_get - available_money;
                System.out.println("[+] " + other.get_name() + " must give " +
                                 available_money + " euros to " + this.get_name() + ".");
                this.set_money_to_get(rest);
            } 

        }
        else 
        {
            other.exchange_money(this);
        }
    }
}