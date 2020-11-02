public class BlackBird extends Bird{

    /**
    * Note that, because we don't need to do anything special when constructing a Square, we simply
    * use the superclass constructor called with the correct parameters.
    */
    public BlackBird(int id, int courtWidth, int courtHeight, int posX, int posY, String imgFile) {
    	super(id, courtWidth, courtHeight, posX, posY, imgFile);
    }
    
    public void dropDown() {
    	setVx(0);
    	setVy(50);
    }

}
