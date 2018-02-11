package ihm;



public abstract class UIBuilder<T> {
	public Composite<T> composite;
	public int width;
	public int height;
	
	public void beginX(BuilderAction ba) {
		this.beginX();
		ba.process();
		this.end();
		
	}
	public void beginBuildComposite(Composite<T> composite) {
		
	}
	public void endBuildComposite(Composite<T> composite) {
		
	}
	abstract public void buildFrom(Component<T> component);
	public void beginY(BuilderAction ba) {
		this.beginY();
		ba.process();
		this.end();
		
	}
	public void beginX() {
		CompositeX<T> c = new CompositeX<T>();
		if (composite != null) {
			composite.add(c);
		} 
			
			composite = c;

	}

	public void beginY() {
		CompositeY<T> c = new CompositeY<T>();
		if (composite != null) {
			composite.add(c);
		} 
			
			composite = c;
		

	}
	public void end() {
		if (composite.parent == null) {
			
			
			return;
		}
		composite = composite.parent;
		
	}

	public void setSize(int width, int height) {
			this.width = width;
			this.height = height;
	}

	public void space(int value) {
		composite.space(value);

	}

	public void add(T component) {
		Component<T> c = new Component<T>();
		c.width = width;
		c.height = height;
		c.component = component;
		composite.add(c);
		

	}
	public void add(T component,ComponentAction<T> action) {
		Component<T> c = new Component<T>();
		c.width = width;
		c.height = height;
		c.component = component;
		composite.add(c);
		action.exec(component);
		

	}
	
	

}
