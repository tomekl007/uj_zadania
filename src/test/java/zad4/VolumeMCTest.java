package zad4;

import org.junit.Test;

public class VolumeMCTest {

  @Test
  public void shouldPass(){
    Shape3DInterface si = new Shape3D();
    VolumeMCInterface vi = new VolumeMC();

    vi.setShape3D( si );

    System.out.println( "Objetosc oszacowano na " + vi.estimateVolume( 100000 ));
  }

}