package tk.jacobempire.cloakpaneladdon.tardis;

import com.swdteam.common.tardis.Location;
import com.swdteam.util.math.Position;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TardisShieldManager {
    private Entity shield;
    public Location location;

    // called when hasShield is set to true
    public void createShield(World level, BlockPos position){
        Entity shieldEntity = new ArmorStandEntity(EntityType.ARMOR_STAND, level);
        shieldEntity.setNoGravity(true);
        shieldEntity.moveTo(position.getX()+0.5, position.getY()+2, position.getZ()+0.5);
        level.addFreshEntity(shieldEntity);
        shield = shieldEntity;
    }

    // called when hasShield is set ot false
    public void removeShield(){
        if(shield != null){
            shield.remove();
            shield = null;
        }
    }
}
