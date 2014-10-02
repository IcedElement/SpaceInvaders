import javafx.collections.ObservableList;
import java.util.List;
import java.util.ArrayList;

public class Compute{

	static float normalised_sum;

	public static String calculate_money(ObservableList<Person> persons){
		String result = "";
		float total_sum =0;
		for(int counter = 0; counter != persons.size(); counter++){
			Person current_person = persons.get(counter);
			total_sum += current_person.get_money_spent();
		}
		//System.out.println(total);
		normalised_sum = total_sum /persons.size();
		result = update_persons(persons);
		return result;
		//System.out.println(moyenne);
	} 

	public static String update_persons(ObservableList<Person> persons){
		
		String result="";
		List<Person> need_money = new ArrayList<Person>();
		List<Person> owe_money = new ArrayList<Person>();
		for (int counter=0; counter < persons.size(); counter++){
			Person current_person = persons.get(counter);
			current_person.update_money(normalised_sum);
			if (current_person.needs_money()) {
				need_money.add(current_person);
			}
			else {
				owe_money.add(current_person);
			}
		}
		// now compare the lists and print the results
		int counter_need = 0;
		int counter_owe = 0;
		for (; counter_need != need_money.size() && counter_owe != owe_money.size(); ) {
			Person current_needer = need_money.get(counter_need);
			Person current_giver = owe_money.get(counter_owe);
			result +=current_needer.exchange_money(current_giver);
			if (!current_needer.needs_money()) {
				counter_need += 1;
			}
			if (!current_giver.owes_money()) {
				counter_owe += 1;
			}
		}
		if (need_money.size() == 0) {
			result = "All's good!";
		}
		return result;
	}

}