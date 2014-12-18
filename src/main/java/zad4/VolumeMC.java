package zad4;

import java.util.Random;

/**
 * @author Tomasz Lelek
 * @since 2014-12-18
 */
public class VolumeMC implements VolumeMCInterface {
  private Shape3DInterface s3d;
  Random random = new Random();

  public VolumeMC() {
  }

  @Override
  public void setShape3D(Shape3DInterface s3d) {
    this.s3d = s3d;
  }

  @Override
  public double estimateVolume(int shotsNumber) {
    double numberOfCorrectShots = 0;
    for (int i = 0; i < shotsNumber; i++) {
      if (s3d.isInside(new Shape3DInterface.Point3D(random.nextDouble(), random.nextDouble(), random.nextDouble()))) {
        numberOfCorrectShots++;
      }

    }
    return 1.0 * numberOfCorrectShots / shotsNumber;
  }
}
