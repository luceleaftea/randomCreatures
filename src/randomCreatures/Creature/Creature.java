package randomCreatures.Creature;

import java.util.concurrent.ThreadLocalRandom;

import org.newdawn.slick.Graphics;

import randomCreatures.Food;
import randomCreatures.Plant;
import randomCreatures.World;
import randomCreatures.Creature.Attributes.Color;
import randomCreatures.Creature.Attributes.Shape;
import randomCreatures.Creature.Behaviors.*;

public class Creature {
	
	// Variables
	private int id;
	private Shape shape;
	private Color color;
	private Breeding breedingBehavior;
	private Eating eatingBehavior;
	
	private AIState breedingState = new AIStateBreeding();
	private AIState eatingState = new AIStateEating();
	private AIState huntingState = new AIStateHunting();
	private AIState currentState = huntingState;
	
	private double birthrate;
	private int litterSize;
	private int currentAge = 0;
	private int maxAge;
	private boolean dead = false;
	private int size;
	private int maxHunger, currentHunger, hungerLossRate, foodValue;
	private int x, y, width, height;
	private int targetX, targetY = -1;
	private boolean targetExists = false;
	private boolean currentlySelected = false;
	
	private Plant plantToFind = null;
	private Creature creatureToFind = null;
	
	// Constructor
	public Creature(int id, int x, int y, Shape shape, Color color, Eating eatingBehavior, Breeding breedingBehavior, int litterSize, double birthrate, int maxAge, 
			int size, int maxHunger, int hungerLossRate, int foodValue) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.shape = shape;
		this.color = color;
		this.breedingBehavior = breedingBehavior;
		this.eatingBehavior = eatingBehavior;
		this.litterSize = litterSize;
		this.birthrate = birthrate;
		this.maxAge = maxAge;
		this.size = size;
		this.maxHunger = this.currentHunger = maxHunger;
		this.hungerLossRate = hungerLossRate;
		this.foodValue = foodValue;
		width = height = 10;
	}
	
	// "Destructor" (Get rid of all references to self)
	public void destroy() {
		shape = null;
		color = null;
		breedingBehavior = null;
		eatingBehavior = null;
	}
	
	// To be performed every step
	// Unsure if giving the Creature access to the world is safe...
	public void run(World world) {
		
//		// Use the current breeding behavior
//		if ((currentHunger / (double) maxHunger) > 0.7) {
////			System.out.println(currentHunger / (double) maxHunger);
//			breedingBehavior.breed(world, this);
//		}
		
		// Aging
		if (currentAge == maxAge) {
			dead = true;
		}
		currentAge++;
		
		// Hunger
		if (!dead) {
			currentHunger -= hungerLossRate;
			if (currentHunger <= 0) {
				dead = true;
			}
		}
		
		// Run current state's AI
		currentState.run(world, this);
		
		// Loop creature around to opposite end of the world if they go past it
		if (x < 0) {
			x = world.getWorldWidth();
		} else if (x > world.getWorldWidth()) {
			x = 0;
		}
		
		if (y < 0) {
			y = world.getWorldHeight();
		} else if (y > world.getWorldHeight()) {
			y = 0;
		}
		
	}
	
	// Eat the given food
	public void creatureEat(Food food) {
		currentHunger += food.getFoodValue();
		
		// Make sure the added hunger doesn't cause the current hunger to go over the max value
		if (currentHunger > maxHunger) {
			currentHunger = maxHunger;
		}
	}
	
	// Draw the creature at its location in the given graphics window
	public void displayGraphics(Graphics g, int tileSize) {
		color.setColor(g, currentlySelected);
		shape.displayGraphics(g, x * tileSize, y * tileSize);
	}
	
	// Print some info about the creature to the graphics windows (used for debugging)
	public void displayInfo(Graphics g, int worldHeight, int worldWidth, int tileSize) {
		int leftPosition = 1 * tileSize;
		int initialSpacing = 5 * tileSize;
		int lineSpacing = (int) (1.5 * tileSize);

		g.setColor(new org.newdawn.slick.Color(255, 255, 255));
		
		g.drawString(eatingBehavior.getLetter() + " " + Integer.toString(x) + ", " + Integer.toString(y), 
				leftPosition, initialSpacing);
		g.drawString("Hunger: " + currentHunger + "/" + maxHunger, leftPosition, initialSpacing + lineSpacing);
		g.drawString("Age: " + currentAge + "/" + maxAge, leftPosition, initialSpacing + 2 * lineSpacing);
		g.drawString(currentState.toString(), leftPosition, initialSpacing + 3 * lineSpacing);
		if (currentState.toString() == "Hunting" && targetExists) {
			g.drawString(Integer.toString(targetX) + ", " + Integer.toString(targetY), leftPosition, initialSpacing + 4 * lineSpacing);
		}
	}
	
	// Change the creature's color and circle it when it's highlighted
	public void highlightCreature(Graphics g, int tileSize) {
		g.setColor(new org.newdawn.slick.Color(255, 255, 255));
		g.drawOval(x * tileSize - 3, y * tileSize - 3, tileSize + 6, tileSize + 6);
	}
	
	@Override
	public String toString() {
		return "Creature with id of " + Integer.toString(id) + ", with a " + shape.toString() + " shape, the color of " + color.toString() + 
				" a birthrate of " + birthrate + ", and a max age of " + maxAge + ".\n\t" + "It has a " + breedingBehavior + " and a " + 
				eatingBehavior + "." + "\n\t It's current hunger is " + currentHunger + " out of " + maxHunger + ".";
				
	}
	
	// Kill the creature and return a food object
	public Food beEaten() {
		if (!dead) {
			dead = true;
			return new Food(foodValue);
		} else {
			return new Food(0);
		}
	}
	
	// Check if given coordinates are inside the creature
	public boolean coordsInsideCreature(int coordX, int coordY, int tileSize) {
		if(coordX >= x * tileSize && coordX <= (x * tileSize + tileSize) && coordY >= y * tileSize && coordY <= (y * tileSize + tileSize)) {
			return true;
		}
		return false;
	}
	
	// Return a letter corresponding to the creature's eating behavior
	public String getEatingBehaviorLetter() {
		return eatingBehavior.getLetter();
	}
	
	// Getters and Setters //
	public int getID() { return id; }
	public double getBirthrate() { return birthrate; }
	public int getLitterSize() { return litterSize; }
	public boolean getDead() { return dead; }
	public int getX() { return x; }
	public int getY() { return y; }
	public int getCurrentHunger() { return currentHunger; }
	public int getMaxHunger() { return maxHunger; }
	public Breeding getBreedingBehavior() { return breedingBehavior; }
	public Eating getEatingBehavior() { return eatingBehavior; }
	public Plant getPlantToFind() { return plantToFind; }
	public Creature getCreatureToFind() { return creatureToFind; }
	public AIState getBreedingState() { return breedingState; }
	public AIState getEatingState() { return eatingState; }
	public AIState getHuntingState() { return huntingState; }
	public AIState getCurrentState() { return currentState; }
	public boolean getCurrentlySelected() { return currentlySelected; }
	public int getTargetX() { return targetX; }
	public int getTargetY() { return targetY; }
	public boolean getTargetExists() { return targetExists; }
	
	public void setPlantToFind(Plant plant) { this.plantToFind = plant; }
	public void setCreatureToFind(Creature creature) { this.creatureToFind = creature; }
	public void setState(AIState state) { this.currentState = state; }
	public void shiftX(int x) { this.x += x; }
	public void shiftY(int y) { this.y += y; }
	public void setCurrentlySelected(boolean currentlySelected) { this.currentlySelected = currentlySelected; }
	public void setTargetX(int targetX) { this.targetX = targetX; }
	public void setTargetY(int targetY) { this.targetY = targetY; }
	public void setTargetExists(boolean targetExists) { this.targetExists = targetExists; }
	
	
}
