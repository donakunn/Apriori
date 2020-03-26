
package data;

import java.io.Serializable;

/**
 * Classe astratta che modella un generico attributo discreto o continuo. Implementa l'interfaccia Serializable	
 * in modo che gli oggetti delle sue sottoclassi siano serializzabili in flussi di byte che li rappresentino.
 *
 * @author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */

public abstract class Attribute implements Serializable{

	/**
	 * Nome simbolico dell'attributo.
	 */
	private String name;

	/** 
	 * Identificativo numerico dell'attributo.
	 */
	private int index;

	/**
	 * Costruttore; inizializza i valori dei membri name e index. Necessario per le sottoclassi di Attribute, affinch&eacute; possano
	 * richiamare tale costruttore all'interno del proprio, nel rispetto del concetto di relazione di generalizzazione "is a"; infatti, 
	 * secondo tale principio, istanze di una sottoclasse devono contenere al loro interno le caratteristiche ereditate dalla propria superclasse,
	 * nonch&eacute; tipiche di istanze di questa stessa.
	 * @param name nome simbolico dell'attributo
	 * @param index identificativo numerico dell'attributo
	 */
	protected Attribute(String name, int index){
		this.name=name;
		this.index=index;		
	}

	/**
	 * Restituisce il valore nel membro name dell'attributo.
	 * @return Nome dell'attributo
	 */
	protected String getName(){
		return name;
	}

	/**
	 * Restituisce il valore del membro index dell'attributo.
	 * @return Identificativo numerico dell'attributo
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * Restituisce il valore del membro name.
	 * Ridefinisce il metodo toString() della superclasse Object.
	 * @return Nome simbolico dell'attributo
	 */
	public String toString(){		 
		return name;
	}
}
