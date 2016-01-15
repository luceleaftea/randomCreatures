package randomCreatures;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.tree.DefaultMutableTreeNode;

import randomCreatures.Creature.*;
import randomCreatures.CreatureFactory.CreatureFactory;
import randomCreatures.CreatureFactoryFactory.CreatureFactoryFactory;

public class World {
	
	// Variables
	private HashMap<Integer, List<Creature>> creatureLists;
	private HashMap<Integer, CreatureFactory> creatureFactories;
	private FoodChain foodChain;
	private CreatureFactoryFactory creatureFactoryFactory;
	
	// Creature Trait Lists
	private List<Color> colorList;
	private List<Shape> shapeList;
	
	public World() {
		
		// Initialize lists of creature trait lists
		colorList = new ArrayList<Color>();
		colorList.add(new ColorBlue());
		colorList.add(new ColorRed());
		
		shapeList = new ArrayList<Shape>();
		shapeList.add(new ShapeSquare());
		shapeList.add(new ShapeTriangle());
		
		// Create the Factory for making CreatureFactories
		creatureFactoryFactory = new CreatureFactoryFactory(colorList, shapeList);
		
		// Create HashMap of Lists of Creatures
		creatureLists = new HashMap<Integer, List<Creature>>();
		creatureFactories = new HashMap<Integer, CreatureFactory>();
		
		foodChain = new FoodChain(creatureFactoryFactory);
		
		for(int i = 0; i < 10; i++) {
			addNewSpecies();
		}
		
		populateWorld();
		
		displayWorld();
	}
	
	// Needs to add a new randomly created Creature Factory to the list, create a random amount of creatures, and then add the id to the tree
	public void addNewSpecies() {
		
		// Add id to the tree and get the new factory (the FoodChain makes the new factory and adding the id to the tree)
		CreatureFactory newFactory = foodChain.addSpecies();
		
		// Add the new factory to the hash map with its id as the key
		creatureFactories.put(newFactory.getID(), newFactory);
		
		// Add the a creature List for the new species
		creatureLists.put(newFactory.getID(), new ArrayList<Creature>());
	}
	
	// Loop through the FoodChain and add creatures for each node
	public void populateWorld() {
		
		// Make a depth-first enumeration of the food chain tree
		Enumeration e = ((DefaultMutableTreeNode) foodChain.getFoodChain().getRoot()).depthFirstEnumeration();
		int id;
		CreatureFactory currentFactory;
		
		// Loop through the enumeration and use the ids to populate each species
		while(e.hasMoreElements()) {
			id = (int) ((DefaultMutableTreeNode) e.nextElement()).getUserObject();
			currentFactory = creatureFactories.get(id);
			for(int i = 0; i < ThreadLocalRandom.current().nextInt(1, 11); i++) {
				creatureLists.get(id).add(currentFactory.createCreature());
			}
		}
	}
	
	public void displayWorld() {
		// Output strings of everything
		for(int i = 0; i < creatureLists.size(); i++) {
			for(int j = 0; j < creatureLists.get(i).size(); j++) {
				System.out.println(creatureLists.get(i).get(j));
			}
		}
	}

}