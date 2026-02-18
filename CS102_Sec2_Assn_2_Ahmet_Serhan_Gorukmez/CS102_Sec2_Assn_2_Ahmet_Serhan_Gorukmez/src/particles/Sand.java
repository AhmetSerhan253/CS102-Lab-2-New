package particles;

import simulation.World;
import java.util.Random;

public class Sand extends Particle {

    private static final Random rand = new Random();

    // ✅ Sand artık SADECE Water'ı yer değiştirebilir (Smoke'u değil)
    private boolean canDisplace(Particle p) {
        return (p instanceof Water);
    }

    // Hedef boşsa move, dolu ama displace edilebiliyorsa swap
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

        int down = r + 1;

        // 1) Aşağı: boşsa veya Water ise displace ederek aşağı in
        if (moveOrDisplace(world, r, c, down, c)) {
            return;
        }

        // 2) Çaprazlar (random sıra) :contentReference[oaicite:0]{index=0}
        boolean leftFirst = rand.nextBoolean();

        if (leftFirst) {
            // down-left: hem left hem down-left boş veya (SADECE water) displace edilebilir olmalı
            if (world.inBounds(r, c - 1) && world.inBounds(down, c - 1)) {
                Particle left = world.get(r, c - 1);
                Particle downLeft = world.get(down, c - 1);

                if ((left == null || canDisplace(left)) && (downLeft == null || canDisplace(downLeft))) {
                    if (moveOrDisplace(world, r, c, down, c - 1)) return;
                }
            }

            // down-right
            if (world.inBounds(r, c + 1) && world.inBounds(down, c + 1)) {
                Particle right = world.get(r, c + 1);
                Particle downRight = world.get(down, c + 1);

                if ((right == null || canDisplace(right)) && (downRight == null || canDisplace(downRight))) {
                    moveOrDisplace(world, r, c, down, c + 1);
                }
            }

        } else {
            // down-right
            if (world.inBounds(r, c + 1) && world.inBounds(down, c + 1)) {
                Particle right = world.get(r, c + 1);
                Particle downRight = world.get(down, c + 1);

                if ((right == null || canDisplace(right)) && (downRight == null || canDisplace(downRight))) {
                    if (moveOrDisplace(world, r, c, down, c + 1)) return;
                }
            }

            // down-left
            if (world.inBounds(r, c - 1) && world.inBounds(down, c - 1)) {
                Particle left = world.get(r, c - 1);
                Particle downLeft = world.get(down, c - 1);

                if ((left == null || canDisplace(left)) && (downLeft == null || canDisplace(downLeft))) {
                    moveOrDisplace(world, r, c, down, c - 1);
                }
            }
        }

        // 3) hareket yoksa kalır
    }
}