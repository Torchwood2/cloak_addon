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

    public void createShield(World level, BlockPos position){
        Entity shieldEntity = new ChickenEntity(EntityType.CHICKEN, level);
        shieldEntity.moveTo(position.getX(), position.getY(), position.getZ());
        level.addFreshEntity(shieldEntity);
        shield = shieldEntity;
    }

    public void removeShield(){
        if(shield != null){
            shield.remove();
            shield = null;
        }
    }
}
