package particles;

import simulation.World;
import java.util.Random;

public class Smoke extends Particle {

    private static final Random rand = new Random();

    private int age = 0;
    private final int maxAge = 100;

    // -1 sola, +1 sağa
    private int direction;

    public Smoke() {
        direction = rand.nextBoolean() ? -1 : 1;
    }

    // ✅ SimulationPanel'de fade için lazım
    public int getAge() {
        return age;
    }

    public int getMaxAge() {
        return maxAge;
    }

    // ✅ Smoke SADECE Water'ı displace edebilir (kum/taş engeldir)
    private boolean canDisplace(Particle p) {
        return (p instanceof Water);
    }

    private boolean moveOrDisplace(World world, int r, int c, int nr, int nc) {
        if (!world.inBounds(nr, nc)) return false;

        Particle target = world.get(nr, nc);
        if (target == null) {
            return world.moveToEmpty(r, c, nr, nc);
        }
        if (canDisplace(target)) {
            return world.swap(r, c, nr, nc);
        }
        return false;
    }

    @Override
    public void update(World world, int r, int c) {

        age++;

        // 100 adım sonra yok ol :contentReference[oaicite:1]{index=1}
        if (age >= maxAge) {
            world.set(r, c, null);
            return;
        }

        int up = r - 1;

        // Yukarı çıkma olasılığı :contentReference[oaicite:2]{index=2}
        double riseProb = 1 - 0.8 * ((double) age / maxAge);

        if (rand.nextDouble() < riseProb) {

            // 1) up
            if (moveOrDisplace(world, r, c, up, c)) {
                return;
            }

            // 2) diagonals random order :contentReference[oaicite:3]{index=3}
            if (rand.nextBoolean()) {
                if (moveOrDisplace(world, r, c, up, c - 1)) return;
                if (moveOrDisplace(world, r, c, up, c + 1)) return;
            } else {
                if (moveOrDisplace(world, r, c, up, c + 1)) return;
                if (moveOrDisplace(world, r, c, up, c - 1)) return;
            }
        }

        // Yatay drift olasılığı :contentReference[oaicite:4]{index=4}
        double spreadProb = 0.2 + 0.5 * ((double) age / maxAge);

        if (rand.nextDouble() < spreadProb) {
            // preferred direction
            if (moveOrDisplace(world, r, c, r, c + direction)) {
                return;
            }

            // bloklandıysa yön değiştir
            direction *= -1;

            moveOrDisplace(world, r, c, r, c + direction);
        }
    }
}