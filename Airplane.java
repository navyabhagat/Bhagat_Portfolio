/**
 * @author Navya Bhagat
 *
 */

package Airplane;

import java.util.Scanner;
import java.util.regex.Pattern;

class AirplaneData {
	//Data for each of the planes
	private String name;
	public void setName(String name) {
		this.name = name;
	}

	public void setTOW_min(double tOW_min) {
		TOW_min = tOW_min;
	}

	public void setTOW_max(double tOW_max) {
		TOW_max = tOW_max;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void setHourly_cost(double hourly_cost) {
		this.hourly_cost = hourly_cost;
	}

	public void setFuel_rate(double fuel_rate) {
		this.fuel_rate = fuel_rate;
	}

	private double TOW_min;
	private double TOW_max;
	private double range;
	private double speed;
	private double hourly_cost;
	private double fuel_rate;
	
	public AirplaneData() {
		
	}

	public AirplaneData(String name, double TOW_min, double TOW_max, double range,
	double speed, double hourly_cost, double fuel_rate) {
		this.name = name;
		this.TOW_min = TOW_min;
		this.TOW_max = TOW_max;
		this.range = range;
		this.speed = speed;
		this.hourly_cost = hourly_cost;
		this.fuel_rate = fuel_rate;
	}
	public String getName() {
		return name;
	}
	public double getTOW_min() {
		return TOW_min;
	}
	public double getTOW_max() {
		return TOW_max;
	}
	public double getRange() {
		return range;
	}
	public double getSpeed() {
		return speed;
	}
	public double getHourly_cost() {
		return hourly_cost;
	}
	public double getFuel_rate() {
		return fuel_rate;
	}
	

	public double calculateFuelCost(double mass, double distance) {
		double flight_time = distance / speed;
		double total_weight = TOW_min + mass;
		double fuel_cost = fuel_rate * total_weight * flight_time;
		return fuel_cost;
	}
	
	public double calculateTotalCost(double mass, double distance) {
		double fuel_cost = calculateFuelCost(mass, distance);
		double crew_cost = hourly_cost * (distance / speed);
		double total_cost = fuel_cost + crew_cost;
		return total_cost;
	}	
}


class ContractData {
	
}




class Airplane {

	//Total number of planes user provided
	private int num_airplanes = 0;
	//Array of data for all the planes
	AirplaneData[] fleet = new AirplaneData[num_airplanes];
	
	//Array of contracts
	
	//Analysis Data


	//Function to get data from users
	//Uses Scanner class to first get Airplane data
	//Uses Scanner class to get Contract Data
	public void getUserData(){
		Scanner scanner = new Scanner(System.in);
		
		this.getAirplaneFlightDetails(scanner);
		this.getContractDetails(scanner);
		scanner.close();
		
	}
	
	//Get Airplane data and return array of airplane data
	public AirplaneData[] getAirplaneFlightDetails(Scanner scanner){
		//Get this function to set number of planes
		this.setNum_airplanes(scanner.nextInt());
		
		//Airplane[] fleet = new Airplane[num_airplanes];
		int num = this.getNum_airplanes();
		AirplaneData[] fleet = new AirplaneData[num];
		
		for (int i = 0; i < num; i++) {
			String name = scanner.next();
			double TOW_min = scanner.nextDouble();
			double TOW_max = scanner.nextDouble();
			double range = scanner.nextDouble();
			double speed = scanner.nextDouble();
			double hourly_cost = scanner.nextDouble();
			double fuel_rate = scanner.nextDouble();
			fleet[i] = new AirplaneData(name, TOW_min, TOW_max, range, speed, hourly_cost,
			fuel_rate);
		}
		return fleet;

	}
	
	//Get Contract Data and return list of all the contracts
	public double[][] getContractDetails(Scanner scanner){
		
		//This function is doing two jobs assuming user will enter integers or double value
		//as there is no exception handling so it is very strict form of user input slight 
		//changes in the way user inputs data will cause error. 
		int num_of_contracts=scanner.nextInt();
		
		double[][]contracts = new double[num_of_contracts][3];
		int contract = 0;
		
		while (scanner.hasNext(Pattern.compile("quit")) != true) {
			System.out.println("Enter quit to stop entering contracts");
			System.out.println(scanner.hasNext(Pattern.compile("quit")));
		    
			double mass = scanner.nextDouble();			
			double distance = scanner.nextDouble();
			double payment = scanner.nextDouble();
			
			contracts[contract][0] = mass;
			contracts[contract][1] = distance;
			contracts[contract][2] = payment;
			System.out.println(mass + " " +  distance+" " + payment);
			contract++;
		}
		return contracts;
		
	}
	/**
	* Returns an analyzed object that will contain array of airplane id and their status. 
	
	* <p>
	* This method always returns status of all the air planes entered by user. 
	*
	* @param  Airplane Data - array list
	* @param  Contract Data - array list
	* @return      the analyzed data
	
	*/
	
	
	public double CalculateAggregateProfits(){
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Input no of planes and data ");
		AirplaneData[] fleet = this.getAirplaneFlightDetails(scanner);
		System.out.println("Input no of contracts and data ");
		double contracts[][] = this.getContractDetails(scanner);
		
		scanner.close();
		AirplaneData best_option = null;
		double best_profit = 0;
		double total_profit = 0;
		for (double[] contract : contracts){
			for (AirplaneData airplane : fleet) {
				System.out.print("Airplane name:"+ airplane.getName());
				System.out.print("Contract mass:"+contract[0]);
				if (contract[0] <= airplane.getTOW_max() - airplane.getTOW_min() && contract[1] <=
				airplane.getRange()) {
					double cost = airplane.calculateTotalCost(contract[0], contract[1]);
					double profit = contract[2] - cost;
						if (profit > 0 && (best_option == null || profit > best_profit)) {
							best_option = airplane;
							best_profit = profit;
						}
					}
				if (best_option == null) {
					System.out.println("Decline");
				}else {
					System.out.printf("%s %.2f\n", best_option.getName(), best_profit);
						total_profit += best_profit;
					}
			}
		}
		return total_profit;

	}
	public void OutputAnalysis() {
		
	}
	
	public int getNum_airplanes() {
		return num_airplanes;
	}
	public void setNum_airplanes(int num_airplanes) {
		this.num_airplanes = num_airplanes;
	}
	
	
	public AirplaneData[] getFleet() {
		return fleet;
	}
	public void setFleet(AirplaneData[] fleet) {
		this.fleet = fleet;
	}
	public static void main(String[] args) {
		Airplane a = new Airplane();
		//Get Function to Calculate and print the profits pass fleet data and contract data in the in the function
		System.out.println("Best Profit: " + a.CalculateAggregateProfits());

	
	}

}
