
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class AirportSimulation {

	public static void main(String[] args) {

		// Test:
		// input
		int timeToLand = 3;
		int timeToTakeoff = 4;
		int averageTimeLanding = 2;
		int averageTimeTakingOff = 1;
		int maxTimeInQueue = 3;
		int totalTime = 600;

		// random number
		int randomSeed = 1;
		Random rand;

		// probability
		int probabilityToLand = 25;
		int probabilityToTakeoff = 50;
		int probabilityMax = 100;

		// output
		int numTookOff = 0;
		int numLanded = 0;
		int numCrashed = 0;
		int totalTimeTakingOff = 0;
		int totalTimeLanding = 0;
		// average time spent in the takeoff queue = totalTimeTakingOff / numTookOff
		// average time spent in the landing queue = totalTimeLanding / numLanded

		/*
		 * Scanner scanner = new Scanner(System.in);
		 * 
		 * System.out.print("Time to land: "); timeToLand = scanner.nextInt();
		 * System.out.print("Time to takeoff: "); timeToTakeoff = scanner.nextInt();
		 * System.out.print("Average time between landing planes: "); averageTimeLanding
		 * = scanner.nextInt();
		 * System.out.print("Average time between taking off planes: ");
		 * averageTimeTakingOff = scanner.nextInt();
		 * System.out.print("Max time in queue: "); maxTimeInQueue = scanner.nextInt();
		 * System.out.print("Total time to simulate: "); totalTime = scanner.nextInt();
		 * System.out.print("    Random seed: "); randomSeed = scanner.nextInt();
		 * System.out.print("    Probability of a plane arriving to land: ");
		 * probabilityToLand = scanner.nextInt();
		 * System.out.print("    Probability of a plane arriving to take off: ");
		 * probabilityToTakeoff = scanner.nextInt(); System.out.println();
		 * 
		 * scanner.close(); //
		 */

		// *

		System.out.println("Input:");
		System.out.println("(1) The amount of time needed for one plane to land = " + timeToLand);
		System.out.println("(2) The amount of time needed for one plane to takeoff = " + timeToTakeoff);
		System.out.println("(3) The average amount of time between arrival of planes to the landing queue = "
				+ averageTimeLanding);
		System.out.println("(4) The average amount of time between arrival of planes to the takeoff queue = "
				+ averageTimeTakingOff);
		System.out.println(
				"(5) The maximum amount of time that a plane can stay in the landing queue without running out of fuel and crashing = "
						+ maxTimeInQueue);
		System.out.println("(6) The total length of time to be simulated = " + totalTime);
		System.out.println("    Random seed = " + randomSeed);
		System.out.println("    Probability of a plane arriving to land = " + probabilityToLand);
		System.out.println("    Probability of a plane arriving to take off = " + probabilityToTakeoff);
		System.out.println();

		rand = new Random(randomSeed);

		Queue<Integer> queueLanding = new LinkedList<Integer>();
		Queue<Integer> queueTakeoff = new LinkedList<Integer>();

		int planeReadyTime = 0; // The time that a plane is ready to land or to take off.
		int waitingTime = 0; // the waiting time for the runway.

		for (int now = 1; now <= totalTime; now++) {

			System.out.println("-------- Current time = #" + now + " minute --------");
			System.out.println();

			// *
			if (rand.nextDouble() < (1.0 / averageTimeLanding)) {
				queueLanding.add(now);
				/*
				 * / if (probabilityToLand > rand.nextInt(probabilityMax)) {
				 * queueLanding.add(now + averageTimeLanding); //
				 */
				System.out.println("    A plane is added into Landing Queue at #" + now + " minute.");
				System.out.println();
			}

			// *
			if (rand.nextDouble() < (1.0 / averageTimeTakingOff)) {
				queueTakeoff.add(now);
				/*
				 * / if (probabilityToTakeoff > rand.nextInt(probabilityMax)) {
				 * queueTakeoff.add(now + averageTimeTakingOff); //
				 */
				System.out.println("    A plane is added into Takeoff Queue at #" + now + " minute.");
				System.out.println();
			}

			if (waitingTime > 0) {
				// watingTime > 0 means there is a plane using the runway.
				System.out.println("    The runway is in use. Wait for " + waitingTime-- + " minutes.");
				System.out.println();
				System.out.println("    Landing Queue = " + queueLanding);
				System.out.println("    Takeoff Queue = " + queueTakeoff);
				System.out.println();
				continue;
			}

			while (!queueLanding.isEmpty() // the landing queue is not empty
					&& queueLanding.peek() <= now) { // the ready time of the first plane in the landing queue before
														// now

				planeReadyTime = queueLanding.peek();

				if (now - planeReadyTime > maxTimeInQueue) {
					// the plane crashed when it waited more than the max time in queue
					queueLanding.remove();
					numCrashed++;
					System.out.println(
							"!!!!    The plane in Landing Queue at #" + planeReadyTime + " minute is crashed.");
					System.out.println();
					continue;
				}

				if (totalTime - now > timeToLand) {
					// there is enough time for the plane to land during the simulation
					queueLanding.remove();
					numLanded++;
					totalTimeLanding += now - planeReadyTime;
					waitingTime += timeToLand - 1; // the waiting time starts from this minute so the remaining time
													// should minus one
					System.out
							.println("        The plane in Landing Queue at #" + planeReadyTime + " minute is landed.");
					System.out.println();
					System.out.println("    The runway is in use. Wait for " + timeToLand + " minutes.");
					System.out.println();
				}

				break;
			}
			System.out.println("    The number of planes that landed in the simulated time = " + numLanded);
			System.out.println(
					"    The number of planes that crashed because they ran out of fuel before they could land = "
							+ numCrashed);
			System.out.println("    The average time that a plane spent in the landing queue = " + totalTimeLanding);
			System.out.println();

			if (waitingTime <= 0 // waitingTime < 0 means there is no plane landing
					&& !queueTakeoff.isEmpty() // the takeoff queue is not empty
					&& queueTakeoff.peek() <= now // the ready time of the first plane in the takeoff queue before now
					&& totalTime - now > timeToTakeoff) { // there is enough time for the plane to take off during the
															// simulation

				planeReadyTime = queueTakeoff.remove();

				numTookOff++;
				totalTimeTakingOff += now - planeReadyTime;
				waitingTime += timeToTakeoff - 1; // the waiting time starts from this minute so the remaining time
													// should minus one
				System.out.println("        The plane in Takeoff Queue at #" + planeReadyTime + " minute is took off.");
				System.out.println();
				System.out.println("    The runway is in use. Wait for " + timeToTakeoff + " minutes.");
				System.out.println();
			}
			System.out.println("    The number of planes that took off in the simulated time = " + numTookOff);
			System.out.println("    The average time that a plane spent in the takeoff queue = " + totalTimeTakingOff);
			System.out.println();

			System.out.println("    Landing Queue = " + queueLanding);
			System.out.println("    Takeoff Queue = " + queueTakeoff);
			System.out.println();
		}

		System.out.println("Output:");
		System.out.println("(1) The number of planes that took off in the simulated time = " + numTookOff);
		System.out.println("(2) The number of planes that landed in the simulated time = " + numLanded);
		System.out
				.println("(3) The number of planes that crashed because they ran out of fuel before they could land = "
						+ numCrashed);
		System.out.println("(4) The average time a plane spent in the takeoff queue = "
				+ (double) totalTimeTakingOff / numTookOff);
		System.out.println("(5) The average time that a plane spent in the landing queue = "
				+ (double) totalTimeLanding / numLanded);
	}
}
