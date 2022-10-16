package com.lagadd.biocubes.common.entities.creatures.cryodon;

public enum CryodonAttacks {
    STUN("hit stun", 1, 20, 100, 5),
    ROAR("roar", 2, 55, 160, 0),
    BITE("bite", 3, 22, 20, 13);

    private String name;
    private int id;
    private int maxframe;
    private int cooldown;
    private int damageframe;

    CryodonAttacks(String name, int id, int maxframe, int cooldown, int damageframe) {
        this.name = name;
        this.id = id;
        this.maxframe = maxframe;
        this.cooldown = cooldown;
        this.damageframe = damageframe;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    //Check if Animation is ongoing;
    public boolean isAnimationGoing() {
        return this.id != 0 || this.id > 0;
    }

    public int getMaxframe() {
        return Math.max(this.maxframe, 0);
    }

    public int getDamageframe() {
        return this.damageframe;
    }

    public int getCooldown() {
        return this.cooldown;
    }

}
