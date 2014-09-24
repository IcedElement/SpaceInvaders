// exceptions 

// parent class for all our exceptions
class FinanceException extends Exception {

    int error_code; 

    // constructeur
    FinanceException(int error_code) {
        this.error_code = error_code;
    }
    public String toString() {
        return "Unknown exception occured";
    }
}

class WrongInputException extends FinanceException {

    public String toString() {
        String error_message;
        switch (error_code) {
            case 10 : 
                error_message = "Please enter a number";
                break;
            case 11 :
                error_message = "Please enter a valid ammount"
                break;
            case 12 : 
                error_message = "Please enter a name"
                break;
        }  
        return error_message;
    }
}

class SamePersonException extends FinanceException {

    public String toString() {
        String error_message;
        switch (error_code) {
            case 20 : 
                error_message = "Warning: Person already added";
                break;
        }  
        return error_message;
}