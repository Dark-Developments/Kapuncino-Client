package coffee.client.helper.util;

import net.minecraft.util.math.BlockPos;

public class StrongholdPosition {
    public static int[] triangulatePosition(double[] throw1, double[] throw2) {
        double px1 = throw1[0];
        double pz1 = throw1[1];
        double ex1 = throw1[2];
        double ez1 = throw1[3];

        double px2 = throw2[0];
        double pz2 = throw2[1];
        double ex2 = throw2[2];
        double ez2 = throw2[3];

        double k1 = (pz1-ez1)/(px1-ex1);
        double k2 = (pz2-ez2)/(px2-ex2);

        double m1 = pz1 - k1*px1;
        double m2 = pz2 - k2*px2;

        double x = (m2-m1)/(k1-k2);
        double z = k1*x + m1;

        return new int[] {(int)x, (int)z};
    }

    public static int getDistance(BlockPos playerPos, int[] strongholdPos) {
        int x = playerPos.getX() - strongholdPos[0];
        int z = playerPos.getZ() - strongholdPos[1];
        return (int) Math.sqrt(x * x + z * z);
    }

}
