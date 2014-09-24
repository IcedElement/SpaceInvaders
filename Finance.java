import java.util.ArrayList;
import java.util.List;

class Finance  {

    public static void main(String[] args) {

        print_header();
        List<Person> persons = new ArrayList<Person>();
        int selection;
        do {    
            selection = ask_choice();
            if (selection == 1) {
                String name;
                float money;
                try {
                    name = ask_name();
                    // look if the name exists already in the list
                    for (int counter = 0; counterr<persons.size(); counter++) {
                        Person current_person = persons.get(person);
                        if (name.equals(current_person.get_name())) {
                            throw SamePersonException(20);
                        }
                    }
                    money = ask_money();
                    Person new_person = new Person(name, money);
                    persons.add(new_person);
                }
                catch (SamePersonException exception) {
                    exception_handler(exception);
                }
            }
        }
        while (selection != 2);
        System.out.println("[...] Calculating");
        Compute.calculate_money(persons);
    }

    public int ask_choice() {
        int selection;
        boolean selection_ok = false;
        while (!selection_ok) {
            print_menu();
            try {            
                try {
                    selection = Integer.parseInt(System.console().readLine()); 
                    if (selection > 2 || selection < 1) {
                        throw WrongInputException(13);
                    }
                    selection_ok = true;
                }
                catch (Exception temporary) {
                    throw WrongInputException(10);
            }
            catch (WrongInputException exception) {
                exception_handler(exception);
            }
        }
        return selection;
    }

    public String ask_name() {
        String name;
        System.out.println("[!] Adding a new person...");
        boolean done = false;
            while (!done) {
                try {
                    System.out.print("Name: ");
                    name = System.console().readLine();
                    if (name.equals("")) {
                        throw WrongInputException(12);
                    }
                    done = true;
                }
                catch (WrongInputException exception) {
                    exception_handler(exception);
                }
        }
        return name;
    }
    
    public float ask_money() {
        float money;
        boolean done = false;
        while (!done) {
            System.out.print("Money: ");
            try {
                try {                   
                    money = Float.parseFloat(System.console().readLine());
                    done = true; 
                }
                catch (Exception temporary) {
                    throw WrongInputException(11);
                }
            catch (FileException exception) {
                exception_handler(exception);
            }
        }
        return money;
    }

    public void exception_handler(FinanceException exception){
        String error_message = exception.soString();
        System.out.println("[!] " + error_message + ".");
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
        System.out.println("1: Add a new person");
        System.out.println("2: Calculate");
        System.out.print("Choice: ");
    }

}
