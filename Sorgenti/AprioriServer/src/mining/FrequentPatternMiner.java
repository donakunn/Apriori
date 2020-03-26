
package mining;

import data.Attribute;
import data.Data;
import data.DiscreteAttribute;
import data.DiscreteItem;
import data.EmptySetException;
import data.ContinuousAttribute;
import utility.ConnectionLog;
import utility.EmptyQueueException;
import utility.Queue;
import java.util.*;

/**
 * Descrive i metodi per la scoperta di pattern frequenti con algoritmo APRIORI.
 * 
 * @author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class FrequentPatternMiner {

	/**
	 * Genera tutti i pattern k=1 frequenti e per ognuno di	questi genera quelli con k&gt;1 richiamando expandFrequentPatterns().
	 * @param data Insieme delle transazioni di training
	 * @param minSup supporto minimo richiesto
	 * @return Lista collegata contenente i pattern frequenti scoperti in Data
	 * @throws EmptySetException l'insieme di training è vuoto
	 */
	public static LinkedList<FrequentPattern> frequentPatternDiscovery(Data data,float minSup) throws EmptySetException {
		Queue<FrequentPattern> fpQueue=new Queue<FrequentPattern>();		
		LinkedList<FrequentPattern> outputFP=new LinkedList<FrequentPattern>();	

		for(int i=0;i<data.getNumberOfAttributes();i++)
		{
			Attribute currentAttribute=data.getAttribute(i);
			if (currentAttribute instanceof DiscreteAttribute) {
				for(int j=0;j<((DiscreteAttribute)currentAttribute).getNumberOfDistinctValues();j++){
					DiscreteItem item=new DiscreteItem( 
							(DiscreteAttribute)currentAttribute, 
							((DiscreteAttribute)currentAttribute).getValue(j));
					FrequentPattern fp=new FrequentPattern();
					fp.addItem(item);
					fp.setSupport(FrequentPatternMiner.computeSupport(data, fp));
					if(fp.getSupport()>=minSup){ 
						fpQueue.enqueue(fp);
						outputFP.add(fp);
					}
				}
			}
			else {
				Iterator<Float>it=((ContinuousAttribute)currentAttribute).iterator();
				if(it.hasNext()) {
					float inf=it.next() ;
					while(it.hasNext()){
						float sup=it.next();
						ContinuousItem item;
						if(it.hasNext())
							item=new ContinuousItem((ContinuousAttribute)currentAttribute,new Interval(inf, sup));
						else
							item=new ContinuousItem((ContinuousAttribute)currentAttribute,new Interval(inf, sup+0.01f*(sup-inf)));
						inf=sup;
						FrequentPattern fp=new FrequentPattern();
						fp.addItem(item);
						fp.setSupport(FrequentPatternMiner. computeSupport(data,fp));
						if(fp.getSupport()>=minSup){ 
							fpQueue.enqueue(fp);
							outputFP.add(fp);
						}
					}
				}
			}
		}
		outputFP=expandFrequentPatterns(data,minSup,fpQueue,outputFP);
		return outputFP;
	}

	/**
	 * Crea un nuovo pattern a cui aggiunge tutti gli item di FP e il parametro item.
	 * @param FP pattern frequente da espandere
	 * @param item Item da aggiungere 
	 * @return Nuovo pattern ottenuto per effetto del raffinamento
	 */
	private static FrequentPattern refineFrequentPattern(FrequentPattern FP, Item item){
		FrequentPattern newFP = new FrequentPattern();

		for(int i=0; i<FP.getPatternLength(); i++) {
			newFP.addItem(FP.getItem(i));
		}
		newFP.addItem(item);

		return newFP;
	}

	/**
	 * Finch&eacute; fpQueue contiene elementi, si estrae un elemento dalla coda di pattern frequenti, si generano i raffinamenti per quest'ultimo
	 * (aggiungendo un nuovo item non incluso). Per ogni raffinamento si verifica se &egrave; frequente e, in caso affermativo, 
	 *  lo si aggiunge sia ad	fpQueue sia ad outputFP.
	 *  Il metodo pu&ograve; generare un'eccezione di tipo EmptyQueueException nel caso in cui pattern non siano presenti 
	 *  nella coda dalla quale generare i pattern di lunghezza k+1. 
	 *  
	 * @param data Insieme delle transazioni di training
	 * @param minSup supporto minimo richiesto
	 * @param fpQueue coda contenente i pattern da valutare
	 * @param outputFP lista di pattern frequenti gi&agrave; estratti
	 * @return Lista collegata popolata con pattern frequenti di lunghezza k>1
	 */
	private static   LinkedList<FrequentPattern> expandFrequentPatterns(Data data, float minSup, Queue<FrequentPattern> fpQueue, LinkedList<FrequentPattern> outputFP){
		while (!fpQueue.isEmpty()) {
			FrequentPattern FP= new FrequentPattern();
			try {
				FP = (FrequentPattern)fpQueue.first();
				fpQueue.dequeue(); 
			}
			catch(EmptyQueueException e) {
				System.out.println(e.getMessage());
				ConnectionLog.writeLogText(e.getMessage());
			}

			for(int i=0; i<data.getNumberOfAttributes(); i++){
				Attribute currentAttribute=data.getAttribute(i);
				if (currentAttribute instanceof DiscreteAttribute) {
					boolean verifica = false;
					for(int j=0; j<FP.getPatternLength(); j++){
						if(FP.getItem(j).getAttribute().equals(data.getAttribute(i))){					
							verifica = true;
							break;
						}
					}
					if(!verifica){
						for(int j=0;j<((DiscreteAttribute)data.getAttribute(i)).getNumberOfDistinctValues();j++){
							DiscreteItem item=new DiscreteItem((DiscreteAttribute)data.getAttribute(i),((DiscreteAttribute)(data.getAttribute(i))).getValue(j));
							FrequentPattern nFP=refineFrequentPattern(FP,item);
							nFP.setSupport(FrequentPatternMiner.computeSupport(data, nFP));
							if(nFP.getSupport()>=minSup){
								fpQueue.enqueue(nFP);
								outputFP.add(nFP);
							}
						}
					}
				}
				else {		
					boolean verifica = false;
					for(int j=0; j<FP.getPatternLength(); j++){
						if(FP.getItem(j).getAttribute().equals(data.getAttribute(i))){					
							verifica = true;
							break;
						}
					}
					if(!verifica){
						Iterator<Float> it = ((ContinuousAttribute)currentAttribute).iterator();
						if (it.hasNext()) {
							float inf = it.next();
							while (it.hasNext()) {
								float sup = it.next();
								ContinuousItem item;
								if (it.hasNext()) {
									item=new ContinuousItem((ContinuousAttribute)data.getAttribute(i),new Interval(inf,sup));
								}
								else {
									item=new ContinuousItem((ContinuousAttribute)data.getAttribute(i),new Interval(inf,sup+0.01f*(sup-inf)));
								}
								inf = sup;
								FrequentPattern nFP=refineFrequentPattern(FP,item);
								nFP.setSupport(FrequentPatternMiner.computeSupport(data, nFP));
								if(nFP.getSupport()>=minSup){
									fpQueue.enqueue(nFP);
									outputFP.add(nFP);
								}
							}
						}
					}
				}
			}									
		}
		return outputFP;
	}

	/**
	 * Per ognuna delle transazioni memorizzate in data, verifica se ognuno degli Item costituenti il pattern FP occorre
	 * @param data Insieme delle transazioni di training
	 * @param FP pattern 
	 * @return Supporto del pattern corrente
	 */
	private static float computeSupport(Data data,FrequentPattern FP){
		int suppCount=0;

		for(int i=0;i<data.getNumberOfExamples();i++){

			boolean isSupporting=true;
			for(int j=0;j<FP.getPatternLength();j++)
			{
				Item item=FP.getItem(j);
				if (item instanceof DiscreteItem) {
					DiscreteItem currentItem = (DiscreteItem) item;
					DiscreteAttribute attribute=(DiscreteAttribute)currentItem.getAttribute();
					Object valueInExample=data.getAttributeValue(i, attribute.getIndex());
					if(!currentItem.checkItemCondition(valueInExample)){
						isSupporting=false;
						break; 
					}
				}
				else {
					ContinuousItem currentCItem = (ContinuousItem) item;
					ContinuousAttribute attribute=(ContinuousAttribute)currentCItem.getAttribute();
					Object valueInExample=data.getAttributeValue(i, attribute.getIndex());
					if(!currentCItem.checkItemCondition(valueInExample)){
						isSupporting=false;
						break;
					}
				}
			}
			if(isSupporting)
				suppCount++;
		}
		return ((float)suppCount)/(data.getNumberOfExamples());
	}
}



