import java.util.ArrayList;
import java.util.List;

class Finance  {

    public static void main(String[] args) {

        print_header();
        List<Person> persons = new ArrayList<Person>();
        print_menu();
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

    public void exception_handler(FinanceException exception){
        String error_message = exception.soString();
        System.out.println("[!] " + error_message + ".");
    }    
        }    

    public void print_header(){
        System.out.println("");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("-----------------Partage----------------");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("");
    }
    
    public void print_menu(){
        System.out.println("------------------Menu------------------");
        System.out.println("1: ajouter une personne");
        System.out.println("2: stop");
        System.out.print("Choix: ");
    }

>>>>>>> origin/master
}

