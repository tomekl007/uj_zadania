package zad4;

/**
 * Przykładowa implementacja interfejsu {@link Shape3DInterface}
 * @author oramus
 *
 */
public class Shape3D implements Shape3DInterface {

	@Override
	public boolean isInside(Point3D p) {
		
		double dx = p.getX() - 0.5;
		double dy = p.getY() - 0.5;
		double r = Math.sqrt( dx * dx + dy * dy );
		
		if ( ( r < 0.3 ) && ( p.getZ() < 0.5 ) ) return true; 			
		
		return false;
	}

}
