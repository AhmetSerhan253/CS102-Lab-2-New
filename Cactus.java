package particles;

import simulation.World;
import java.util.Random;

public class Cactus extends Particle {

    private static final Random rand = new Random();

    private int growthLevel;          // 1..8
    private int stepsSinceGrowth = 0; // her 10 step'te bir büyüme dener

    public Cactus(int growthLevel) {
        this.growthLevel = growthLevel;
    }

    public int getGrowthLevel() {
        return growthLevel;
    }

    @Override
    public void update(World world, int r, int c) {
        // Cactus stationary ve displace edilemez (hareket yok) 2

        if (growthLevel >= 8) return;

        stepsSinceGrowth++;
        if (stepsSinceGrowth % 10 != 0) return; // her 10 update adımı 3

        attemptGrowth(world, r, c);
    }

    private void attemptGrowth(World world, int r, int c) {
        // Kurallar: %80 up, %10 left, %10 right 4
        int roll = rand.nextInt(100); // 0..99

        if (roll < 80) {
            if (tryGrow(world, r, c, r - 1, c)) return; // up
            // up olmazsa sırayla left/right dene (dokümandaki "cannot grow upward" şartını koruyalım)
            if (roll < 90) {
                tryGrow(world, r, c, r, c - 1); // left
            } else {
                tryGrow(world, r, c, r, c + 1); // right
            }
        } else if (roll < 90) {
            // left only if cannot grow upward
            if (!canGrow(world, r - 1, c)) {
                tryGrow(world, r, c, r, c - 1);
            }
        } else {
            // right only if cannot grow upward or left
            if (!canGrow(world, r - 1, c) && !canGrow(world, r, c - 1)) {
                tryGrow(world, r, c, r, c + 1);
            }
        }
    }

    private boolean canGrow(World world, int tr, int tc) {
        return world.inBounds(tr, tc) && world.get(tr, tc) == null && cactusCrowdingOk(world, tr, tc);
    }

    private boolean tryGrow(World world, int r, int c, int tr, int tc) {
        if (!canGrow(world, tr, tc)) return false;

        // Yeni cactus oluştur: level = parent+1 5
        world.set(tr, tc, new Cactus(growthLevel + 1));
        return true;
    }

    private boolean cactusCrowdingOk(World world, int tr, int tc) {
        // Hedefin çevresi: above, above-left, above-right, left, right
        // Eğer bu 5 pozisyonda >=2 cactus varsa büyüme yok 6
        int count = 0;

        count += isCactus(world, tr - 1, tc) ? 1 : 0;
        count += isCactus(world, tr - 1, tc - 1) ? 1 : 0;
        count += isCactus(world, tr - 1, tc + 1) ? 1 : 0;
        count += isCactus(world, tr, tc - 1) ? 1 : 0;
        count += isCactus(world, tr, tc + 1) ? 1 : 0;

        return count < 2;
    }

    private boolean isCactus(World world, int r, int c) {
        if (!world.inBounds(r, c)) return false;
        return world.get(r, c) instanceof Cactus;
    }
}
