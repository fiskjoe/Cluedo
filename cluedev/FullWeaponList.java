/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cluedev;

import java.util.ArrayList;

/**
 *
 * @author bobja
 */
public class FullWeaponList {

    ArrayList<Weapon> allWeapons = new ArrayList<Weapon>();
    int weaponsToSelect;
    Weapon[] selectedWeapons;

    /**
     * Constructs a list of all possible weapons in the game, allows the user to choose from 
     * this list and have custom weapons cards in their game
     *
     * @param numOfWeapons Number of weapons in the gamed
     */
    public FullWeaponList(int numOfWeapons) {
        allWeapons.add(new Weapon("Candlestick"));
        allWeapons.add(new Weapon("Dagger"));
        allWeapons.add(new Weapon("Diamond Sword"));
        allWeapons.add(new Weapon("Elder Wand"));
        allWeapons.add(new Weapon("Golden Gun"));
        allWeapons.add(new Weapon("Lead Pipe"));
        allWeapons.add(new Weapon("Lightsaber"));
        allWeapons.add(new Weapon("Pokeball"));
        allWeapons.add(new Weapon("Red Shell"));
        allWeapons.add(new Weapon("Revolver"));
        allWeapons.add(new Weapon("Rope"));
        allWeapons.add(new Weapon("Spanner"));

        this.weaponsToSelect = numOfWeapons;
        this.selectedWeapons = new Weapon[numOfWeapons];
        for (int i = 0; i < numOfWeapons; i++) {
            this.selectedWeapons[i] = this.allWeapons.get(i);
        }
    }

    /**
     * Updates the selectedWeapons variable to the users new selection 
     * 
     * @param newWeapon new weapon that the user chose
     * @param oldIndex index of weapon to swap out when building weapon list for game handler
     */
    public void setWeapon(Weapon newWeapon, int oldIndex) {
        this.selectedWeapons[oldIndex] = newWeapon;
    }

    public ArrayList<Weapon> getAllWeapons() {
        return this.allWeapons;
    }

}
