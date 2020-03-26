
package database;

/**
 * Classe enumerativa che delinea le entità aggregate che possono essere eventualmente ricercate nella tabella 
 * di un database SQL strutturato secondo l'omonimo linguaggio, attraverso funzioni di aggregazione messe 
 * a disposizione da tale linguaggio. 
 * 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public enum QUERY_TYPE {

	/**
	 * Astrazione del risultato di una query che fa uso di funzioni aggregate per la ricerca degli elementi massimo
	 * e minimo all'interno di una tabella del database corrente
	 */
	MIN, MAX;
}
