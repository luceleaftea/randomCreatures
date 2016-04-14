package randomCreatures.CreatureFactoryFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import randomCreatures.Creature.Attributes.Color;
import randomCreatures.Creature.Attributes.ColorFactory;
import randomCreatures.Creature.Attributes.Shape;
import randomCreatures.Creature.Behaviors.*;
import randomCreatures.CreatureFactory.CreatureFactory;

public class CreatureFactoryFactory {
	
	// Variables
	private List<Color> colorList;
	private List<Shape> shapeList;
	private BreedingFactory breedingFactory = new BreedingFactory();
	private EatingFactory eatingFactory = new EatingFactory();
	private ColorFactory colorFactory = new ColorFactory();
	
	// Constructor
	public CreatureFactoryFactory(List<Color> colorList, List<Shape> shapeList) {
		this.colorList = colorList;
		this.shapeList = shapeList;
	}
	
	// Create a new randomized creature factory with the given id
	public CreatureFactory createCreatureFactory(int id) {
		//int randomColor = getRandomIndex(colorList);
		int randomShape; //= getRandomIndex(shapeList);
		
		Color randomColor = colorFactory.getRandomColor();
		Shape shape;
		
		Eating eatingBehavior;
		int litterSize;
		double birthrate;
		
		if (getRandomInt(0, 100) < 70) {
			eatingBehavior = eatingFactory.createEating("herbivore");
			randomShape = 0;
			litterSize = getRandomInt(1, 5 + 1); // 5 is the intended max, but it needs to be 6 since the upper bound is not included
			birthrate = .1;
		} else {
			eatingBehavior = eatingFactory.createEating("carnivore");
			randomShape = 1;
			litterSize = 1;
			birthrate = .05;
		}
		
		Breeding breedingBehavior = breedingFactory.createBreeding(getRandomInt(0, breedingFactory.getSize()));
		
//		double birthrate = getRandomDouble(.01, .1);
//		int maxAge = getRandomInt(50, 150);
//		int size = getRandomInt(1, 5);
//		int maxHunger = getRandomInt(30, 70);
//		int hungerLossRate = getRandomInt(3, 8);
//		int foodValue = getRandomInt(30, 50) * size;
	
		int maxAge = 300;
		int size = 3;
		int maxHunger = 500;
		int hungerLossRate = 5;
		int foodValue = 200 * size;
		
		return new CreatureFactory(id, shapeList.get(randomShape), randomColor, eatingBehavior, breedingBehavior, litterSize, birthrate, 
				maxAge, size, maxHunger, hungerLossRate, foodValue);
	}
	
	// Get a random index for the given list according to its size
	public int getRandomIndex(List list) {
		return getRandomInt(0, list.size());
	}
	
	// Get random double between the two numbers given
	public double getRandomDouble(double low, double high) {
		return ThreadLocalRandom.current().nextDouble(low, high);
	}
	
	// Get random int between the two numbers given
	public int getRandomInt(int low, int high) {
		return ThreadLocalRandom.current().nextInt(low, high);
	}

}
