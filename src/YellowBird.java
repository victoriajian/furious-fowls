public class YellowBird extends Bird{

    /**
    * Note that, because we don't need to do anything special when constructing a Square, we simply
    * use the superclass constructor called with the correct parameters.
    */
    public YellowBird(int id, int courtWidth, int courtHeight, int posX, int posY, String imgFile) {
    	super(id, courtWidth, courtHeight, posX, posY, imgFile);
    }
    
    public Pelt dropPelt(int cw, int ch) {
    	Pelt p = new Pelt(cw, ch, this.getPx(), this.getPy(), "files//pelt.png");
    	return p;
    }

}
