
package data;

/**
 * Modella un attributo discreto rappresentando l'insieme di valori distinti
 * del relativo dominio.
 *
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class DiscreteAttribute extends Attribute{

	/**
	 * Array di oggetti String, uno per ciascun valore discreto.
	 */
	private String values[];

	/**
	 * Costruttore di attributo discreto; invoca il costruttore della classe madre e avvalora l'array values[] con i valori discreti in input.
	 * @param name nome simbolico dell'attributo
	 * @param index identificativo numerico dell'attributo
	 * @param values array di stringhe, una per ciascun valore discreto
	 */
	DiscreteAttribute(String name, int index, String values[]){
		super(name,index);
		this.values=values;
	}

	/**
	 * Restituisce la cardinalit&agrave; del membro values dell'istanza corrente di DiscreteAttribute.
	 * @return Dimensione  del membro values
	 */
	public int getNumberOfDistinctValues(){
		return values.length;
	}

	/**
	 * Restituisce il valore in posizione i del membro values dell'istanza corrente di DiscreteAttribute.
	 * Ridefinisce il metodo toString() della superclasse Object.
	 * @param index posizione del valore da ricercare all'interno del membro values
	 * @return Valore in posizione i-esima del membro values
	 */
	public String getValue(int index){
		return values[index];
	}
}
