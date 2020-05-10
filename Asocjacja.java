package Laboratorium;

import java.util.HashSet;
import java.util.Set;

public class Asocjacja<T extends ObjectPlusPlus,R extends ObjectPlusPlus> implements IAsocjacja {
	private String name;
	private Class<T> class1;
	private Class<R> class2;
	private Asocjacja<R,T> opposite;
	int maxCardinalityClass;
	
	private static Set<Asocjacja<?,?>> associationSet= new HashSet<>();
	
	private Asocjacja(Class<T> class1, Class<R> class2, int maxCardinalityClass, String name) {
		super();
		this.name=name;
		this.class1 = class1;
		this.class2 = class2;
		this.maxCardinalityClass=maxCardinalityClass;
	}
	
	public static <X extends ObjectPlusPlus,Y extends ObjectPlusPlus> boolean createAssociation(Class<X> class1, Class<Y> class2, int maxCardinalityClass1, int maxCardinalityClass2) {
			
			Asocjacja<X,Y> o=new Asocjacja<>(class1,class2, maxCardinalityClass2, null);
			if (Asocjacja.associationSet.contains(o)) return false;
			associationSet.add(o);
			o.opposite= new Asocjacja<Y,X>(class2,class1, maxCardinalityClass1,null);
			o.opposite.opposite=o;
			associationSet.add(o.opposite);
			return true;
			
		}
	
	public static <X extends ObjectPlusPlus,Y extends ObjectPlusPlus> boolean createAssociation(Class<X> class1, Class<Y> class2, int maxCardinalityClass1, int maxCardinalityClass2, String name, String reverseName) {
		
		Asocjacja<X,Y> o=new Asocjacja<>(class1,class2, maxCardinalityClass2, name);
		if (Asocjacja.associationSet.contains(o)) return false;
		associationSet.add(o);
		o.opposite= new Asocjacja<Y,X>(class2,class1, maxCardinalityClass1, reverseName);
		o.opposite.opposite=o;
		associationSet.add(o.opposite);
		return true;
		
	}
	
	@SuppressWarnings("unchecked")
	public static <X extends ObjectPlusPlus,Y extends ObjectPlusPlus> Asocjacja<X,Y> getAssociation(Class<X> class1, Class<Y> class2) {
		return (Asocjacja<X, Y>) Asocjacja.associationSet.stream()
				.filter(obj->obj.getClass1()
				.equals(class1)&&obj.getClass2().equals(class2))
				.findAny().get();
	}
	
	@SuppressWarnings("unchecked")
	public static <X extends ObjectPlusPlus,Y extends ObjectPlusPlus> Asocjacja<X,Y> getAssociation(Class<X> class1, Class<Y> class2, String name) {
		return (Asocjacja<X, Y>) Asocjacja.associationSet.stream()
				.filter(obj->obj.getClass1()
						.equals(class1)&&obj.getClass2().equals(class2)&&obj.name.equals(name))
				.findAny().get();
	}

	@Override
	public <X, Y> boolean verifyInstance(X o1, Y o2) {
		return class1.isInstance(o1)&&class2.isInstance(o2);
	}

	@Override
	public IAsocjacja getOpposite() {
		return this.opposite;
	}

	@Override
	public int getMaxCardinality() {
		return maxCardinalityClass;
	}

	public Class<T> getClass1() {
		return class1;
	}

	public Class<R> getClass2() {
		return class2;
	}

	@Override
	public String toString() {
		return "Asocjacja [class1=" + class1 + ", class2=" + class2 + ", name="+name+"]"+", Opposite Asocjacja [class1=" + opposite.class1 + ", class2=" + opposite.class2 + ", name="+opposite.name+"]";
	}
}
