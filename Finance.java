import java.util.ArrayList;
import java.util.List;

class Finance  {

    public static void main(String[] args) {

        List<Person> persons = new ArrayList<Person>();
        System.out.println("----------------");
        System.out.println("Menu: ");
        System.out.println("1: Add a person");
        System.out.println("2: End");
        int selection;
        do {    
            System.out.print("[>] Choice: ");
            selection = Integer.parseInt(System.console().readLine());
            if (selection == 1) {
                String name;
                float money;
                System.out.println("[!] Adding a new person...");
                
                System.out.print("Name: ");
                name = System.console().readLine();
                System.out.print("Money: ");               
                money = Float.parseFloat(System.console().readLine());
                Person new_person = new Person(name, money);
                persons.add(new_person);
            }
        }
        while (selection != 2);
        System.out.println("[...] Calculating");
        Compute.calculate_money(persons);
        }    
}
