package baritone.bot.pathing;

import baritone.bot.pathing.movements.Movement;
import baritone.bot.utils.Utils;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.List;

public interface IPath {
    /**
     * Ordered list of movements to carry out.
     * movements.get(i).getSrc() should equal positions.get(i)
     * movements.get(i).getDest() should equal positions.get(i+1)
     * movements.size() should equal positions.size()-1
     */
    List<Movement> movements();

    /**
     * All positions along the way.
     * Should begin with the same as getSrc and end with the same as getDest
     */
    List<BlockPos> positions();

    /**
     * What's the next step
     *
     * @param currentPosition the current position
     * @return
     */
    default Movement subsequentMovement(BlockPos currentPosition) {
        List<BlockPos> pos = positions();
        List<Movement> moves = movements();
        for (int i = 0; i < pos.size(); i++) {
            if (currentPosition.equals(pos.get(i))) {
                return moves.get(i);
            }
        }
        throw new UnsupportedOperationException(currentPosition + " not in path");
    }

    /**
     * @param currentPosition
     * @return
     */
    default boolean isInPath(BlockPos currentPosition) {
        return positions().stream().anyMatch(pos -> currentPosition.equals(pos));
    }

    default Tuple<Double, BlockPos> closestPathPos(double x, double y, double z) {
        double best = -1;
        BlockPos bestPos = null;
        for (BlockPos pos : positions()) {
            double dist = Utils.distanceToCenter(pos, x, y, z);
            if (dist < best || best == -1) {
                best = dist;
                bestPos = pos;
            }
        }
        return new Tuple<>(best, bestPos);
    }

    /**
     * Where does this path start
     */
    BlockPos getSrc();

    /**
     * Where does this path end
     */
    BlockPos getDest();

    /**
     * For rendering purposes, what blocks should be highlighted in red
     *
     * @return an unordered collection of positions
     */
    Collection<BlockPos> getBlocksToMine();

    /**
     * For rendering purposes, what blocks should be highlighted in green
     *
     * @return an unordered collection of positions
     */
    Collection<BlockPos> getBlocksToPlace();
}
