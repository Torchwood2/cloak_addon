package tk.jacobempire.cloakpaneladdon.data;

public interface ITardisTileEntityMixin {
    boolean invisible = false;
    boolean isInvisible ();
    void setInvisible(boolean invisible);

    boolean hasShield = false;
    boolean hasShield ();
    void setHasShield(boolean hasShield);
}
