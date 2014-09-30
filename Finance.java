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
                    for (int counter = 0; counter<persons.size(); counter++) {
                        Person current_person = persons.get(counter);
                        if (name.equals(current_person.get_name())) {
                            throw new SamePersonException(20);
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

    private static int ask_choice() {
        int selection=0;
        boolean selection_ok = false;
        while (!selection_ok) {
            print_menu();
            try {            
                try {
                    selection = Integer.parseInt(System.console().readLine()); 
                    if (selection > 2 || selection < 1) {
                        throw new WrongInputException(13);
                    }
                    selection_ok = true;
                }
                catch (IllegalArgumentException temporary) {
                    throw new WrongInputException(10);
                }
            } 
            catch (WrongInputException exception) {
                exception_handler(exception);
            }
        }
        return selection;       
    }

    private static String ask_name() {
        String name="";
        System.out.println("[!] Adding a new person...");
        boolean done = false;
            while (!done) {
                try {
                    System.out.print("Name: ");
                    name = System.console().readLine();
                    if (name.equals("")) {
                        throw new WrongInputException(12);
                    }
                    done = true;
                }
                catch (WrongInputException exception) {
                    exception_handler(exception);
                }
        }
        return name;
    }
    
    private static float ask_money() {
        float money=0;
        boolean done = false;
        while (!done) {
            System.out.print("Money: ");
            try {
                try {                   
                    money = Float.parseFloat(System.console().readLine());
                    done = true; 
                }
                catch (Exception temporary) {
                    throw new WrongInputException(11);
                }
            }
            catch (FinanceException exception) {
                exception_handler(exception);
            }
        }
        return money;
    }

    private static void exception_handler(FinanceException exception){
        String error_message = exception.toString();
        System.out.println("[!] " + error_message + ".");
    }        
        
    private static void print_header(){
        System.out.println("");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("-----------------Partage----------------");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("");
    }
    
    private static void print_menu(){
        System.out.println("------------------Menu------------------");
        System.out.println("1: Add a new person");
        System.out.println("2: Calculate");
        System.out.print("Choice: ");
    }

}
