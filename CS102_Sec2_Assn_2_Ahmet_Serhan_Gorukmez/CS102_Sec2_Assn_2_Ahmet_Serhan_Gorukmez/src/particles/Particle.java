package particles;

import simulation.World;

public abstract class Particle {

    private boolean updated;

    public abstract void update(World world, int r, int c);

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean b) {
        updated = b;
    }
}