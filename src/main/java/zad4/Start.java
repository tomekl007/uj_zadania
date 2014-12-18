package zad4;
public class Start {

	public static void main(String[] args) {
		Shape3DInterface si = new Shape3D();
		VolumeMCInterface vi = new VolumeMC();
		
		vi.setShape3D( si );
		
		System.out.println( "Objetosc oszacowano na " + vi.estimateVolume( 100000 ));
		
	}

}
