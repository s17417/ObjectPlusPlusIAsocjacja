package Laboratorium;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ObjectPlusPlus extends ObjectPlus {
				
	    private Map<IAsocjacja, Map<Object, ObjectPlusPlus>> links = new Hashtable<>();
	    private static Set<ObjectPlusPlus> allParts = new HashSet<>();
	 
	    public ObjectPlusPlus() {
	        super();
	    }
		
	    private void addLink(IAsocjacja roleName, ObjectPlusPlus targetObject, Object qualifier, int counter) throws Exception {
	        Map<Object, ObjectPlusPlus> objectLinks;
	        if(!roleName.verifyInstance(this,targetObject)) throw new ClassCastException("obiekty nie pasuja do danej asocjacji");
	        
	        if(counter < 1) {
	            return;
	        }
	        
	       
	        if (targetObject.containsRole(roleName.getOpposite())) {
				if(roleName.getOpposite().getMaxCardinality()!=0&&counter==2&&!(targetObject.roleSize(roleName.getOpposite())<roleName.getOpposite().getMaxCardinality()))
					throw new Exception("Maksymalna licznosc w  "+targetObject.getClass().getSimpleName()+  " osiagnieta ma juz powiazane: "+targetObject.roleSize(roleName.getOpposite())+" obiektow");
	        }
	         
	        if(links.containsKey(roleName)) {
	           objectLinks = links.get(roleName);
	           if (roleName.getMaxCardinality()!=0&&!(objectLinks.size()<roleName.getMaxCardinality()))
	        	   throw new Exception("Maksymalna licznosc osiagnieta  "+this.getClass().getSimpleName()+"  ma juz powiazane: "+this.roleSize(roleName)+" obiektow");
	        }
	        
	        else {
	            objectLinks = new HashMap<>();
	            links.put(roleName, objectLinks);
	        }
	        if(!objectLinks.containsKey(qualifier)) {
	            objectLinks.put(qualifier, targetObject);
	 
	            targetObject.addLink(roleName.getOpposite(), this, this, counter - 1);
	        }
	    }
	    
	    public void addLink(IAsocjacja roleName, ObjectPlusPlus targetObject, Object qualifier) throws Exception {
	        addLink(roleName, targetObject, qualifier, 2);
	    }
	 
	    public void addLink(IAsocjacja roleName, ObjectPlusPlus targetObject) throws Exception {
	        addLink(roleName, targetObject, targetObject);
	    }
	    
	    public void addPart(IAsocjacja roleName, ObjectPlusPlus partObject) throws Exception {
	        if(allParts.contains(partObject)) {
	            throw new Exception("The part is already connected to a whole!");
	        }
	 
	        addLink(roleName, partObject);
	        allParts.add(partObject);
	    }
	    
	    public ObjectPlusPlus[] getLinks(IAsocjacja roleName) throws Exception {
	        Map<Object, ObjectPlusPlus> objectLinks;
	 
	        if(!links.containsKey(roleName)) {
	            throw new Exception("No links for the role: " + roleName);
	        }
	        objectLinks = links.get(roleName);
	        
	        return (ObjectPlusPlus[]) objectLinks.values().toArray(new ObjectPlusPlus[0]);
	    } 
	    
	    public void showLinks(IAsocjacja roleName, PrintStream stream) throws Exception {
	        Map<Object, ObjectPlusPlus> objectLinks;
	 
	        if(!links.containsKey(roleName)) {
	            throw new Exception("No links for the role: " + roleName);
	        }
	 
	        objectLinks = links.get(roleName);
	 
	        Collection col = objectLinks.values();
	 
	        stream.println(this.getClass().getSimpleName() + " links, role '" + roleName + "':");
	 
	        for(Object obj : col) {
	            stream.println("   " + obj);
	        }
	    }
	    
	    public ObjectPlusPlus getLinkedObject(IAsocjacja roleName, Object qualifier) throws Exception {
	        Map<Object, ObjectPlusPlus> objectLinks;
	 
	        if(!links.containsKey(roleName)) {
	            throw new Exception("No links for the role: " + roleName);
	        }
	 
	        objectLinks = links.get(roleName);
	        if(!objectLinks.containsKey(qualifier)) {
	            throw new Exception("No link for the qualifer: " + qualifier);
	        }
	 
	        return objectLinks.get(qualifier);
	    }
	    
	    public boolean containsRole (IAsocjacja roleName) {
	    	return this.links.containsKey(roleName);
	    }
	    
	    public int roleSize(IAsocjacja roleName) throws Exception {
	    	if (!this.containsRole(roleName)) throw new Exception("Can't get size, No links for the role: " + roleName);
	    	return this.links.get(roleName).size();
	    }
	    
	   
	    
	    private void removeLink(IAsocjacja roleName, ObjectPlusPlus targetObject, Object qualifier, int counter) throws Exception {
	    	if (counter<1) return;
	    	Map<Object, ObjectPlusPlus> objectLinks;
	    	if(!links.containsKey(roleName)) {
	            throw new Exception("No links for the role: " + roleName);
	        }
	        objectLinks = links.get(roleName);
	        if (objectLinks.containsKey(qualifier)) {
	        objectLinks.remove(qualifier);
	        targetObject.removeLink(roleName.getOpposite(), this, this, counter-1);
	        }
	        
	        objectLinks.remove(targetObject);
	        targetObject.removeLink(roleName.getOpposite(), this, qualifier, counter-1);
	        
	    }
	    
	    public void removeLink(IAsocjacja roleName, ObjectPlusPlus targetObject, Object qualifier) throws Exception {
	    	removeLink(roleName, targetObject, qualifier, 2);
	    }
	    
	    public void removeLink(IAsocjacja roleName, ObjectPlusPlus targetObject) throws Exception {
	    	removeLink(roleName, targetObject, targetObject, 2);
	    }
	    
	    public void removePart(IAsocjacja roleName, ObjectPlusPlus targetObject) {
	    	targetObject.removeObject();
	    	allParts.remove(targetObject);
	    }

		@Override
		public void removeObject(){
			links.entrySet().iterator().forEachRemaining(entry ->{
				Map<Object, ObjectPlusPlus> objectLinks=entry.getValue();
				
				objectLinks.entrySet().iterator().forEachRemaining(innerEntry ->{
					try {
						this.removeLink(entry.getKey(), innerEntry.getValue(), innerEntry.getKey());
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				
			});
			super.removeObject();
		}
	    
	    
}
