
package mining;

import data.Data;
import data.DiscreteAttribute;
import data.DiscreteItem;
import data.ContinuousAttribute;
import java.util.*;

/**
 * Modella la scoperta di regole d'associazione confidenti a partire da un pattern frequente.
 * 
 * @author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class AssociationRuleMiner {

	/**
	 * Per ogni pattern di fp, si generano le regole di associazione (invocando il metodo confidentAssociationRuleDiscovery) 
	 * e si calcola la confidenza. Le regole confidenti sono inserite in una lista
	 * @param data Insieme delle transazioni di training
	 * @param fp pattern frequente 
	 * @param minConf confidenza minima richiesta
	 * @return Lista collegata popolata con regole d'associazione confidenti scoperti da fp in data
	 * @throws OneLevelPatternException pattern di lunghezza 1
	 */
	public static LinkedList<AssociationRule> confidentAssociationRuleDiscovery(Data data,FrequentPattern fp,float minConf)	throws OneLevelPatternException {

		LinkedList<AssociationRule> outputAR = new LinkedList<AssociationRule>();
		if (fp.getPatternLength() == 1) throw new OneLevelPatternException("La linghezza del pattern" + fp.toString() + " � 1" );
		else {
			for(int j=1; j<fp.getPatternLength(); j++){
				AssociationRule ar= (confidentAssociationRuleDiscovery(data, fp, minConf, j));

				if(computeConfidence(data, ar) >= minConf){
					outputAR.add(ar);
				}
			}
		}
		return outputAR;
	}

	/**
	 * Crea una regola di associazione estraendo come antecedente l'insieme degli item posizionati in fp prima dell'indice iCut 
	 * e come conseguente l'insieme degli item posizionati in fp dopo dell'indice
	 * iCut (compresi iCut). Si calcola la confidenza della regola.
	 * @param data Insieme delle transazioni di training
	 * @param fp pattern frequente da cui generare la regola d'associazione confidente
	 * @param minConf minima confidenza richiesta
	 * @param iCut posizione dell'item di taglio
	 * @return Regola d'associazione
	 */
	private static AssociationRule confidentAssociationRuleDiscovery(Data data,FrequentPattern fp,float minConf, int iCut){

		AssociationRule AR=new AssociationRule(fp.getSupport());

		for(int j=0;j<iCut;j++){
			AR.addAntecedentItem(fp.getItem(j));		
		}

		for(int j=iCut;j<fp.getPatternLength();j++){
			AR.addConsequentItem(fp.getItem(j));
		}	
		AR.setConfidence(AssociationRuleMiner.computeConfidence(data,AR));
		return AR;
	}

	/**
	 * Si ottiene da AR il numero di transazioni che supportano il pattern, si calcola il numero di transazione che supportano
	 * l'antecedente, si calcola/restituisce la confidenza secondo la formula: p(XuY)/p(X), con X antecedente, Y conseguente della regola d'associazione,
	 * p(XuY) probabilit&agrave; di occorrenza di antecedente e conseguente, p(X) probabilit&agrave; di occorrenza dell'antecedente della regola
	 * @param data insieme delle transazioni di training
	 * @param AR regola d'associazione 
	 * @return confidenza della regola d'associazione
	 */
	private static float  computeConfidence(Data data, AssociationRule AR){	

		int suppAntCount=0;		

		for(int i=0;i<data.getNumberOfExamples();i++){
			boolean isSupporting=true;
			for(int j=0;j<AR.getAntecedentLength();j++)
			{
				Item item=AR.getAntecedentItem(j);
				if (item instanceof DiscreteItem) {
					DiscreteItem currentItem = (DiscreteItem) item;		//??
					DiscreteAttribute attribute=(DiscreteAttribute)currentItem.getAttribute();
					Object valueInExample=data.getAttributeValue(i, attribute.getIndex());
					if(!currentItem.checkItemCondition(valueInExample)){
						isSupporting=false;
						break; 
					}
				}
				else {
					ContinuousItem currentItem = (ContinuousItem) item;
					ContinuousAttribute attribute=(ContinuousAttribute)currentItem.getAttribute();
					Object valueInExample=data.getAttributeValue(i, attribute.getIndex());
					if(!currentItem.checkItemCondition(valueInExample)){
						isSupporting=false;
						break; 
					}
				}
			}
			if(isSupporting)
				suppAntCount++;
		}
		return ((float)((AR.getSupport())*data.getNumberOfExamples())/(suppAntCount));
	}
}
